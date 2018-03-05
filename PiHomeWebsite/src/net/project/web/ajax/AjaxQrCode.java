package net.project.web.ajax;

import java.io.Serializable;

public class AjaxQrCode implements Serializable{
	

	private static final long serialVersionUID = -2976294484795612841L;
	private String qrBase64 = "";
	private String htmlImage = "";
	
	public String getQrBase64() {
		return qrBase64;
	}
	public void setQrBase64(String qrBase64) {
		this.qrBase64 = qrBase64;
	}
	public String getHtmlImage() {
		return htmlImage;
	}
	public void setHtmlImage(String htmlImage) {
		this.htmlImage = htmlImage;
	}
	
	

}
