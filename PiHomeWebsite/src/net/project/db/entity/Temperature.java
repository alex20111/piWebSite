package net.project.db.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.google.gson.annotations.Expose;

public class Temperature {

	public static String TBL_NAME = "temperature";
	public static String ID 		= "id";
	public static String TEMP 		= "temp_c";
	public static String REC_DATE 	= "recorded_date";
	public static String REC_NAME 	= "recorder_name";
	public static String BATT_LVL 	= "battery_level";
	public static String HUMIDITY 	= "humidity";	
	
	private int 	id 				= 0;
	@Expose
	private Float 	tempC 			= null;
	@Expose
	private Date	recordedDate	= null;
	@Expose
	private String  recorderName	= "";
	@Expose
	private String  batteryLevel	= null;	
	@Expose
	private String  humidity		= "";	
	
	public Temperature(){}
	
	public Temperature(ResultSet rs) throws SQLException{
		this.id = rs.getInt(ID);
		this.tempC = rs.getFloat(TEMP);
		this.recordedDate = rs.getTimestamp(REC_DATE);
		this.recorderName = rs.getString(REC_NAME);
		this.batteryLevel = rs.getString(BATT_LVL);
		this.humidity     = rs.getString(HUMIDITY);
	
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Float getTempC() {
		return tempC;
	}
	public void setTempC(Float tempC) {
		this.tempC = tempC;
	}
	public Date getRecordedDate() {
		return recordedDate;
	}
	public void setRecordedDate(Date recordedDate) {
		this.recordedDate = recordedDate;
	}
	public String getRecorderName() {
		return recorderName;
	}
	public void setRecorderName(String recorderName) {
		this.recorderName = recorderName;
	}
	public String getBatteryLevel() {
		return batteryLevel;
	}
	public void setBatteryLevel(String batteryLevel) {
		this.batteryLevel = batteryLevel;
	}
	
	public String getHumidity() {
		return humidity;
	}

	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}

	public static String createTable(){
		StringBuilder create = new StringBuilder();
		create.append("CREATE TABLE " + TBL_NAME + " (");
		create.append(ID + " INT PRIMARY KEY auto_increment");
		create.append(", " + TEMP + " DOUBLE" );
		create.append(", " + REC_DATE + " TIMESTAMP" ); 
		create.append(", " + REC_NAME + " VARCHAR(5)" ); 
		create.append(", " + BATT_LVL + " VARCHAR(6)" );
		create.append(", " + HUMIDITY + " VARCHAR(10)" );	
		create.append(")");
		
		return create.toString();
	}
	
	public static String createRecDateIndex(){
		return "create INDEX idx_tmp_dt on " + TBL_NAME +"(" + REC_DATE + ");";
	}

	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Temperature [id=");
		builder.append(id);
		builder.append(", tempC=");
		builder.append(tempC);
		builder.append(", Humidity=");
		builder.append(humidity);
		builder.append(", recordedDate=");
		builder.append(recordedDate);
		builder.append(", recorderName=");
		builder.append(recorderName);
		builder.append(", batteryLevel=");
		builder.append(batteryLevel);
		builder.append("]");
		return builder.toString();
	}

}
