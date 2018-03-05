package net.project.common;

import home.db.DbClass;

public class Constants {

	public static final String USER 	= "user";
	public static final int NBR_TRIES 	= 3;
	
//	public static final String url 		= "jdbc:h2:c:/temp/dd/db/RaspiWebDb"; //TODO change for linux
//	public static final String url 		= "jdbc:h2:file:/media/usb/webDb/RaspiWebDb";
	
	//switch to set to test mode
	public static boolean testMode = true; //testmode set in startup manager
	
	//DB local testing
	public static final String urlH2 		= "jdbc:h2:d:/javaUtil/homeTestDb";
	
	//DB production
	public static final String urlMysql = "jdbc:mysql://localhost:3306/";
	
	//DB parameters
	public static String url =  "" ;
	public static DbClass dbType = null;
	
	public static final String dbUser 	= "";
	public static final String dbPassword = "";
	
	
	public static final String ACCESS_DENIED = "accessdenied";
	
	public static String EMAILKEY = "120485YDD&46aAge3g@";

	public static final String logDirLoc = "logs";
	
	public static boolean loginAfter = false; //determine if you will have the login screen 1st or you can log in after.
	public static boolean mustLogin = false;
	
	public static String UPL_BASE_DIR = "/media/usb/webfiles/";
	
	public static final String sideMenuFolderList 	= "sideMenuFolderList";
	public static final String folderExpander    	= "folderExpanded";
	public static final String GLOBAL_WEATHER    	= "currentGlobalWeather";
	public static final String sideMenuTasks 		= "sideMenuTasks";
	
	public static final String sideMenuInventoryGroups 		= "sideMenuInventory";
	
	public static final int WINTER_ST_MMDD = 9;
	public static final int WINTER_END_MMDD = 5;
	
	public static String PROJECT_DIR = "c:\\temp\\dd\\";
	
}
