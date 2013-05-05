package com.jsolutionssp.pill.service;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.jsolutionssp.pill.ContraceptivePill;
import com.jsolutionssp.pill.PillLogics;
import com.jsolutionssp.pill.widget.WidgetProvider;

public class SetAlarms extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		SharedPreferences settings = context.getSharedPreferences(ContraceptivePill.PREFS_NAME, 0);
		int diary = settings.getInt("diaryAlarm", -1);
		int cycle = settings.getInt("cycleAlarm", -1);
		int startCycleDayofYear = settings.getInt("startCycleDayofYear", -1);
		int startCycleYear = settings.getInt("startCycleYear", -1);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		if (diary == 1 && startCycleDayofYear != -1 && startCycleYear != -1) {
			long diaryAlarm = PillLogics.diaryAlarm(settings);
			if (diaryAlarm != -1) {
				Intent alarmIntent = new Intent(context, DiaryAlarmTriggered.class);
				PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
				alarmManager.set(AlarmManager.RTC_WAKEUP, diaryAlarm,pendingIntent);
			}
		}
		if (cycle == 1 && startCycleDayofYear != -1 && startCycleYear != -1) {
			long cycleAlarm = PillLogics.cycleAlarm(settings);
			if (cycleAlarm != -1) {
				Intent alarmIntent = new Intent(context, CycleAlarmTriggered.class);
				PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
				alarmManager.set(AlarmManager.RTC_WAKEUP, cycleAlarm,pendingIntent);
			}
		}
		GregorianCalendar date = new GregorianCalendar();
		//Update widget info
		boolean isPillDay = PillLogics.isPillDay(settings, 
				date.get(Calendar.DAY_OF_YEAR), date.get(Calendar.YEAR));
		String daysLeft = PillLogics.DaysLeft(settings);
		WidgetProvider.updateWidgetContent(context,
			    AppWidgetManager.getInstance(context), isPillDay, daysLeft);
		//Set next alarm if not changed before
		date.set(Calendar.DAY_OF_YEAR, (date.get(Calendar.DAY_OF_YEAR) + 1));
		date.set(Calendar.HOUR_OF_DAY, 0);
		date.set(Calendar.MINUTE, 1);
		long alarm = date.getTimeInMillis();
		Intent alarmIntent = new Intent(context, SetAlarms.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		alarmManager.set(AlarmManager.RTC_WAKEUP, alarm,pendingIntent);
	}
}