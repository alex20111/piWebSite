package net.project.enums;

public enum Interval {
	ten("10", 10), twenty("20", 20), thirthy("30", 30), fourthy("40", 40), fifty("50", 50), oneHour("60", 60);
	
	
	private String intvName;
	private int intvTime;
	
	private Interval (String name, int time){
		intvName = name;
		intvTime = time;
	}
	
	public String getIntvName(){
		return intvName;
	}
	public int getIntvTime(){
		return intvTime;
	}
}
