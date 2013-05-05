package com.jsolutionssp.pill;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.content.SharedPreferences;

public class PillLogics {

	private static GregorianCalendar calendar;
	private static SharedPreferences settings;

	public static boolean isPillDay(SharedPreferences sett, int repDay, int repYear) {
		calendar = new GregorianCalendar();
		settings = sett;
		int dayNumber = getDayNumber(repDay, repYear);
		return isPillDay(dayNumber);
	}

	public static long cycleAlarm(SharedPreferences sett) {
		settings = sett;
		calendar = new GregorianCalendar();
		return nextCycleDay();
	}

	public static long diaryAlarm(SharedPreferences sett) {
		calendar = new GregorianCalendar();
		settings = sett;

		int actualDay = calendar.get(Calendar.DAY_OF_YEAR);
		int actualYear = calendar.get(Calendar.YEAR);
		String settingTime = settings.getString("diary_alarm_set_time", "-1");
		String time[] = settingTime.split(":");
		int settingHour = Integer.parseInt(time[0]);
		int settingMinute = Integer.parseInt(time[1]);
		int actualHour = calendar.get(Calendar.HOUR_OF_DAY);
		int actualMinute = calendar.get(Calendar.MINUTE);

		if (settingHour != -1 && settingMinute != -1) {
			int i = 0;
			//we check if today's alarm has passed
			if (settingHour < actualHour || (settingHour == actualHour && settingMinute <= actualMinute))
				i++;

			int day = getDayNumber(actualDay + i, actualYear);
			//We check if the i th day is pill day, if not, we add days till it is
			boolean pillDay = PillLogics.isPillDay(day);
			while (!pillDay) {
				i++;
				day++;
				pillDay = PillLogics.isPillDay(day);
			}
			//we set that moment
			GregorianCalendar cal = new GregorianCalendar();
			cal.set(Calendar.DAY_OF_YEAR, (actualDay + i));
			cal.set(Calendar.HOUR_OF_DAY, settingHour);
			cal.set(Calendar.MINUTE, settingMinute);
			long date = cal.getTimeInMillis();
			return date;
		}
		return -1;
	}

	public static String DaysLeft(SharedPreferences sett) {
		settings = sett;
		String daysLeft;

		int leftDays = 0;
		String settingTime = settings.getString("pill_type", "-1");
		String time[] = settingTime.split("/");
		int pillTakingDays = Integer.parseInt(time[0]);
		int pillPlaceboDays = Integer.parseInt(time[1]);
		int pillRestDays = Integer.parseInt(time[2]);
		if (pillTakingDays != -1 && pillRestDays != -1) {
			int day = getDayNumber(calendar.get(Calendar.DAY_OF_YEAR), calendar.get(Calendar.YEAR));
			if (isPillDay(day)) {
				while (isPillDay(++day)) {
					leftDays++;//I increment days until it's not pill day
				}
				daysLeft = (pillTakingDays - leftDays) + "/" + pillTakingDays;
			}
			else {
				while (!isPillDay(++day)) {
					leftDays++;//I increment days until it's not pill day
				}
				daysLeft = (pillRestDays - leftDays) + "/" + pillRestDays;;
			}
		}
		else
			daysLeft = "";
		return daysLeft;
	}

	//Number of days since (or from when) i (will) took the pill
	private static int getDayNumber(int representingDayofYear, int representingYear) {
		//Keep in mind that days are negative if: myDay < day i took the pill
		int dayofrepresent = representingDayofYear;
		
		long startPackTime = settings.getLong("start_pack_date", System.currentTimeMillis());
		calendar = new GregorianCalendar();
		calendar.setTimeInMillis(startPackTime);
		int startCycleDayofYear = calendar.get(Calendar.DAY_OF_YEAR);
		int startCycleYear = calendar.get(Calendar.YEAR);

		while (representingYear > startCycleYear) {
			int daysSince = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
			startCycleYear++;
			calendar.set(Calendar.YEAR, startCycleYear); //I already incremented the year
			//I don't substract the startCycleDayofYear cause it will do it anyway at the end
			dayofrepresent += daysSince;
		}
		while (representingYear < startCycleYear) {
			//As they're future days, they're negatives
			startCycleYear--;
			calendar.set(Calendar.YEAR, startCycleYear);
			//I update the year before getting the days, because i have no interest in the days of that year
			int daysSince = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
			if (startCycleYear == representingYear)
				daysSince += (2*startCycleDayofYear); //I do it twice because after it will substract once
			dayofrepresent -= daysSince;
		}
		if (startCycleYear == representingYear)
			dayofrepresent -= startCycleDayofYear;
		calendar = new GregorianCalendar();
		return dayofrepresent;
	}
	private static boolean isPillDay(int dayNumber) {
		String settingTime = settings.getString("pill_type", "-1");
		String time[] = settingTime.split("/");
		int pillTakingDays = Integer.parseInt(time[0]);
		int pillPlaceboDays = Integer.parseInt(time[1]);
		int pillRestDays = Integer.parseInt(time[2]);
		if (dayNumber <= 0) {
			int multi = (int) (dayNumber/(pillTakingDays + pillRestDays));//We get a multiplicator to normalize the dayNumber value
			dayNumber = (dayNumber - (multi*(pillTakingDays + pillRestDays))); //We get the day in between [-27, 0]

			//if we're not in the rest days previous to take the pill is pill time
			if ((dayNumber < (-pillRestDays)) || (dayNumber == 0))
				return true;
			//if we're is rest time
			return false;
		}
		else {
			int multi = (int) (dayNumber/(pillTakingDays + pillRestDays));//We get a multiplicator to normalize the dayNumber value
			dayNumber = (dayNumber - (multi*(pillTakingDays + pillRestDays))); //We get the day in between (0, 27)
			//if we're in the next [0,20] days from taking the pill, is pill time
			if (dayNumber < pillTakingDays)
				return true;
			//else, is rest time
			return false;
		}
	}

	private static long nextCycleDay() {
		int startCycleDay;
		int actualDay = calendar.get(Calendar.DAY_OF_YEAR);
		int actualYear = calendar.get(Calendar.YEAR);

		//Now we get the hour of the alarm
		int settingHour = settings.getInt("cycleHourNotification", -1);
		int settingMinute = settings.getInt("cycleMinuteNotification", -1);
		int actualHour = calendar.get(Calendar.HOUR_OF_DAY);
		int actualMinute = calendar.get(Calendar.MINUTE);
		//check if it has already passed the day
		if (settingHour == -1 || settingMinute == -1)
			return -1;
		if (settingHour < actualHour || (settingHour == actualHour && settingMinute <= actualMinute))
			actualDay++;
		int day = getDayNumber(actualDay, actualYear);

		if (isPillDay(day)) {
			if (!isPillDay(day - 1)) {
				startCycleDay = actualDay;
			}
			else {
				while (isPillDay(day++)) {
					actualDay++;//I increment days until it's not pill day
				}
				startCycleDay = settings.getInt("pillRestDays", -1);
				startCycleDay += actualDay;
			}
		}
		else {
			while (!isPillDay(day++)) {
				actualDay++;//I increment days until it's not pill day
			}
			startCycleDay = actualDay;
		}
		//we get the offset from today in wich it will be the start cycle day
		int prevAlarmDays = settings.getInt("prevAlarmDays", 0);
		startCycleDay -= prevAlarmDays;
		//If that day has passed, then we set, next Cycle's alarm
		if (startCycleDay < calendar.get(Calendar.DAY_OF_YEAR)) {
			startCycleDay += settings.getInt("pillRestDays", -1);
			startCycleDay += settings.getInt("pillTakingDays", -1);
		}
		GregorianCalendar cal = new GregorianCalendar();
		cal.set(Calendar.DAY_OF_YEAR, startCycleDay);
		cal.set(Calendar.HOUR_OF_DAY, settingHour);
		cal.set(Calendar.MINUTE, settingMinute);
		long date = cal.getTimeInMillis();
		return date;
	}
}
