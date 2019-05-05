package com.hyipc.util;
import java.util.Calendar;

public class TimeUtil {
	public static String getCurrentTime() {
		Calendar calendar = Calendar.getInstance();
		String year = Integer.toString(calendar.get(Calendar.YEAR));
		String month = Integer.toString(calendar.get(Calendar.MONTH) + 1);
		String day = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
		String hour = Integer.toString(calendar.get(Calendar.HOUR_OF_DAY));
		String min = Integer.toString(calendar.get(Calendar.MINUTE));
		String sec = Integer.toString(calendar.get(Calendar.SECOND));
		if (month.length() < 2) {
			month = "0" + month;
		}
		if (day.length() < 2) {
			day = "0" + day;
		}
		if (hour.length() < 2) {
			hour = "0" + hour;
		}
		if (min.length() < 2) {
			min = "0" + min;
		}
		if (sec.length() < 2) {
			sec = "0" + sec;
		}
		return year + month + day + hour + min + sec;
	}

	public static String getCurrentTime2() {
		Calendar calendar = Calendar.getInstance();
		String year = Integer.toString(calendar.get(Calendar.YEAR));
		String month = Integer.toString(calendar.get(Calendar.MONTH) + 1);
		String day = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
		String hour = Integer.toString(calendar.get(Calendar.HOUR_OF_DAY));
		String min = Integer.toString(calendar.get(Calendar.MINUTE));
		String sec = Integer.toString(calendar.get(Calendar.SECOND));
		if (month.length() < 2) {
			month = "0" + month;
		}
		if (day.length() < 2) {
			day = "0" + day;
		}
		if (hour.length() < 2) {
			hour = "0" + hour;
		}
		if (min.length() < 2) {
			min = "0" + min;
		}
		if (sec.length() < 2) {
			sec = "0" + sec;
		}
		return year + "-" + month + "-" + day + " " + hour + ":" + min;
	}


	
	public static String getCurrentDay() {
		Calendar calendar = Calendar.getInstance();
		String year = Integer.toString(calendar.get(Calendar.YEAR));
		String month = Integer.toString(calendar.get(Calendar.MONTH) + 1);
		String day = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
		if (month.length() < 2) {
			month = "0" + month;
		}
		if (day.length() < 2) {
			day = "0" + day;
		}
		return year + month + day;
	}
	


}
