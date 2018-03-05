package net.project.db.manager;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.project.common.Constants;
import net.project.db.entity.Project;
import net.project.db.entity.ProjectFile;
import net.project.db.entity.ProjectRef;
import net.project.db.sql.ProjectSql;

public class ProjectManager {

	private ProjectSql sql;
	
	public ProjectManager(){
		this.sql = new ProjectSql();
	}	
	
	public List<Project> loadUserProjects(int userId, boolean loadRef, boolean loadFiles) throws ClassNotFoundException, SQLException{
		List<Project> projects = new ArrayList<Project>();
		
		projects = sql.loadAllProjectsByUser(userId, loadFiles, loadRef);
		
		return projects;
	}
	public Project loadProjectById(int projectId, boolean loadFiles, boolean loadRef) throws ClassNotFoundException, SQLException{
		return sql.loadProject(projectId, true, true);
	}
	public Project addProject(Project project) throws ClassNotFoundException, SQLException{

		if (project.getRefs() != null && !project.getRefs().isEmpty()){

			for (Iterator<ProjectRef> iter = project.getRefs().listIterator(); iter.hasNext(); ) {
				ProjectRef ref = iter.next();
				if (ref == null || ref.getTitle() == null || ref.getTitle().trim().length() == 0){
					iter.remove();
				}
			}
		}
		
		if (project.getFiles() != null && !project.getFiles().isEmpty()){

			for (Iterator<ProjectFile> iter = project.getFiles().listIterator(); iter.hasNext(); ) {
				ProjectFile file = iter.next();
				if (file == null || file.getFileName() == null || file.getFileName().trim().length() == 0){
					iter.remove();
				}
			}
		}

			

		return sql.addProject(project);
	}
	public void updateProject(Project project) throws ClassNotFoundException, SQLException{

		Project p = sql.loadProjectField(project.getId(), Project.CRT_DT, Project.UPD_DT);		

		project.setCrtDt(p.getCrtDt());
		project.setUpdDt(new Date());	


		//then update the references:
		if (project.getRefs() != null && !project.getRefs().isEmpty()){
			List<ProjectRef> toAdd = new ArrayList<ProjectRef>();
			List<ProjectRef> toUpd = new ArrayList<ProjectRef>();
			List<ProjectRef> toDel = new ArrayList<ProjectRef>();

			for(ProjectRef ref : project.getRefs()){
				if (ref.getId() > 0){
					if (ref.getTitle().trim().length() == 0){
						toDel.add(ref);
					}else{
						toUpd.add(ref);
					}
				}else if(ref.getId() < 0 && ref.getTitle().trim().length() > 0){
					toAdd.add(ref);
				}
			}

			if(!toAdd.isEmpty()){
				sql.addReference(null, toAdd, project.getId());
			}
			if(!toUpd.isEmpty()){
				sql.updateReference(null, toUpd, project.getId());
			}
			if(!toDel.isEmpty()){
				List<Integer> refIds = new ArrayList<Integer>();
				for(ProjectRef ref: toDel){
					refIds.add(ref.getId());				
				}		
				sql.deleteListOfReferences(refIds);			
			}			
		}

		//then update the references:
		if (project.getFiles() != null && !project.getFiles().isEmpty()){
			List<ProjectFile> toAdd = new ArrayList<ProjectFile>();
			List<ProjectFile> toUpd = new ArrayList<ProjectFile>();
			List<ProjectFile> toDel = new ArrayList<ProjectFile>();

			for(ProjectFile file : project.getFiles()){
				if (file.getId() > 0){
					if (file.getFileName().trim().length() == 0){
						toDel.add(file);
					}else{
						toUpd.add(file);
					}
				}else if(file.getId() < 0 && file.getFileName().trim().length() > 0){
					toAdd.add(file);
				}
			}

			if(!toAdd.isEmpty()){
				sql.addFiles(null, toAdd, project.getId());
			}
			if(!toUpd.isEmpty()){
				sql.updateFiles(null, toUpd, project.getId());
			}
			if(!toDel.isEmpty()){
				List<Integer> fileIds = new ArrayList<Integer>();
				for(ProjectFile ref: toDel){
					fileIds.add(ref.getId());				
				}		
				sql.deleteListOfFiles(fileIds);	
				
				//then remove them from the server
				for(ProjectFile delFile: toDel){
					File f = new File(Constants.PROJECT_DIR + delFile.getFileDiskName());
					f.delete();
				}
			}			
		}

		sql.updateProject(project);


	}
	
	public void deleteProject(int projectId, int userId) throws ClassNotFoundException, SQLException{

		//1st load the project
		Project project = loadProjectById(projectId, true, true);

		if (project.getOwnerId() == userId){


			//then delete
			if (project.getFiles() != null && !project.getFiles().isEmpty()){
				//delete files on disk then on db.
				List<Integer> delIds = new ArrayList<Integer>();
				for(ProjectFile file : project.getFiles()){
					File f = new File(Constants.PROJECT_DIR + file.getFileDiskName());
					f.delete();
					delIds.add(file.getId());
				}

				if (!delIds.isEmpty()){
					sql.deleteListOfFiles(delIds);
				}
			}


			if (project.getRefs() != null && !project.getRefs().isEmpty()){
				List<Integer> refIds = new ArrayList<Integer>();

				for(ProjectRef ref : project.getRefs()){
					refIds.add(ref.getId());

				}
				sql.deleteListOfReferences(refIds);
			}

			sql.deleteProject(projectId);		
		}

	}
}
