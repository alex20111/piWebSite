package net.project.db.entity;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class SubTask {
	//Table name
	public static String TBL_NAME 	= "subtask";

	//columns for user
	public static final String ID 					= "id";
	public static final String SUB_TASK 			= "sub_task";
	public static final String COMPLETE 			= "completed";
	public static final String TASK_ID_FK 			= "user_id_fk";

	private int id 					= -1;
	private String subTask 			= "";
	private Boolean complete;
	private int taskId				= -1;

	public SubTask(){}
	public SubTask(ResultSet rs) throws SQLException{

		ResultSetMetaData rsmd = rs.getMetaData();
		for(int i = 1 ; i <= rsmd.getColumnCount()  ; i++){
			if (ID.equalsIgnoreCase(rsmd.getColumnName(i))){
				this.id 			= rs.getInt(ID);
			}else if (SUB_TASK.equalsIgnoreCase(rsmd.getColumnName(i))){
				this.subTask 			= rs.getString(SUB_TASK);
			}else if (COMPLETE.equalsIgnoreCase(rsmd.getColumnName(i))){
				this.complete	= rs.getBoolean(COMPLETE);
			}else if (TASK_ID_FK.equalsIgnoreCase(rsmd.getColumnName(i))){
				this.taskId 		= rs.getInt(TASK_ID_FK);
			}
		}
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSubTask() {
		return subTask;
	}
	public void setSubTask(String subTask) {
		this.subTask = subTask;
	}
	public Boolean getComplete() {
		return complete;
	}
	public void setComplete(Boolean complete) {
		this.complete = complete;
	}
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	public static String createTable(){
		StringBuilder create = new StringBuilder();
		create.append("CREATE TABLE " + TBL_NAME + " (");
		create.append( ID + " INT PRIMARY KEY auto_increment");
		create.append(", " + SUB_TASK + " VARCHAR(200)" );
		create.append(", " + COMPLETE + " TINYINT(1)" ); 
		create.append(", " + TASK_ID_FK + " INT" ); 
		create.append(")");
		
		return create.toString();
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SubTask [id=");
		builder.append(id);
		builder.append(", subTask=");
		builder.append(subTask);
		builder.append(", complete=");
		builder.append(complete);
		builder.append(", taskId=");
		builder.append(taskId);
		builder.append("]");
		return builder.toString();
	}
}
