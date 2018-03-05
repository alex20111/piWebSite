package net.project.bean;

import java.util.ArrayList;
import java.util.List;

import net.project.db.entity.FolderShare;

public class ShareToBean {

	private int    dbUseId = -1;
	private String fullName;
	private List<FolderShare> share;
	public List<FolderShare> getShare() {
		if (share == null){
			this.share = new ArrayList<FolderShare>();
		}	
		
		return share;
	}
	public void setShare(List<FolderShare> share) {
		if (share == null){
			this.share = new ArrayList<FolderShare>();
		}		
		this.share = share;
	}
	public int getDbUseId() {
		return dbUseId;
	}
	public void setDbUseId(int dbUseId) {
		this.dbUseId = dbUseId;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	
	public void addShare(FolderShare fldShare){
		if (share == null){
			this.share = new ArrayList<FolderShare>();
		}
		this.share.add(fldShare);
		
	}
	
}
