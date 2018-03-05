package net.project.utils;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.project.bean.WeatherBean;
import net.project.common.Constants;
import net.project.db.entity.Temperature;
import net.project.db.manager.TempManager;
import net.project.enums.TempRecName;
import net.weather.action.WeatherAction;
import net.weather.bean.WeatherCurrentModel;
import net.weather.bean.WeatherGenericModel;
import net.weather.enums.EnvCanLang;

public class WeatherHandler {
	
	private Log log = LogFactory.getLog(getClass());

	private TempManager mngr;
	
	public WeatherHandler(){}
	public WeatherHandler(TempManager mngr){
		this.mngr = mngr;
	}
	
	/**
	 * Update the weather from the main page and the 
	 * @param mngr
	 * @param wthBean
	 * @param loadWebWeather
	 * @throws SQLException
	 * @throws ClassNotFoundException 
	 */
	public void updateWeather(WeatherBean wthBean, boolean loadHomeWeather, boolean loadWebWeather) throws SQLException, ClassNotFoundException{

		if (loadHomeWeather){

			Temperature tempAA = mngr.getCurrentTemp(TempRecName.AA.name());
			Temperature tempBB = mngr.getCurrentTemp(TempRecName.BB.name());
			Temperature tempPool = mngr.getCurrentTemp(TempRecName.pool.name());

			//add local temp
			Map<TempRecName, Float> temp = new HashMap<TempRecName, Float>();
			Map<TempRecName, String> humidity = new HashMap<TempRecName, String>();
			Map<TempRecName, String> batt = new HashMap<TempRecName, String>();
			Map<TempRecName, Date> lastUpdate = new HashMap<TempRecName, Date>();

			if (tempAA != null){
				temp.put(TempRecName.AA, tempAA.getTempC());
				humidity.put(TempRecName.AA, tempAA.getHumidity());
				batt.put(TempRecName.AA, tempAA.getBatteryLevel());
				lastUpdate.put(TempRecName.AA, tempAA.getRecordedDate());
			}
			if (tempBB != null){
				temp.put(TempRecName.BB, tempBB.getTempC());
				humidity.put(TempRecName.BB, tempBB.getHumidity());
				batt.put(TempRecName.BB, tempBB.getBatteryLevel());
				lastUpdate.put(TempRecName.BB, tempBB.getRecordedDate());
			}
			if (tempPool != null){
				temp.put(TempRecName.pool, tempPool.getTempC());
				humidity.put(TempRecName.pool, tempPool.getHumidity());
				batt.put(TempRecName.pool, tempPool.getBatteryLevel());
				lastUpdate.put(TempRecName.pool, tempPool.getRecordedDate());
			}

			wthBean.setTemp(temp);
			wthBean.setBatt(batt);
			wthBean.setHumidity(humidity);
			wthBean.setLastUpdate(lastUpdate);
		}

		if (loadWebWeather){

			WeatherGenericModel wgm = null;

			try{
				wgm = WeatherAction.getEnvironmentCanadaRSSWeather("on-118", EnvCanLang.english, false, false);
			}catch(Exception ex){
				log.error("Error fetching weather", ex);
			}

			if (wgm != null){
				//from ENV Can Rss
				WeatherCurrentModel wcm = wgm.getWeatherCurrentModel();
				wthBean.setCurrentCond(wcm.getWeather());			
				wthBean.setEnvCanIcon(wthBean.getEnvCanIcon() + 1);
				wthBean.setForecast(wgm.getWForecastModel().get(0).getForecast());
				try{
					wthBean.setEnvCanWebDate(new SimpleDateFormat("EEE MMM d hh:mm:ss z yyyy").parse(wcm.getObservationTime())); //last updated by
				}catch(ParseException pe){log.error("Parse exception", pe);}
				wthBean.setWebTemp(String.valueOf(wcm.getCurrectTempC()));

				wthBean.setLastSyncToEnvCan(new Date());
			}else{
				wthBean.setCurrentCond("Error From reader");			
				wthBean.setEnvCanIcon(wthBean.getEnvCanIcon() + 1);
				wthBean.setForecast("Invalid data");
				wthBean.setEnvCanWebDate(new Date()); //last updated by
				wthBean.setWebTemp("00");

				wthBean.setLastSyncToEnvCan(new Date());				
			}
		}

		wthBean.setCurrentDate(new Date());


	}
	
	public String loadMainPageWeatherFromEnvCan(Map<String, Object> session) throws Exception{

		int fiveMin = 1000 * 60 * 5;

		//try to see if we have the weather is  the session.. if yes , get it and verify if it's been more than 
		//5 min that is has been updated.. If yes, re-update it.

		WeatherBean wthBean = (WeatherBean)session.get(Constants.GLOBAL_WEATHER);

		if (wthBean != null){

			//check if we need to update it
			if (new Date().getTime() - wthBean.getLastSyncToEnvCan().getTime() > fiveMin){
				updateWeather(wthBean, false, true);
			}
		}else{
			wthBean = new WeatherBean();
			updateWeather(wthBean, false, true);
		}		

		session.put(Constants.GLOBAL_WEATHER, wthBean);			

		StringBuilder currWth = new StringBuilder();
		currWth.append("<div class=\"text-center\">");
		currWth.append("<strong>" + new SimpleDateFormat("EEE d").format(new Date()) + "</strong> ");
		currWth.append("<img src=\"/web/images/mostly_cloudy.png\" class=\"img-responsive center-block\"/> <br/>");
		currWth.append("<p class=\"text-center\"><strong>" + wthBean.getCurrentCond() + "</strong></p>");
		currWth.append("<h3 class=\"text-center \"> "+ wthBean.getWebTemp()+"C</h3><br/>");
		currWth.append("<p><Strong> Forecast: (Weather.gc.ca)</Strong> <br/>");
		currWth.append(wthBean.getForecast() +" </p>");		
		currWth.append("<p><strong>Last Updated:</strong><br/> " + new SimpleDateFormat("EEE, MMM d, HH:mm").format(wthBean.getEnvCanWebDate()) + "</p>");		
		currWth.append("</div>");
		
		return currWth.toString();

	}
}
