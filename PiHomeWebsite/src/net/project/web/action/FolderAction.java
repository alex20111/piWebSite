package net.project.web.action;

import home.fileutils.CompressionLevel;
import home.fileutils.Zip;
import home.fileutils.ZipMethod;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.project.bean.ShareToBean;
import net.project.common.Constants;
import net.project.db.entity.FileInfo;
import net.project.db.entity.Folder;
import net.project.db.entity.FolderShare;
import net.project.db.entity.User;
import net.project.db.manager.FolderManager;
import net.project.db.manager.UserManager;
import net.project.exception.ValidationException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

public class FolderAction extends ActionSupport implements SessionAware{

	private Log log = LogFactory.getLog(getClass());

	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;

	private InputStream fileInputStream;
	private String fileNameToDownload			= "";
	private FolderManager folderManager;

	private List<Folder> dirList;	

	//from form
	private String newFolder;
	private List<Integer> folderIdList;

	private Integer folderId;	

	//form buttons folder page
	private String addNewFolderBtn;
	private String deleteFolderBtn;
	private String shareFolderBtn;	
	//
	//fileUpload
	//
	private File file;
	private String fileFileName;

	//result of file upload
	private String result = "";
	private Map<String, String> errorUpl;
	private String fileToCheck;
	
	//
	// folder file page values
	//
	private Folder viewFolder;
	
	//btn
	private String downloadFilesBtn;
	private String deleteFilesBtn;
	
	private List<Integer> fileIdList;
	
	//share page
	private List<User> userList;
	private String addShareBtn;
	private String updateShareBtn;
	private String deleteShareBtn;
	private Integer userId;
	private boolean addDownChk;
	private boolean addUplChk;
	private boolean addDelFolderChk;
	private boolean addDelFileChk;
	private List<ShareToBean> shareToList;
	private List<ShareToBean> shareFrom;
	
	private List<Integer> shareIdSh;
	private List<Integer> userIdSh;
	private List<Boolean> updDownChk;
	private List<Boolean> updDelFldChk;
	private List<Boolean> updDelFileChk;
	private List<Boolean> updUplChk;
	private List<String> delShare;

	
	private Boolean folderExpanded = true;
	
	/**
	 * called by manage folders link
	 * @return
	 */
	public String loadDirectoriesForUser(){

		String retVal = SUCCESS;
		User user = (User)session.get(Constants.USER);

		try{
			if (user != null){

				folderManager = new FolderManager();

				dirList = folderManager.loadFoldersByUserId(user.getId(), false, true, true, true, false);
				
				updateSideMenuFolderList(null, dirList);

			}else{
				retVal = Constants.ACCESS_DENIED;
			}
		}catch (Exception ex){
			log.error("Error in loadDirectoriesForUser" , ex);
		}

		return retVal;
	}
	/**
	 * manage the folder page acions
	 * @return
	 */
	public String manageFolderActions(){

		String retVal = SUCCESS;
		try{
			if (getAddNewFolderBtn() != null){
				retVal = addNewFolder();
			}else if (getDeleteFolderBtn() != null && getDeleteFolderBtn().length() > 0){
				deleteFolders();
			}else if (getShareFolderBtn() != null){
				retVal = shareFolder();
			}
		}catch(Exception ex){
			addActionError("Error, please contact system admin");
			log.error("Error in manageFolderActions" , ex);
		}

		return retVal;
	}
	
	
	public String manageFilesActions(){

		String retVal = SUCCESS;
		try{

			
			if (getDownloadFilesBtn() != null){
				retVal = fileDownload();
			}else if (getDeleteFilesBtn() != null){
				 deleteFiles();
				 retVal = loadFolderFiles();
			}
		}catch(Exception ex){
			addActionError("Error, please contact system admin");
			log.error("Error in manageFilesActions" , ex);
		}

		return retVal;
	}
	
	private String addNewFolder() throws SQLException, ClassNotFoundException{

		User user = (User)session.get(Constants.USER);
		String retVal = SUCCESS;

		if (user != null){

			folderManager = new FolderManager();
			
			if (newFolder != null && newFolder.trim().length() > 0){

				try{					
					Folder fld = new Folder();
					fld.setOwnerId(user.getId());
					fld.setFolderName(getNewFolder().trim());
					fld.setCreatedDate(new Date());

					folderManager.addFolder(fld);

					addActionMessage("Folder created successfully");	
					
					setNewFolder("");
					
				}catch (ValidationException ve){
					addActionError(ve.getMessage());
				}
			}else{
				addActionError("Please enter a folder name");
			}
			
			dirList = folderManager.loadFoldersByUserId(user.getId(), false, true, true, true, false);

			session.put(Constants.sideMenuFolderList, dirList);
			folderIdList = new ArrayList<Integer>(dirList.size());
			
		}else{
			retVal = Constants.ACCESS_DENIED;

		}
		return retVal;
	}
	
	private void deleteFolders() throws SQLException, ValidationException, ClassNotFoundException{

		User user = (User)session.get(Constants.USER);

		folderManager = new FolderManager();

		List<Integer> folderIds = new ArrayList<Integer>();
		for(Integer id : folderIdList){
			if (id != null){
				folderIds.add(id);
			}
		}

		if (folderIds.size() > 0 ){

			folderManager.deleteFolders(folderIds, user);
		}

		//delete the files on the disk also	
		dirList = folderManager.loadFoldersByUserId(user.getId(), false, true, true, true, false);

		session.put(Constants.sideMenuFolderList, dirList);
		
		setDeleteFolderBtn(null);

	}
	private String shareFolder(){

		String retVal = "toShare";
		User user = (User)session.get(Constants.USER);

		try{
			if (user != null){
				folderManager = new FolderManager();
				UserManager userMan = new UserManager();

				dirList = folderManager.loadFoldersByUserId(user.getId(), false, false, false, false, true);
				userList = userMan.loadAllUsers();
				
				//remove current user
				if (userList.size() > 0){
					
					int idx = -1;
					
					for(int i = 0 ; i < userList.size() ; i ++){
						User usr = userList.get(i);
						if (usr.getId() == user.getId()){
							idx = i;
						}
					}
					
					if (idx >= 0){
						userList.remove(idx);
					}
				}
				

				shareToList = new ArrayList<ShareToBean>();
				Map<Integer, ShareToBean> tmpSaveToList = new HashMap<Integer, ShareToBean>();
				
				for(Folder f: dirList){
					
					if (f.getSharedTo() != null){
						for(FolderShare fs : f.getSharedTo()){
							if (tmpSaveToList.containsKey(fs.getToUserId())){
								fs.setFolderName(f.getFolderName());
								ShareToBean share = tmpSaveToList.get(fs.getToUserId());
								share.addShare(fs);

							}else{
								ShareToBean share = new ShareToBean();
								User usr = userMan.loadUserById(fs.getToUserId());
								share.setFullName(usr.fullName());
								share.addShare(fs);
								fs.setFolderName(f.getFolderName());
								tmpSaveToList.put(fs.getToUserId(), share);
							}
						}
					}
				}
				
				if (tmpSaveToList.size() > 0){
					for(Map.Entry<Integer, ShareToBean> stb : tmpSaveToList.entrySet()){
						shareToList.add(stb.getValue());
					}
				}
				
				List<FolderShare> shFr = folderManager.loadShareFromByUserId(user.getId());
				
				Map<Integer, ShareToBean> fromtmp = new HashMap<Integer, ShareToBean>();				
				for (FolderShare s2 : shFr){
					
					if (fromtmp.containsKey(s2.getOwnerUserId())){
						ShareToBean share = fromtmp.get(s2.getOwnerUserId());
						share.addShare(s2);
					}else{
						ShareToBean share = new ShareToBean();
						share.setFullName(s2.getOwner().fullName());						
						share.addShare(s2);
						
						fromtmp.put(s2.getOwnerUserId(), share);
					}				
					
				}
				shareFrom = new ArrayList<ShareToBean>();
				if (fromtmp.size() > 0){
					for(Map.Entry<Integer, ShareToBean> stb2 : fromtmp.entrySet()){
						shareFrom.add(stb2.getValue());
					}
				}
				
				
			}else{
				retVal = Constants.ACCESS_DENIED;
			}
		}catch(Exception ex){
			addActionError("Error");
			log.error("Error in shareFolder" , ex);
		}
		return retVal;
		
		
	}
	private void deleteFiles() throws SQLException, ValidationException, ClassNotFoundException{

		User user = (User)session.get(Constants.USER);

		folderManager = new FolderManager();

		List<Integer> filesIds = new ArrayList<Integer>();
		for(Integer id : fileIdList){
			if (id != null){
				filesIds.add(id);
			}
		}

		if (filesIds.size() > 0 ){

			folderManager.deleteFiles(filesIds, user.getUserName(), folderId);
		}	
	}
	/**
	 * load files for the folder
	 * 
	 * @return
	 */
	public String loadFolderFiles(){
		String retVal = SUCCESS;
		User user = (User)session.get(Constants.USER);

		try{
			if (user != null && folderId != null){
				folderManager = new FolderManager();

				viewFolder = folderManager.loadFolderByDirId(folderId, true, true, true, true);

				//if we have a folder and its not the user folder or not a shared folder, then access denien
				if(viewFolder!= null && !viewFolder.hasUserFolderAccess(user.getId())){					
					retVal = Constants.ACCESS_DENIED;					
				}else if (viewFolder == null){
					//if the folder is null, access denied					
					retVal = Constants.ACCESS_DENIED;	
				}else{
					//load success
					updateSideMenuFolderList(viewFolder, null);
					setFolderId(viewFolder.getId());
				}				

			}else {
				retVal = Constants.ACCESS_DENIED;
			}
		}catch(Exception ex){
			addActionError("Error while loading folder");
			log.error("error in loadFolderFiles" , ex);
		}
		return retVal;
	}
	/**
	 * upload new files to the folder
	 * @return
	 */
	public String uploadFilesToFolder(){

		String retVal = SUCCESS;

		User user = (User) session.get(Constants.USER);

		if (user != null){

			try{


				folderManager = new FolderManager();
				Folder fld = folderManager.loadFolderByDirId(folderId, false, false, false, false);

				File folder = fld.getFolderAsFile(user);
				//check if folder exist:

				if (!folder.exists()){
					//create it
					folder.mkdirs();
				}				

				File newFile = new File(fld.getFolderLocAsString(user) + File.separatorChar + fileFileName);

				//rename and more the new file

				FileUtils.copyFile(file, newFile );
				
				//get file length 
				long fileSize = newFile.length();
				
				//save on DB
				FileInfo dbFile = new FileInfo();
				dbFile.setFileCreated(new Date());
				dbFile.setFileName(fileFileName);
				dbFile.setFileSize(fileSize);
				dbFile.setFolderId(folderId);

				folderManager.addFile(dbFile);	
				
				fld.setFolderSize(fld.getFolderSize() + fileSize);
				
				folderManager.updateFolder(fld);
				
				result = "Files suucccesss";

			}catch(Exception ex){
				log.error("error in uploading" , ex);

				if (file != null && file.exists()){
					//erase file if problem occured
					file.delete();
				}

				errorUpl = new HashMap<String, String>();
				errorUpl.put("jquery-upload-file-error", "Unexpected error.");

				retVal = "error";
			}
		}
		else{
			errorUpl = new HashMap<String, String>();
			errorUpl.put("jquery-upload-file-error", "You are not logged in, cannot upload file");

			retVal = "error";
		}
		return retVal;
	}
	/**
	 * Download any files by stream
	 *  
	 * @return
	 */
	public String fileDownload(){

		String retVal = "download";
		User user = (User) session.get(Constants.USER);
		try{
			folderManager = new FolderManager();
			Folder fld = folderManager.loadFolderByDirId(folderId, true, false, false, true);

			if (fld != null && fld.canDownload(user.getId())){
				
				fileIdList.removeAll(Collections.singleton(null));
				
				String downloadPath = ""; //base dir for the file based if it's a single file or multiple files.
				
				if (fileIdList.size() == 1){
					//only download the selected file.
					for(FileInfo f : fld.getFiles()){
						if (f.getId() == fileIdList.get(0).intValue()){
							downloadPath = fld.getFolderLocAsString(user) + File.separatorChar;
							fileNameToDownload = f.getFileName();
							break;
						}
					}
					
				}else if (fileIdList.size() > 1){					
					
					downloadPath = Constants.UPL_BASE_DIR;
					fileNameToDownload = fld.getFolderName()  + new SimpleDateFormat("yyyy-MM-dd_hh_mm_ss").format(new Date()) + ".zip" ; //file name to display while downloading
					
					//zip the selected files and download
					Zip z = new Zip(downloadPath + fileNameToDownload, false);
					
					for(Integer id : fileIdList){
						for(FileInfo fi : fld.getFiles()){
							if (id.intValue() == fi.getId()){
								z.addFile(new File(fi.getFileLocAsString(fld.getFolderName(), user.getUserName())));
								break;
							}
						}
					}
					
					z.compress(CompressionLevel.NoComp, ZipMethod.Deflated);
				}
				
				
				
				File file = new File(downloadPath + fileNameToDownload  );

				if (file.exists())
				{
					try 
					{ 
						fileInputStream = new FileInputStream(file);
					} 	
					catch (IOException ex)
					{

						log.error("error in fileDownload", ex );
						addFieldError("FileError", "File download error");
						retVal = "input";
					} 		
				}		
				else
				{
					addFieldError("FileDoNotExist", "File do not exist");
					retVal = "input";
				}
			}else if (fld != null && !fld.canDownload(user.getId())){
				addActionError("Access denied to download");
			}
		}catch(Exception ex){
			addActionError("Error in downloading, please contact system admin");
			retVal = "input";
			log.error("Error in fileDownload" , ex);
		}
		return retVal;
	}
	/**
	 * before uploading, check if the file exist.
	 * @return
	 */
	public String checkIfFileExist(){

		User user = (User) session.get(Constants.USER);

		if (user != null){

			try{
				folderManager = new FolderManager();
				Folder fld = folderManager.loadFolderByDirId(folderId, false, false, false, false);
				
				File f = new File(fld.getFolderLocAsString(user) + File.separatorChar + fileToCheck  );			

				if (f.exists()){
					result = "file Exist on disk";
				}
				else{
					result = "success";
				}

			}catch (Exception ex){
				log.error("Error in checkIfFileExist" , ex);
			}
		}
		else
		{
			result = "ERROR not logged in";
		}
		return SUCCESS;
	}

	public String uploadPage(){
		//verify if the user has access to the upload page.
		String retVal = SUCCESS;

		User user = (User)session.get(Constants.USER);

		try{
			if (user != null ){

				//get the folder id
				folderManager = new FolderManager();

				viewFolder = folderManager.loadFolderByDirId(folderId, false, true, false, true);
				
				if (!viewFolder.canUpload(user.getId())){					
					retVal = Constants.ACCESS_DENIED;
				}			

			}else{
				retVal = Constants.ACCESS_DENIED;
			}
		}catch(Exception ex){
			log.error("Error in uploadPage verificatiob" , ex);
			retVal = Constants.ACCESS_DENIED;
		}

		return retVal;
	}
	/**
	 * Update data after upload.
	 * @return
	 */
	public String updateUponSuccess(){

		try{		
	
			
			folderManager = new FolderManager();

			Folder fld = folderManager.loadFolderByDirId(folderId, false, false, false, true);			

			//update folder last updated:
			fld.setLastUpated(new Date());
			
			folderManager.updateFolder(fld);	
							

			
		}catch(Exception ex){
			log.error("Error in updateUponSuccess" , ex);
		}
		return SUCCESS;
	}

	private void updateSideMenuFolderList(Folder singleFolder, List<Folder> foldersList){


		if (singleFolder != null){

			@SuppressWarnings("unchecked")
			List<Folder> folders = (List<Folder>) session.get(Constants.sideMenuFolderList);

			if (folders == null){
				folders = new ArrayList<Folder>();
			}


			//	match and replace
			if (folders.size() > 0){
				for (Folder f : folders){
					if (f.getId() == viewFolder.getId()){
						f.setFileNumber(viewFolder.getFileNumber());
						f.setLastUpated(viewFolder.getLastUpated());						
					}
				}
			}
			else
			{
				folders.add(viewFolder);
			}

			session.put(Constants.sideMenuFolderList, folders);

		}else if (foldersList != null && foldersList.size() > 0){
			session.put(Constants.sideMenuFolderList, foldersList);
		}


	}
	
	public String manageSharesActions(){
		
			
		String retVal = SUCCESS;
		
		session.put("expandAddInShare", false);
		if (getAddShareBtn() != null){
			retVal = addShare();
		}else if (getUpdateShareBtn() != null){
			updateShareTo();
		}else if (getDeleteShareBtn() != null){
			retVal = deleteShare();
		}
		
		shareFolder();
		
		return retVal;
	}
	
	private String addShare(){

		try{
			session.put("expandAddInShare", true);
			
			if (!addDelFolderChk && !addDelFileChk && !addDownChk && !addUplChk){
				addActionError("Please select an access right option");

			}
			if (folderId == null){
				addActionError("Please select a folder");
			}

			if (userId == null){
				addActionError("Please select a user ");
			}
			if (!hasActionErrors()){
				folderManager = new FolderManager();

				Folder fl = folderManager.loadFolderByDirId(folderId, false, false, false, false);		


				//check if share exist before saving. //TODO
				FolderShare oldShare = folderManager.loadShareByFolderAndUserId(fl.getId(), userId);

				if (oldShare == null){
					FolderShare s = new FolderShare();
					s.setDeleteFile(addDelFileChk);
					s.setDeleteFolder(addDelFolderChk);
					s.setDownload(addDownChk);
					s.setFolderId(folderId);
					s.setOwnerUserId(fl.getOwnerId());
					s.setShareCreated(new Date());
					s.setShareUpdated(new Date());
					s.setUpload(addUplChk);
					s.setToUserId(userId);

					folderManager.addShare(s);
					
					setAddDelFileChk(false);
					setAddDelFolderChk(false);
					setAddDownChk(false);
					setAddUplChk(false);
					setUserId(-1);
					setFolderId(-1);
				}
				else
				{
					addActionError("Share already exist");
				}

			}
		}catch(Exception ex)
		{
			log.error("Error in addShare" , ex);
			addActionError("Error in adding new share");
		}


		return SUCCESS;
	}
	
	public String updateShareTo(){

		try{
			//see if any fields has changed
			List<FolderShare> toUpdShare = new ArrayList<FolderShare>();

			folderManager = new FolderManager();

			for(int i = 0 ; i <  shareIdSh.size() ; i ++){

				FolderShare fsh = new FolderShare();
				fsh.setDeleteFile(updDelFileChk.get(i));
				fsh.setDeleteFolder(updDelFldChk.get(i));
				fsh.setDownload(updDownChk.get(i));
				fsh.setUpload(updUplChk.get(i));
				fsh.setToUserId(userIdSh.get(i));
				fsh.setId(shareIdSh.get(i));
				toUpdShare.add(fsh);
			}

			if (toUpdShare.size() > 0){
				boolean success = folderManager.updateSharesTOByShareList(toUpdShare);
				if (success){
					addActionMessage("Update successful");
				}
			}
		}catch(Exception ex){
			addActionError("Error in update");
			log.error("Error in updateShareTo" , ex);
		}
		return SUCCESS;
	}
	/**
	 * delete shared by their ids.
	 * @return
	 */
	private String deleteShare(){

		String retVal = SUCCESS;

		User user = (User)session.get(Constants.USER);

		try{
			if (user != null){

				if (delShare.size() > 0){					

					List<Integer> shareIds = new ArrayList<Integer>();

					for(String str : delShare){
						if (!"false".equalsIgnoreCase(str)){
							shareIds.add(Integer.parseInt(str));
						}
					}

					if (shareIds.size() > 0){
						folderManager = new FolderManager();
						
						folderManager.deleteShares(shareIds);
						
						addActionMessage("Share(s) deleted");
					}
				}
			}else{
				retVal = Constants.ACCESS_DENIED;
			}

		}catch(Exception ex){
			addActionError("Error in deleting shares");
			log.error("Error in deleteShare" , ex);
		}

		return retVal;
	}
	
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;

	}
	public InputStream getFileInputStream() {
		return fileInputStream;
	}
	public void setFileInputStream(InputStream fileInputStream) {
		this.fileInputStream = fileInputStream;
	}
	public String getFileNameToDownload() {
		return fileNameToDownload;
	}
	public void setFileNameToDownload(String fileNameToDownload) {
		this.fileNameToDownload = fileNameToDownload;
	}
	public List<Folder> getDirList() {
		return dirList;
	}
	public void setDirList(List<Folder> dirList) {
		this.dirList = dirList;
	}
	public String getNewFolder() {
		return newFolder;
	}
	public void setNewFolder(String newFolder) {
		this.newFolder = newFolder;
	}
	public String getAddNewFolderBtn() {
		return addNewFolderBtn;
	}
	public void setAddNewFolderBtn(String addNewFolderBtn) {
		this.addNewFolderBtn = addNewFolderBtn;
	}

	public String getDeleteFolderBtn() {
		return deleteFolderBtn;
	}
	public void setDeleteFolderBtn(String deleteFolderBtn) {
		this.deleteFolderBtn = deleteFolderBtn;
	}
	public String getShareFolderBtn() {
		return shareFolderBtn;
	}
	public void setShareFolderBtn(String shareFolderBtn) {
		this.shareFolderBtn = shareFolderBtn;
	}
	public List<Integer> getFolderIdList() {
		return folderIdList;
	}
	public void setFolderIdList(List<Integer> folderIdList) {
		this.folderIdList = folderIdList;
	}
	public Integer getFolderId() {
		return folderId;
	}
	public void setFolderId(Integer folderId) {
		this.folderId = folderId;
	}
	public Folder getViewFolder() {
		return viewFolder;
	}
	public void setViewFolder(Folder viewFolder) {
		this.viewFolder = viewFolder;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public Map<String, String> getErrorUpl() {
		return errorUpl;
	}
	public void setErrorUpl(Map<String, String> errorUpl) {
		this.errorUpl = errorUpl;
	}
	public String getFileToCheck() {
		return fileToCheck;
	}
	public void setFileToCheck(String fileToCheck) {
		this.fileToCheck = fileToCheck;
	}
	public String getFileFileName() {
		return fileFileName;
	}
	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}
	public String getDownloadFilesBtn() {
		return downloadFilesBtn;
	}
	public void setDownloadFilesBtn(String downloadFilesBtn) {
		this.downloadFilesBtn = downloadFilesBtn;
	}
	public String getDeleteFilesBtn() {
		return deleteFilesBtn;
	}
	public void setDeleteFilesBtn(String deleteFilesBtn) {
		this.deleteFilesBtn = deleteFilesBtn;
	}
	public List<Integer> getFileIdList() {
		return fileIdList;
	}
	public void setFileIdList(List<Integer> fileIdList) {
		this.fileIdList = fileIdList;
	}
	public List<User> getUserList() {
		return userList;
	}
	public void setUserList(List<User> userList) {
		this.userList = userList;
	}
	public String getAddShareBtn() {
		return addShareBtn;
	}
	public void setAddShareBtn(String addShareBtn) {
		this.addShareBtn = addShareBtn;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public boolean getAddDownChk() {
		return addDownChk;
	}
	public void setAddDownChk(boolean addDownChk) {
		this.addDownChk = addDownChk;
	}
	public boolean getAddUplChk() {
		return addUplChk;
	}
	public void setAddUplChk(boolean addUplChk) {
		this.addUplChk = addUplChk;
	}
	public boolean getAddDelFolderChk() {
		return addDelFolderChk;
	}
	public void setAddDelFolderChk(boolean addDelFolderChk) {
		this.addDelFolderChk = addDelFolderChk;
	}
	public boolean getAddDelFileChk() {
		return addDelFileChk;
	}
	public void setAddDelFileChk(boolean addDelFileChk) {
		this.addDelFileChk = addDelFileChk;
	}
	public List<ShareToBean> getShareToList() {
		return shareToList;
	}
	public void setShareToList(List<ShareToBean> shareToList) {
		this.shareToList = shareToList;
	}
	public List<Integer> getShareIdSh() {
		return shareIdSh;
	}
	public void setShareIdSh(List<Integer> shareIdSh) {
		this.shareIdSh = shareIdSh;
	}
	public List<Integer> getUserIdSh() {
		return userIdSh;
	}
	public void setUserIdSh(List<Integer> userIdSh) {
		this.userIdSh = userIdSh;
	}
	public List<Boolean> getUpdDownChk() {
		return updDownChk;
	}
	public void setUpdDownChk(List<Boolean> updDownChk) {
		this.updDownChk = updDownChk;
	}
	public List<Boolean> getUpdDelFldChk() {
		return updDelFldChk;
	}
	public void setUpdDelFldChk(List<Boolean> updDelFldChk) {
		this.updDelFldChk = updDelFldChk;
	}
	public List<Boolean> getUpdDelFileChk() {
		return updDelFileChk;
	}
	public void setUpdDelFileChk(List<Boolean> updDelFileChk) {
		this.updDelFileChk = updDelFileChk;
	}
	public List<Boolean> getUpdUplChk() {
		return updUplChk;
	}
	public void setUpdUplChk(List<Boolean> updUplChk) {
		this.updUplChk = updUplChk;
	}
	public String getUpdateShareBtn() {
		return updateShareBtn;
	}
	public void setUpdateShareBtn(String updateShareBtn) {
		this.updateShareBtn = updateShareBtn;
	}
	public List<ShareToBean> getShareFrom() {
		return shareFrom;
	}
	public void setShareFrom(List<ShareToBean> shareFrom) {
		this.shareFrom = shareFrom;
	}
	public List<String> getDelShare() {
		return delShare;
	}
	public void setDelShare(List<String> delShare) {
		this.delShare = delShare;
	}
	public String getDeleteShareBtn() {
		return deleteShareBtn;
	}
	public void setDeleteShareBtn(String deleteShareBtn) {
		this.deleteShareBtn = deleteShareBtn;
	}
	public Boolean getFolderExpanded() {
		return folderExpanded;
	}
	public void setFolderExpanded(Boolean folderExpanded) {
		this.folderExpanded = folderExpanded;
	}

}
