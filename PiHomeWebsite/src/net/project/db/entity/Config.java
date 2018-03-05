package net.project.db.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Config {

	//Table name
	public static String TBL_NAME 	= "config";

	//columns for config
	public static final String ID 		= "id";
	public static final String EMAIL_USER 		= "email_user"; //user name (usually mail addresss bob@gmail.com)
	public static final String EMAIL_PASS 		= "email_pass";
	
	private Integer id;
	private String emailUserName = "";
	private String emailPassword = "";
	
	public Config(){}
	
	public Config(ResultSet rs) throws SQLException{
		this.id = rs.getInt(ID);
		this.emailUserName = rs.getString(EMAIL_USER);
		this.emailPassword = rs.getString(EMAIL_PASS);
	}
		
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getEmailUserName() {
		return emailUserName;
	}
	public void setEmailUserName(String emailUserName) {
		this.emailUserName = emailUserName;
	}
	public String getEmailPassword() {
		return emailPassword;
	}
	public void setEmailPassword(String emailPassword) {
		this.emailPassword = emailPassword;
	}

	public static String createTable(){
		StringBuilder create = new StringBuilder();
		create.append("CREATE TABLE " + TBL_NAME + " (");
		create.append(ID + " INT PRIMARY KEY auto_increment");
		create.append(", " + EMAIL_USER + " VARCHAR(60)" );
		create.append(", " + EMAIL_PASS + " VARCHAR(400)" );  	
		create.append(")");

		return create.toString();
	}
}
