package com.jsolutionssp.pill;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.jsolutionssp.pill.db.DayStorageDB;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PillDayInfo {

	//Public fields so all classes can use them
	public static final int PILL_TAKEN = 0; 

	public static final int PILL_NOT_TAKEN = 1;

	public static final int PILL_TAKEN_LATE = 2;

	public static final int PILL_PENDING = 3;

	public static final int PILL_PLACEBO = 4;

	public static final int PILL_REST_DAY = 5;

	private static SharedPreferences settings;

	private Context context;

	public PillDayInfo(Context context) {
		this.context = context;
		settings = PreferenceManager.getDefaultSharedPreferences(context);
	}

	public int getPillType(GregorianCalendar cellCalendar, int dayOfYear, int year) {
		DayStorageDB db = new DayStorageDB(context);
		String pillType = db.getPillType(String.valueOf(year), String.valueOf(dayOfYear));
		boolean existedInDB = false;
		if (pillType.equalsIgnoreCase("-1"))
			existedInDB = true;
		else if (pillType != null) {
			db.close();
			return Integer.valueOf(pillType);
		}
		//It has a return so else is not necessary  
		long startPackTime = settings.getLong("start_pack_date", System.currentTimeMillis());
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTimeInMillis(startPackTime);
		int startPackDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
		int startPackYear = calendar.get(Calendar.YEAR);
		int differenceOfDays = calculateDifferenceDays(dayOfYear, year, startPackDayOfYear, startPackYear);
		int calculatedPillType = getPillType(differenceOfDays);
		int finalPillType = checkPastDay(cellCalendar, calculatedPillType);
		//Now I have to store the information in the database
		if (existedInDB)
			db.updatePillType(String.valueOf(year), String.valueOf(dayOfYear), String.valueOf(finalPillType));
		else
			db.setPillType(String.valueOf(year), String.valueOf(dayOfYear), String.valueOf(finalPillType));
		db.close();
		return finalPillType;
	}

	private int checkPastDay(GregorianCalendar cellCalendar, int pillType) {
		GregorianCalendar todayCalendar = new GregorianCalendar();
		//So today's pill is always shown until taken and changed in the db
		todayCalendar.set(Calendar.HOUR_OF_DAY, 23);
		todayCalendar.set(Calendar.MINUTE, 59);
		if (cellCalendar.before(todayCalendar)) {
			//There is only two types that future differ from past, pending and placebo
			switch (pillType) {
			case PillDayInfo.PILL_PENDING:
				return PillDayInfo.PILL_TAKEN;
				//Wether if it is placebo or rest day in the past it will appear as rest day
			default:
				return PillDayInfo.PILL_REST_DAY;
			}
		}
		return pillType;
	}

	private int getPillType(int differenceOfDays) {
		String pillDaysString = settings.getString("pill_type", "");//Format is NumberTakingDays/NumberPlaceboDays/NumberOfRestDays
		if (pillDaysString == "")
			return -1;
		String pillDaysStringArray[] = pillDaysString.split("/");

		int pillDays[] = {Integer.valueOf(pillDaysStringArray[0]), Integer.valueOf(pillDaysStringArray[1]), Integer.valueOf(pillDaysStringArray[2])};

		if (differenceOfDays < 0) { // This means we're looking to a day before starting the pack
			differenceOfDays = -differenceOfDays; //Change it to positive to work easily
			return getPillTypeDayBeforeStartPackDate(differenceOfDays, pillDays);
		}
		else {
			return getPillTypeDayAfterStartPackDate(differenceOfDays, pillDays);
		}
	}

	/**
	 * This method gets the pill type of the day we're displaying knowing that the day is before the user started the pack
	 * 
	 * @param differenceOfDays The difference of days between the day we're displaying on the screen and the day the user
	 * started the pack
	 * @param pillDays it is an array that holds the information about the cycle of the user
	 * @return the pill status of the day
	 */
	private int getPillTypeDayBeforeStartPackDate(int differenceOfDays, int[] pillDays) {
		//pillDays[0] --> NumberTakingDays, pillDays[1] --> NumberPlaceboDays, pillDays[2] --> NumberOfRestDays
		int totalCycleDays = pillDays[0] + pillDays[1] + pillDays[2]; 
		//Strict less because i.e. 28 days is from day 0 to day 27
		if (differenceOfDays <= totalCycleDays) {
			if (differenceOfDays <= pillDays[1] && pillDays[1] != 0) { // Check if it is placebo day
				return PILL_PLACEBO;
			}
			else if (differenceOfDays <= pillDays[2] && pillDays[2] != 0){ // Check if it is rest day
				return PILL_REST_DAY;
			}
			else { // We check if it is taking day
				return PILL_PENDING;
			}
		}
		else {
			return getPillTypeDayBeforeStartPackDate(differenceOfDays - totalCycleDays, pillDays);
		}
	}

	private int getPillTypeDayAfterStartPackDate(int differenceOfDays, int[] pillDays) {
		//pillDays[0] --> NumberTakingDays, pillDays[1] --> NumberPlaceboDays, pillDays[2] --> NumberOfRestDays
		int totalCycleDays = pillDays[0] + pillDays[1] + pillDays[2]; 
		//Strict less because i.e. 28 days is from day 0 to day 27
		if (differenceOfDays < totalCycleDays) {
			if (differenceOfDays < pillDays[0]) { // We check if it is taking day
				return PILL_PENDING;
			}
			else if (differenceOfDays < pillDays[0] + pillDays[1] && pillDays[1] != 0) { // Check if it is placebo day
				return PILL_PLACEBO;
			}
			else { // Check if it is rest day
				return PILL_REST_DAY;
			}
		}
		else {
			return getPillTypeDayAfterStartPackDate(differenceOfDays - totalCycleDays, pillDays);
		}
	}

	private int calculateDifferenceDays(int dayOfYear, int year, int startPackDayOfYear, int startPackYear) {

		GregorianCalendar calendar = new GregorianCalendar();
		if (year == startPackYear ) {
			if (dayOfYear < startPackDayOfYear)
				return - (startPackDayOfYear - dayOfYear);
			else 
				return dayOfYear - startPackDayOfYear;
		}
		else if (year < startPackYear) {
			if (year + 1 == startPackYear) {
				calendar.set(Calendar.YEAR, year);
				int numberOfDays = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
				return - (startPackDayOfYear + (numberOfDays - dayOfYear));
			}
			else {
				calendar.set(Calendar.YEAR, year + 1);
				int numberOfDays = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
				return  - (numberOfDays + calculateDifferenceDays(dayOfYear, year + 1, startPackDayOfYear, startPackYear));
			}
		}
		else {
			if (year - 1 == startPackYear) {
				calendar.set(Calendar.YEAR, startPackYear);
				int numberOfDays = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
				return dayOfYear + (numberOfDays - startPackDayOfYear);
			}
			else {
				calendar.set(Calendar.YEAR, year - 1);
				int numberOfDays = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
				return numberOfDays + calculateDifferenceDays(dayOfYear, year - 1, startPackDayOfYear, startPackYear);
			}
		}
	}
}
