package com.imporve.skill.transactionservice.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtils {

	public static final Locale APP_DEFAULT_LOCALE = Locale.ENGLISH;
	public static final Locale LOCALE_THAI = new Locale("th", "TH");
	public static final String DATE_FORMAT = "dd/MM/yyyy";
	public static final String DATETIME_FORMAT = "dd/MM/yyyy HH:mm:ss";

	private static Calendar getCalendar() {
		return Calendar.getInstance(APP_DEFAULT_LOCALE);
	}
	
	public static Date addDay(Date date, int addDay) {
		if (date == null) {
			return null;
		} else if (addDay == 0) {
			return date;
		}

		Calendar dateCal = getCalendar();
		dateCal.setTime(date);

		dateCal.add(Calendar.DAY_OF_YEAR, addDay);

		return dateCal.getTime();
	}

	public static Date currentDate() {
		Calendar dateCal = getCalendar();

		return dateCal.getTime();
	}

	public static Date toDate(long dateVal) {
		return new Date(dateVal);
	}

	public static Date getYesterDay() {
		Calendar dateCal = getCalendar();

		dateCal.add(Calendar.DAY_OF_YEAR, -1);

		return dateCal.getTime();
	}

	public static String getEffective(Date d) {
		if (d == null) {
			return "Yes";
		} else if (d.getTime() < currentDate().getTime()) {
			return "Yes";
		}
		return "No";
	}

	public static String formatDate(String pattern, Date src) {
		if (pattern == null || pattern.isEmpty() || src == null) {
			return null;
		}

		DateFormat df = new SimpleDateFormat(pattern);

		return df.format(src);
	}

	public static Date parseDate(String pattern, String src) throws ParseException {
		if (pattern == null || pattern.isEmpty() || src == null || src.isEmpty()) {
			return null;
		}

		DateFormat df = new SimpleDateFormat(pattern, DateTimeUtils.APP_DEFAULT_LOCALE);

		return df.parse(src);
	}

	public static boolean isInCurrentDate(Date start, Date end) throws Exception {

		if (start != null && end != null) {
			long startTime = start.getTime();
			long endTime = end.getTime();

			long current = currentDate().getTime();

			if (startTime <= current && endTime >= current) {
				return true;
			}
		}

		return false;
	}

	/***
	 * date <= current date
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isBeforeFuture(Date date) {
		if (date != null) {
			long time = date.getTime();

			long current = currentDate().getTime();

			return time <= current;
		}

		return false;
	}

	public static String parseDate(Date date) throws Exception {
		if (date == null)
			return null;

		SimpleDateFormat fmt = new SimpleDateFormat(DATE_FORMAT, APP_DEFAULT_LOCALE);
		return fmt.format(date);
	}

	public static String parseDate(Date date, String pattern) throws Exception {
		if (date == null)
			return null;

		SimpleDateFormat fmt = new SimpleDateFormat(pattern, APP_DEFAULT_LOCALE);
		return fmt.format(date);
	}

	public static java.sql.Date utilDateToSqlDate(java.util.Date src) {
		if (src == null) {
			return null;
		}
		return new java.sql.Date(src.getTime());
	}

	public static Date getBeginDate(Date date) {
		Calendar calendar = getCalendar();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public static Date getEndDate(Date date) {
		Calendar calendar = getCalendar();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 99);
		return calendar.getTime();
	}
}