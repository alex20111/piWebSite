package net.project.web.action;


import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

import net.project.common.Constants;
import net.project.db.entity.Inventory;
import net.project.db.entity.InventoryGroup;
import net.project.db.entity.User;
import net.project.db.manager.InventoryManager;
import net.project.web.ajax.AjaxGroup;
import net.project.web.ajax.AjaxItem;
import net.project.web.ajax.AjaxQrCode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.SessionAware;
import org.imgscalr.Scalr;

import com.opensymphony.xwork2.ActionSupport;

public class InventoryAction extends ActionSupport implements SessionAware{

	private Log log = LogFactory.getLog(getClass());
	
	private static final long serialVersionUID = 1L;
	
	
	private Map<String, Object> session;
	
	private int groupId = -1;
	private String ajaxError  = "";
	private int itemId = -1;
	
	private String newGroupName ="";

	private List<Inventory> inventoryList = new ArrayList<Inventory>();
	private Inventory inventory;//for the add edit page
	private String formGroupName;
	
	private Boolean addItem = false;
	
	//ajax returns
	AjaxGroup ajaxGroup = null;
	AjaxItem ajaxItem = null;
	AjaxQrCode ajaxQrCode = null;
	
	//file
	private File thumbImage;
	private String thumbImageFileName;
	
	public String loadAllInventory(){

		User user = (User) session.get(Constants.USER);

		session.remove("groupName");
		
		try{
			
			if (user != null){
				InventoryManager im = new InventoryManager();

				inventoryList = im.loadAllInventoryForUser(user.getId(), true);
			}else{
				return Constants.ACCESS_DENIED;
			}

		}catch(Exception ex)
		{
			addActionError("System error.");
			log.error("Error in loadAllInventory." , ex);
		}

		return SUCCESS;
	}
	
	public String loadInventoryByGroup(){

		User user = (User) session.get(Constants.USER);

		try{

			if (user != null){

				if(groupId > 0){


					InventoryManager im = new InventoryManager();

					inventoryList = im.loadAllInventoryByUserAndGroup(user.getId(), getGroupId());

					InventoryGroup grp = im.loadInvetoryGroupById(getGroupId());
					if (grp != null){
						session.put("groupName", grp.getGroupName());
					}

				}else{
					addActionError("Invalid group, please re-load");
				}

			}else{
				return Constants.ACCESS_DENIED;
			}

		}catch(Exception ex)
		{
			addActionError("System error.");
			log.error("Error in loadInventoryByGroup." , ex);
		}

		return SUCCESS;
	}

	/**
	 * display the add or edit page
	 * @return
	 */
	public String manageItems(){

		try{
			User user = (User)session.get(Constants.USER);

			if (user != null){
				//load groups
				InventoryManager im = new InventoryManager();

				List<InventoryGroup> mngIg = im.loadSideMenuGroupsForUser(user.getId());

				session.put(Constants.sideMenuInventoryGroups, mngIg);

				if (addItem){
					inventory = new Inventory();
					inventory.setGroupId(groupId);

				}else{

					inventory = im.loadInventoryById(inventory.getId());

				}
			}else{
				return Constants.ACCESS_DENIED;
			}
		}catch(Exception ex){
			addActionError("System error.");
			log.error("Error in manageItems." , ex);
		}
		return SUCCESS;
	}
	
	/**
	 * ajax
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String addToSideMenu(){

		User user = (User)session.get(Constants.USER);

		String retVal = SUCCESS;

		try{
			if (user != null){
				List<InventoryGroup> imSideMenu = (List<InventoryGroup>) session.get("sideMenuInventory");

				boolean nameNotEmpty = true;
				//check if not empty
				if (newGroupName == null || newGroupName.trim().length() == 0){
					ajaxGroup = new AjaxGroup();
					ajaxGroup.setError("Please enter a new Group Name");
					retVal = "error";
					nameNotEmpty = false;
				}				
				
				if (nameNotEmpty){
					//check if group name already exist
					boolean exist = false;
					for(InventoryGroup ig : imSideMenu){
						if (ig.getGroupName().equalsIgnoreCase(newGroupName)){
							exist = true;
							break;
						}
					}

					//if group does not exist, add it
					if (!exist){

						InventoryManager im = new InventoryManager();

						InventoryGroup ig = new InventoryGroup();
						ig.setGroupName(newGroupName.trim());
						ig.setOwnerId(user.getId());
						im.addInvGroup(ig);

						imSideMenu.add(ig);

						ajaxGroup = new AjaxGroup();
						ajaxGroup.setGroupId(ig.getId());


					}else{
						ajaxGroup = new AjaxGroup();
						ajaxGroup.setError("Group already exist");
						retVal = "error";
					}
				}
			}else{
				return Constants.ACCESS_DENIED;
			}
		}catch(Exception ex){
			ajaxGroup = new AjaxGroup();
			ajaxGroup.setError("System error");
			retVal = "error";
			log.error("Error in addToSideMenu." , ex);
		}

		return retVal;
	}	

	public String saveItem(){
		
		try{
								
			User user = (User)session.get(Constants.USER);

			if (user != null){
		
				//check
				if (inventory != null && inventory.getName().trim().length() == 0 ){				
					addFieldError("itemName", "Please enter an item name");
				}
				if (inventory != null && inventory.getGroupId() <= 0 ){			
					addFieldError("groupId", "Please select a group");
				}	

				if (!hasFieldErrors()){
					
					if (getThumbImage() != null){
						
						//encode it and save
						BufferedImage bi = Scalr.resize(ImageIO.read(getThumbImage()), 100);
						
						inventory.setThumbBase64(imgToBase64(bi));
					}
					
					InventoryManager im = new InventoryManager();

					if (inventory != null && inventory.getId() > 0){
						//this is an edit
						im.update(inventory);

						setGroupId(inventory.getGroupId());

					}else{
						//add
						inventory.setOwnerId(user.getId());

						Inventory saved = im.add(inventory);

						setGroupId(saved.getGroupId());
					}
				}
				else{
					return INPUT;
				}
			}else{
				return Constants.ACCESS_DENIED;
			}
		}catch(Exception ex){
			addActionError("System error.");
			log.error("Error in saveItem." , ex);
		}

		return SUCCESS;

	}
	
	public String deleteInventoryItem(){

		ajaxItem = new AjaxItem();
		try{

			User user = (User)session.get(Constants.USER);
			
			if (user != null){
				InventoryManager im = new InventoryManager();

				Inventory item = im.loadInventoryById(getItemId());

				//1st look if user is owner of the item.
				InventoryGroup ig = im.loadInvetoryGroupById(item.getGroupId());

				
				if (ig.getOwnerId() == user.getId()){			
					im.deleteItem(getItemId());		
					ajaxItem.setDeleteSuccess(true);

				}else{
					ajaxItem.setError("User does not have access to delete");
				}
			}else{
				ajaxItem.setError("Cannot delete, you are not logged in.");
			}

		}catch(Exception ex){
			ajaxItem.setError("Critical error: " + ex.getMessage() );
			log.error("Error in deleteInventoryItem." , ex);
		}
		return SUCCESS;
	}
	
	public String deleteGroup(){

		try{

			User user = (User)session.get(Constants.USER);
			if (user != null){
				InventoryManager im = new InventoryManager();
				
				im.deleteGroup(groupId, user.getId());
			}else{
				return Constants.ACCESS_DENIED;
			}
		}catch (Exception ex){
			addActionError("System error.");
			log.error("Error in deleteGroup." , ex);
		}
		return SUCCESS;
	}
	
	public String renameInvGroup(){

		System.out.println("renameInvGroup: " + newGroupName + " groupId: " + groupId);

		try{

			User user = (User)session.get(Constants.USER);
			if (user != null){

				if(newGroupName != null && newGroupName.trim().length() > 0){


					InventoryManager im = new InventoryManager();

					InventoryGroup grp = im.loadInvetoryGroupById(groupId);
					grp.setGroupName(newGroupName);

					im.updateInvGroup(grp);
					
					//reload side menu
					List<InventoryGroup> mngIg = im.loadSideMenuGroupsForUser(user.getId());

					session.put(Constants.sideMenuInventoryGroups, mngIg);

				}

			}else{
				return Constants.ACCESS_DENIED;
			}
		}catch (Exception ex){
			addActionError("System error.");
			log.error("Error in deleteGroup." , ex);
		}


		return SUCCESS;
	}
	
//	public String generateQrCodeImage(){
//		
//		System.out.println("Gen qr code");
//		
//		return SUCCESS;
//	}
	
	private String imgToBase64( BufferedImage bi ) throws IOException{
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		ImageIO.write(bi, "jpg", output);
		return DatatypeConverter.printBase64Binary(output.toByteArray());
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;		
	}

	public List<Inventory> getInventoryList() {
		return inventoryList;
	}
	public void setInventoryList(List<Inventory> inventoryList) {
		this.inventoryList = inventoryList;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getNewGroupName() {
		return newGroupName;
	}

	public void setNewGroupName(String newGroupName) {
		this.newGroupName = newGroupName;
	}

	public Boolean getAddItem() {
		return addItem;
	}

	public void setAddItem(Boolean addItem) {
		this.addItem = addItem;
	}

	public String getAjaxError() {
		return ajaxError;
	}

	public void setAjaxError(String ajaxError) {
		this.ajaxError = ajaxError;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	public String getFormGroupName() {
		return formGroupName;
	}

	public void setFormGroupName(String formGroupName) {
		this.formGroupName = formGroupName;
	}

	public AjaxGroup getAjaxGroup() {
		return ajaxGroup;
	}

	public void setAjaxGroup(AjaxGroup ajaxGroup) {
		this.ajaxGroup = ajaxGroup;
	}

	public AjaxItem getAjaxItem() {
		return ajaxItem;
	}

	public void setAjaxItem(AjaxItem ajaxItem) {
		this.ajaxItem = ajaxItem;
	}

	public File getThumbImage() {
		return thumbImage;
	}

	public void setThumbImage(File thumbImage) {
		this.thumbImage = thumbImage;
	}

	public String getThumbImageFileName() {
		return thumbImageFileName;
	}

	public void setThumbImageFileName(String thumbImageFileName) {
		this.thumbImageFileName = thumbImageFileName;
	}
}
