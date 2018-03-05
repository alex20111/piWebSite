package net.project.db.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.project.enums.RefType;

public class ProjectRef {

	//Table name
	public static String TBL_NAME 	= "project_ref";

	//columns for user
	public static final String ID 			= "id";
	public static final String REF 			= "reference";
	public static final String REF_TYPE 	= "type";
	public static final String PROJECT_ID 	= "project_id";

	private int id 			= -1;
	private String title 	= "";
	private RefType	type 	= RefType.text;
	private int projectId	= -1;

	public ProjectRef(){}
	
	public ProjectRef(ResultSet rs) throws SQLException{
		this.id = rs.getInt(ID);
		this.title = rs.getString(REF);
		this.type = RefType.valueOf(rs.getString(REF_TYPE));
		this.projectId = rs.getInt(PROJECT_ID);
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
	public RefType getType() {
		return type;
	}
	public void setType(RefType type) {
		this.type = type;
	}
	public int getProjectId() {
		return projectId;
	}
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	public static String createTable(){
		StringBuilder create = new StringBuilder();
		create.append("CREATE TABLE " + TBL_NAME + " (");
		create.append(ID + " INT PRIMARY KEY auto_increment");
		create.append(", " + REF + " VARCHAR(2000)" );
		create.append(", " + REF_TYPE + " VARCHAR(20)" ); 
		create.append(", " + PROJECT_ID + " INT" );	
		create.append(")");

		return create.toString();
	}
	@Override
	public String toString() {
		return "ProjectRef [id=" + id + ", title=" + title + ", type=" + type
				+ ", projectId=" + projectId + "]";
	}
}
