package net.project.web.action;

import home.crypto.BCryptHash;
import home.crypto.Encryptor;
import home.email.EmailMessage;
import home.email.EmailType;
import home.email.SendMail;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.project.common.Constants;
import net.project.db.entity.Config;
import net.project.db.entity.User;
import net.project.db.manager.ConfigManager;
import net.project.db.manager.UserManager;
import net.project.exception.ValidationException;
import net.project.utils.KeyGenerator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;


import com.opensymphony.xwork2.ActionSupport;

public class LoginAction extends ActionSupport implements SessionAware, ServletRequestAware{

	private Log log = LogFactory.getLog(getClass());
	
	private static final long serialVersionUID = 1L;

	private String userName = "";
	private String password = "";
	private Map<String, Object> session;
	private HttpServletRequest request;
	private UserManager userManager;
	
	private String newPassword = "";
	private String confirmPassword = "";
	
	private String forgotPassBtn;

	public LoginAction(){
		userManager = new UserManager();
	}

	public String login(){

		String retVal = "login";

		try{
			if (forgotPassBtn != null){
				retVal = resetPassword();
			}else{

				log.info("User " + getUserName() + " login from : " + request.getRemoteAddr());
				if (getUserName().trim().length() == 0){
					addFieldError("userName", "Please enter a user name");
				}
				if (getPassword().trim().length() == 0){
					addFieldError("password", "Please enter a password");
				}

				if (!hasFieldErrors()){
					//check login
					User user = userManager.loginUser(getUserName(), getPassword());

					if (user != null){				
						session.put(Constants.USER, user);					
						retVal = "loggedIn";

					}else{
						addActionError("Invalid user or password");
					}
				}
			}
		} catch (ValidationException e) {

			if(e.getMessage().equals("Password empty")){
				addActionError("Please enter password");
			}
			if(e.getMessage().equals("userName empty")){
				addActionError("Please enter a User Name");	
			}
			if(e.getMessage().equals("Invalid password")){
				addActionError("Wrong Password");
			}
			if(e.getMessage().equals("User over maximum number of tries")){
				addActionError("Exceeded maximum number of tries. Please contact Administrator.");
			}

		}catch(Exception ex){
			addActionError("Error performing action, please contact Administrator");
			log.error("Error in login action: ", ex);
		}

		return retVal;
	}

	public String logout()
	{
		if (session instanceof SessionMap)
		{
			((SessionMap<String,Object>)session).invalidate();
		}
		
		return "success";
	}
	public String changePass(){
		
		User user = (User) session.get(Constants.USER);
		
		try {
			
			if (getNewPassword().trim().length() == 0){
				addFieldError("newpass","Please enter a new password");
			}
			if (getConfirmPassword().trim().length() == 0){
				addFieldError("confpass","Please confirm the new password");
			}
			
			if (getNewPassword().trim().length() > 0 && getConfirmPassword().trim().length() > 0 &&
					!getConfirmPassword().trim().equals(getNewPassword().trim()))
			{
				addFieldError("newpass","The new password and the confirmation password do not match");
			}
			
			if (!hasFieldErrors()){
				user.setPassword(BCryptHash.hashString(getNewPassword().trim()));
				userManager.updateUser(user);
				addActionMessage("Save successful");
			}
			
			
		} catch (Exception e) {
			log.error("Error in changePass: ", e);
		}
				
		return SUCCESS;
	}
	public String resetPassword() throws Exception{

			if (getUserName() == null || getUserName().isEmpty()){
				addFieldError("userName","Please enter a user to reset the password");
			}

			if (!hasFieldErrors()){

				UserManager um = new UserManager();
				User user = um.loadUserByUserName(getUserName());
				
				if (user != null){

					Config cfg = new ConfigManager().loadConfig();

					if (cfg != null && cfg.getEmailUserName() != null && !cfg.getEmailUserName().isEmpty()){
						//generate key
						String key = new KeyGenerator().generateKey(10);
						
						//reset password
						EmailMessage email = new EmailMessage();
						email.setTo(user.getEmail());
						email.setFrom("Irrigation@home.com");
						email.setSubject("Pass reset");
						email.setMessageBody("New password: " + key);
						SendMail.sendGoogleMail(cfg.getEmailUserName(), Encryptor.decryptString(cfg.getEmailPassword(), Constants.EMAILKEY.toCharArray()), email, EmailType.html);
						
						
						user.setPassword(BCryptHash.hashString(key));
						user.setNbOfTries(0);
						
						um.updateUser(user);
						addActionMessage("Password reset sent");
					}
				}else{
					addActionError("Password reset error");
					log.error("User to reset does not exist: " + getUserName());
				}				
			}

		return "resetPass";
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;	
	}
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;		
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getForgotPassBtn() {
		return forgotPassBtn;
	}

	public void setForgotPassBtn(String forgotPassBtn) {
		this.forgotPassBtn = forgotPassBtn;
	}
}
