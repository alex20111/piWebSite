package net.esp.pool.temp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import net.project.common.Constants;
import net.project.enums.TempRecName;

public class TestProp {

	public static void main(String[] args) throws FileNotFoundException, IOException {

		System.out.println("Start");
		//		Properties cfgProp = null;
		//		File prodInitFile = new File("d:\\prodinit.cfg");
		//		
		//		if (prodInitFile.exists()){
		//			cfgProp = new Properties();
		//			cfgProp.load(new FileInputStream(prodInitFile));
		//			String baseDirectory = cfgProp.getProperty("base_dir");
		//			Constants.UPL_BASE_DIR = baseDirectory;
		//			
		//			System.out.println("Loaded: " + Constants.UPL_BASE_DIR);
		//		}else{
		//			//create
		//			cfgProp = new Properties();
		//			cfgProp.setProperty("base_dir", "/media/usb/webfiles/");
		//			cfgProp.setProperty("email_key", "120485YDD&46aAge3g@");
		//			
		//			cfgProp.store(new FileOutputStream(prodInitFile), "Server initial configuration. You need to restart the server for the changes to take effect");
		//		}
		//		
		//System.out.println("prodInitFile : " + prodInitFile.exists());

		Calendar now = Calendar.getInstance();
//		now.set(Calendar.MONTH, Calendar.JULY);
//		now.set(Calendar.YEAR, 2018);

		Calendar winterStDt = Calendar.getInstance();
		winterStDt.set(Calendar.MONTH, Calendar.OCTOBER);
		winterStDt.set(Calendar.DAY_OF_MONTH, 1);

		Calendar winterEndDt = Calendar.getInstance();
		winterEndDt.set(Calendar.YEAR , winterStDt.get(Calendar.YEAR) + 1);
		winterEndDt.set(Calendar.MONTH, Calendar.MAY);
		winterEndDt.set(Calendar.DAY_OF_MONTH, 1);

		System.out.println("St: " + winterStDt.getTime() + "\nEnd " + winterEndDt.getTime());

		if(now.getTime().after(winterStDt.getTime()) && now.getTime().before(winterEndDt.getTime())){
			System.out.println("winter");
		}else{
			System.out.println("summer");
		}

	}

}
