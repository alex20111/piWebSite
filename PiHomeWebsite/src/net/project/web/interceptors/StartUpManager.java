package net.project.web.interceptors;


import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import net.project.common.Constants;
import net.project.db.sql.CreateTables;
import net.project.pi.TempSerialListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import home.db.DbClass;
import home.misc.Exec;

public class StartUpManager implements ServletContextListener    {

	private Log log = LogFactory.getLog(getClass());
	

	/* Application Startup Event */
	public void contextInitialized(ServletContextEvent ce) 
	{
		//
		// Pre-load some information
		//
		try
		{
			log.debug("Starting app");

			
			//find config file.. if not found, this means we are in test mode
			Properties cfgProp = null;
			File prodInitFile = new File("/media/usb/prodinit.cfg");
			
			if (prodInitFile.exists()){
				log.debug("Loaded server properties");
				cfgProp = new Properties();
				cfgProp.load(new FileInputStream(prodInitFile));

				String baseDirectory = cfgProp.getProperty("base_dir");
				Constants.UPL_BASE_DIR = baseDirectory;

				String emailKey = cfgProp.getProperty("email_key");
				Constants.EMAILKEY = emailKey;

				Constants.testMode = false;
				
				//set DB urls:
				Constants.url =  Constants.urlMysql ;
				Constants.dbType = DbClass.Mysql;
				
				//project base
				Constants.PROJECT_DIR = Constants.UPL_BASE_DIR + "projects" + File.separatorChar;
				
				log.debug("baseDirectory : " + baseDirectory);
				log.debug("EMAILKEY: " + Constants.EMAILKEY);
				log.debug("DB: " + Constants.url + "  " + Constants.dbType);
				log.debug("PROJECT_DIR: " + Constants.PROJECT_DIR );
			}			
			
			ce.getServletContext().setAttribute("callAction", "mainPageAction");

			if (!Constants.testMode){
				TempSerialListener.startTempLogger();

				Exec e = new Exec();
				e.addCommand("hostname").addCommand("--all-ip-addresses");
				e.run();

				String ip = e.getOutput();

				ce.getServletContext().setAttribute("localIp", ip);
			}
			else
			{
				//test mode
				ce.getServletContext().setAttribute("localIp", "127.0.0.1");
				
				log.debug("Loaded server properties in test mode");

				String baseDirectory = "d:\\temp\\";
				Constants.UPL_BASE_DIR = baseDirectory;

				Constants.EMAILKEY = "12345";

				Constants.testMode = true;
				
				//set DB urls:
				Constants.url =  Constants.urlH2 ;
				Constants.dbType = DbClass.H2;
				
				log.debug("baseDirectory : " + baseDirectory);
				log.debug("EMAILKEY: " + Constants.EMAILKEY);
				log.debug("DB: " + Constants.url + "  " + Constants.dbType);
			}		
			
			//create new tables if needed
		//	CreateTables.createTables();
			
			
			log.debug("test mode? " + Constants.testMode);

			log.debug("App ready");

		}
		catch(Exception ex)
		{
			log.error("Error in startup manager" , ex);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) 
	{
		try {
			TempSerialListener.stopTempLogger();
		} catch (Exception e) {
			log.error("Error while closing serial" , e);
		}
	}
	
}