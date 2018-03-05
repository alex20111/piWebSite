package net.project.db.sql;

import home.db.DBConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.project.common.Constants;
import net.project.db.entity.FileInfo;
import net.project.db.entity.Folder;
import net.project.db.entity.FolderShare;
import net.project.db.entity.User;
import net.project.db.manager.UserManager;

public class FolderSql {

	//user map
	private Map<Integer, User> userMap;
	private UserManager um = null;
	
	
	/**
	 * load all folders by user ID. 
	 * @param userId - User id the user
	 * @param loadFiles	- true , load files for that folder.
	 * @param 
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException 
	 */
	public List<Folder> loadFoldersByUserId(int userId, boolean loadFiles, boolean loadOwner, boolean loadShareTo) throws SQLException, ClassNotFoundException{
		
		List<Folder> dirList = new ArrayList<Folder>();		

		DBConnection con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);

		String query = "SELECT * FROM " + Folder.TBL_NAME + " where " + Folder.OWNER_ID + " = :userId";

		ResultSet rs = con.createSelectQuery(query)
				.setParameter("userId", userId)
				.getSelectResultSet();

		while (rs.next()) {

			Folder dir = new Folder(rs);

			if (loadFiles){
				dir.setFiles(loadFilesByFolderId(dir.getId(), con));
			}
			if (loadOwner){				
				dir.setOwner(loadOwner(dir.getOwnerId()));
			}		
			if (loadShareTo){
				dir.setSharedTo(loadFolderShares(dir.getId(), con));
			}
			
			dirList.add(dir);			
		}

		con.close();
		return dirList;
	}
	/**
	 * @param loadShareTo - load all user that this folder is shared to
	 * @throws ClassNotFoundException 
	 *
	 */
	public Folder loadFolderByFolderId(int folderId, boolean loadFiles, boolean loadOwner, boolean loadShareTo) throws SQLException, ClassNotFoundException{

		Folder folder = null;

		DBConnection con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);

		String query = "SELECT * FROM " + Folder.TBL_NAME + " where " + Folder.ID+ " = :folderId";

		ResultSet rs = con.createSelectQuery(query)
				.setParameter("folderId", folderId)
				.getSelectResultSet();

		while (rs.next()) {
			folder = new Folder(rs);
		
			if (loadFiles){				
				folder.setFiles(loadFilesByFolderId(folder.getId(), con));
			}
			if (loadOwner){				
				folder.setOwner(loadOwner(folder.getOwnerId()));
			}
			if (loadShareTo){				
				folder.setSharedTo(loadFolderShares(folder.getId(), con));
			}
					
		}

		con.close();
		return folder;
	}
	
	
	
	public Folder addFolder(Folder fld) throws SQLException, ClassNotFoundException{

		DBConnection con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);

		int key = con.buildAddQuery(Folder.TBL_NAME)
				.setParameter(Folder.CREATED_DATE, fld.getCreatedDate())
				.setParameter(Folder.FOLDER_NAME, fld.getFolderName())
				.setParameter(Folder.LAST_UPDATE, fld.getLastUpated())
				.setParameter(Folder.FOLDER_SIZE, fld.getFolderSize())
				.setParameter(Folder.OWNER_ID, fld.getOwnerId())								
				.add();

		con.close();

		fld.setId(key);

		return fld;
	}
	
	public void updateFolder(Folder fld) throws SQLException, ClassNotFoundException{

		DBConnection con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);

		con.buildUpdateQuery(Folder.TBL_NAME)

		.setParameter(Folder.CREATED_DATE, fld.getCreatedDate())
		.setParameter(Folder.FOLDER_NAME, fld.getFolderName())
		.setParameter(Folder.LAST_UPDATE, fld.getLastUpated() )
		.setParameter(Folder.FOLDER_SIZE, fld.getFolderSize())
		.setParameter(Folder.OWNER_ID, fld.getOwnerId() )

		.addUpdWhereClause("WHERE " + Folder.ID + " = :dirId", fld.getId())
		.update();


		con.close();
	}
	
	public void deleteFolder(int folderId) throws SQLException, ClassNotFoundException{
		
		DBConnection con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);
		
		String delete = "DELETE FROM " + Folder.TBL_NAME + " where id = :id";
		con.createSelectQuery(delete)
		.setParameter("id", folderId).delete();
		con.close();
		
	}
	public void deleteFolders(List<Integer> folderIds) throws SQLException, ClassNotFoundException{
		DBConnection con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);
		
		StringBuilder delete = new StringBuilder("DELETE FROM " + Folder.TBL_NAME + " where ( " );//id = :id";
		
		boolean first = true;
		for(int i=0 ; i < folderIds.size() ; i ++) {
			
			if(first){
				delete.append(" id = :id"+i );
				first = false;
			}else{
				delete.append(" OR id = :id"+i );
			}
		}
		delete.append( " ) ");
		
		con.createSelectQuery(delete.toString());
		
		for(int i=0 ; i < folderIds.size() ; i ++){
			con.setParameter("id"+i, folderIds.get(i));
		}
		con.delete();
		
		con.close();
	}


	
	///////////////////////
	//  SHares
	//////////////////////
	/**
	 * load all Folders that is shared to the user id provided.
	 * @param sharedToUserId
	 * @return
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public List<FolderShare> loadSharesToUserId(int sharedToUserId) throws SQLException, ClassNotFoundException{
		List<FolderShare> shareList = new ArrayList<FolderShare>();
		
		DBConnection con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);

		String query = "SELECT * FROM " + FolderShare.TBL_NAME + " where " + FolderShare.TO_USER + " = :toUser";

		ResultSet rs = con.createSelectQuery(query)
				.setParameter("toUser", sharedToUserId)
				.getSelectResultSet();

		while (rs.next()) {
				FolderShare share = new FolderShare(rs);
				shareList.add(share);
		}

		con.close();
		
		
		
		return shareList;
	}
	public FolderShare addShare(FolderShare share) throws SQLException, ClassNotFoundException{

		DBConnection con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);

		int key = con.buildAddQuery(FolderShare.TBL_NAME)
				.setParameter(FolderShare.DELETE_FILE,share.isDeleteFile())
				.setParameter(FolderShare.DELETE_FLD,share.isDeleteFolder())
				.setParameter(FolderShare.DOWNLOAD,share.isDownload())
				.setParameter(FolderShare.OWNER_USER,share.getOwnerUserId())
				.setParameter(FolderShare.SHARE_CRTD,share.getShareCreated())
				.setParameter(FolderShare.SHARE_UPD,share.getShareUpdated())
				.setParameter(FolderShare.TO_USER,share.getToUserId())
				.setParameter(FolderShare.UPLOAD,share.isUpload())
				.setParameter(FolderShare.FOLDER_ID,share.getFolderId())
				.add();

		con.close();

		share.setId(key);

		return share;
	}
	/**
	 * Return a folder share based on the folder id and user Id.
	 * @param folderId
	 * @param userId
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException 
	 */
	public FolderShare loadShareByFolderAndUserId(int folderId, int userId) throws SQLException, ClassNotFoundException{
		
		FolderShare sh = null;
		
		DBConnection con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);

		String query = "SELECT * FROM " + FolderShare.TBL_NAME + " where " + FolderShare.TO_USER + " = :toUser AND " +  FolderShare.FOLDER_ID + " = :fldId ";

		ResultSet rs = con.createSelectQuery(query)
				.setParameter("toUser", userId)
				.setParameter("fldId", folderId)
				.getSelectResultSet();

		while (rs.next()) {
				sh = new FolderShare(rs);				
		}

		con.close();
		
		return sh;
		
	}
	public List<FolderShare> loadSharesByFolderIds(List<Integer> folderIdList) throws SQLException, ClassNotFoundException{
		
	List<FolderShare> shareList = new ArrayList<FolderShare>();
		
		DBConnection con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);

		StringBuilder query = new StringBuilder("SELECT * FROM " + FolderShare.TBL_NAME + " where ( ");// + FolderShare.ID + " = :toUser AND " +  FolderShare.FOLDER_ID + " = :fldId ");

		boolean first = true;
		for(int i=0 ; i < folderIdList.size() ; i ++) {
			
			if(first){
				query.append( FolderShare.ID + " = :id"+i );
				first = false;
			}else{
				query.append(" OR " + FolderShare.ID + " = :id"+i );
			}
		}
		query.append( " ) ");
		
		con.createSelectQuery(query.toString());
		
		for(int i=0 ; i < folderIdList.size() ; i ++){
			con.setParameter("id"+i, folderIdList.get(i));
		}
		
		
		ResultSet rs = con.getSelectResultSet();

		while (rs.next()) {
				FolderShare sh = new FolderShare(rs);
				shareList.add(sh);
		}

		con.close();
		
		return shareList;
		
	}
	public void updateShareAccessFields(FolderShare share) throws SQLException, ClassNotFoundException{

		DBConnection con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);

		con.buildUpdateQuery(FolderShare.TBL_NAME)

		.setParameter(FolderShare.DELETE_FILE, share.isDeleteFile())
		.setParameter(FolderShare.DELETE_FLD, share.isDeleteFolder())
		.setParameter(FolderShare.DOWNLOAD, share.isDownload() )
		.setParameter(FolderShare.UPLOAD, share.isUpload() )
		.setParameter(FolderShare.SHARE_UPD, new Date() )

		.addUpdWhereClause("WHERE " + FolderShare.ID + " = :shareId", share.getId())
		.update();


		con.close();
	}
	
	public void deleteShareList(List<Integer> shareIds) throws SQLException, ClassNotFoundException{

		DBConnection con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);
		
		StringBuilder delete = new StringBuilder("DELETE FROM " + FolderShare.TBL_NAME + " where ( " );//id = :id";
		
		boolean first = true;
		for(int i=0 ; i < shareIds.size() ; i ++) {
			
			if(first){
				delete.append(" id = :id"+i );
				first = false;
			}else{
				delete.append(" OR id = :id"+i );
			}
		}
		delete.append( " ) ");
		
		con.createSelectQuery(delete.toString());
		
		for(int i=0 ; i < shareIds.size() ; i ++){
			con.setParameter("id"+i, shareIds.get(i));
		}
		con.delete();
		
		con.close();
	}
	
	/////////////////////////////
	// Files
	////////////////////////////
		
	public int countFilesForFolder(int folderId) throws SQLException, ClassNotFoundException{
		int count = 0;
		DBConnection con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);

		String query = "SELECT count(*) FROM " + FileInfo.TBL_NAME + " where " + FileInfo.FOLDER_ID + " = :" + FileInfo.FOLDER_ID ;

		ResultSet rs = con.createSelectQuery(query)
				.setParameter(FileInfo.FOLDER_ID , folderId)
				.getSelectResultSet();

		while (rs.next()) {
			count = rs.getInt(1);
		}

		con.close();
		
		return count;
	}
	public List<FileInfo> loadFilesByDirId(int dirId) throws SQLException, ClassNotFoundException{

		List<FileInfo> fileList = new ArrayList<FileInfo>();

		DBConnection con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);

		String query = "SELECT * FROM " + FileInfo.TBL_NAME + " where " + FileInfo.FOLDER_ID + " = :dirId";

		ResultSet rs = con.createSelectQuery(query)
				.setParameter("dirId", dirId)
				.getSelectResultSet();

		while (rs.next()) {
			FileInfo fi  = new FileInfo(rs);
			fileList.add(fi);			
		}

		con.close();
		return fileList;
	}
	
	public FileInfo addFile(FileInfo file) throws SQLException, ClassNotFoundException{

		DBConnection con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);

		int key = con.buildAddQuery(FileInfo.TBL_NAME)
				.setParameter(FileInfo.FOLDER_ID, file.getFolderId())
				.setParameter(FileInfo.FILE_CREATED, file.getFileCreated())
				.setParameter(FileInfo.FILE_NAME, file.getFileName())
				.setParameter(FileInfo.FILE_SIZE, file.getFileSize())
								
				.add();

		con.close();

		file.setId(key);

		return file;
	}
	
	
	public void addFileList(List<FileInfo> files) throws SQLException, ClassNotFoundException{
		DBConnection con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);
		
		con.buildAddQuery(FileInfo.TBL_NAME);
		 
		for(FileInfo file : files){
			con.setParameter(FileInfo.FOLDER_ID, file.getFolderId())
				.setParameter(FileInfo.FILE_CREATED, file.getFileCreated())
				.setParameter(FileInfo.FILE_NAME, file.getFileName())
				.setParameter(FileInfo.FILE_SIZE, file.getFileSize())
			.addToBatch();
		}
		
		con.executeBatchQuery();
		con.close();
	}
	
	public void updateFile(FileInfo file) throws SQLException, ClassNotFoundException{

		DBConnection con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);

		con.buildUpdateQuery(FileInfo.TBL_NAME)

		.setParameter(FileInfo.FOLDER_ID, file.getFolderId())
		.setParameter(FileInfo.FILE_CREATED, file.getFileCreated())
		.setParameter(FileInfo.FILE_NAME, file.getFileName())
		.setParameter(FileInfo.FILE_SIZE, file.getFileSize())

		.addUpdWhereClause("WHERE " + FileInfo.ID + " = :fileId", file.getId())
		.update();


		con.close();
	}
	public void deleteFile(int fileId) throws SQLException, ClassNotFoundException{

		DBConnection con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);
		
		String delete = "DELETE FROM " + FileInfo.TBL_NAME + " where id = :id";
		con.createSelectQuery(delete)
		.setParameter("id", fileId).delete();
		con.close();
	}
	public void deleteFileList(List<Integer> fileIds) throws SQLException, ClassNotFoundException{

		DBConnection con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);
		
		StringBuilder delete = new StringBuilder("DELETE FROM " + FileInfo.TBL_NAME + " where ( " );//id = :id";
		
		boolean first = true;
		for(int i=0 ; i < fileIds.size() ; i ++) {
			
			if(first){
				delete.append(" id = :id"+i );
				first = false;
			}else{
				delete.append(" OR id = :id"+i );
			}
		}
		delete.append( " ) ");
		
		con.createSelectQuery(delete.toString());
		
		for(int i=0 ; i < fileIds.size() ; i ++){
			con.setParameter("id"+i, fileIds.get(i));
		}
		con.delete();
		
		con.close();
	}
	public List<FileInfo> loadFilesByFileIds(List<Integer> fileIds) throws SQLException, ClassNotFoundException{

		List<FileInfo> fileList = new ArrayList<FileInfo>();

		DBConnection con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);

		StringBuilder query = new StringBuilder("SELECT * FROM " + FileInfo.TBL_NAME + " where ( "); // + FileInfo.FOLDER_ID + " = :dirId");
		
		boolean first = true;
		for(int i = 0 ; i < fileIds.size() ; i++){
			
			if (first){
				query.append(FileInfo.ID + " = :id"+i);
				first = false;
			}else{
				query.append(" OR " + FileInfo.ID + " = :id"+i);
			}
		}
		query.append(" ) " );

		 con.createSelectQuery(query.toString());
		
		 for(int i=0 ; i < fileIds.size() ; i ++){
				con.setParameter("id"+i, fileIds.get(i));
			}
		 
		 ResultSet rs = con.getSelectResultSet();

		while (rs.next()) {
			FileInfo fi  = new FileInfo(rs);
			fileList.add(fi);			
		}

		con.close();
		return fileList;
	}
	
	
	//////////////////////
	// class methods
	/////////////////////
	private Set<FileInfo> loadFilesByFolderId(int folderId, DBConnection con) throws SQLException{
		
		//load files
		String loadFilesQuery = "SELECT * FROM " + FileInfo.TBL_NAME + " where " + FileInfo.FOLDER_ID + " = :folderId";
		ResultSet rs2 = con.createSelectQuery(loadFilesQuery)
				.setParameter("folderId", folderId)
				.getSelectResultSet();

		Set<FileInfo> fiList = new HashSet<FileInfo>();
		while(rs2.next()){
			FileInfo fi = new FileInfo(rs2);
			fiList.add(fi);
		}
		
		return fiList;
	}
	private User loadOwner(int id) throws SQLException, ClassNotFoundException{
		
		if (userMap == null){
			userMap = new HashMap<Integer, User>();
		}
		
		if (um == null){
			um = new UserManager();
		}
		
		User user = null;
		
		if(userMap.containsKey(id)){
			user = userMap.get(id);
		}else{
			user = um.loadUserById(id);
			userMap.put(user.getId(), user);
		}
		
		return user;
	}
	
	private List<FolderShare> loadFolderShares(int folderId, DBConnection con) throws SQLException{

		//load files
		String loadFilesQuery = "SELECT * FROM " + FolderShare.TBL_NAME + " where " + FolderShare.FOLDER_ID + " = :folderId";
		ResultSet rs2 = con.createSelectQuery(loadFilesQuery)
				.setParameter("folderId", folderId)
				.getSelectResultSet();

		List<FolderShare> shares = new ArrayList<FolderShare>();
		while(rs2.next()){
			FolderShare sh = new FolderShare(rs2);
			shares.add(sh);
		}
		return shares;
	}
}
