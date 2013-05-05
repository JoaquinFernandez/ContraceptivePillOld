package com.jsolutionssp.pill.widget;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.jsolutionssp.pill.ContraceptivePill;
import com.jsolutionssp.pill.PillLogics;
import com.jsolutionssp.pill.R;

public class WidgetProvider extends AppWidgetProvider {

	public static final String DEBUG_TAG = "TutWidgetProvider";

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		GregorianCalendar date = new GregorianCalendar();
		SharedPreferences settings = context.getSharedPreferences(ContraceptivePill.PREFS_NAME, 0);
		//Update widget info
		boolean isPillDay = PillLogics.isPillDay(settings, 
				date.get(Calendar.DAY_OF_YEAR), date.get(Calendar.YEAR));
		String daysLeft = PillLogics.DaysLeft(settings);

		try {
			updateWidgetContent(context, appWidgetManager, isPillDay, daysLeft);
		} catch (Exception e) {
		}
	}

	public static void updateWidgetContent(Context context,
			AppWidgetManager appWidgetManager, boolean isPillDay, String daysLeft) {

		RemoteViews remoteView = new RemoteViews(context.getPackageName(),
				R.layout.appwidget_layout);

		Intent launchAppIntent = new Intent(context, ContraceptivePill.class);
		PendingIntent launchAppPendingIntent = PendingIntent.getActivity(context,
				0, launchAppIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		remoteView.setOnClickPendingIntent(R.id.full_widget, launchAppPendingIntent);
		if (isPillDay) {
			remoteView.setImageViewResource(R.id.logo_widget, R.drawable.pill_icon);
			remoteView.setTextViewText(R.id.text_widget, context.getResources().getString(R.string.pill_name));
		}
		else {
			remoteView.setImageViewResource(R.id.logo_widget, R.drawable.pill_rest_day);
			remoteView.setTextViewText(R.id.text_widget, context.getResources().getString(R.string.rest_name));
		}
		remoteView.setTextViewText(R.id.days_left_widget, daysLeft);
		ComponentName listWidget = new ComponentName(context,
				WidgetProvider.class);
		appWidgetManager.updateAppWidget(listWidget, remoteView);
	}
}
