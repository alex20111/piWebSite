package net.project.db.entity;

import home.fileutils.FileUtils;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import net.project.common.Constants;
import net.project.exception.ValidationException;


public class Folder {
	//Table name
	public static String TBL_NAME 	= "folder_tbl";

	//columns for user
	public static final String ID 			= "id";
	public static final String FOLDER_NAME 	= "folder_name";
	public static final String LAST_UPDATE 	= "last_update";
	public static final String CREATED_DATE	= "created_date";
	public static final String FOLDER_SIZE	= "folder_size";
	public static final String OWNER_ID 	= "user_id";

	//DB fields
	private int    	id			= -1;
	private String 	folderName;
	private Date 	lastUpated;
	private Date 	createdDate;
	private long 	folderSize;
	private int 	ownerId = -1;

	//relations
	private Set<FileInfo> files;
	private List<FolderShare> sharedTo;// list of user that this folder is shared to

	private User owner;

	//folder info
	private int fileNumber = 0; //number of files in the folder

	public Folder(){}
	public Folder(ResultSet rs) throws SQLException{
		this.id 			= rs.getInt(ID);
		this.folderName		= rs.getString(FOLDER_NAME);
		this.lastUpated 	= rs.getTimestamp(LAST_UPDATE);
		this.createdDate 	= rs.getTimestamp(CREATED_DATE);
		this.folderSize		= rs.getLong(FOLDER_SIZE);
		this.ownerId 		= rs.getInt(OWNER_ID);
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public static String createTable(){
		StringBuilder create = new StringBuilder();
		create.append("CREATE TABLE " + TBL_NAME + " (");
		create.append("ID INT PRIMARY KEY auto_increment");
		create.append(", " + FOLDER_NAME + " VARCHAR(30)" );
		create.append(", " + LAST_UPDATE + " TIMESTAMP" ); 
		create.append(", " + CREATED_DATE + " TIMESTAMP" );
		create.append(", " + FOLDER_SIZE + " BIGINT" ); 
		create.append(", " + OWNER_ID + " INT" ); 	
		create.append(")");

		return create.toString();
	}
	public String getFolderName() {
		return folderName;
	}
	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}
	public Date getLastUpated() {
		return lastUpated;
	}
	public void setLastUpated(Date lastUpated) {
		this.lastUpated = lastUpated;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Integer getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}
	/**
	 * return the complete Folder as a abstract File
	 * @param user
	 * @return
	 * @throws ValidationException
	 */
	public File getFolderAsFile(User user) throws ValidationException{

		if (user == null){
			throw new ValidationException("User is needed");
		}

		File dir = new File(Constants.UPL_BASE_DIR + user.getUserName() + File.separatorChar + this.folderName);

		return dir;

	}
	/**
	 * return the complete Folder as a string
	 * @param user
	 * @return
	 * @throws ValidationException
	 */
	public String getFolderLocAsString(User user) throws ValidationException{

		if (user == null){
			throw new ValidationException("User is needed");
		}

		String dir = Constants.UPL_BASE_DIR + user.getUserName() + File.separatorChar + this.folderName;

		return dir;

	}

	public Set<FileInfo> getFiles() {
		return files;
	}
	public void setFiles(Set<FileInfo> files) {
		this.files = files;
	}
	public User getOwner() {
		return owner;
	}
	public void setOwner(User owner) {
		this.owner = owner;
	}
	public int getFileNumber() {
		return fileNumber;
	}
	public void setFileNumber(int fileNumber) {
		this.fileNumber = fileNumber;
	}
	public List<FolderShare> getSharedTo() {
		return sharedTo;
	}
	public void setSharedTo(List<FolderShare> sharedTo) {
		this.sharedTo = sharedTo;
	}

	/**
	 * return true if the user has access to the folder.
	 * @return
	 */
	public boolean hasUserFolderAccess(int userId){

		if (this.ownerId == userId){
			return true;
		}else if (this.sharedTo != null && this.sharedTo.size() > 0){
			for(FolderShare share : this.sharedTo){
				if (share.getToUserId() == userId){
					return true;
				}
			}
		}

		return false;
	}
	public boolean canUpload(int userId){

		if (this.ownerId == userId){
			return true;
		}else if (this.sharedTo != null && this.sharedTo.size() > 0){
			for(FolderShare share : this.sharedTo){
				if (share.getToUserId() == userId && share.isUpload()){
					return true;
				}
			}
		}

		return false;
	}
	public boolean canDeleteFolder(int userId){

		if (this.ownerId == userId){
			return true;
		}else if (this.sharedTo != null && this.sharedTo.size() > 0){
			for(FolderShare share : this.sharedTo){		
				if (share.getToUserId() == userId && share.isDeleteFolder()){
					return true;
				}
			}
		}

		return false;
	}
	public boolean canDeleteFile(int userId){

		if (this.ownerId == userId){
			return true;
		}else if (this.sharedTo != null && this.sharedTo.size() > 0){
			for(FolderShare share : this.sharedTo){
				if (share.getToUserId() == userId && share.isDeleteFile()){
					return true;
				}
			}
		}

		return false;
	}
	public boolean canDownload(int userId){

		if (this.ownerId == userId){
			return true;
		}else if (this.sharedTo != null && this.sharedTo.size() > 0){
			for(FolderShare share : this.sharedTo){
				if (share.getToUserId() == userId && share.isDownload()){
					return true;
				}
			}
		}

		return false;
	}

	public long getFolderSize() {
		return folderSize;
	}
	public void setFolderSize(long folderSize) {
		this.folderSize = folderSize;
	}
	public String getReadeableFolderSize(){
		if(this.folderSize > 0){
			
			return FileUtils.fileSize(this.folderSize);
		}
		return "N/A";
		
	}
	/**
	 * Verify if this folder is shared to the current viewing user.
	 * 
	 * @param userId
	 * @return
	 */
	public boolean sharedFolder(int userId){
		if (this.ownerId != userId){
			return true;
		}		
		return false;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Folder [id=");
		builder.append(id);
		builder.append(", folderName=");
		builder.append(folderName);
		builder.append(", lastUpated=");
		builder.append(lastUpated);
		builder.append(", createdDate=");
		builder.append(createdDate);
		builder.append(", ownerId=");
		builder.append(ownerId);
		builder.append("]");
		return builder.toString();
	}
}
