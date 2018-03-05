package net.project.db.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 
 *Contains the task category 
 *
 */
public class TaskCategory {

	//Table name
	public static String TBL_NAME 	= "task_category";

	//columns for user
	public static final String ID 		= "id";
	public static final String CATEGORY = "category";
	public static final String USER_FK 	= "user_fk";

	private int id 				= -1;
	private String categoryName = "";
	private int userId 			= -1;
	
	public TaskCategory(){}
	
	public TaskCategory(ResultSet rs) throws SQLException{
		this.id 			= rs.getInt(ID);
		this.categoryName 	=rs.getString(CATEGORY);
		this.userId 		=rs .getInt(USER_FK);
	}
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public static String createTable(){
		StringBuilder create = new StringBuilder();
		create.append("CREATE TABLE " + TBL_NAME + " (");
		create.append( ID + " INT PRIMARY KEY auto_increment");
		create.append(", " + CATEGORY + " VARCHAR(200)" );
		create.append(", " + USER_FK + " INT" ); 
		create.append(")");
		
		return create.toString();
	}
	
}
