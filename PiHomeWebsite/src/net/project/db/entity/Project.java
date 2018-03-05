package net.project.db.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class Project {

	//Table name
	public static String TBL_NAME 	= "project";

	//columns for user
	public static final String ID 			= "id";
	public static final String TITLE 		= "title";
	public static final String DESCRIPTION 	= "description";
	public static final String CRT_DT 		= "created_date";
	public static final String UPD_DT 		= "last_updated";
	public static final String OWNER_ID 	= "owner_id";

	
	private int id 			= -1;
	private String title 	= "";
	private String desc		= "";
	private Date crtDt		= null;
	private Date updDt		= null;
	private int ownerId 	= -1;
	
	private List<ProjectRef> refs;
	private List<ProjectFile> files;
	
	public Project(){}
	
	public Project(ResultSet rs) throws SQLException{
		  this.id 		= rs.getInt(ID);
		  this.title 	= rs.getString(TITLE);
		  this.desc		= rs.getString(DESCRIPTION);
		  this.crtDt	= rs.getTimestamp(CRT_DT);
		  this.updDt	= rs.getTimestamp(UPD_DT);
		  this.ownerId 	= rs.getInt(OWNER_ID);
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public Date getCrtDt() {
		return crtDt;
	}
	public void setCrtDt(Date crtDt) {
		this.crtDt = crtDt;
	}
	public Date getUpdDt() {
		return updDt;
	}
	public void setUpdDt(Date updDt) {
		this.updDt = updDt;
	}
	public int getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}

	public List<ProjectRef> getRefs() {
		return refs;
	}

	public void setRefs(List<ProjectRef> refs) {
		this.refs = refs;
	}

	public List<ProjectFile> getFiles() {
		return files;
	}

	public void setFiles(List<ProjectFile> files) {
		this.files = files;
	}

	public static String createTable(){
		StringBuilder create = new StringBuilder();
		create.append("CREATE TABLE " + TBL_NAME + " (");
		create.append(ID + " INT PRIMARY KEY auto_increment");
		create.append(", " + TITLE + " VARCHAR(500)" );
		create.append(", " + DESCRIPTION + " TEXT" ); 
		create.append(", " + CRT_DT + " TIMESTAMP" ); 
		create.append(", " + UPD_DT + " TIMESTAMP" ); 
		create.append(", " + OWNER_ID + " INT" );	
		create.append(")");
		
		return create.toString();
	}

	@Override
	public String toString() {
		return "Project [id=" + id + ", title=" + title + ", desc=" + desc
				+ ", crtDt=" + crtDt + ", updDt=" + updDt + ", ownerId="
				+ ownerId + "]";
	}
	
}
