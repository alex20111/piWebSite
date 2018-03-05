package net.project.db.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import home.db.DBConnection;
import home.db.DbClass;
import net.project.common.Constants;
import net.project.db.entity.Project;
import net.project.db.entity.ProjectFile;
import net.project.db.entity.ProjectRef;

public class ProjectSql {

	public Project loadProjectField(int projectId, String... fields) throws SQLException, ClassNotFoundException{
		Project project = null;
		DBConnection con = null;
		try{
			con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, DbClass.H2);

			StringBuilder query = new StringBuilder("SELECT "); 
			
			boolean first = true;
			for(String field: fields){
				if (first){
					query.append(field);
					first = false;
				}else{
					query.append(", " + field);
				}
			}			
			
			query.append(" FROM " + Project.TBL_NAME + " where " + Project.ID + " = :id ");

			ResultSet rs = con.createSelectQuery(query.toString())
					.setParameter("id", projectId)
					.getSelectResultSet();

			while (rs.next()) {
				project = new Project();
				for(String field: fields){
					if (field.equals(Project.CRT_DT)){
						project.setCrtDt(rs.getTimestamp(Project.CRT_DT));
					}else if(field.equals(Project.DESCRIPTION)){
						project.setDesc(rs.getString(Project.DESCRIPTION));
					}else if(field.equals(Project.ID)){
						project.setId(rs.getInt(Project.ID));
					}else if(field.equals(Project.OWNER_ID)){
						project.setOwnerId(rs.getInt(Project.OWNER_ID));
					}else if(field.equals(Project.TITLE)){
						project.setTitle(rs.getString(Project.TITLE));
					}else if(field.equals(Project.UPD_DT)){
						project.setUpdDt(rs.getTimestamp(Project.UPD_DT));
					}
				}		
			
			}

		}finally{
			if (con!=null){
				con.close();
			}
		}
		return project;
	}
	
	
	public List<Project> loadAllProjectsByUser(int userId, boolean loadFiles, boolean loadRef) throws SQLException, ClassNotFoundException{
		List<Project> projList = new ArrayList<Project>();
		DBConnection con = null;
		try{
			con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, DbClass.H2);

			String query = "SELECT * FROM " + Project.TBL_NAME + " where " + Project.OWNER_ID + " = :owner ";

			ResultSet rs = con.createSelectQuery(query)
					.setParameter("owner", userId)
					.getSelectResultSet();

			while (rs.next()) {
				Project inv = new Project(rs);		

				if (loadFiles){
					inv.setFiles(loadFiles(inv.getId(), con));
				}
				if (loadRef){
					inv.setRefs(loadReferences(inv.getId(), con));
				}

				projList.add(inv);
			}

		}finally{
			if (con!=null){
				con.close();
			}
		}
		return projList;
	}

	public Project loadProject(int projectId, boolean loadFiles, boolean loadRef) throws SQLException, ClassNotFoundException{
		Project project = null;
		DBConnection con = null;
		try{
			con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, DbClass.H2);

			String query = "SELECT * FROM " + Project.TBL_NAME + " where " + Project.ID + " = :id ";

			ResultSet rs = con.createSelectQuery(query)
					.setParameter("id", projectId)
					.getSelectResultSet();

			while (rs.next()) {
				project = new Project(rs);		

				if (loadFiles){
					project.setFiles(loadFiles(project.getId(), con));
				}
				if (loadRef){
					project.setRefs(loadReferences(project.getId(), con));
				}			
			}

		}finally{
			if (con!=null){
				con.close();
			}
		}
		return project;
	}
		
	
	public Project addProject(Project project) throws SQLException, ClassNotFoundException{

		DBConnection con = null;
		try{
			con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, DbClass.H2);

			int key = con.buildAddQuery(Project.TBL_NAME)
					.setParameter(Project.CRT_DT, project.getCrtDt())
					.setParameter(Project.DESCRIPTION, project.getDesc())
					.setParameter(Project.OWNER_ID, project.getOwnerId())
					.setParameter(Project.TITLE, project.getTitle())
					.setParameter(Project.UPD_DT, project.getUpdDt())
					.add();

			project.setId(key);
			
			//add the references to the item.
			if (project.getRefs() != null && !project.getRefs().isEmpty()){
				addReference(con, project.getRefs(), key);
			}
			//add the references to the item.
			if (project.getFiles() != null && !project.getFiles().isEmpty()){
				addFiles(con, project.getFiles(), key);
			}

		}finally{
			if (con!=null){
				con.close();
			}
		}

		return project;
	}
	public void updateProject(Project project) throws SQLException, ClassNotFoundException{

		DBConnection con = null;
		try{
			con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, DbClass.H2);

			con.buildUpdateQuery(Project.TBL_NAME)
			.setParameter(Project.CRT_DT, project.getCrtDt())
			.setParameter(Project.DESCRIPTION, project.getDesc())
			.setParameter(Project.TITLE, project.getTitle())
			.setParameter(Project.UPD_DT, project.getUpdDt())

			.addUpdWhereClause("WHERE " + Project.ID + " = :id", project.getId())
			.update();

		}finally{
			if (con!=null){
				con.close();
			}
		}
	}

	public void addReference(DBConnection con, List<ProjectRef> refs, int projectId) throws SQLException, ClassNotFoundException{

		boolean closeCon = false;
		if (con == null){
			con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, DbClass.H2);
			closeCon = true;
		}
		
		
		con.buildAddQuery(ProjectRef.TBL_NAME);

		for(ProjectRef ref : refs){						
			con.setParameter(ProjectRef.PROJECT_ID, projectId)
			.setParameter(ProjectRef.REF, ref.getTitle())
			.setParameter(ProjectRef.REF_TYPE, ref.getType().name())			
			.addToBatch();
		}
		con.executeBatchQuery();
		
		if (closeCon){
			con.close();
		}
		
	}
	public void updateReference(DBConnection con, List<ProjectRef> refs,  int projectId) throws SQLException, ClassNotFoundException{

		boolean closeCon = false;
		if (con == null){
			con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, DbClass.H2);
			closeCon = true;
		}		

		for(ProjectRef ref : refs){	

			con.buildUpdateQuery(ProjectRef.TBL_NAME)

			.setParameter(ProjectRef.REF, ref.getTitle())
			.setParameter(ProjectRef.REF_TYPE, ref.getType().name())

			.addUpdWhereClause("WHERE " + ProjectRef.ID + " = :invId", ref.getId())
			.update();
		}

		if (closeCon){
			con.close();
		}	
	}
	
	
	public void addFiles(DBConnection con, List<ProjectFile> files, int projectId) throws SQLException, ClassNotFoundException{

		boolean closeCon = false;
		if (con == null){
			con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, DbClass.H2);
			closeCon = true;
		}
		
		con.buildAddQuery(ProjectFile.TBL_NAME);

		for(ProjectFile file : files){						
			con.setParameter(ProjectFile.PROJECT_ID, projectId)
			.setParameter(ProjectFile.FILE_DISK_NAME, file.getFileDiskName())
			.setParameter(ProjectFile.FILE_NAME, file.getFileName())			
			.addToBatch();
		}
		
		con.executeBatchQuery();
		
		if (closeCon){
			con.close();
		}
		
	}
	public void updateFiles(DBConnection con, List<ProjectFile> files, int projectId) throws SQLException, ClassNotFoundException{

		boolean closeCon = false;
		if (con == null){
			con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, DbClass.H2);
			closeCon = true;
		}		

		for(ProjectFile file : files){	
			con.buildUpdateQuery(ProjectFile.TBL_NAME)
			.setParameter(ProjectFile.FILE_DISK_NAME, file.getFileDiskName())
			.setParameter(ProjectFile.FILE_NAME, file.getFileName())

			.addUpdWhereClause("WHERE " + ProjectFile.ID + " = :invId", file.getId())
			.update();
		}

		if (closeCon){
			con.close();
		}	
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void deleteListOfFiles(List<Integer> filesIds) throws SQLException, ClassNotFoundException{

		DBConnection con = null;
		try{
			con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, DbClass.H2);

			con.deleteInBatch(ProjectFile.TBL_NAME, ProjectFile.ID, (List)filesIds);
			
		}finally{
			if (con!=null){
				con.close();
			}
		}
	}	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void deleteListOfReferences(List<Integer> refIds) throws SQLException, ClassNotFoundException{

		DBConnection con = null;
		try{
			con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, DbClass.H2);

			con.deleteInBatch(ProjectRef.TBL_NAME, ProjectRef.ID, (List)refIds);
			
		}finally{
			if (con!=null){
				con.close();
			}
		}
	}	
	public void deleteProject(int projectId) throws SQLException, ClassNotFoundException{
		DBConnection con = null;
		try{
			con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, DbClass.H2);

			String query = "DELETE FROM " + Project.TBL_NAME + " where id = :id";

			con.createSelectQuery(query)
			.setParameter("id", projectId)
			.delete();

		}finally{
			if (con!=null){
				con.close();
			}
		}
	}
	private List<ProjectRef> loadReferences(int projectId, DBConnection con) throws ClassNotFoundException, SQLException{

		boolean closeCon = false;
		if (con == null){
			con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, DbClass.H2);
			closeCon = true;
		}		

		String refQuery = "Select * from " + ProjectRef.TBL_NAME + " WHERE " + ProjectRef.PROJECT_ID + " = :projId";
		ResultSet refRs = con.createSelectQuery(refQuery).setParameter("projId", projectId).getSelectResultSet();

		List<ProjectRef> refs = new ArrayList<ProjectRef>();
		while(refRs.next()){
			ProjectRef ref = new ProjectRef(refRs);
			refs.add(ref);
		}

		if (closeCon){
			con.close();
		}

		return refs;
	}
	private List<ProjectFile> loadFiles(int projectId, DBConnection con) throws ClassNotFoundException, SQLException{

		boolean closeCon = false;
		if (con == null){
			con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, DbClass.H2);
			closeCon = true;
		}		

		String refQuery = "Select * from " + ProjectFile.TBL_NAME + " WHERE " + ProjectFile.PROJECT_ID + " = :projId";
		ResultSet refRs = con.createSelectQuery(refQuery).setParameter("projId", projectId).getSelectResultSet();

		List<ProjectFile> files = new ArrayList<ProjectFile>();
		while(refRs.next()){
			ProjectFile file = new ProjectFile(refRs);
			files.add(file);
		}

		if (closeCon){
			con.close();
		}

		return files;
	}
}
