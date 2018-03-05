package net.project.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import net.project.enums.TempRecName;

/**
 * class containing the weather forecast.
 *
 *
 */
public class WeatherBean implements Serializable{
	
	private static final long serialVersionUID = -8103826136399362609L;
	
	private Map<TempRecName, Float> temp;
	private Map<TempRecName, String> humidity;
	private Map<TempRecName, String> batt;
	private Map<TempRecName, Date> lastUpdate;
	private String webTemp 		= "";
	private String currentCond 	= "";
	private String forecast		= "";
	private int envCanIcon 		= 0;
	private Date envCanWebDate;
	private Date lastSyncToEnvCan; //last time the data was retrieve from the web
	private Date currentDate;
	

	private SimpleDateFormat hourTimeSdf = new SimpleDateFormat("hh:mm a");
	
	
	public Map<TempRecName, Float> getTemp() {
		return temp;
	}
	public void setTemp(Map<TempRecName, Float> temp) {
		this.temp = temp;
	}
	public Map<TempRecName, String> getHumidity() {
		return humidity;
	}
	public void setHumidity(Map<TempRecName, String> humidity) {
		this.humidity = humidity;
	}
	public Map<TempRecName, String> getBatt() {
		return batt;
	}
	public void setBatt(Map<TempRecName, String> batt) {
		this.batt = batt;
	}
	public Map<TempRecName, Date> getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(Map<TempRecName, Date> lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public String getCurrentCond() {
		return currentCond;
	}
	public void setCurrentCond(String currentCond) {
		this.currentCond = currentCond;
	}
	public String getForecast() {
		return forecast;
	}
	public void setForecast(String forecast) {
		this.forecast = forecast;
	}
	public int getEnvCanIcon() {
		return envCanIcon;
	}
	public void setEnvCanIcon(int envCanIcon) {
		this.envCanIcon = envCanIcon;
	}
	public Date getCurrentDate() {
		return currentDate;
	}
	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}
	
	public Date getEnvCanWebDate() {
		return envCanWebDate;
	}
	public void setEnvCanWebDate(Date envCanWebDate) {
		this.envCanWebDate = envCanWebDate;
	}
	public Date getLastSyncToEnvCan() {
		return lastSyncToEnvCan;
	}
	public void setLastSyncToEnvCan(Date lastSyncToEnvCan) {
		this.lastSyncToEnvCan = lastSyncToEnvCan;
	}
	public String getFormattedTemp(TempRecName rec){

		if (temp != null && temp.containsKey(rec)){

			return String.format("%.2f", temp.get(rec));
		}
		return "N/A";
	}
	public String getFormattedDate(TempRecName rec){

		if (lastUpdate != null && lastUpdate.containsKey(rec)){

			return hourTimeSdf.format(lastUpdate.get(rec));
		}
		return "N/A";
	}
	public String getWebTemp() {
		return webTemp;
	}
	public void setWebTemp(String webTemp) {
		this.webTemp = webTemp;
	}
}
