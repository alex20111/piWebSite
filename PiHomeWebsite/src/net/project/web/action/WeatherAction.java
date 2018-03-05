package net.project.web.action;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.project.bean.WeatherBean;
import net.project.common.Constants;
import net.project.db.entity.Temperature;
import net.project.db.manager.TempManager;
import net.project.enums.TempRecName;
import net.project.utils.CookieHandler;
import net.project.utils.WeatherHandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

public class WeatherAction extends ActionSupport implements SessionAware ,ServletResponseAware, ServletRequestAware{

	private Log log = LogFactory.getLog(getClass());
	private static final long serialVersionUID = 467696784777900420L;

	private final String INTV_COOKIE = "IntervalCookie";

	private Map<String, Object> session;
	protected HttpServletResponse servletResponse;
	protected HttpServletRequest servletRequest;

	private TempManager tempManager;
	private String wb;

	private String chartData = "";

	private Integer interval;

	public WeatherAction(){
		tempManager = new TempManager();
	}

	public String loadWeather(){


		try {			
			
			if (interval == null){
				Cookie c = new CookieHandler(servletRequest).verifyCookie(INTV_COOKIE);			

				if (c != null  ){
					interval = Integer.parseInt(c.getValue());
				}
			}else if (interval != null){
				CookieHandler ch = new CookieHandler(servletRequest, servletResponse);
				Cookie c =  ch.verifyCookie(INTV_COOKIE);	
				if (c == null || c != null && Integer.parseInt(c.getValue()) != interval.intValue() ){
					ch.saveCookie(INTV_COOKIE, interval);
				}
			}

			int intv = (interval != null? interval : 30);	

			setInterval(intv);

			List<Temperature> glbTemp = tempManager.getGlobalDailyTemperature(intv);			

			Collections.sort(glbTemp, new Comparator<Temperature>() {
				public int compare(Temperature o1, Temperature o2) {				
					return o1.getRecordedDate().compareTo(o2.getRecordedDate());					
				}
			});

			buildChart(glbTemp);

		} catch (Exception e) {
			log.error("Error in loadWeather: " , e);
		}		
		return SUCCESS;
	}

	private void buildChart( List<Temperature> tmpChart){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		StringBuilder chart = new StringBuilder("$(function() {");
		chart.append("var chart = Morris.Line({");		

		chart.append("element: 'morris-area-chart', ");

		if (!tmpChart.isEmpty()){
			boolean first = true;
			for(Temperature t : tmpChart){
				if (first){
					chart.append("data: [{");
					chart.append("period: '" + sdf.format(t.getRecordedDate()) + "' ," );

					chart.append(addRecord(t));

					first = false;
					chart.append("}");
				}
				else{

					chart.append(",{");
					chart.append("period: '" + sdf.format(t.getRecordedDate()) + "' ," );

					chart.append(addRecord(t));

					chart.append("}");
				}
			}		

			chart.append("],");
		}else{
			
		}
		chart.append("xkey: 'period',");		

		boolean f = true;
		chart.append("ykeys:");
		for(TempRecName rec : TempRecName.values()){			
			if (f){
				chart.append("['"+ rec + "'");
				f = false;
			}else{
				chart.append(", '" + rec + "'");

			}			
		}
		chart.append("],");

		boolean fName = true;
		chart.append("labels:");
		for(TempRecName rec : TempRecName.values()){

			if (fName){
				chart.append("['"+ rec.getRecName() + "'");
				fName = false;
			}else{
				chart.append(", '" + rec.getRecName() + "'");

			}			
		}
		chart.append("],");

		chart.append(" pointSize: 2,");
		chart.append(" lineColors: ['#0b62a4','#d81a29'],");
		chart.append("hideHover: 'auto',");

		chart.append(" resize: true");
		chart.append(" });");

		//legend
		chart.append("chart.options.labels.forEach(function(label, i) {");
		chart.append("var legendItem = $('<span></span>').text( label ).prepend('<span>&nbsp;</span>');");
		chart.append("legendItem.find('span')");
		chart.append(".css('backgroundColor', chart.options.lineColors[i])");
		chart.append(".css('width', '18px')");
		chart.append(".css('width', '18px')");
		chart.append(".css('margin', '2px');");
		chart.append("$('#legend').append(legendItem);");
		chart.append("});");
		//end legend

		chart.append(" });"); // end of function		

		chartData = chart.toString();

	}

	private String addRecord(Temperature t){

		StringBuilder ch = new StringBuilder();

		for(TempRecName rec : TempRecName.values()){

			if (rec == TempRecName.valueOf(t.getRecorderName()) ){
				ch.append(rec + " : " + t.getTempC().intValue() );
			}	
		}
		return ch.toString();
	}
	
	public String loadWeatherFromEnvCan(){

		int fiveMin = 1000 * 60 * 5;
		Calendar now = Calendar.getInstance();
		
		Calendar winterStDt = Calendar.getInstance();
		winterStDt.set(Calendar.MONTH, Calendar.OCTOBER);
		winterStDt.set(Calendar.DAY_OF_MONTH, 1);
		
		Calendar winterEndDt = Calendar.getInstance();
		winterEndDt.set(Calendar.YEAR , winterStDt.get(Calendar.YEAR) + 1);
		winterEndDt.set(Calendar.MONTH, Calendar.MAY);
		winterEndDt.set(Calendar.DAY_OF_MONTH, 1);
		
		try{
			//try to see if we have the weather is  the session.. if yes , get it and verify if it's been more than 
			//5 min that is has been updated.. If yes, re-update it.
			
			WeatherBean wthBean = (WeatherBean)session.get(Constants.GLOBAL_WEATHER);
			
			WeatherHandler wthHandl = new WeatherHandler(new TempManager());
			
			if (wthBean != null){
				
				//check if we need to update it
				if (now.getTimeInMillis() - wthBean.getLastSyncToEnvCan().getTime() > fiveMin){
					wthHandl.updateWeather(wthBean, true, true);
				}else{
					wthHandl.updateWeather(wthBean, true, false);
				}
			}else{
				wthBean = new WeatherBean();
				wthHandl.updateWeather(wthBean, true, true);
			}		
			
			session.put(Constants.GLOBAL_WEATHER, wthBean);			
						
			StringBuilder currWth = new StringBuilder("<div class=\"row\">");
			currWth.append("<div class=\"col-lg-12\">");
			currWth.append("<div class=\"pull-left text-center \" style=\"margin-right: 20px;\" >");
			currWth.append("<strong>" + new SimpleDateFormat("EEE d").format(new Date()) + "</strong> <br/> ");
			currWth.append("<img src=\"/web/images/mostly_cloudy.png\" /> <br/>");
			currWth.append("<p><strong>Last Updated:</strong><br/> " + new SimpleDateFormat("EEE, MMM d, HH:mm").format(wthBean.getEnvCanWebDate()) + "</p>");
			currWth.append("</div>");
			currWth.append("<div style=\"margin-top:10px\">");
			currWth.append("<h4>" + wthBean.getCurrentCond() + "</h4>");
			currWth.append("<h5><i class=\"glyphicon glyphicon-sunglasses\"></i> " + wthBean.getFormattedTemp(TempRecName.AA) + "C &nbsp;<small>"+
							wthBean.getFormattedDate(TempRecName.AA) +" </small></h5>");
			
			currWth.append("<h5><i class=\"fa fa-sun-o\"></i> " + wthBean.getFormattedTemp(TempRecName.BB) + "C &nbsp;<small>"+
							wthBean.getFormattedDate(TempRecName.BB) +" </small></h5> 	");
			

			if(now.after(winterStDt.getTime()) && now.before(winterEndDt.getTime())){
				
				float tmp = wthBean.getTemp().get(TempRecName.pool); 
												
				currWth.append("<h5><i class=\"fa fa-life-buoy\"></i> " + (((tmp - 32)*5)/9) + "C &nbsp;<small>"+
							wthBean.getFormattedDate(TempRecName.pool) +" </small></h5><br/> ");
			}else{
				currWth.append("<h5><i class=\"fa fa-life-buoy\"></i> " + wthBean.getFormattedTemp(TempRecName.pool) + "F &nbsp;<small>"+
						wthBean.getFormattedDate(TempRecName.pool) +" </small></h5><br/> ");
			}
			currWth.append("<h4> Forecast: (Weather.gc.ca) </h4>");
			currWth.append("<p>"+ wthBean.getForecast() +"</p>	");
			currWth.append("</div>");
			currWth.append("</div>");
			currWth.append(" </div>");

			wb = currWth.toString();
		}catch(Exception ex){
			log.error("ERROR: " , ex);
			wb = "Error Loading Weather";
		}
		return SUCCESS;
	}	

	@Override
	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}
	@Override
	public void setServletRequest(HttpServletRequest servletRequest) {
		this.servletRequest = servletRequest;
	}
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;		
	}
	public String getChartData() {
		return chartData;
	}
	public void setChartData(String chartData) {
		this.chartData = chartData;
	}
	public Integer getInterval() {
		return interval;
	}
	public void setInterval(Integer interval) {
		this.interval = interval;
	}
	public void setWb(String wb) {
		this.wb = wb;
	}
	public String getWb() {
		return wb;
	}
}