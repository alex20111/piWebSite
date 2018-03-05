package net.project.web.action;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.project.common.Constants;
import net.project.db.entity.SubTask;
import net.project.db.entity.Task;
import net.project.db.entity.TaskCategory;
import net.project.db.entity.User;
import net.project.db.manager.TaskManager;
import net.project.enums.TaskCatg;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

public class TaskAction extends ActionSupport implements SessionAware{


	private static final long serialVersionUID = -5715238535522383892L;
	
	private String SESSION_TSK_CATG = "taskCategoryList";
	
	private Log log = LogFactory.getLog(getClass());
	private Map<String, Object> session;
	private Boolean taskExpanded = true;
	
	private String addNewTaskBtn;
	private String updateTaskBtn;
	private String addNewCategoryBtn;
	private String deleteCategoryBtn;
	
	private Boolean deleteTaskChkbox;
	
	//add task
	private String newTask;
	private String newCatgName;
	///
	
	private String categoryName;
	private Integer taskId;
	private Boolean completed = false;
	
	private List<Task> tasks; // in progress tasks
	private List<Task> taskCompleted;
	private Task task;
	
	//update task
	private String formTaskName;
	private String formTaskDueDate;
	private String formTaskComment;
	private String formCategory;
	
	//subTasks
	private List<String> subTaskName;
	private List<Integer> subTskChk;
	
	private String updateSuccess = "";

	public String loadAllTasks(){

		User user = (User)session.get(Constants.USER);

		try{			

			if (user != null){
				
				TaskManager tm = new TaskManager();
				if (categoryName == null){
					tasks = tm.loadAllUncompletedTaskForUser(user.getId());
				}else if (categoryName != null){		
		
					List<Task> unsortedTasks = tm.loadTasksByCategory(user.getId(), categoryName, false);
					
					tasks = new ArrayList<Task>();
					taskCompleted = new ArrayList<Task>();
					//verify if any are completed and add it to the completed list.
					for(Task task: unsortedTasks){
						if (task.isTaskComplete()){
							taskCompleted.add(task);
						}else{
							tasks.add(task);
						}
					}
				}
		
				//add task category list in the session if exist	
				//for the user to be able to select it .
				List<TaskCategory> tc = tm.loadAllCategoryByUser(user.getId());
				if (!tc.isEmpty()){
					//don't add delete as a selectable option
					for(TaskCategory tg : tc){
						if (tg.getCategoryName().equals(TaskCatg.deleted.name())){
							tc.remove(tg);
							break;
						}
					}
					session.put(SESSION_TSK_CATG, tc);
				}
				
				
			}
			else
			{
				return Constants.ACCESS_DENIED;
			}

		}catch(Exception ex){
			log.error("Error in loadAllTasks" , ex );
		}

		return SUCCESS;
	}
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
		
	}
	
	
	public String manageTasks(){
	
		
		if (addNewTaskBtn != null){
			addNewTask();
		}else if (updateTaskBtn != null){
			updateTask();
		}else if (addNewCategoryBtn != null){
			addNewCategory();
		}else if (deleteCategoryBtn != null){
			deleteCategory();
		}
		
		loadAllTasks(); //reload all tasks
		
		return SUCCESS;
	}

	private void addNewTask(){

		try{
			User user = (User) session.get(Constants.USER);
			
			if (newTask == null || newTask.isEmpty()){
				addActionError("Please enter a task");
			}

			if (!hasActionErrors()){

				TaskManager tm = new TaskManager();

				Task task = new Task();

				task.setTask(newTask);			
				task.setTaskCreated(new Date());
				task.setUserId(user.getId());
				
				if (categoryName != null && !categoryName.isEmpty()){
					task.setCategory(categoryName);
				}else{
					task.setCategory(TaskCatg.inbox.name());
				}

				tm.addTask(task);
				
				updateTaskSideMenu(task, false, null);				

				addActionMessage("Task added");
				
				setNewTask("");
			}

		}catch (Exception ex){
			log.error("Error in addNewTask" , ex);
			addActionError("Error , please contact system administrator");
		}

	}
	//ajax call
	public String loadTask(){

		try{
			TaskManager tm = new TaskManager();

			task = tm.loadTasksById(taskId, true);
			
			//format dates
			if (task.getToBeCompletedBy() != null){
				task.setDueDateString(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(task.getToBeCompletedBy()));				
			}
						

		}catch(Exception ex){
			ex.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String toggleTaskForCompleted(){
		
		try{
			Task tskComl = new Task();
			tskComl.setId(taskId);
			if(completed){
				tskComl.setTaskComplete(true);
			}else{
				tskComl.setTaskComplete(false);
			}
					
			new TaskManager().updateTask(tskComl);
			
			tskComl.setCategory(categoryName);
			
			updateTaskSideMenu(tskComl, completed, null);
			
			updateSuccess = "success";
			
		}catch(Exception ex){
			log.error("Exception in toggleTaskForCompleted" ,ex );
		}
		
		return SUCCESS;
	}
	
	public String toggleSubTaskForCompleted(){
		
		try{
		TaskManager tm = new TaskManager();
			
		SubTask st = new SubTask();
		st.setId(taskId);//task id is used instated of subTaskId to re-use a variable.
		st.setComplete(completed);
		
		tm.updateSubTask(st);
		
		updateSuccess = "success";
		}catch(Exception ex){
			log.error("Exception in toggleSubTaskForCompleted" ,ex );
		}
		return SUCCESS;
	}
	
	private void updateTask(){
			
		try{		
			String categoryChanged = "";
			
			TaskManager tm = new TaskManager();
			Task task = tm.loadTasksById(taskId, true);
			
			if(!task.getCategory().equals(formCategory)){
				categoryChanged = task.getCategory();
			}			
			
			try{
				if (TaskCatg.valueOf(formCategory) == TaskCatg.completed){
					task.setTaskComplete(true);
				}
			}catch(IllegalArgumentException ie){
				task.setCategory(formCategory);
			}
				
			task.setTask(formTaskName);
			task.setTaskComment(formTaskComment);
			if (formTaskDueDate != null && !formTaskDueDate.isEmpty()){
				Date due = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(formTaskDueDate);
				
				task.setToBeCompletedBy(due);
			}
					
			
			//check subTasks
			List<SubTask> subTasks = new ArrayList<SubTask>();
			if (subTaskName != null && !subTaskName.isEmpty()){
				for(int i = 0 ; i < subTaskName.size() ; i ++){					
					String st = subTaskName.get(i);
					
					if (st != null && st.length() > 0){						
						SubTask subT = new SubTask();
						subT.setSubTask(st);
						subT.setTaskId(taskId);
						
						if (subTskChk.get(i) != null){
							subT.setId(subTskChk.get(i));
						}						
						subTasks.add(subT);
					}else if ( (st == null || st.length() ==0) && subTskChk.get(i) != null){
						SubTask subT = new SubTask();					
						subT.setTaskId(taskId)		;				
						subT.setId(subTskChk.get(i));
											
						subTasks.add(subT);
					}
				}
			}
						
			task.setSubTasks(subTasks);
			
			tm.updateTask(task);			
			
			setCategoryName(task.getCategory());			
			if (!categoryChanged.isEmpty()){
				updateTaskSideMenu(task, task.isTaskComplete(), categoryChanged);
			}
			
			
		}catch(Exception ex){
			log.error("Error while updating task" , ex);
			addActionError("Error while updating");
		}	
		
	}
	@SuppressWarnings("unchecked")
	private void addNewCategory(){
		try{

			User user = (User)session.get(Constants.USER);		

			if (newCatgName != null && newCatgName.length() > 0){

				Map<String, Integer> sideMenuTask = new LinkedHashMap<String, Integer>();
				if (session.containsKey(Constants.sideMenuTasks)){
					sideMenuTask = (Map<String, Integer>) session.get(Constants.sideMenuTasks);
				}

				if (sideMenuTask.containsKey(newCatgName)){
					addActionError("Category name already exist");
				}

				if (!hasActionErrors() && !hasFieldErrors()){
					TaskManager tm = new TaskManager();

					TaskCategory tc = new TaskCategory();
					tc.setCategoryName(newCatgName);
					tc.setUserId(user.getId());

					tm.addCategory(tc);

					//add the new category to the side menu.
					if (sideMenuTask != null && !sideMenuTask.isEmpty()) {					
						sideMenuTask.put(tc.getCategoryName(), 0);
					}
				}
			}else{
				addActionError("Please enter a cat name");
			}

		}catch(Exception ex){
			log.error("Exception in addNewCategory" ,ex );
		}
	}
	
	@SuppressWarnings("unchecked")
	private void deleteCategory(){
		User user = (User)session.get(Constants.USER);	
		
		try{
			TaskManager tm = new TaskManager();
			
			tm.deleteCategory(categoryName, user.getId(), deleteTaskChkbox);	
			
			if (session.containsKey(Constants.sideMenuTasks)){
				Map<String, Integer> sideMenuTask = (Map<String, Integer>) session.get(Constants.sideMenuTasks);
				
				sideMenuTask.remove(categoryName);
				
			}
			
			addActionMessage("Category " + categoryName + " deleted");
			
			setCategoryName(TaskCatg.inbox.name()); //re-direct to the inbox.			
						
		}catch(Exception ex){
			log.error("Exception in deleteCategory" ,ex );
		}		
	}
	
	@SuppressWarnings("unchecked")
	private void updateTaskSideMenu(Task task, boolean isCompleted, String oldCat) throws SQLException{		
		
		if (session.containsKey(Constants.sideMenuTasks)){

			Map<String, Integer> sideMenuTask = (Map<String, Integer>) session.get(Constants.sideMenuTasks);
			//no category change, proceed as normal
			if (oldCat == null ||  oldCat.isEmpty()){
				
				int value = sideMenuTask.get(task.getCategory());

				if (isCompleted){
					value --;
				}else{
					value ++;
				}
				sideMenuTask.put(task.getCategory(), value);
			}
			else if (!oldCat.isEmpty()){
				int newCatVal = sideMenuTask.get(task.getCategory());
				int oldCatVal = sideMenuTask.get(oldCat);
				
				//if it's completed for a change, only remove it from the old category
				if (isCompleted){
					oldCatVal --;				
				}else{
					newCatVal ++;
					oldCatVal --;	
				}
				sideMenuTask.put(task.getCategory(), newCatVal);
				sideMenuTask.put(oldCat, oldCatVal);
			}
			session.put(Constants.sideMenuTasks, sideMenuTask);			
		}
	}
	
	public String getAddNewTaskBtn() {
		return addNewTaskBtn;
	}
	public void setAddNewTaskBtn(String addNewTaskBtn) {
		this.addNewTaskBtn = addNewTaskBtn;
	}
	public String getNewTask() {
		return newTask;
	}
	public void setNewTask(String newTask) {
		this.newTask = newTask;
	}
	public List<Task> getTasks() {
		return tasks;
	}
	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public Task getTask() {
		return task;
	}
	public void setTask(Task task) {
		this.task = task;
	}
	public Integer getTaskId() {
		return taskId;
	}
	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}
	public String getUpdateTaskBtn() {
		return updateTaskBtn;
	}
	public void setUpdateTaskBtn(String updateTaskBtn) {
		this.updateTaskBtn = updateTaskBtn;
	}
	public String getFormTaskName() {
		return formTaskName;
	}
	public void setFormTaskName(String formTaskName) {
		this.formTaskName = formTaskName;
	}
	public String getFormTaskDueDate() {
		return formTaskDueDate;
	}
	public void setFormTaskDueDate(String formTaskDueDate) {
		this.formTaskDueDate = formTaskDueDate;
	}
	public String getFormTaskComment() {
		return formTaskComment;
	}
	public void setFormTaskComment(String formTaskComment) {
		this.formTaskComment = formTaskComment;
	}
	public String getFormCategory() {
		return formCategory;
	}
	public void setFormCategory(String formCategory) {
		this.formCategory = formCategory;
	}
	public Boolean getTaskExpanded() {
		return taskExpanded;
	}
	public void setTaskExpanded(Boolean taskExpanded) {
		this.taskExpanded = taskExpanded;
	}
	public List<Task> getTaskCompleted() {
		return taskCompleted;
	}
	public void setTaskCompleted(List<Task> taskCompleted) {
		this.taskCompleted = taskCompleted;
	}
	public Boolean getCompleted() {
		return completed;
	}
	public void setCompleted(Boolean completed) {
		this.completed = completed;
	}
	public String getAddNewCategoryBtn() {
		return addNewCategoryBtn;
	}
	public void setAddNewCategoryBtn(String addNewCategoryBtn) {
		this.addNewCategoryBtn = addNewCategoryBtn;
	}
	public String getNewCatgName() {
		return newCatgName;
	}
	public void setNewCatgName(String newCatgName) {
		this.newCatgName = newCatgName;
	}
	public String getDeleteCategoryBtn() {
		return deleteCategoryBtn;
	}
	public void setDeleteCategoryBtn(String deleteCategoryBtn) {
		this.deleteCategoryBtn = deleteCategoryBtn;
	}
	public Boolean getDeleteTaskChkbox() {
		return deleteTaskChkbox;
	}
	public void setDeleteTaskChkbox(Boolean deleteTaskChkbox) {
		this.deleteTaskChkbox = deleteTaskChkbox;
	}
	public List<String> getSubTaskName() {
		return subTaskName;
	}
	public void setSubTaskName(List<String> subTaskName) {
		this.subTaskName = subTaskName;
	}
	public List<Integer> getSubTskChk() {
		return subTskChk;
	}
	public void setSubTskChk(List<Integer> subTskChk) {
		this.subTskChk = subTskChk;
	}
	public String getUpdateSuccess() {
		return updateSuccess;
	}
	public void setUpdateSuccess(String updateSuccess) {
		this.updateSuccess = updateSuccess;
	}	
}