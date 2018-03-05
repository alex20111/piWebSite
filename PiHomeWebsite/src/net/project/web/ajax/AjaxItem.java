package net.project.web.ajax;

import java.io.Serializable;

public class AjaxItem implements Serializable{
	

	private static final long serialVersionUID = -2976294484795612841L;
	private int itemId = -1;
	private Boolean deleteSuccess = false;
	private String error = null;
	
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public Boolean getDeleteSuccess() {
		return deleteSuccess;
	}
	public void setDeleteSuccess(Boolean deleteSuccess) {
		this.deleteSuccess = deleteSuccess;
	}
}
