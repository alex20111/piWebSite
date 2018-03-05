package net.project.web.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import home.misc.StringUtl;
import net.project.common.Constants;
import net.project.db.entity.Project;
import net.project.db.entity.ProjectFile;
import net.project.db.entity.User;
import net.project.db.manager.ProjectManager;

public class ProjectAction extends ActionSupport implements SessionAware{

	private static final long serialVersionUID = 1L;
	private Log log = LogFactory.getLog(getClass());
	
	private Map<String, Object> session;
	
	private List<Project> projectList;
	private Project project;
	
	//file for editing
	private List<File> projectFileList = new ArrayList<File>();
	private List<String> projectFileListFileName =  new ArrayList<String>();
	
	//view
	private Integer viewProjectId = -1;
	private String fileDownloadName 			= "";
	private InputStream fileDownloadStream;
	
	
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;		
	}	
	
	public String loadAllUserProjects(){
		User user = (User)session.get(Constants.USER);
	
		if (user != null){

			try{

				ProjectManager manager = new ProjectManager();

				projectList = manager.loadUserProjects(user.getId(), false, false);
				
			}catch(Exception ex){
				log.error("Error in loadAllUserProjects" , ex);
				addActionError("Error while loading all projects ");
			}
		}

		return SUCCESS;
	}

	public String saveProject(){


		User user = (User)session.get(Constants.USER);

		String retVal = SUCCESS;

		try{

			if (user != null){

				ProjectManager pm = new ProjectManager();

				
				//check files
				if (project.getFiles() != null && !project.getFiles().isEmpty()){		


					for(int i = 0 ; i < project.getFiles().size() ; i++){
						ProjectFile file = project.getFiles().get(i);

						if(file.getId() > 0
								&& !projectFileList.isEmpty()
								&& projectFileList.get(i) != null  
								&& file.getFileName().trim().length() > 0){ //a
							//update file
							File newFile = new File(Constants.PROJECT_DIR + file.getFileDiskName());
							newFile.delete();//replace file by deleting the old one
							FileUtils.copyFile(projectFileList.get(i), newFile);
						}else if(file.getId() < 0
								&& !projectFileList.isEmpty()
								&& projectFileList.get(i) != null 
								&& file.getFileName().trim().length() > 0){

							//new File, add
							File newFile = new File(Constants.PROJECT_DIR + file.getFileDiskName());

							if(newFile.exists()){
								//generate random string and append
								String rmndStr = StringUtl.randomString(8);

								file.setFileDiskName(file.getFileDiskName() + "_" + rmndStr );
								newFile = new File(Constants.PROJECT_DIR + file.getFileDiskName());
							}

							FileUtils.copyFile(projectFileList.get(i), newFile); //TODO
						}else if(file.getId() < 0  //check where new one added and filename is there and file is missing.
								&& file.getFileName().trim().length() > 0
								&& file.getFileDiskName().trim().length() == 0	){
							addActionError("Please add file to : " + file.getFileName());
						}
					
					}

				}	

				if (!hasActionErrors() && !hasFieldErrors()){
					if (project != null && project.getId() > 0){				
						pm.updateProject(project);

					}else{
						project.setOwnerId(user.getId());
						project.setCrtDt(new Date());
						project = pm.addProject(project);

					}
				}else{
					retVal = INPUT;
				}

			}else{
				return Constants.ACCESS_DENIED;
			}

		}catch(Exception ex){
			log.error("Error in saveProject. " , ex);
			addActionError("Error while saving, ");
		}
		return retVal;
	}
	
	public String loadProjectForView(){

		try{
			ProjectManager pm = new ProjectManager();

			User user = (User)session.get(Constants.USER);

			if (user != null){

				if (viewProjectId > 0){
					project = pm.loadProjectById(viewProjectId, true, true);

					if (project != null ){
						return SUCCESS;
					}
					else{
						addActionError("Error loading project");
						return INPUT;
					}			

				}else{
					addActionError("Error , viewProjectId null");
					return INPUT;
				}			

			}else{
				return Constants.ACCESS_DENIED;
			}
		}catch(Exception ex){
			log.error("Error in loadProjectForView. " , ex);
			addActionError("Error while Loading project ");
		}

		return INPUT;
	}
	
	public String checkIfCanEdit(){

		try{
			ProjectManager pm = new ProjectManager();

			User user = (User)session.get(Constants.USER);

			if (user != null){
				
				if (viewProjectId > 0 ){
					
					Project p = pm.loadProjectById(viewProjectId, true, true);
					
					if (p.getOwnerId() == user.getId()){
						project = p;
						return SUCCESS;
					}else{
						return Constants.ACCESS_DENIED;
					}
					
				}else{
					return Constants.ACCESS_DENIED;
				}
				
			}
		}catch(Exception ex){
			log.error("Error in checkIfCanEdit. " , ex);
			addActionError("Error while editing ");
		}
		
		return SUCCESS;
	}
	public String delete(){

		User user = (User)session.get(Constants.USER);

		try{
			if (user != null){
				ProjectManager pm = new ProjectManager();

				if (viewProjectId > 0){
					pm.deleteProject(viewProjectId, user.getId());
					addActionMessage("Delete succesful");
				}else{
					log.error("delete::ViewProjectId is not valid. ViewProjectId: " + viewProjectId);
					addActionError("Error in action, please see logs");
				}

			}else{
				return Constants.ACCESS_DENIED;
			}
		}catch(Exception ex){
			log.error("Error in delete. " , ex);
			addActionError("Error while deleting ");
		}
		return SUCCESS;
	}
	public String download(){	

		String retVal = SUCCESS;
		
		String filename = getFileDownloadName();

		File file = new File(Constants.PROJECT_DIR, filename);
		
		if (file.exists())
		{
			try 
			{ 
				fileDownloadStream = new FileInputStream(file);
			}catch(Exception ex){
				addActionError("Error downloading file");
			}
		}else{
			addActionError("File not found: " + filename);
			retVal = INPUT;
		}
		
		return retVal;
	}
	
	public List<Project> getProjectList() {
		return projectList;
	}
	public void setProjectList(List<Project> projectList) {
		this.projectList = projectList;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public List<File> getProjectFileList() {
		return projectFileList;
	}

	public void setProjectFileList(List<File> projectFileList) {
		this.projectFileList = projectFileList;
	}

	public List<String> getProjectFileListFileName() {
		return projectFileListFileName;
	}

	public void setProjectFileListFileName(List<String> projectFileListFileName) {
		this.projectFileListFileName = projectFileListFileName;
	}

	public Integer getViewProjectId() {
		return viewProjectId;
	}

	public void setViewProjectId(Integer viewProjectId) {
		this.viewProjectId = viewProjectId;
	}

	public String getFileDownloadName() {
		return fileDownloadName;
	}

	public void setFileDownloadName(String fileDownloadName) {
		this.fileDownloadName = fileDownloadName;
	}

	public InputStream getFileDownloadStream() {
		return fileDownloadStream;
	}

	public void setFileDownloadStream(InputStream fileDownloadStream) {
		this.fileDownloadStream = fileDownloadStream;
	}
}
