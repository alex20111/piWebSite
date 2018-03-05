package net.project.db.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProjectFile {
	//Table name
	public static String TBL_NAME 	= "project_file";

	//columns for user
	public static final String ID 				= "id";
	public static final String FILE_NAME		= "file_name";
	public static final String FILE_DISK_NAME	= "file_disk_name";
	public static final String PROJECT_ID 		= "project_id";
	
	
	private int id = -1;
	private String fileName = "";
	private String fileDiskName = "";
	private int projectId = -1;
	
	public ProjectFile(){}
	
	//transient
	private Boolean hasFile = false; //set when the user add a new file to the web interface.
	
	public ProjectFile(ResultSet rs) throws SQLException{
		  this.id 				= rs.getInt(ID);
		  this.fileName 		= rs.getString(FILE_NAME);
		  this.fileDiskName		= rs.getString(FILE_DISK_NAME);
		  this.projectId 		= rs.getInt(PROJECT_ID);
	}


	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileDiskName() {
		return fileDiskName;
	}
	public void setFileDiskName(String fileDiskName) {
		this.fileDiskName = fileDiskName;
	}
	public int getProjectId() {
		return projectId;
	}
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	
	public Boolean getHasFile() {
		return hasFile;
	}
	public void setHasFile(Boolean hasFile) {
		this.hasFile = hasFile;
	}

	public static String createTable(){
		StringBuilder create = new StringBuilder();
		create.append("CREATE TABLE " + TBL_NAME + " (");
		create.append(ID + " INT PRIMARY KEY auto_increment");
		create.append(", " + FILE_NAME + " VARCHAR(2000)" );
		create.append(", " + FILE_DISK_NAME + " VARCHAR(2000)" ); 
		create.append(", " + PROJECT_ID + " INT" );	
		create.append(")");

		return create.toString();
	}


	@Override
	public String toString() {
		return "ProjectFile [id=" + id + ", fileName=" + fileName
				+ ", fileDiskName=" + fileDiskName + ", projectId=" + projectId + ", hasFile=" + hasFile 
				+ "]";
	}
}
