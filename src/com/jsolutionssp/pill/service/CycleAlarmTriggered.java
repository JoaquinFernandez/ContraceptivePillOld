package com.jsolutionssp.pill.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.jsolutionssp.pill.ContraceptivePill;
import com.jsolutionssp.pill.R;

public class CycleAlarmTriggered extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
	}
    /*private Context context;
	private SharedPreferences settings;

	@Override
    public void onReceive(Context context, Intent intent) {
    	this.context = context;
		settings = context.getSharedPreferences(ContraceptivePill.PREFS_NAME, 0);
		int cycle = settings.getInt("cycleAlarm", -1);
		if (cycle == 1) {
			String tickerText;
			int day = settings.getInt("prevAlarmDays", -1);
			if (day == -1)
				tickerText = null;
			else if (day == 0)
				tickerText = context.getResources().getText(R.string.notification_bar_cycle_text3).toString();
			else if (day == 1) {
				tickerText = context.getResources().getText(R.string.notification_bar_cycle_text4).toString();
			}
			else {
				tickerText = context.getResources().getText(R.string.notification_bar_cycle_text1).toString();
				tickerText += " " + day + " ";
				tickerText += context.getResources().getText(R.string.notification_bar_cycle_text2).toString();
			}
			boolean sound = false;
			if (settings.getInt("cycleAlarmSound", -1) == 1)
				sound = true;
			boolean vibrate = false;
			if (settings.getInt("cycleAlarmVibrate", -1) == 1)
				vibrate = true;
			
			if (tickerText != null)
				notificate(tickerText, sound, vibrate);
		}
    }
    
    private void notificate(String title, boolean sound, boolean vibrate) {

    	String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(ns);
		int icon = R.drawable.pill_icon;
		long when = System.currentTimeMillis();

		Notification notification = new Notification(icon, title, when);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		Intent notificationIntent = new Intent(context, ContraceptivePill.class);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

		notification.setLatestEventInfo(context, title, "", contentIntent);

		if (sound) {
			notification.defaults |= Notification.DEFAULT_SOUND;
			notification.flags |= Notification.FLAG_INSISTENT;
		}
		if (vibrate) {
			notification.defaults |= Notification.DEFAULT_VIBRATE;
			notification.vibrate = new long[] {0, 500, 300, 500, 300, 500, 300};
		}
		notification.ledARGB = 0xff00ff00;
		notification.ledOnMS = 600;
		notification.ledOffMS = 900;
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		
		final int HELLO_ID = R.id.preferences_cycle_alarm;
		mNotificationManager.notify(HELLO_ID, notification);
	}*/
}