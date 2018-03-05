package net.project.db.sql;

import home.db.DBConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import net.project.common.Constants;
import net.project.db.entity.Temperature;

public class TempSql {

	public Temperature getCurrentTemp(String recorderName) throws SQLException, ClassNotFoundException{
		Temperature temp = null;

		Calendar dailyStart = Calendar.getInstance();	
		dailyStart.set(Calendar.HOUR_OF_DAY, 00);
		dailyStart.set(Calendar.MINUTE, 00);
		dailyStart.set(Calendar.SECOND, 00);
		dailyStart.set(Calendar.MILLISECOND, 00);

		Calendar dailyEnd = Calendar.getInstance();


		dailyEnd.set(Calendar.HOUR_OF_DAY, 23);
		dailyEnd.set(Calendar.MINUTE, 59);
		dailyEnd.set(Calendar.SECOND, 59);
		dailyEnd.set(Calendar.MILLISECOND, 00);


		DBConnection con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);
		try{
			String query = "Select * FROM " + Temperature.TBL_NAME + " WHERE " + Temperature.REC_NAME + " = :firstRec AND " + Temperature.REC_DATE +  " between :start and :end AND " +
					Temperature.REC_DATE + "  = (select max("+ Temperature.REC_DATE + ") from "+ Temperature.TBL_NAME +" where "+ Temperature.REC_NAME +" = :recName  AND " + Temperature.REC_DATE +  " between :start2 and :end2 )";

			ResultSet rs = con.createSelectQuery(query)
					.setParameter("firstRec", recorderName)
					.setParameter("recName", recorderName)
					.setParameter("start",dailyStart.getTime())
					.setParameter("end", dailyEnd.getTime())
					.setParameter("end2", dailyEnd.getTime())
					.setParameter("start2", dailyStart.getTime())
					.getSelectResultSet();

			while (rs.next()) {

				temp = new Temperature(rs);

			}
		}finally{
			con.close();
		}		
		return temp;
	}

	public List<Temperature> getDailyTemp(Date start, Date end, String recorderName) throws SQLException, ClassNotFoundException{

		List<Temperature> temps = new ArrayList<Temperature>();

		DBConnection con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);
		try{
			//		Select * from temperature where recorded_date between ? and ? AND recorder_name = ?";
			String query = "Select * FROM " + Temperature.TBL_NAME + " WHERE " + Temperature.REC_DATE + " between :start and :end AND recorder_name = :recName ORDER BY "+Temperature.REC_DATE+ " ASC ";

			ResultSet rs = con.createSelectQuery(query)
					.setParameter("recName", recorderName)
					.setParameter("start", start)
					.setParameter("end", end)
					.getSelectResultSet();

			while (rs.next()) {

				Temperature temp = new Temperature(rs);

				temps.add(temp);

			}
		}finally{
			con.close();
		}
		return temps;
	}	

	public void addTempList(List<Temperature> temps) throws SQLException, ClassNotFoundException{
		DBConnection con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);
		try{
			con.buildAddQuery(Temperature.TBL_NAME);

			for(Temperature temp : temps){
				con.setParameter(Temperature.REC_DATE, temp.getRecordedDate())
				.setParameter(Temperature.REC_NAME, temp.getRecorderName())
				.setParameter(Temperature.TEMP, temp.getTempC())
				.setParameter(Temperature.BATT_LVL, temp.getBatteryLevel())
				.setParameter(Temperature.HUMIDITY, temp.getHumidity())
				.addToBatch();
			}

			con.executeBatchQuery();
		}finally{
			con.close();
		}
	}

	public void addTemperature(Temperature temp) throws SQLException, ClassNotFoundException{

		DBConnection con = new DBConnection(Constants.url, Constants.dbUser, Constants.dbPassword, Constants.dbType);
		try{
			con.buildAddQuery(Temperature.TBL_NAME)
			.setParameter(Temperature.BATT_LVL, temp.getBatteryLevel())
			.setParameter(Temperature.REC_DATE, temp.getRecordedDate())
			.setParameter(Temperature.REC_NAME, temp.getRecorderName())
			.setParameter(Temperature.TEMP, temp.getTempC())
			.setParameter(Temperature.HUMIDITY, temp.getHumidity())
			.add();

		}finally{
			con.close();
		}

	}
}
