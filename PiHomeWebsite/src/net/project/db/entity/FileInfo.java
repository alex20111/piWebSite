package net.project.db.entity;

import home.fileutils.FileUtils;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import net.project.common.Constants;
import net.project.exception.ValidationException;

public class FileInfo {

	//Table name
	public static String TBL_NAME 	= "file_info_tbl";

	//columns for user
	public static final String ID 			= "id";
	public static final String FILE_NAME 	= "file_name";
	public static final String FILE_SIZE 	= "file_size";
	public static final String FILE_CREATED	= "file_created";
	public static final String FOLDER_ID	= "dir_id";

	//DB fields
	private int    	id			= -1;
	private String 	fileName;		
	private long    fileSize;
	private Date 	fileCreated;
	private int 	folderId = -1;

	public FileInfo(){}
	public FileInfo(ResultSet rs) throws SQLException{
		this.id 			= rs.getInt(ID);
		this.fileName		= rs.getString(FILE_NAME);
		this.fileSize 		= rs.getLong(FILE_SIZE);
		this.fileCreated 	= rs.getTimestamp(FILE_CREATED);
		this.folderId 		= rs.getInt(FOLDER_ID);


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
		create.append(", " + FILE_NAME + " VARCHAR(30)" );
		create.append(", " + FILE_SIZE + " BIGINT" ); 
		create.append(", " + FILE_CREATED + " TIMESTAMP" ); 
		create.append(", " + FOLDER_ID + " INT" ); 	
		create.append(")");

		return create.toString();
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public long getFileSize() {
		return fileSize;
	}
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
	public Date getFileCreated() {
		return fileCreated;
	}
	public void setFileCreated(Date fileCreated) {
		this.fileCreated = fileCreated;
	}
	public int getFolderId() {
		return folderId;
	}
	public void setFolderId(int folderId) {
		this.folderId = folderId;
	}
	public String getFileLocAsString(String folderName, String userName) throws ValidationException{

		if (userName == null || userName.length() == 0){
			throw new ValidationException("userName is needed");
		}
		if (folderName == null || folderName.length() == 0){
			throw new ValidationException("folderName is needed");
		}

		String dir = Constants.UPL_BASE_DIR + userName + File.separatorChar + folderName + File.separatorChar + this.fileName;

		return dir;

	}
	public String getReadeableFileSize(){
		if(this.fileSize > 0 ){
			return FileUtils.fileSize(this.fileSize);
		}
		return "N/A";
		
	}
}
