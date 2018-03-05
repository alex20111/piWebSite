package net.project.db.entity;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;


public class Task {
	//Table name
	public static String TBL_NAME 	= "task";

	//columns for user
	public static final String ID 					= "id";
	public static final String TASK 				= "task";
	public static final String CMNT					= "comment";
	public static final String COMPLETE 			= "completed";
	public static final String CREATED 				= "created";
	public static final String TO_BE_COMPLETED_BY 	= "tobe_completed_by";
	public static final String CATEGORY				= "category";  //What kind of task.. to sort out by category. 
	public static final String USER_ID_FK 			= "user_id_fk";
	
	private int id 					= -1;
	private String task 			= "";
	private String taskComment		= "";
	private Boolean taskComplete 	= false;
	private Date taskCreated;
	private Date toBeCompletedBy;
	private String category			= "";
	private int userId 				= -1; // task owner
	
	private TaskCategory taskCategory;
	
	private List<SubTask> subTasks; 
	private List<User> users; //many? share it?
	
	//for update screen , convert date.
	private String dueDateString;
	
	
	public Task(){}
	public Task(ResultSet rs) throws SQLException{
		
		ResultSetMetaData rsmd = rs.getMetaData();
		for(int i = 1 ; i <= rsmd.getColumnCount()  ; i++){
			if (ID.equalsIgnoreCase(rsmd.getColumnName(i))){
				this.id 			= rs.getInt(ID);
			}else if (TASK.equalsIgnoreCase(rsmd.getColumnName(i))){
				this.task 			= rs.getString(TASK);
			}else if (CMNT.equalsIgnoreCase(rsmd.getColumnName(i))){
				this.taskComment 	= rs.getString(CMNT);
			}else if (COMPLETE.equalsIgnoreCase(rsmd.getColumnName(i))){
				this.taskComplete	= rs.getBoolean(COMPLETE);
			}else if (CREATED.equalsIgnoreCase(rsmd.getColumnName(i))){
				this.taskCreated 	= rs.getTimestamp(CREATED);
			}else if (TO_BE_COMPLETED_BY.equalsIgnoreCase(rsmd.getColumnName(i))){
				this.toBeCompletedBy = rs.getTimestamp(TO_BE_COMPLETED_BY);
			}else if (CATEGORY.equalsIgnoreCase(rsmd.getColumnName(i))){
				this.category 		= rs.getString(CATEGORY);
			}else if (USER_ID_FK.equalsIgnoreCase(rsmd.getColumnName(i))){
				this.userId 		= rs.getInt(USER_ID_FK);
			}
		}
	}
	
	public static String createTable(){
		StringBuilder create = new StringBuilder();
		create.append("CREATE TABLE " + TBL_NAME + " (");
		create.append( ID + " INT PRIMARY KEY auto_increment");
		create.append(", " + TASK + " VARCHAR(200)" );
		create.append(", " + CMNT + " VARCHAR" ); 
		create.append(", " + COMPLETE + " TINYINT" ); 
		create.append(", " + CREATED + " TIMESTAMP" ); 
		create.append(", " + TO_BE_COMPLETED_BY + " TIMESTAMP" );
		create.append(", " + CATEGORY + " VARCHAR(200)" ); 
		create.append(", " + USER_ID_FK + " INT" ); 
		create.append(")");
		
		return create.toString();
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTask() {
		return task;
	}
	public void setTask(String task) {
		this.task = task;
	}
	public String getTaskComment() {
		return taskComment;
	}
	public void setTaskComment(String taskComment) {
		this.taskComment = taskComment;
	}
	public Boolean isTaskComplete() {
		return taskComplete;
	}
	public void setTaskComplete(Boolean taskComplete) {
		this.taskComplete = taskComplete;
	}
	public Date getTaskCreated() {
		return taskCreated;
	}
	public void setTaskCreated(Date taskCreated) {
		this.taskCreated = taskCreated;
	}
	public Date getToBeCompletedBy() {
		return toBeCompletedBy;
	}
	public void setToBeCompletedBy(Date toBeCompletedBy) {
		this.toBeCompletedBy = toBeCompletedBy;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Task [id=");
		builder.append(id);
		builder.append(", task=");
		builder.append(task);
		builder.append(", taskComment=");
		builder.append(taskComment);
		builder.append(", taskComplete=");
		builder.append(taskComplete);
		builder.append(", taskCreated=");
		builder.append(taskCreated);
		builder.append(", toBeCompletedBy=");
		builder.append(toBeCompletedBy);
		builder.append(", category=");
		builder.append(category);
		builder.append(", userId=");
		builder.append(userId);
		builder.append(", users=");
		builder.append(users);
		builder.append("]");
		return builder.toString();
	}
	public TaskCategory getTaskCategory() {
		return taskCategory;
	}
	public void setTaskCategory(TaskCategory taskCategory) {
		this.taskCategory = taskCategory;
	}
	public String getDueDateString() {
		return dueDateString;
	}
	public void setDueDateString(String dueDateString) {
		this.dueDateString = dueDateString;
	}
	public Boolean getTaskComplete() {
		return taskComplete;
	}
	public List<SubTask> getSubTasks() {
		return subTasks;
	}
	public void setSubTasks(List<SubTask> subTasks) {
		this.subTasks = subTasks;
	}	
}