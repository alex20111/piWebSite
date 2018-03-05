package net.project.web.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ApplicationAware;
import org.apache.struts2.interceptor.ServletRequestAware;

import net.project.db.entity.Temperature;
import net.project.db.manager.TempManager;
import net.project.db.manager.UserManager;
import net.project.enums.WsType;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opensymphony.xwork2.ActionSupport;

/**
 * Examples
 * CMD: add, get.
 * Type: temp (for temperature entity)   I0oCIadso9nDRfZYo3wihYFe8BEpRMQvAvy
 * 
 * Add:(over the internet you will need a key);
 * 	http://localhost:8080/web/service.action?key=I0oCIadso9nDRfZYo3wihYFe8BEpRMQvAvy&cmd=add&type=temp&jsonObject={"tempC":22.45,"recorderName":"pool", "batteryLevel":"3.14"}
 * Add:(Locally, no need key);
 *  http://localhost:8080/web/service.action?cmd=add&type=temp&jsonObject={"tempC":22.45,"recorderName":"pool","batteryLevel":"3.14"} 
 *  
 * Get:(over the internet you will need a key);
 *  Get current temperature 
 *  	http://localhost:8080/web/service.action?key=cLP5aV6Wk81Ex7e5RALXjjkAir78FqDXFuD&cmd=get&type=temp&jsonObject={"recorderName":"out"}&max=true
 *  Get a range of temperature
		http://localhost:8080/web/service.action?key=cLP5aV6Wk81Ex7e5RALXjjkAir78FqDXFuD&cmd=get&type=temp&jsonObject={"recorderName":"out"}&from=2017-01-01&to=2017-02-02
 * Get:(Locally, no need key);
 *  Get current temperature 
 *  	http://localhost:8080/web/service.action?cmd=get&type=temp&jsonObject={"recorderName":"out"}&max=true
 *  Get a range of temperature
		http://localhost:8080/web/service.action?cmd=get&type=temp&jsonObject={"recorderName":"out"}&from=2017-01-01&to=2017-02-02
 * @author axb161
 *
 */
public class WebService  extends ActionSupport implements ServletRequestAware, ApplicationAware{

	private static final long serialVersionUID = -8743974686600784212L;
	private Log log = LogFactory.getLog(getClass());
	
	private InputStream jsonObjectStream;
	private HttpServletRequest request;
	private Map<String, Object> application;
	
	private static String localIp = "";
	private String userIp = "";
	private String jsonName;
	
	private String key;
	private String cmd;
	private String type;
	private String jsonObject;
	
	//webservice commands
	private boolean max	= false; // get the most recent date (max date)
	private String from;
	private String to;
	
	
	public String handleObject(){
		
		boolean canProceed = false;
		
		try{
			
			
			//get local ip
			if (localIp.length() == 0){
				localIp = localIp();
			}
			
			//get user ip addres.
			userIp = getUserIp();

			
			boolean inLocalNetwork = verifyIfInLocalMode();
			
			UserManager um = new UserManager();

			//if key is null, user needs to be in the local network. Else it is invalid.
			if (key == null && !inLocalNetwork){
				jsonObjectStream = new ByteArrayInputStream("Error. Invalid key or not local".getBytes("UTF-8"));

				return "error";
			}
			else if (key != null){
				//validate the key
				String retKey = um.validateApiKey(key);

				//valide key
				if (retKey != null){
					canProceed = true;
				}else{
					jsonObjectStream = new ByteArrayInputStream("Error. Invalid key".getBytes("UTF-8"));
					return "error";
				}
			}else if (inLocalNetwork){
				canProceed = true;
			}
			
			
			if (canProceed){
				
				//process request
				if ("add".equals(cmd)){

					return processAdd();

				}else if("get".equals(cmd)){
					return processGet();
				}		
			}

		}catch(Exception ex){
			log.error("Exception in handleObject" ,ex );
		}
		return "success";
	}
	
	//http://localhost:8080/web/service.action?key=cLP5aV6Wk81Ex7e5RALXjjkAir78FqDXFuD&cmd=add&type=temp&jsonObject={"tempC":22.45,"recorderName":"out", "batteryLevel":"3.14"}
	private String processAdd(){

		try{
			try{
				if (type != null && WsType.temp == WsType.valueOf(this.type)){

					Gson gson = new Gson();

					Temperature t = gson.fromJson(jsonObject, Temperature.class );
					
					if (t.getTempC() != null){

						TempManager tm = new TempManager();

						if (t.getRecordedDate() == null){

							t.setRecordedDate(new Date());
						}
						tm.addTemperature(t);

						jsonObjectStream = new ByteArrayInputStream("Success".getBytes("UTF-8"));
					}else{
						jsonObjectStream = new ByteArrayInputStream("Input empty".getBytes("UTF-8"));
					}

					return  "ok";
				}
				else{
					String types = "No Type or invalid type found. Type allowed: " + types();
					jsonObjectStream = new ByteArrayInputStream(types.getBytes("UTF-8"));
					return "error";
				}
			}catch(IllegalArgumentException ie){
				String types = "No Type or invalid type found. Type allowed: " + types();
				jsonObjectStream = new ByteArrayInputStream(types.getBytes("UTF-8"));
				return "error";
			}

		}catch(Exception ex){
			try {
				String error = "Error while processing. " + ex.getMessage();
				jsonObjectStream = new ByteArrayInputStream(error.getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {				
				log.error("Exception " ,e );
			}
			log.error("Exception " ,ex );
			return "error";
		}
	}
	
	//http://localhost:8080/web/service.action?key=cLP5aV6Wk81Ex7e5RALXjjkAir78FqDXFuD&cmd=get&type=temp&jsonObject={"recorderName":"out"}&max=true
	//http://localhost:8080/web/service.action?key=cLP5aV6Wk81Ex7e5RALXjjkAir78FqDXFuD&cmd=get&type=temp&jsonObject={"recorderName":"out"}&from=2017-01-01&to=2017-02-02
	private String processGet(){

		Gson gson =  new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		try{

			try{
				if (type != null && WsType.temp == WsType.valueOf(this.type)){

					Temperature t = gson.fromJson(jsonObject, Temperature.class );

					if (t.getRecorderName() != null && t.getRecorderName().length() > 0 && t.getRecorderName().length() < 6){

						TempManager tm = new TempManager();
						if (isMax()){
							Temperature currTemp = tm.getCurrentTemp(t.getRecorderName().trim());

							String result = gson.toJson(currTemp);

							jsonObjectStream = new ByteArrayInputStream(result.getBytes("UTF-8"));

						}else if (getFrom() != null && getTo() != null){
							Date st = null;
							Date en = null;
							try{
								SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
								st = sd.parse(from);
								en = sd.parse(to);


								List<Temperature> temps = tm.getDailyTemp(st, en, t.getRecorderName().trim() );

								String result = gson.toJson(temps);
								jsonObjectStream = new ByteArrayInputStream(result.getBytes("UTF-8"));
							}catch(ParseException pe){
								String recName = "Dates wrong. : " + pe.getMessage();
								jsonObjectStream = new ByteArrayInputStream(recName.getBytes("UTF-8"));
							}							

						}else{
							String recName = "Incorrect values.  ";
							jsonObjectStream = new ByteArrayInputStream(recName.getBytes("UTF-8"));
						}								
					}else{

						String recName = "Recorder name error: " + t.getRecorderName();
						jsonObjectStream = new ByteArrayInputStream(recName.getBytes("UTF-8"));
					}

					return  "ok";

				}
				else{
					String types = "No Type or invalid type found. Type allowed: " + types();
					jsonObjectStream = new ByteArrayInputStream(types.getBytes("UTF-8"));
					return "error";
				}
			}catch(IllegalArgumentException ie){
				String types = "No Type or invalid type found. Type allowed: " + types();
				jsonObjectStream = new ByteArrayInputStream(types.getBytes("UTF-8"));
				return "error";
			}

		}catch(Exception ex){
			try {
				String error = "Error while processing. " + ex.getMessage();
				jsonObjectStream = new ByteArrayInputStream(error.getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				log.error("Exception in processGet" ,e );
			}
			log.error("Exception in processGet" ,ex );
			return "error";
		}
	}
	
	private String types(){
		
		StringBuilder sb = new StringBuilder();
		 
		boolean first = true;
		for(WsType type : WsType.values()){
			
			if (first){
				sb.append(type);
				first = false;
			}else{
				sb.append(", " + type);
			}			
		}		
		return sb.toString();
	}
	
	
	private String localIp(){
		String result = "";
	
			
			
			String ip  = (String)application.get("localIp");
			//String ip = InetAddress.getLocalHost().getHostAddress();
			result = ip.substring(0, ip.indexOf("."));		

	
		return result.trim();
	}
	private String getUserIp(){

		String ip = request.getRemoteAddr();
		String result = ip.substring(0, ip.indexOf("."));	


		return result.trim();
	}
	
	private boolean verifyIfInLocalMode(){
		
		if (localIp != null && userIp != null && userIp.length() > 0 &&
				localIp.equals(userIp)){
			return true;
		}
		return false;
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getJsonObject() {
		return jsonObject;
	}
	public void setJsonObject(String jsonObject) {
		this.jsonObject = jsonObject;
	}
	public String getCmd() {
		return cmd;
	}
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	public InputStream getJsonObjectStream() {
		return jsonObjectStream;
	}
	public void setJsonObjectStream(InputStream jsonObjectStream) {
		this.jsonObjectStream = jsonObjectStream;
	}
	public String getJsonName() {
		return jsonName;
	}
	public void setJsonName(String jsonName) {
		this.jsonName = jsonName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean isMax() {
		return max;
	}
	public void setMax(boolean max) {
		this.max = max;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;		
	}

	@Override
	public void setApplication(Map<String, Object> app) {
		this.application = app;
		
	}

}
