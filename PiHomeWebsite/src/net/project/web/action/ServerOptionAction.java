package net.project.web.action;

import home.crypto.Encryptor;

import java.util.Map;

import net.project.common.Constants;
import net.project.db.entity.Config;
import net.project.db.entity.User;
import net.project.db.manager.ConfigManager;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

public class ServerOptionAction extends ActionSupport implements SessionAware {

	
	private static final long serialVersionUID = 6401116538574137146L;

	private Map<String, Object> session;
	
	private Config cfg;
	
	private String saveEmailBtn;
	private String testEmailBtn;
	
	public String loadEmailOptions(){

		try{

			User user = (User)session.get(Constants.USER);

			if (user != null && user.isAdministrator()){

				cfg = new ConfigManager().loadConfig();
				if (cfg.getEmailPassword() != null && !cfg.getEmailPassword().isEmpty()){
					cfg.setEmailPassword(Encryptor.decryptString(cfg.getEmailPassword(), Constants.EMAILKEY.toCharArray()));
				}

			}else{
				return Constants.ACCESS_DENIED;
			}

		}catch (Exception ex){
			ex.printStackTrace();
		}


		return SUCCESS;
	}
	public String handleEmailBtn(){

		try{
			User user = (User)session.get(Constants.USER);

			ConfigManager cfm;

			if (user != null && user.isAdministrator()){

				//HANDLE save
				if (saveEmailBtn != null){

					if (cfg.getEmailPassword() != null && !cfg.getEmailPassword().isEmpty() && 
							cfg.getEmailUserName() != null && !cfg.getEmailUserName().isEmpty()){
						//save password
						cfm = new ConfigManager();
						Config toSave = cfm.loadConfig();
						toSave.setEmailPassword(Encryptor.encryptString(cfg.getEmailPassword(), Constants.EMAILKEY.toCharArray()));
						toSave.setEmailUserName(cfg.getEmailUserName());

						cfm.updateConfig(toSave);
					}


				}
				//HANDLE test
				else if (testEmailBtn != null){
					System.out.println("testing now!!!");
				}
			}
			else{
				return Constants.ACCESS_DENIED;
			}
		}catch(Throwable ex){
			ex.printStackTrace();
		}
		return SUCCESS;
	}
	
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
		
	}

	public Config getCfg() {
		return cfg;
	}
	public void setCfg(Config cfg) {
		this.cfg = cfg;
	}

	public String getSaveEmailBtn() {
		return saveEmailBtn;
	}

	public void setSaveEmailBtn(String saveEmailBtn) {
		this.saveEmailBtn = saveEmailBtn;
	}

	public String getTestEmailBtn() {
		return testEmailBtn;
	}

	public void setTestEmailBtn(String testEmailBtn) {
		this.testEmailBtn = testEmailBtn;
	}
}
