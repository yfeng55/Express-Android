package com.dtf.hellobeacon.util;
import java.util.Calendar;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


/**
 * Util for anything date and time related,keep progressively adding to as needed
 */


public class DateUtil {
	
	public static final int WEEK = 1;
	public static final int MONTH = 2;
	public static final int YEAR = 3;
	

	public static String getCurrentHour() {
		DateTime dateTime = new DateTime();
		return String.valueOf(dateTime.getHourOfDay());
	}
	
	public static String getCurrentTime() {
		DateTime dateTime = new DateTime();
		DateTimeFormatter dtf = DateTimeFormat.forPattern("hh:mm aa");
		return dtf.print(dateTime);
	}
	
	public static Long getWeekStart(){
		
		Calendar cal = getToday();
		cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
		
		return cal.getTimeInMillis();
	}
	
	public static Long getMonthStart(){
		
		Calendar cal = getToday();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		
		return cal.getTimeInMillis();
	}
	
	public static Long getYearStart(){

		Calendar cal = getToday();
		cal.set(Calendar.DAY_OF_YEAR, 1);
		
		return cal.getTimeInMillis();

	}
	
	public static Calendar getToday(){
		// get today and clear time of day
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);

		return cal;
	}
	
	/**
	 * Used to get formatted time from ints of hour and minutes
	 * @param hour
	 * @param minutes
	 * @return
	 */
	public static String convertHourToTime(int hour, int minutes) {
		int minute = 0;
		String amPM = "AM";
		if(hour == 0 || hour == 24) {
			//is 12 am
			hour = 12;
		}
		else if(hour >= 12) {
			hour = hour - 12;
			amPM = "PM";
		}
		else if(hour > 23 || minutes > 60){
			throw new IllegalArgumentException("Invalid hour in DateUtil - convertHourToTime");
		}
		
		if(minute == 0)
		{
			return String.valueOf(hour) + ":00 " + amPM;
		}			
		return String.valueOf(hour) + ":" + String.valueOf(minute) + " " + amPM;
	}
	
	public static int increment(int v, int type){
		switch(type){
		case WEEK:
			if(v < 7){
				return v++;
			}
			break;
		case MONTH:
			if(v < 30){
				return v++;
			}
			break;
		case YEAR:
			if(v < 365){
				return v++;
			}
			break;
		}
		return v;
	}
	
}







