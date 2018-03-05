package net.project.db.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import home.db.DBConnection;
import net.project.common.Constants;
import net.project.db.entity.Temperature;

public class CopyDb {

	public static void main(String args[]) throws SQLException, ClassNotFoundException{
		
		boolean run = true;
		long start = new Date().getTime();
		
		
		//CreateTables.createTables();
		
		
		if(run){
		DBConnection remoteConnection = new DBConnection("jdbc:mysql://192.168.1.110:3306/webservice", "tempreader", "newpass", Constants.dbType); //TO REmote JDBC, mysql database // change pass
		
		
		
		DBConnection conNew = new DBConnection("jdbc:h2:file:c:/temp/RaspiWebDb", Constants.dbUser,  Constants.dbPassword, Constants.dbType);
		
		String q = "select * from " + Temperature.TBL_NAME;// + " where " + Temperature.ID + " < :nbr";
		
		ResultSet origrs = remoteConnection.createSelectQuery(q).getSelectResultSet();
		
//		conNew.createQuery(Temp.createTable()).delete();
				
		conNew.buildAddQuery(Temperature.TBL_NAME);
		while(origrs.next()){
			
			conNew.setParameter(Temperature.REC_DATE, origrs.getTimestamp(Temperature.REC_DATE))
			.setParameter(Temperature.BATT_LVL, origrs.getString(Temperature.BATT_LVL))
			.setParameter(Temperature.REC_NAME, origrs.getString(Temperature.REC_NAME))
			.setParameter(Temperature.TEMP, origrs.getFloat(Temperature.TEMP))
			.addToBatch();
			
		}		
		 		
		conNew.executeBatchQuery();
		conNew.close();
		remoteConnection.close();
		
		}
		long end = new Date().getTime();
		System.out.println("Processing time: " + (end - start));
	}
	

}
