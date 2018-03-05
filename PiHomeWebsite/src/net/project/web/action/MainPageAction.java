package net.project.web.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.project.common.Constants;
import net.project.db.entity.InventoryGroup;
import net.project.db.entity.Folder;
import net.project.db.entity.Task;
import net.project.db.entity.TaskCategory;
import net.project.db.entity.User;
import net.project.db.manager.FolderManager;
import net.project.db.manager.InventoryManager;
import net.project.db.manager.TaskManager;
import net.project.enums.TaskCatg;
import net.project.utils.WeatherHandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

public class MainPageAction extends ActionSupport implements SessionAware {
	
	private Log log = LogFactory.getLog(getClass());
	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;
	
	private String weather;
	
	private InputStream dateStream; //REfresh temperature
	
	public String loadMainPageInformation(){
		try
		{		
			User user = (User)session.get(Constants.USER);

			//load folders.
			if (user != null){

				FolderManager folderManager = new FolderManager();

				List<Folder> list = folderManager.loadFoldersByUserId(user.getId(), false, true, false, true, false); 

				session.put(Constants.sideMenuFolderList, list);		
				
				
				//tasks
				TaskManager tm = new TaskManager();
				
				Map<String, Integer> sideMenuTask = new LinkedHashMap<String, Integer>();
				
				//load all user categories
				List<TaskCategory> tskCat = tm.loadAllCategoryByUser(user.getId());
				
				if (tskCat.isEmpty()){
					//if empty, create default categories.
					//add.
					for(TaskCatg cat : TaskCatg.values()){
						TaskCategory c = new TaskCategory();
						c.setCategoryName(cat.name());
						c.setUserId(user.getId());
						tm.addCategory(c);
						
						if (cat != TaskCatg.completed){						
							sideMenuTask.put(cat.name(), 0);
						}
					}
										
				}else{
					//add the task to the categories.
					for(TaskCategory cat : tskCat){
						
						if (!cat.getCategoryName().equals(TaskCatg.completed.name())){	
							List<Task> tasks = tm.loadTasksByCategory(user.getId(), cat.getCategoryName(), false);

							int tasksNbr = 0;
							//do not include the completed in the count
							for(Task t : tasks ){
								if (!t.isTaskComplete()){
									tasksNbr++;
								}
							}
					
						
						sideMenuTask.put(cat.getCategoryName(), tasksNbr);
						}	
					}
				}

				session.put(Constants.sideMenuTasks, sideMenuTask);
				
				InventoryManager im = new InventoryManager();
				List<InventoryGroup> imSideMenu = im.loadSideMenuGroupsForUser(user.getId());
				session.put(Constants.sideMenuInventoryGroups, imSideMenu);
				
				
			}			

		}catch(Exception ex){
			addActionError("Error while loading information, please see logs");
			log.error("Error in loadMainPageInformation.", ex);
		}
		return SUCCESS;
	}
	
	
	public String loadWeatherMainPage(){

		try{
			//load current weather.

			WeatherHandler wthHandl = new WeatherHandler();

			weather = wthHandl.loadMainPageWeatherFromEnvCan(session);
			
		}catch(Exception ex){
			log.error("Error loadWeatherMainPage", ex);
			weather = "Error loading current weather";
		}
		return SUCCESS;
	}
	
	public String dateAjax(){
		try {
			
			String text = "<p><Strong>Current Date:</Strong> " + new SimpleDateFormat("yyyy-MM-dd HH:mm.ss").format(new Date()) +  " </p>";
			
			dateStream = new ByteArrayInputStream(text.getBytes("UTF-8"));

		} catch (Exception e) {
			try {
				dateStream = new ByteArrayInputStream("Error".getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e1) {	}
			log.error("Error in dateAjax. " , e);
		}

		return SUCCESS;
	}
	
	/**
	 * update the side menu based on the dropdown clicked.
	 */
	public void updateSideMenu(){

		
		Boolean folderExpanded = (Boolean)session.get(Constants.folderExpander);
		
		if (folderExpanded == null || !folderExpanded){
			session.put(Constants.folderExpander, true);
			
		}else if (folderExpanded != null && folderExpanded){
			session.put(Constants.folderExpander, false);
		}
	}
	
	
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
		
	}

	public InputStream getDateStream() {
		return dateStream;
	}

	public void setDateStream(InputStream dateStream) {
		this.dateStream = dateStream;
	}


	public String getWeather() {
		return weather;
	}


	public void setWeather(String weather) {
		this.weather = weather;
	}
}
