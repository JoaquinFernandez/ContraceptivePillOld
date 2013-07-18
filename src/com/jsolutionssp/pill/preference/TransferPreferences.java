package com.jsolutionssp.pill.preference;

import java.util.GregorianCalendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.provider.Settings;

public class TransferPreferences {

	public static void TransferOldPreferences(Context context) {
		SharedPreferences newPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		Editor newEditor = newPrefs.edit();
		SharedPreferences oldPrefs = context.getSharedPreferences("com.jsolutionssp.pill", 0);

		//Transfer pill type
		int pillTakingDays = oldPrefs.getInt("pillTakingDays", -1);//how many days is taking pills
		int pillRestDays = oldPrefs.getInt("pillRestDays", -1);//How many days is rest
		if (pillTakingDays != -1 && pillRestDays != -1) {
			newEditor.putString("pill_type", pillTakingDays + "/" + "0/" + pillRestDays);//Format is NumberTakingDays/NumberPlaceboDays/NumberOfRestDays
			// if there was a pill time, is not the first time they use the application so they don't need the
			//tour about the preferences
			newEditor.putBoolean("first_time_used_preference", false);
		}
		//Transfer start week day
		boolean whichDayStartsWeek = oldPrefs.getBoolean("firstDayofWeek", true);//True = Monday, false,Sunday
		if (whichDayStartsWeek)
			newEditor.putString("start_week_day", "Monday");
		else
			newEditor.putString("start_week_day", "Sunday");

		//Transfer start pack date
		int startCycleDayofYear = oldPrefs.getInt("startCycleDayofYear", -1);
		int startCycleYear = oldPrefs.getInt("startCycleYear", -1);
		//Create calendar
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.set(GregorianCalendar.YEAR, startCycleYear);
		calendar.set(GregorianCalendar.DAY_OF_YEAR, startCycleDayofYear);
		//So the values will update
		calendar.get(GregorianCalendar.DAY_OF_MONTH);
		calendar.get(GregorianCalendar.MONTH);
		//We get the time in Milliseconds and store it in the new preferences
		long timeInmillis = calendar.getTimeInMillis();
		newEditor.putLong("start_pack_date", timeInmillis);

		//Transfer cycle alarm
		int isCycleAlarmActivated = oldPrefs.getInt("cycleAlarm", 0);//0 means notActivated
		//If isCycleAlarmActivated is not 0 then isCycleAlarm is true
		boolean isCycleAlarm = (isCycleAlarmActivated != 0);
		newEditor.putBoolean("cycle_alarm", isCycleAlarm);
		
		//Transfer cycle alarm vibration
		int isCycleAlarmVibrateActivated = oldPrefs.getInt("cycleAlarmVibrate", 0);//0 is notActivated
		boolean isCycleAlarmVibrate = (isCycleAlarmVibrateActivated != 0);
		newEditor.putBoolean("cycle_alarm_vibrate", isCycleAlarmVibrate);
		
		//Transfer cycle alarm sound
		int isCycleAlarmSoundActivated = oldPrefs.getInt("cycleAlarmSound", 0);//0 is notActivated
		if (isCycleAlarmSoundActivated != 0)
			newEditor.putString("cycle_alarm_ringtone", Settings.System.DEFAULT_NOTIFICATION_URI.toString());
		
		//Transfer set times for the alarm
		int cycleAlarmHourNotification = oldPrefs.getInt("cycleHourNotification", -1);//-1 is not set
		int cycleAlarmMinuteNotification = oldPrefs.getInt("cycleMinuteNotification", -1);//-1 is not set
		if (cycleAlarmHourNotification != -1 && cycleAlarmMinuteNotification != -1) {
			//To assure it is a two digit time
			String hour = "";
			String minute = "";
			if(cycleAlarmHourNotification <= 9)
				hour = "0";
			if (cycleAlarmMinuteNotification <= 9)
				minute = "0";
			
			hour += cycleAlarmHourNotification;
			minute += cycleAlarmMinuteNotification;
			String time = hour + ":" + minute;
			newEditor.putString("cycle_alarm_set_time", time);
		}
			
		//Transfer cycleALarm previous days for ringing
		newEditor.putInt("cycle_alarm_prev_days",  oldPrefs.getInt("prevAlarmDays", 0));//each value is valid)
		
		//Transfer diary alarm
		int isDiaryAlarmActivated = oldPrefs.getInt("diaryAlarm", 0);//0 means notActivated
		//If isDiaryAlarmActivated is not 0 then isCycleAlarm is true
		boolean isDiaryAlarm = (isDiaryAlarmActivated != 0);
		newEditor.putBoolean("diary_alarm", isDiaryAlarm);
		
		//Transfer diary alarm vibration
		int isDiaryAlarmVibrateActivated = oldPrefs.getInt("diaryAlarmVibrate", 0);//0 is notActivated
		boolean isDiaryAlarmVibrate = (isDiaryAlarmVibrateActivated != 0);
		newEditor.putBoolean("diary_alarm_vibrate", isDiaryAlarmVibrate);
		
		//Transfer diary alarm sound
		int isDiaryAlarmSoundActivated = oldPrefs.getInt("diaryAlarmSound", 0);//0 is notActivated
		if (isDiaryAlarmSoundActivated != 0)
			newEditor.putString("diary_alarm_ringtone", Settings.System.DEFAULT_NOTIFICATION_URI.toString());
		
		//Transfer set times for the alarm
		int diaryAlarmHourNotification = oldPrefs.getInt("diaryHourNotification", -1);//-1 is not set
		int diaryAlarmMinuteNotification = oldPrefs.getInt("diaryMinuteNotification", -1);//-1 is not set
		if (diaryAlarmHourNotification != -1 && diaryAlarmMinuteNotification != -1) {
			//To assure it is a two digit time
			String hour = "";
			String minute = "";
			if(diaryAlarmHourNotification <= 9)
				hour = "0";
			if (diaryAlarmMinuteNotification <= 9)
				minute = "0";
			
			hour += diaryAlarmHourNotification;
			minute += diaryAlarmMinuteNotification;
			String time = hour + ":" + minute;
			newEditor.putString("diary_alarm_set_time", time);
		}
		//Add that the preferences have already been settled so no tour is needed
		newEditor.putBoolean("first_time_used_preference", false);
		newEditor.commit();
		oldPrefs.edit().clear().commit();
	}
}
