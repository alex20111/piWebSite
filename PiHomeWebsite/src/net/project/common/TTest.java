package net.project.common;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import net.project.db.entity.Temperature;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class TTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Temperature t = new Temperature();
		
		t.setRecordedDate(new Date());
		t.setRecorderName("out");
		t.setTempC(220.45545f);
		t.setBatteryLevel(null);
		
		Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd").excludeFieldsWithoutExposeAnnotation().create();
		
		String l = g.toJson(t);
		
		System.out.println(l);
		
		
		String f = "{ recorderName:out}";
		
		
		Temperature t2 = g.fromJson(f, Temperature.class );
		
		System.out.println(t2);
		
		
		
		JsonParser parser = new JsonParser();
		JsonElement e = parser.parse(f);
		System.out.println(e.isJsonObject());
		System.out.println(e.getAsJsonObject().get("recorderName"));
		
		String gg = e.getAsJsonObject().get("recorderName").getAsString();
		System.out.println(gg);
		
	

			System.out.println(String.format("%.2f", t.getTempC()));
	
			try {
				String ip = InetAddress.getLocalHost().getHostAddress();
				String result = ip.substring(0, ip.indexOf("."));
				
				System.out.println(ip + "  " + result);
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

	}

}
