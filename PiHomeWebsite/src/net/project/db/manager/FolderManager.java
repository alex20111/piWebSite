package net.project.db.manager;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.project.db.entity.FileInfo;
import net.project.db.entity.Folder;
import net.project.db.entity.FolderShare;
import net.project.db.entity.User;
import net.project.db.sql.FolderSql;
import net.project.exception.ValidationException;

public class FolderManager {
	
	private FolderSql sql = null;
	
	public FolderManager(){
		this.sql = new FolderSql();
	}
	

	/**
	 * Load the folder by user id
	 * 
	 * @param userId
	 * @param loadFiles
	 * @param loadSharedFolders - Load the folders that has been shared to the user.
	 * @param loadOwner - Load the owner of the folder.
	 * @param loadShareTo - load all user that this folder is shared to
	 * @param countFiles
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException 
	 */
	public List<Folder> loadFoldersByUserId(int userId, boolean loadFiles, boolean loadSharedFrom,
												boolean loadOwner, boolean countFiles, boolean loadSharedTo) throws SQLException, ClassNotFoundException{
		
		List<Folder> dirs = sql.loadFoldersByUserId(userId, loadFiles, loadOwner, loadSharedTo);
		
		if (loadSharedFrom){			
		//	load share that an other user has shared with this user. 		
			List<FolderShare> shareList = sql.loadSharesToUserId(userId);
			
			for(FolderShare share : shareList){
				dirs.add(sql.loadFolderByFolderId(share.getFolderId(), loadFiles, loadOwner, true));
			}
		}	
		
		if (countFiles){
			for(Folder f : dirs){
				if (f.getFiles() != null && f.getFiles().size() > 0){
					f.setFileNumber(f.getFiles().size());
				}else{
					f.setFileNumber(sql.countFilesForFolder(f.getId()));
				}
			}
		}	
		return dirs;
	}
	public List<FolderShare> loadShareFromByUserId(int userId) throws SQLException, ClassNotFoundException{
		
		
		
		List<FolderShare> shareList = sql.loadSharesToUserId(userId);
		
		for(FolderShare share : shareList){
			Folder f = sql.loadFolderByFolderId(share.getFolderId(), false, true, false);
			share.setFolderName(f.getFolderName());
			share.setOwner(f.getOwner());
		}
		
		return shareList;
	}
	/**
	 * Load the folder by Folder Id
	 * @param dirId
	 * @param loadFiles
	 * @param loadOwner
	 * @param countFiles
	 * @param loadShareTo - load all user that this folder is shared to
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException 
	 */
	public Folder loadFolderByDirId(int dirId, boolean loadFiles, boolean loadOwner, boolean countFiles, boolean loadShareTo) throws SQLException, ClassNotFoundException{

		Folder f = sql.loadFolderByFolderId(dirId, loadFiles, loadOwner, loadShareTo);

		if (countFiles){
			if (f.getFiles() != null && f.getFiles().size() > 0){
				f.setFileNumber(f.getFiles().size());
			}else{
				f.setFileNumber(sql.countFilesForFolder(f.getId()));
			}			
		}

		return f;
	}
	
	public void deleteFolders(List<Integer> folderIds, User user) throws SQLException, ValidationException, ClassNotFoundException{
		//1st you need to delete all the files for the folder.

		for(Integer folderId: folderIds){

			//1st get folder to delete
			Folder fld = loadFolderByDirId(folderId, false, false, false, true);

			if (user.getId() == fld.getOwnerId().intValue() || fld.canDeleteFolder(user.getId()) ){

				//gather all files for the folder
				List<FileInfo> files = loadFilesByDirId(folderId.intValue());			
				if (files.size() > 0){
					List<Integer> fileIds = new ArrayList<Integer>();
					for(FileInfo f : files){
						fileIds.add(f.getId());
					}
					//delete all files from DB
					sql.deleteFileList(fileIds);
				}
				//delete the folder from DB
				sql.deleteFolder(folderId);

				//then delete on disk			
				for (FileInfo f : files){
					File diskFile = new File(f.getFileLocAsString(fld.getFolderName(), user.getUserName()));

					diskFile.delete();
				}

				File dir = new File(fld.getFolderLocAsString(user));
				dir.delete();
			}else{
				throw new ValidationException("Cannot delete folder " + fld.getFolderName() + " since you don't own it and you are not given permission to delete");
			}
		}

	}
	
	public Folder addFolder(Folder fld) throws SQLException, ValidationException, ClassNotFoundException{

		Folder folder = null;
		
		//check if folder name exist for the user 1st.
		List<Folder> flds = loadFoldersByUserId(fld.getOwnerId(), false, false, false, false, false);

		if (flds.size() > 0){
			for(Folder f : flds){
				if (f.getFolderName().trim().equals(fld.getFolderName().trim())){
					throw new ValidationException("Folder name already exist");				
				}
			}
		}

		folder = sql.addFolder(fld);

		return folder;
	}
	public void updateFolder(Folder fld) throws SQLException, ClassNotFoundException{
		sql.updateFolder(fld);
	}
	
	public List<FileInfo> loadFilesByDirId(int dirId) throws SQLException, ClassNotFoundException{
		return sql.loadFilesByDirId(dirId);
	}
	public FileInfo addFile(FileInfo file) throws SQLException, ClassNotFoundException{
		return sql.addFile(file);
	}
	public void deleteFiles(List<Integer> fileIds, String userName, Integer folderId) throws SQLException, ValidationException, ClassNotFoundException{
		
		Folder folder = sql.loadFolderByFolderId(folderId, false, false, false);
		
		List<FileInfo> fi = sql.loadFilesByFileIds(fileIds);
		
		sql.deleteFileList(fileIds);
		
		//delete from disk
		long fileSizeToRemove = 0l;
		for(FileInfo fdel : fi){
			fileSizeToRemove += fdel.getFileSize();
			
			File del = new File(fdel.getFileLocAsString(folder.getFolderName(), userName));
			
			del.delete();
		}
		
		//then update folder with last updated date and size
		folder.setLastUpated(new Date());
		folder.setFolderSize(folder.getFolderSize() - fileSizeToRemove);
		
		updateFolder(folder);		
		
	}
	
	public void addShare(FolderShare fs) throws SQLException, ClassNotFoundException{
		
		sql.addShare(fs);
		
	}
	
	public FolderShare loadShareByFolderAndUserId(int folderId, int userId) throws SQLException, ClassNotFoundException{
		return sql.loadShareByFolderAndUserId(folderId, userId);
	}
	
	/**
	 * update the share to other user by share id.
	 * @param shares
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException 
	 */
	public boolean updateSharesTOByShareList(List<FolderShare> shares) throws SQLException, ClassNotFoundException{
		
		boolean fieldUpdated = false;
		
		List<Integer> shareIds = new ArrayList<Integer>();
		for(FolderShare s : shares){
			shareIds.add(s.getId());
		}
		
		List<FolderShare> oldShareList = sql.loadSharesByFolderIds(shareIds);
		
		//verify if any changed.		
		for (FolderShare newShare : shares){
			for(FolderShare oldShare: oldShareList){
				if (newShare.getId() == oldShare.getId() && 
						( newShare.isDeleteFile() != oldShare.isDeleteFile() ||
						newShare.isDeleteFolder() != oldShare.isDeleteFolder() ||
						newShare.isDownload() != oldShare.isDownload() ||
						newShare.isUpload() != oldShare.isUpload() )){
					fieldUpdated = true;
					sql.updateShareAccessFields(newShare);					
					break;					
				}
			}
		}	
		
		return fieldUpdated;
	}
	
	public void deleteShares(List<Integer> shareIds) throws SQLException, ClassNotFoundException{
		
		sql.deleteShareList(shareIds);
		
	}

}
