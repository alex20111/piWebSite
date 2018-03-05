package net.project.db.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import home.db.DBConnection;
import net.project.common.Constants;
import net.project.db.entity.SubTask;
import net.project.db.entity.Task;
import net.project.db.entity.TaskCategory;

public class TaskSql {

	public void updateTask(Task task) throws SQLException, ClassNotFoundException{
		DBConnection con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);

		con.buildUpdateQuery(Task.TBL_NAME);

		//only update the fields that needs to be updated.
		if (task.getTask() != null && !task.getTask().isEmpty()){
			con.setParameter(Task.TASK, task.getTask());
		}
		if (task.getCategory() != null && !task.getCategory().isEmpty() ){
			con.setParameter(Task.CATEGORY, task.getCategory());
		}
		if (task.getTaskComment() != null && !task.getTaskComment().isEmpty()){
			con.setParameter(Task.CMNT, task.getTaskComment());
		}
		if (task.getTaskCreated() != null){
			con.setParameter(Task.CREATED, task.getTaskCreated());
		}
		if (task.getToBeCompletedBy() != null){
			con.setParameter(Task.TO_BE_COMPLETED_BY, task.getToBeCompletedBy());
		}
		if (task.getUserId() > 0){
			con.setParameter(Task.USER_ID_FK, task.getUserId());
		}
		if (task.isTaskComplete() != null){
			con.setParameter(Task.COMPLETE, task.isTaskComplete());
		}
		
		con.addUpdWhereClause("WHERE " + Task.ID + " = :taskId", task.getId())
		.update();


		con.close();
	}
	
	public Task addTask(Task tsk) throws SQLException, ClassNotFoundException{
		DBConnection con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);

		int key = con.buildAddQuery(Task.TBL_NAME)
				.setParameter(Task.CATEGORY, tsk.getCategory())
				.setParameter(Task.CMNT, tsk.getTaskComment())
				.setParameter(Task.COMPLETE, tsk.isTaskComplete())
				.setParameter(Task.CREATED, tsk.getTaskCreated())
				.setParameter(Task.TASK, tsk.getTask())
				.setParameter(Task.TO_BE_COMPLETED_BY, tsk.getToBeCompletedBy())
				.setParameter(Task.USER_ID_FK, tsk.getUserId())				
				.add();

		con.close();

		tsk.setId(key);

		return tsk;
	}
	
	public List<Task> loadAllTasksForUser(int userId) throws SQLException, ClassNotFoundException{

		List<Task> tasks = new ArrayList<Task>();
		DBConnection con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);
		
		String query = "Select * From " + Task.TBL_NAME + " WHere " + Task.USER_ID_FK + " = :userIdFk";
		
		ResultSet rs = con.createSelectQuery(query).setParameter("userIdFk", userId).getSelectResultSet();
		
		while(rs.next()){
			Task t = new Task(rs);
			tasks.add(t);
		}
		con.close();
		
		return tasks;

	}
	public List<Task> loadAllUncompletedTaskForUser(int userId) throws SQLException, ClassNotFoundException{

		List<Task> tasks = new ArrayList<Task>();
		
		DBConnection con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);
		
		String query = "Select * From " + Task.TBL_NAME + " WHere " + Task.USER_ID_FK + " = :userIdFk AND " + Task.COMPLETE + " = :unc";
		
		ResultSet rs = con.createSelectQuery(query)
				.setParameter("userIdFk", userId)
				.setParameter("unc", false)
				.getSelectResultSet();
		
		while(rs.next()){
			Task t = new Task(rs);
			tasks.add(t);
		}
		
		con.close();
		return tasks;

	}
	
	public List<Task> loadOverdueTasks(int userId) throws SQLException, ClassNotFoundException{
			
		List<Task> tasks = new ArrayList<Task>();
		
		DBConnection con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);
		
		String query = "Select * From " + Task.TBL_NAME + " Where " + Task.USER_ID_FK + " = :userIdFk AND " 
					+ Task.COMPLETE + " = :unc AND " + Task.TO_BE_COMPLETED_BY + " < :nowDate";
		
		ResultSet rs = con.createSelectQuery(query)
				.setParameter("userIdFk", userId)
				.setParameter("unc", false)
				.setParameter("nowDate",new Date())
				.getSelectResultSet();
		
		while(rs.next()){
			Task t = new Task(rs);
			tasks.add(t);
		}
		con.close();
		
		return tasks;
	}
	public List<Task> loadTasksByCategory(int userId, String cat, boolean loadSubTasks) throws SQLException, ClassNotFoundException{

		List<Task> tasks = new ArrayList<Task>();

		DBConnection con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);

		String query = "Select "+ Task.ID + "," + Task.TASK + "," + Task.COMPLETE + ", " + Task.TO_BE_COMPLETED_BY +
				"  From " + Task.TBL_NAME + " Where " + Task.USER_ID_FK + " = :userIdFk AND " 
				+ Task.CATEGORY + " = :cat ";//AND " + Task.COMPLETE + " = :unc";


		ResultSet rs = con.createSelectQuery(query)
				.setParameter("userIdFk", userId)
				.setParameter("cat", cat)

				.getSelectResultSet();

		while(rs.next()){
			Task t = new Task(rs);


			if (loadSubTasks){
				String subTskQuery = "SELECT * FROM " + SubTask.TBL_NAME + " WHERE " + SubTask.TASK_ID_FK + " = :taskId";

				ResultSet rs2 = con.createSelectQuery(subTskQuery)
						.setParameter("taskId", t.getId())	
						.getSelectResultSet();

				List<SubTask> subTasks = new ArrayList<SubTask>();

				while(rs2.next()){
					SubTask st = new SubTask(rs2);
					subTasks.add(st);
				}				
				t.setSubTasks(subTasks);		
			}

			tasks.add(t);

		}
		con.close();

		return tasks;
	}
	
	public Task loadTasksById(int taskId, boolean loadSubTasks) throws SQLException, ClassNotFoundException{

		Task task = null;

		DBConnection con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);

		String query = "Select * From " + Task.TBL_NAME + " Where " + Task.ID + " = :taskId" ;	
		
		ResultSet rs = con.createSelectQuery(query)
				.setParameter("taskId", taskId)	
				.getSelectResultSet();

		while(rs.next()){
			task = new Task(rs);
						
			if(loadSubTasks){
				String subTskQuery = "SELECT * FROM " + SubTask.TBL_NAME + " WHERE " + SubTask.TASK_ID_FK + " = :taskId";
				
				rs = con.createSelectQuery(subTskQuery)
					.setParameter("taskId", taskId)	
					.getSelectResultSet();
				
				List<SubTask> subTasks = new ArrayList<SubTask>();
				
				while(rs.next()){
					SubTask st = new SubTask(rs);
					subTasks.add(st);
				}				
				task.setSubTasks(subTasks);				
			}
		}
		con.close();

		return task;
	}
	
	public List<TaskCategory> loadAllCategoryForUser(int userId) throws SQLException, ClassNotFoundException{
		List<TaskCategory> cats = new ArrayList<TaskCategory>();
		
		DBConnection con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);
		
		String query = "Select *  From " + TaskCategory.TBL_NAME + " Where " + TaskCategory.USER_FK + " = :userIdFk ";
		
		ResultSet rs = con.createSelectQuery(query)
				.setParameter("userIdFk", userId)	
				.getSelectResultSet();
		
		while(rs.next()){
			TaskCategory t = new TaskCategory(rs);
			cats.add(t);
		}
		con.close();
		
		return cats;
	}
	public TaskCategory addTaskCategory(TaskCategory cat) throws SQLException, ClassNotFoundException{
		
		DBConnection con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);

		int key = con.buildAddQuery(TaskCategory.TBL_NAME)
				.setParameter(TaskCategory.CATEGORY, cat.getCategoryName())
				.setParameter(TaskCategory.USER_FK, cat.getUserId())
				.add();

		con.close();

		cat.setId(key);

		return cat;
	}
	
	public TaskCategory findCategoryByName(String catName) throws SQLException, ClassNotFoundException{
		
		TaskCategory tc = null;

		DBConnection con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);

		String query = "Select * From " + TaskCategory.TBL_NAME + " Where " + TaskCategory.CATEGORY + " = :taskCat" ;	

		ResultSet rs = con.createSelectQuery(query)
				.setParameter("taskCat", catName)	
				.getSelectResultSet();

		while(rs.next()){
			tc = new TaskCategory(rs);

		}
		con.close();

		return tc;
	}
	
	public void deleteCategory(TaskCategory cat) throws SQLException, ClassNotFoundException{
		DBConnection con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);

		String delete = "DELETE FROM " + TaskCategory.TBL_NAME + " where id = :id";
		con.createSelectQuery(delete)
		.setParameter("id", cat.getId()).delete();
		con.close();
	}
	public void deleteTask(Task task) throws SQLException, ClassNotFoundException{
		DBConnection con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);

		String delete = "DELETE FROM " + Task.TBL_NAME + " where id = :id";
		con.createSelectQuery(delete)
		.setParameter("id", task.getId()).delete();
		con.close();
	}
	
	public void deleteMultipleTasks(List<Task> tasks) throws SQLException, ClassNotFoundException{

		DBConnection con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);

		StringBuilder delete = new StringBuilder("DELETE FROM " + Task.TBL_NAME + " where ( " );//id = :id";

		boolean first = true;
		for(int i=0 ; i < tasks.size() ; i ++) {

			if(first){
				delete.append(" " + Task.ID + " = :id"+i );
				first = false;
			}else{
				delete.append(" OR "+ Task.ID + " = :id"+i );
			}
		}
		delete.append( " ) ");

		con.createSelectQuery(delete.toString());

		for(int i=0 ; i < tasks.size() ; i ++){
			con.setParameter("id"+i, tasks.get(i).getId());
		}
		con.delete();

		con.close();
	}
	public SubTask addSubTask(SubTask st) throws SQLException, ClassNotFoundException{

		DBConnection con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);

		int key = con.buildAddQuery(SubTask.TBL_NAME)
				.setParameter(SubTask.COMPLETE, st.getComplete())
				.setParameter(SubTask.SUB_TASK, st.getSubTask())
				.setParameter(SubTask.TASK_ID_FK, st.getTaskId())
				.add();

		con.close();

		st.setId(key);

		return st;
	}
	public void updateSubTask(SubTask st) throws SQLException, ClassNotFoundException{
		DBConnection con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);

		con.buildUpdateQuery(SubTask.TBL_NAME);
		if (st.getSubTask() != null && !st.getSubTask().isEmpty() ){
			con.setParameter(SubTask.SUB_TASK, st.getSubTask());
		}
		
		if (st.getComplete() != null ) {
			con.setParameter(SubTask.COMPLETE, st.getComplete());
		}
		
		con.addUpdWhereClause("WHERE " + SubTask.ID + " = :subTaskId", st.getId())
		.update();

		con.close();
	}
	public void deleteSubTask(SubTask st) throws SQLException, ClassNotFoundException{
		DBConnection con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);

		String delete = "DELETE FROM " + SubTask.TBL_NAME + " where id = :id";
		con.createSelectQuery(delete)
		.setParameter("id", st.getId()).delete();
		con.close();
	}
	public void deleteMultipleSubTasks(List<SubTask> sts) throws SQLException, ClassNotFoundException{

		DBConnection con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);

		StringBuilder delete = new StringBuilder("DELETE FROM " + SubTask.TBL_NAME + " where ( " );//id = :id";

		boolean first = true;
		for(int i=0 ; i < sts.size() ; i ++) {

			if(first){
				delete.append(" " + SubTask.ID + " = :id"+i );
				first = false;
			}else{
				delete.append(" OR "+ SubTask.ID + " = :id"+i );
			}
		}
		delete.append( " ) ");

		con.createSelectQuery(delete.toString());

		for(int i=0 ; i < sts.size() ; i ++){
			con.setParameter("id"+i, sts.get(i).getId());
		}
		con.delete();

		con.close();
	}
}
