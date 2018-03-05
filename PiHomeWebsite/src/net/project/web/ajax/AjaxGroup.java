package net.project.web.ajax;

import java.io.Serializable;

public class AjaxGroup implements Serializable{
	
	private static final long serialVersionUID = -213417919947134573L;
	
	private int groupId = -1;
	private String error = null;
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
}
