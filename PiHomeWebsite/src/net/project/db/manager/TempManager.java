package net.project.db.manager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.project.db.entity.Temperature;
import net.project.db.sql.TempSql;
import net.project.enums.TempRecName;

public class TempManager {
	
	private Log log = LogFactory.getLog(getClass());
	
	private TempSql sql;
	
	public TempManager(){
		sql = new TempSql();
	}
	

	public Temperature getCurrentTemp(String recorderName) throws SQLException, ClassNotFoundException{
		return sql.getCurrentTemp(recorderName);
	}
	/**
	 * Get all daily temperature for all recorder based on an interval.
	 * 
	 * @param interval
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException 
	 */
	public List<Temperature> getGlobalDailyTemperature(int interval) throws SQLException, ClassNotFoundException{
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
		
				
		 List<Temperature> glbTemp = new ArrayList<Temperature> ();
		
		for(TempRecName rec : TempRecName.values()){
			
			Calendar end = Calendar.getInstance();	
			end.set(Calendar.HOUR_OF_DAY, 00);
			end.set(Calendar.MINUTE, interval);
			end.set(Calendar.SECOND, 00);
			end.set(Calendar.MILLISECOND, 00);	
			
			List<Temperature> daily = sql.getDailyTemp(dailyStart.getTime(),dailyEnd.getTime(),rec.name());

			boolean addToRecord = true;
			for(Temperature t : daily){

				if ( t.getRecordedDate().before(end.getTime()) && addToRecord){
					//get the record and bypass until date is after.
					glbTemp.add(t);
										
					addToRecord = false;
									
				}else if ( t.getRecordedDate().after(end.getTime()) ){

					int cnt = 0;
					while(true && cnt < 3000){
						cnt ++;
						//increment end date by interval until it is before end
						end.add(Calendar.MINUTE, interval);

						if (t.getRecordedDate().before(end.getTime())){
							glbTemp.add(t);
													
							addToRecord = false;
							break;
						}
					}
					
					if (cnt >= 3000){
						log.error("Err , reached 3000. DailyStart" + dailyStart.getTime() + " " + dailyEnd.getTime());
					}
				}
			}
		}
		
		return glbTemp;
	}
	
	public void addTemperature(Temperature temp) throws SQLException, ClassNotFoundException{
		 sql.addTemperature(temp);
	}
	
	public List<Temperature> getDailyTemp(Date start, Date end, String recorderName) throws SQLException, ClassNotFoundException{
		Calendar dailyStart = Calendar.getInstance();
		dailyStart.setTime(start);
		dailyStart.set(Calendar.HOUR_OF_DAY, 00);
		dailyStart.set(Calendar.MINUTE, 00);
		dailyStart.set(Calendar.SECOND, 00);
		dailyStart.set(Calendar.MILLISECOND, 00);

		Calendar dailyEnd = Calendar.getInstance();
		dailyEnd.setTime(end);
		dailyEnd.set(Calendar.HOUR_OF_DAY, 23);
		dailyEnd.set(Calendar.MINUTE, 59);
		dailyEnd.set(Calendar.SECOND, 59);
		dailyEnd.set(Calendar.MILLISECOND, 00);


		return sql.getDailyTemp(dailyStart.getTime(), dailyEnd.getTime(), recorderName);
	}
	
}
