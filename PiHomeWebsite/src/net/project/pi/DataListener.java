package net.project.pi;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pi4j.io.serial.SerialDataEvent;
import com.pi4j.io.serial.SerialDataEventListener;

import net.project.db.entity.Temperature;
import net.project.db.manager.TempManager;

/**
 * Class that will handle the serial events when they happen.
 * 
 * @author Alex
 *
 */
public class DataListener implements SerialDataEventListener
{
	private static Log log = LogFactory.getLog(DataListener.class);
	
	private final static String TEMP = "TMPA";
	private final static String BATT = "BATT";
	private final static String SLEEP = "SLEEPING";
	private final static String AWAKE = "AWAKE";
	private final static String STARTED = "STARTED";
	
	
	public static String storage 		= "";
	public static Date 	sigReceivedDt	= null;
	
	
	
	public DataListener()
	{
		log.info("Listener started");
		sigReceivedDt = new Date();
	}
	
	/**
	 * catch the event and do something with it.
	 */
	@Override
	public void dataReceived(SerialDataEvent event)
	{
		Date dateNow = new Date();
		
		//calculate difference
	    long diffInMinutes = (dateNow.getTime() - sigReceivedDt.getTime()) / (1000 * 60);

	    //reset date in mem
	    sigReceivedDt = new Date();
	    String evnt = "";
		try
		{
			 evnt= event.getAsciiString();

			boolean bypass = false;
			
			//build temp

			Temperature temp = null;	
			
			//if there is no difference in minutes , this means its a double event that has been fired
			if (diffInMinutes == 0)
			{
				storage += evnt;				
				
				evnt = storage;				
			}
			else
			{
				storage = evnt;
			}
			
			//2 type battery reading and normar temp reading
			if (evnt != null && evnt.length() == 12 && evnt.contains(TEMP))
			{		

				//we got a temperature reading.				
				temp = new Temperature();
				temp.setRecordedDate(new Date());

				String strTemp = evnt.substring(7,12);	
				temp.setTempC(Float.valueOf(strTemp));

				String ident = evnt.substring(1,3);
				temp.setRecorderName(ident);
				
				//reset storage
				storage = "";

			}

			//battery reading
			//this is to manage the event that contain the battery reading
			if (evnt != null &&  evnt.length() > 12 && evnt.contains(BATT) && evnt.contains(TEMP))
			{
				temp = new Temperature();
				temp.setRecordedDate(new Date());

				String ident = "AA";
				

				//if we have more than 34.. this means that we have the battery level.
				if ( evnt.contains(TEMP))
				{
					String len = evnt.substring(evnt.indexOf(TEMP));

					if (len.length() > 8)
					{	
						String strTemp = evnt.substring(evnt.indexOf(TEMP)+4,evnt.indexOf(TEMP)+9);	
						temp.setTempC(Float.valueOf(strTemp));	

						String tmp = "";
						try
						{
							tmp = evnt.substring(evnt.indexOf(TEMP)-2,evnt.indexOf(TEMP) );
							ident = tmp;
						}catch(Exception ex)
						{}
						
						//reset
						storage ="";
					}
					else
					{
						log.info("Event TEMPA found but too short : " + evnt );
					}
				}
				
				if (evnt.contains(BATT))
				{
					//verify that is is the right length.
					String len = evnt.substring(evnt.indexOf(BATT));
					
					if (len.length() > 8)
					{	
						String strBatt = evnt.substring(evnt.indexOf(BATT)+4,evnt.indexOf(BATT)+8);	
						temp.setBatteryLevel(strBatt);
						try
						{
							ident = evnt.substring(evnt.indexOf(BATT)-2,evnt.indexOf(BATT) );
						}catch(Exception ex)
						{}
						
						//reset
						storage ="";
					}
					else
					{
						log.info("Event BATT found but too short : " + evnt );
					}
				}
				temp.setRecorderName(ident);
				
				
			}			
			
			//other possible events that needs to be bypassed
			if (event != null && !evnt.contains(BATT) && !evnt.contains(TEMP) && (evnt.contains(SLEEP) || evnt.contains(AWAKE) || evnt.contains(STARTED)) )
			{
				bypass = true;
				storage = "";
			}			
			
			
			//if everything is fine write to db
			if (temp != null)
			{
				new TempManager().addTemperature(temp);
			}
			else
			{
				if (bypass)
				{
					log.info("Warning -- event bypassed: " + evnt);
					//do nothing 
				}
				else
				{		
					if (storage.length() > 0)
					{
						log.info("Warning -- appending broken signal: " + storage + " event: " + evnt);
					}
					else
					{
						//write error
						log.error("Error not expected data in dataRecived.  Event recived : " + evnt);
					}
				}
			}
		}
		catch (Exception ex)
		{
			log.error("Error in DataListener with Event: " + evnt , ex);
		}
	}
	/**
	 * 
	 * @return
	 */
//	private static Date getCurrDateForDb()
//	{
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
//
//		Date date = null;
//
//		try{
//			String stringDate = sdf.format(new Date());
//			long date4;
//
//			date4 = sdf.parse(stringDate).getTime();
//
//			Timestamp ts = new Timestamp(date4);
//			date = ts;
//			
//		}
//		catch(java.text.ParseException px)
//		{
//
//		}
//		return date;
//	}

}
