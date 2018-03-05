package net.project.web.action;

import home.crypto.BCryptHash;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import net.project.common.Constants;
import net.project.db.entity.User;
import net.project.db.manager.UserManager;
import net.project.enums.AccessEnum;
import net.project.utils.KeyGenerator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

public class UserAction extends ActionSupport implements SessionAware{

	private Log log = LogFactory.getLog(getClass());
	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;
	private String btnSave;

	private int userId = -1;//value passed by link
	
	private String userName;
	private String firstName;
	private String lastName;
	private String email;
	private AccessEnum access;
	private String apiKey;
	
	private List<User> userList;	

	public String loadAllUsers(){

		String retVal = SUCCESS;
		User user = (User)session.get(Constants.USER);
		try {
			if (user != null && user.getAccess() == AccessEnum.ADMIN){
				UserManager mngr = new UserManager();

				userList = mngr.loadAllUsers();				
			}
			else{
				retVal = Constants.ACCESS_DENIED;
			}
		} catch (SQLException | ClassNotFoundException e) {
			addActionError("Error , please see logs ");
			log.error("Error in loadAllUsers" , e);
		}
		return retVal;
	}
	
	public String loadAddEditUser(){

		String retVal = SUCCESS;
		try{
			User user = (User)session.get(Constants.USER);
			if (user != null){
				UserManager mngr = new UserManager();				
				
				//edit mode
				if (userId > 0 && ( user.getAccess() == AccessEnum.ADMIN || userId == user.getId()) ){
					
					User editUser = mngr.loadUserById(userId);
					//fill in the fields
					setUserName(editUser.getUserName());
					setFirstName(editUser.getFirstName());
					setLastName(editUser.getLastName());
					setEmail(editUser.getEmail());
					setAccess(editUser.getAccess());
					setApiKey(editUser.getApiKey());

				}else if (userId == -1 && user.getAccess() == AccessEnum.ADMIN){
					//add mode and 
					retVal = SUCCESS;
				}else{
					retVal = Constants.ACCESS_DENIED;
				}
				
			}else if (user == null){
				//allow user to add himself if wanted.. 
				//the user will have minimal access until review by admin
			}
		}catch(Exception ex){
			addActionError("Error , please see logs ");
			log.error("Error in loadAddEditUser" , ex);
		}
		return retVal;
	}
	
	public String saveUserInformation(){
		String retVal = SUCCESS;
		try{

			User user = (User)session.get(Constants.USER);

			if (user != null && user.getAccess() == AccessEnum.ADMIN){

				UserManager mngr = new UserManager();
				User userInfo = new User();
				
				if (userId > 0){ //Edit

					//1st check if username exist					
					userInfo = mngr.loadUserById(userId);
					
					if (!getUserName().trim().toLowerCase().equals(userInfo.getUserName())){
						//check if user exist
						User dupp = mngr.checkForDupplicateUser(getUserName().trim());
						if (dupp != null){
							addActionError("User name already exist, please change user name");
						}
					}
					
				}else{ //ADD
					userInfo = new User();
				}			
				
				if(!hasActionErrors()){
					userInfo.setAccess(getAccess());
					userInfo.setEmail(getEmail());
					userInfo.setFirstName(getFirstName());
					userInfo.setLastName(getLastName());
					userInfo.setNbOfTries(0);
					userInfo.setUserName(getUserName());
					userInfo.setApiKey(getApiKey());

					if (userId > 0){ //Edit					
						mngr.updateUser(userInfo);
					}else{ //ADD

						//1st check if the user already exist
						User userCheck = mngr.loadUserByUserName(userInfo.getUserName());

						if (userCheck == null){//user does not exist
							userInfo.setPassword(BCryptHash.hashString("12345"));
							mngr.addUser(userInfo);
						}
						else
						{
							addActionError("User name already exist, please change user name");
							return SUCCESS;
						}					
					}				
					addActionMessage("Save successful");

					if(userId > 0 && user.getId() == userId){
						session.put(Constants.USER, userInfo);
					}	
				}
			}
			else
			{
				retVal = Constants.ACCESS_DENIED;
			}

		}catch(Exception ex){
			addActionError("Error , please see logs ");
			log.error("Error in saveUserInformation" , ex);
		}
		return retVal;
	}	
	public String deleteUser(){
		String retVal = SUCCESS;

		User user = (User)session.get(Constants.USER);
		try{

			if (user != null && user.getAccess() == AccessEnum.ADMIN){
				UserManager mngr = new UserManager();

				if (getUserId() > 0){

					mngr.deleteUser(getUserId());
					userList = mngr.loadAllUsers();		//re-load all users
					addActionMessage("Delete successful");
				}

			}else{
				retVal = Constants.ACCESS_DENIED;
			}
		}catch(Exception ex){
			addActionError("Error in deleting the user, please see logs");
			log.error("error in deleteUser", ex);
		}
		return retVal;
	}
	public String restoreDenyAccess(){
		String retVal = SUCCESS;
		
		User user = (User)session.get(Constants.USER);
		try{

			if (user != null && user.getAccess() == AccessEnum.ADMIN){
				UserManager mngr = new UserManager();

				if (getUserId() > 0){

					User usr = mngr.loadUserById(userId);
					
					if (usr.getNbOfTries() < Constants.NBR_TRIES){
						//block user
						usr.setNbOfTries(Constants.NBR_TRIES);
						
						mngr.updateUser(usr);
						addActionMessage("Access blocked for user: " + usr.getUserName());
					}
					else
					{
						//Unblock user
						usr.setNbOfTries(0);
						
						mngr.updateUser(usr);
						addActionMessage("Access UnBlocked for user: " + usr.getUserName());
					}
					userList = mngr.loadAllUsers();		//re-load all users
				}

			}else{
				retVal = Constants.ACCESS_DENIED;
			}
		}catch(Exception ex){
			addActionError("Error in access, please see logs");
			log.error("error in restoreDenyAccess", ex);
		}
		return retVal;
	}
	public String generateApiKey(){
	
		apiKey = new KeyGenerator().generateKey(35);
		
		return SUCCESS;
	}

	
	@Override
	public void validate() 
	{
		if (getBtnSave() != null){
			if(getUserName() == null || getUserName().trim().length() ==0){
				addFieldError("userName","Please enter a user name ");
			}
			if(getFirstName() == null || getFirstName().trim().length() ==0){
				addFieldError("firstName","Please enter a First Name ");
			}
//			if(getLastName() == null || getLastName().trim().length() ==0){
//				addFieldError("lastName","Please enter a Last name ");
//			}
			if(getEmail() == null || getEmail().trim().length() ==0){
				addFieldError("email","Please enter a email ");
			}
			if(getAccess() == null || getAccess().toString().trim().length() ==0){
				addFieldError("access","Please enter a Access Level ");
			}
		}		
	}
	

	
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
		
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public AccessEnum getAccess() {
		return access;
	}
	public void setAccess(AccessEnum access) {
		this.access = access;
	}
	public String getBtnSave() {
		return btnSave;
	}
	public void setBtnSave(String btnSave) {
		this.btnSave = btnSave;
	}
	public List<User> getUserList() {
		return userList;
	}
	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}	
}