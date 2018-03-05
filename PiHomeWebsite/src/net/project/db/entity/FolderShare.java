package net.project.db.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
/**
 * Table containing all user that the folder is shared to.
 * i.e: folder A is shared to user of FolderShare B - C - d.
 * @author axb161
 *
 */
public class FolderShare {


	//Table name
	public static String TBL_NAME 	= "folder_share_tbl";

	//columns for user
	public static final String ID 			= "id";
	public static final String FOLDER_ID	= "folder_id";
	public static final String TO_USER 		= "to_user_id";
	public static final String OWNER_USER 	= "owner_user_id";
	public static final String DOWNLOAD		= "download_access";
	public static final String UPLOAD	 	= "upload_access";
	public static final String DELETE_FLD 	= "delete_Folder_access";
	public static final String DELETE_FILE 	= "delete_File_access";
	public static final String SHARE_CRTD	= "share_created";
	public static final String SHARE_UPD	= "share_updated";

	//DB fields
	private int     id				= -1;
	private int    	folderId		= -1;
	private int    	toUserId		= -1;
	private int    	ownerUserId		= -1;
	private boolean download 		= false;		
	private boolean upload 			= false;
	private boolean deleteFolder	= false;
	private boolean deleteFile		= false;
	private Date shareCreated 		;
	private Date shareUpdated 			;
	
	//not db fields
	private String folderName;
	private User owner;
	

	public FolderShare(){}
	public FolderShare(ResultSet rs) throws SQLException{
		this.id 			= rs.getInt(ID);
		this.folderId 		= rs.getInt(FOLDER_ID);
		this.toUserId		= rs.getInt(TO_USER);
		this.ownerUserId 	= rs.getInt(OWNER_USER);
		this.download 		= rs.getBoolean(DOWNLOAD);
		this.upload 		= rs.getBoolean(UPLOAD);
		this.deleteFolder 	= rs.getBoolean(DELETE_FLD);
		this.deleteFile 	= rs.getBoolean(DELETE_FILE);
		this.shareCreated 	= rs.getTimestamp(SHARE_CRTD);
		this.shareUpdated 	= rs.getTimestamp(SHARE_UPD);
	}


	public static String createTable(){
		StringBuilder create = new StringBuilder();
		create.append("CREATE TABLE " + TBL_NAME + " (");
		create.append(" " + ID + " INT PRIMARY KEY auto_increment");
		create.append(", " + FOLDER_ID + " INT" );
		create.append(", " + TO_USER + " INT" );
		create.append(", " + OWNER_USER + " INT" );		 
		create.append(", " + DOWNLOAD + " TINYINT(1)" );
		create.append(", " + UPLOAD + " TINYINT(1)" ); 
		create.append(", " + DELETE_FLD + " TINYINT(1)" );
		create.append(", " + DELETE_FILE + " TINYINT(1)" );
		create.append(", " + SHARE_CRTD + " TIMESTAMP" );
		create.append(", " + SHARE_UPD + " TIMESTAMP" );
		create.append(")");

		return create.toString();
	}
	public int getToUserId() {
		return toUserId;
	}
	public void setToUserId(int toUserId) {
		this.toUserId = toUserId;
	}
	public int getOwnerUserId() {
		return ownerUserId;
	}
	public void setOwnerUserId(int ownerUserId) {
		this.ownerUserId = ownerUserId;
	}
	public boolean isDownload() {
		return download;
	}
	public void setDownload(boolean download) {
		this.download = download;
	}
	public boolean isUpload() {
		return upload;
	}
	public void setUpload(boolean upload) {
		this.upload = upload;
	}
	public Date getShareCreated() {
		return shareCreated;
	}
	public void setShareCreated(Date shareCreated) {
		this.shareCreated = shareCreated;
	}
	public Date getShareUpdated() {
		return shareUpdated;
	}
	public void setShareUpdated(Date shareUpdated) {
		this.shareUpdated = shareUpdated;
	}
	public int getFolderId() {
		return folderId;
	}
	public void setFolderId(int folderId) {
		this.folderId = folderId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isDeleteFolder() {
		return deleteFolder;
	}
	public void setDeleteFolder(boolean deleteFolder) {
		this.deleteFolder = deleteFolder;
	}
	public boolean isDeleteFile() {
		return deleteFile;
	}
	public void setDeleteFile(boolean deleteFile) {
		this.deleteFile = deleteFile;
	}
	public String getFolderName() {
		return folderName;
	}
	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}
	public User getOwner() {
		return owner;
	}
	public void setOwner(User owner) {
		this.owner = owner;
	}
}
