package com.dtf.hellobeacon;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import android.text.format.DateFormat;
import android.util.Log;

public class Visit {
	
	protected Date enterTime;
	protected Date exitTime;
	protected long lengthOfStay;
	
	public Visit()
	{
		setTime(enterTime);
		lengthOfStay = 0;
	}
	
	/**
	 * sets the given date (time) to the current date in the desired format
	 * @param time
	 */
	public void setTime(Date time){
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm a");
		time = new Date();
		GregorianCalendar timeCal = new GregorianCalendar();
		String timeString = dateFormat.format(timeCal.getTime());
		try {
			time = dateFormat.parse(timeString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}
	
	public void setLengthOfStay() {
		setTime(exitTime);
		long diff = enterTime.getTime() - exitTime.getTime();
		
		long diffHours = diff / (60 * 60 * 1000) % 24;
		long diffMinutes = diff / (60 * 1000) % 60;
		this.lengthOfStay = diffHours + (diffMinutes)/60;
		
	}
	
	//GETTERS
	
	public Date getEnterDate() {
		
		if(enterTime != null)
		{
			return enterTime;
		}
		return null;
		
		/*
		try {
			return enterTime;
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		*/
	}
	
	public Date getExitDate() {
		if(exitTime != null)
		{
			return exitTime;
		}
		return null;
	}
	
	public long getLengthOfStay() {
		return lengthOfStay;
	}
	

	
	
}
