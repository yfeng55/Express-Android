package com.dtf.hellobeacon.util;

/**
 * Util for anything date and time related,keep progressively adding to as needed
 */

import org.joda.time.DateTime;

public class DateAndTimeUtil {

	public static String getCurrentHour() {
		DateTime dateTime = new DateTime();
		return String.valueOf(dateTime.getHourOfDay());
	}
}
