package net.project.db.manager;

import java.sql.SQLException;
import java.util.List;

import net.project.db.entity.SubTask;
import net.project.db.entity.Task;
import net.project.db.entity.TaskCategory;
import net.project.db.sql.TaskSql;
import net.project.enums.TaskCatg;
import net.project.exception.ValidationException;

public class TaskManager {

	private TaskSql sql;

	public TaskManager(){
		sql = new TaskSql();
	}

	public List<Task> loadAllTasksForUser(int userId) throws SQLException, ClassNotFoundException{
		return sql.loadAllTasksForUser(userId);

	}
	public List<Task> loadAllUncompletedTaskForUser(int userId) throws SQLException, ClassNotFoundException{

		return sql.loadAllUncompletedTaskForUser(userId);

	}
	
	/**
	 * load all tasks that are overdue.. pass overdue date.
	 * @param userId
	 * @return
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public List<Task> loadOverdueTasks(int userId) throws SQLException, ClassNotFoundException{
		return sql.loadOverdueTasks(userId);
	}

	public void updateTask(Task task) throws SQLException, ValidationException, ClassNotFoundException{

		if (task != null && task.getId() > 0){

			sql.updateTask(task);
			
			if (task.getSubTasks() != null && !task.getSubTasks().isEmpty()){
				//add or update 
				for(SubTask st : task.getSubTasks()){
					if (st.getId() <= 0){
						//add
						sql.addSubTask(st);
					}else if (st.getId() > 0 && st.getSubTask() != null && st.getSubTask().length() > 0){
						//update
						sql.updateSubTask(st);
					}else if (st.getId() > 0 && st.getSubTask() == null || st.getSubTask().length() == 0){
						sql.deleteSubTask(st);
					}
				}
			}
			
		}
		else{
			throw new ValidationException("Task is null or no ID provided. " + task);
		}
	}
	
	public void addTask(Task task) throws SQLException, ValidationException, ClassNotFoundException{
		if (task != null){
			
			//verify if the user has categories, if not add an initial sample.
			List<TaskCategory> cats = sql.loadAllCategoryForUser(task.getUserId());
			
			if (cats.isEmpty()){
				//add.
				for(TaskCatg cat : TaskCatg.values()){
					TaskCategory c = new TaskCategory();
					c.setCategoryName(cat.name());
					c.setUserId(task.getUserId());
					sql.addTaskCategory(c);
				}
			}
			
			
			sql.addTask(task);
		}else{
			throw new ValidationException("Task is null, cannot add");
		}
	}

	public List<Task> loadTasksByCategory(int userId, String cat, boolean loadSubTasks) throws SQLException, ClassNotFoundException{
		return sql.loadTasksByCategory(userId, cat, loadSubTasks);
	}
	public Task loadTasksById(int taskId, boolean loadSubTask) throws SQLException, ClassNotFoundException{
		return sql.loadTasksById(taskId, loadSubTask);
	}
	
	public List<TaskCategory> loadAllCategoryByUser(int userId) throws SQLException, ClassNotFoundException{
		return sql.loadAllCategoryForUser(userId);
	}
	
	public void addCategory(TaskCategory cat) throws SQLException, ClassNotFoundException{
		sql.addTaskCategory(cat);
	}
	/**
	 * Delete a task category
	 * 
	 * @param catName - Category name
	 * @param userId - user of the task category
	 * @param deleteTasks - option to also delete tasks instated of moving them to the inbox.
	 * 
	 * @throws SQLException
	 * @throws ValidationException
	 * @throws ClassNotFoundException 
	 */
	public void deleteCategory(String catName, int userId, boolean deleteTasks) throws SQLException, ValidationException, ClassNotFoundException{

		if (catName != null && !catName.isEmpty()){
			//1st find the category;		
			TaskCategory tc = sql.findCategoryByName(catName.trim());

			if (tc != null){

				//then re-assign or delete tasks if any under this category
				List<Task> tasks = loadTasksByCategory(userId, tc.getCategoryName(), true);

				if (!tasks.isEmpty()){					
					if (deleteTasks){
						sql.deleteMultipleTasks(tasks);
						
						for(Task t : tasks){
							if (t.getSubTasks() != null && !t.getSubTasks().isEmpty()){
								sql.deleteMultipleSubTasks(t.getSubTasks());
							}
						}
						
					}else{
						for(Task task : tasks){
							task.setCategory(TaskCatg.inbox.name());
							updateTask(task);
						}						
					}					
				}	

				sql.deleteCategory(tc);
			}
		}		
	}
	/**
	 * delete a single task.
	 * 
	 * @param task
	 * @throws SQLException
	 * @throws ClassNotFoundException 
	 */
	public void deleteTask(Task task) throws SQLException, ClassNotFoundException{
		
		if (task != null){
			sql.deleteTask(task);
		}
		
	}
	
	public void updateSubTask(SubTask st) throws SQLException, ClassNotFoundException{
		sql.updateSubTask(st);
	}
	
	
	
}
