package com.dtf.hellobeacon.util;
import java.util.Calendar;

import org.joda.time.DateTime;


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







