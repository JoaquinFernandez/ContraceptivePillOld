package com.jsolutionssp.pill.service;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.jsolutionssp.pill.PillDayInfo;

public class SetAlarms extends BroadcastReceiver {

        private static SharedPreferences settings;
        private static PillDayInfo pillDayInfo;
        private static GregorianCalendar calendar;

        @Override
        public void onReceive(Context context, Intent intent) {
                pillDayInfo = new PillDayInfo(context);
                calendar = new GregorianCalendar();
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                settings = PreferenceManager.getDefaultSharedPreferences(context);
                
                int pillType = pillDayInfo.getPillType(calendar, 
                                calendar.get(Calendar.DAY_OF_YEAR),calendar.get(Calendar.YEAR));
                boolean diary = settings.getBoolean("diary_alarm", false);

                if (diary) {
                        
                        if ((pillType == PillDayInfo.PILL_TAKEN) || (pillType == PillDayInfo.PILL_PLACEBO)
                                        || (pillType == PillDayInfo.PILL_PENDING)) {
                                long diaryAlarmTime = getDiaryAlarmTime();
                                if (diaryAlarmTime != -1) {
                                        Intent i = new Intent("triggerDiaryAlarm");
                                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
                                        alarmManager.set(AlarmManager.RTC_WAKEUP, diaryAlarmTime, pendingIntent);
                                }
                        }
                }
                boolean cycle = settings.getBoolean("cycle_alarm", false);
                boolean isTodayCycleAlarmDay = isTodayCycleAlarmDay();
                if (cycle && isTodayCycleAlarmDay) {
                        long cycleAlarmTime = getCycleAlarmTime();
                        if (cycleAlarmTime != -1) {
                                Intent i = new Intent("triggerCycleAlarm");
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
                                alarmManager.set(AlarmManager.RTC_WAKEUP, cycleAlarmTime, pendingIntent);
                        }
                }
                //Set next alarm if not changed before
                calendar.set(Calendar.DAY_OF_YEAR, (calendar.get(Calendar.DAY_OF_YEAR) + 1));
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 1);
                long alarm = calendar.getTimeInMillis();
                Intent alarmIntent = new Intent(context, SetAlarms.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.set(AlarmManager.RTC_WAKEUP, alarm,pendingIntent);
        }

        private boolean isTodayCycleAlarmDay() {
                GregorianCalendar calendar = new GregorianCalendar();
                // 0 as default because this method will be called only if the alarm is activated
                int previousDays = settings.getInt("cycle_alarm_prev_days", 0);
                //Add the previous days so we have only to check if this day is pillDay and yesterday was rest day to know we have 
                // to fire the alarm
                calendar.set(GregorianCalendar.DAY_OF_YEAR, (calendar.get(GregorianCalendar.DAY_OF_YEAR) + previousDays));
                int representingDay = pillDayInfo.getPillType(calendar, 
                                calendar.get(Calendar.DAY_OF_YEAR),calendar.get(Calendar.YEAR));
                
                calendar.set(GregorianCalendar.DAY_OF_YEAR, (calendar.get(GregorianCalendar.DAY_OF_YEAR) - 1));
                int dayBefore = pillDayInfo.getPillType(calendar, 
                                calendar.get(Calendar.DAY_OF_YEAR),calendar.get(Calendar.YEAR));
                if (representingDay == PillDayInfo.PILL_PENDING && ((dayBefore == PillDayInfo.PILL_REST_DAY) || (dayBefore == PillDayInfo.PILL_PLACEBO)))
                        return true;
                
                return false;
        }

        /**
         * From the actual time it retrieves the preference set time and set its to the calendar
         * (that will be representing today) and returns the time in millis
         * 
         * @return the time the diary alarm have to ring in milliseconds
         */
        private long getDiaryAlarmTime() {
                String time = settings.getString("diary_alarm_set_time", "");
                return getAlarmTime(time);
        }
        
        private long getCycleAlarmTime() {
                String time = settings.getString("cycle_alarm_set_time", "");
                return getAlarmTime(time);
        }
        
        private long getAlarmTime(String time) {
        		//Gets the time and hour from the time stored
                if (time.contentEquals(""))
                        return -1;
                String[] pieces = time.split(":");
                int hour = Integer.parseInt(pieces[0]);
                int minute = Integer.parseInt(pieces[1]);
                //Check that the time has not passed, if so, return -1 (cancel)
                GregorianCalendar date = new GregorianCalendar();
                long actualTimeInMillis = date.getTimeInMillis();
                date.set(Calendar.HOUR_OF_DAY, hour);
                date.set(Calendar.MINUTE, minute);
                long alarmTimeInMillis = date.getTimeInMillis();
                if (alarmTimeInMillis >= actualTimeInMillis)
                	return alarmTimeInMillis;
                return -1;
        }
}