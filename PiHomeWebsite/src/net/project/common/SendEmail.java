package net.project.common;

import java.util.Date;

import net.project.db.entity.Config;

import home.crypto.Encryptor;
import home.email.EmailMessage;
import home.email.EmailType;
import home.email.SendMail;

public class SendEmail {

	public static void send(String title, String content, Config cfg) throws Exception{
		EmailMessage email = new EmailMessage();
		email.setFrom("project");
		email.setSubject(title);
		email.setMessageBody(content);
		email.setTo(cfg.getEmailUserName());
		email.setSentDate(new Date());
		
		SendMail.sendGoogleMail(cfg.getEmailUserName(), 
				Encryptor.decryptString(cfg.getEmailPassword(), Constants.EMAILKEY.toCharArray()), email, EmailType.html);
	}
	
	public static void sendTestMail(String userName, String password) throws Exception{
		EmailMessage email = new EmailMessage();
		email.setFrom("project");
		email.setSubject("Test email from project");
		email.setMessageBody("Test e-mail from your friendly fully Automatic project");
		email.setTo(userName);
		email.setSentDate(new Date());
		
		SendMail.sendGoogleMail(userName,password, email, EmailType.html);
	}

}
