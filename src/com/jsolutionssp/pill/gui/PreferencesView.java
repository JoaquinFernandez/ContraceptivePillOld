package com.jsolutionssp.pill.gui;

import android.content.Context;
import android.widget.LinearLayout;

public class PreferencesView extends LinearLayout {

	public PreferencesView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/*private Context context;

	private LinearLayout preferencesLayout;

	String[] textIni;

	private SharedPreferences settings;

	private Editor editor;

	public PreferencesView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout firstLayout = (LinearLayout) layoutInflater.inflate(R.layout.preferences, this);
		ScrollView scroll = (ScrollView) firstLayout.getChildAt(0);
		preferencesLayout = (LinearLayout) scroll.getChildAt(0);
		this.context = context;
		editor = settings.edit();
		textIni = getResources().getStringArray(R.array.preferences_categories);
	}

	private void setCycleAlarmHour() {
		LinearLayout globalLayout = (LinearLayout) preferencesLayout.findViewById(R.id.preferences_cycle_alarm_time);
		if (settings.getInt("cycleAlarm", 0) == 0)
			globalLayout.setVisibility(GONE);
		else {
			globalLayout.setVisibility(VISIBLE);
			LinearLayout textLayout = (LinearLayout) globalLayout.getChildAt(0);
			TextView title = (TextView) textLayout.getChildAt(0);
			title.setText(textIni[9]);
			TextView info = (TextView) textLayout.getChildAt(1);
			int hour = settings.getInt("cycleHourNotification", -1);
			int minute = settings.getInt("cycleMinuteNotification", -1);
			String hourString = Integer.toString(hour);
			if (hourString.length() == 1) {
				hourString = "0" + hourString;
			}
			String minuteString = Integer.toString(minute);
			if (minuteString.length() == 1) {
				minuteString = "0" + minuteString;
			}
			if (hour != -1 && minute != -1)
				info.setText(" " + hourString + ":" + minuteString + " ");

			globalLayout.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					final Dialog dialog = new Dialog(context, R.style.CenteredDialog);
					LinearLayout lay1 = (LinearLayout) ((LinearLayout) arg0).getChildAt(0);
					final TextView myInfo = (TextView) lay1.getChildAt(1);
					dialog.setContentView(R.layout.dialog_time_select);
					dialog.setTitle(R.string.select_alarm_hour_dialog_title);

					final TimePicker tp = (TimePicker) dialog.findViewById(R.id.time_select_picker);
					tp.setIs24HourView(true);
					Button button = (Button) dialog.findViewById(R.id.time_select_dialog_button);
					button.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							tp.getCurrentHour();
							int hour = tp.getCurrentHour();
							editor.putInt("cycleHourNotification", hour);
							int minute = tp.getCurrentMinute();
							editor.putInt("cycleMinuteNotification", minute);
							editor.commit();
							String hourString = Integer.toString(hour);
							if (hourString.length() == 1) {
								hourString = "0" + hourString;
							}
							String minuteString = Integer.toString(minute);
							if (minuteString.length() == 1) {
								minuteString = "0" + minuteString;
							}
							myInfo.setText(" " + hourString + ":" + minuteString + " ");
							dialog.dismiss();
							launchAlarm();
						}
					});
					dialog.show();
				}
			});
		}
	}
/*
	private void setCycleAlarm() {
		LinearLayout globalLayout = (LinearLayout) preferencesLayout.findViewById(R.id.preferences_cycle_alarm);
		LinearLayout textLayout = (LinearLayout) globalLayout.getChildAt(0);
		TextView title = (TextView) textLayout.getChildAt(0);
		title.setText(textIni[7]);
		TextView info = (TextView) textLayout.getChildAt(1);
		int type = settings.getInt("cycleAlarm", 0);
		final String[] alarms = getResources().getStringArray(R.array.yes_no_array);
		info.setText(alarms[type]);

		globalLayout.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				final Dialog dialog = new Dialog(context,R.style.NoTitleDialog);
				LinearLayout lay1 = (LinearLayout) ((LinearLayout) arg0).getChildAt(0);
				final TextView myInfo = (TextView) lay1.getChildAt(1);
				dialog.setContentView(R.layout.dialog_select);
				TextView text = (TextView) dialog.findViewById(R.id.select_dialog_text);
				text.setText(R.string.select_cyclealarm_dialog_title);

				ListView lv = (ListView) dialog.findViewById(R.id.initial_dialog_listview);
				CycleAlarmAdapter arrayAdapter = 
						new CycleAlarmAdapter(context, R.layout.item_initial_listview, alarms);
				lv.setAdapter(arrayAdapter);
				Button button = (Button) dialog.findViewById(R.id.initial_dialog_button);
				button.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						int type = settings.getInt("cycleAlarm", 0);
						paint();
						myInfo.setText(alarms[type]);
						dialog.dismiss();
						if (type == 1)
							launchAlarm();
					}
				});
				dialog.show();
			}
		});
	}

	private void setDiaryAlarmHour() {
		LinearLayout globalLayout = (LinearLayout) preferencesLayout.findViewById(R.id.preferences_diary_alarm_time);
		if (settings.getInt("diaryAlarm", 0) == 0)
			globalLayout.setVisibility(GONE);
		else {
			globalLayout.setVisibility(VISIBLE);
			LinearLayout textLayout = (LinearLayout) globalLayout.getChildAt(0);
			TextView title = (TextView) textLayout.getChildAt(0);
			title.setText(textIni[4]);
			TextView info = (TextView) textLayout.getChildAt(1);
			int hour = settings.getInt("diaryHourNotification", -1);
			int minute = settings.getInt("diaryMinuteNotification", -1);
			String hourString = Integer.toString(hour);
			if (hourString.length() == 1) {
				hourString = "0" + hourString;
			}
			String minuteString = Integer.toString(minute);
			if (minuteString.length() == 1) {
				minuteString = "0" + minuteString;
			}
			if (hour != -1 && minute != -1)
				info.setText(" " + hourString + ":" + minuteString + " ");

			globalLayout.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					final Dialog dialog = new Dialog(context, R.style.CenteredDialog);
					LinearLayout lay1 = (LinearLayout) ((LinearLayout) arg0).getChildAt(0);
					final TextView myInfo = (TextView) lay1.getChildAt(1);
					dialog.setContentView(R.layout.dialog_time_select);
					dialog.setTitle(R.string.select_alarm_hour_dialog_title);

					final TimePicker tp = (TimePicker) dialog.findViewById(R.id.time_select_picker);
					tp.setIs24HourView(true);
					Button button = (Button) dialog.findViewById(R.id.time_select_dialog_button);
					button.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							tp.getCurrentHour();
							int hour = tp.getCurrentHour();
							editor.putInt("diaryHourNotification", hour);
							int minute = tp.getCurrentMinute();
							editor.putInt("diaryMinuteNotification", minute);
							editor.commit();
							String hourString = Integer.toString(hour);
							if (hourString.length() == 1) {
								hourString = "0" + hourString;
							}
							String minuteString = Integer.toString(minute);
							if (minuteString.length() == 1) {
								minuteString = "0" + minuteString;
							}
							myInfo.setText(" " + hourString + ":" + minuteString + " ");
							dialog.dismiss();
							launchAlarm();
						}
					});
					dialog.show();
				}
			});
		}
	}

	private void setDiaryAlarm() {
		LinearLayout globalLayout = (LinearLayout) preferencesLayout.findViewById(R.id.preferences_diary_alarm);
		LinearLayout textLayout = (LinearLayout) globalLayout.getChildAt(0);
		TextView title = (TextView) textLayout.getChildAt(0);
		title.setText(textIni[3]);
		TextView info = (TextView) textLayout.getChildAt(1);
		int type = settings.getInt("diaryAlarm", 0);
		final String[] alarms = getResources().getStringArray(R.array.yes_no_array);
		info.setText(alarms[type]);

		globalLayout.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				final Dialog dialog = new Dialog(context,R.style.NoTitleDialog);
				LinearLayout lay1 = (LinearLayout) ((LinearLayout) arg0).getChildAt(0);
				final TextView myInfo = (TextView) lay1.getChildAt(1);
				dialog.setContentView(R.layout.dialog_select);
				TextView text = (TextView) dialog.findViewById(R.id.select_dialog_text);
				text.setText(R.string.select_diaryalarm_dialog_title);

				ListView lv = (ListView) dialog.findViewById(R.id.initial_dialog_listview);
				DiaryAlarmAdapter arrayAdapter = 
						new DiaryAlarmAdapter(context, R.layout.item_initial_listview, alarms);
				lv.setAdapter(arrayAdapter);
				Button button = (Button) dialog.findViewById(R.id.initial_dialog_button);
				button.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						int type = settings.getInt("diaryAlarm", 0);
						paint();
						myInfo.setText(alarms[type]);
						dialog.dismiss();
						if (type == 1)
							launchAlarm();
					}
				});
				dialog.show();
			}
		});
	}

	private void setWeekStartDay() {
		LinearLayout globalLayout = (LinearLayout) preferencesLayout.findViewById(R.id.preferences_week_day);
		LinearLayout textLayout = (LinearLayout) globalLayout.getChildAt(0);
		TextView title = (TextView) textLayout.getChildAt(0);
		title.setText(textIni[1]);
		TextView info = (TextView) textLayout.getChildAt(1);
		boolean firstDay = settings.getBoolean("firstDayofWeek", true); //True = Monday, false,Sunday
		final String[] options = getResources().getStringArray(R.array.start_week_day);
		if (firstDay)
			info.setText(options[0]);
		else
			info.setText(options[1]);

		globalLayout.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				final Dialog dialog = new Dialog(context,R.style.NoTitleDialog);
				LinearLayout lay1 = (LinearLayout) ((LinearLayout) arg0).getChildAt(0);
				final TextView myInfo = (TextView) lay1.getChildAt(1);
				dialog.setContentView(R.layout.dialog_select);
				TextView text = (TextView) dialog.findViewById(R.id.select_dialog_text);
				text.setText(R.string.select_startday_dialog_title);

				ListView lv = (ListView) dialog.findViewById(R.id.initial_dialog_listview);
				WeekDayAdapter arrayAdapter = 
						new WeekDayAdapter(context, R.layout.item_initial_listview, options);
				lv.setAdapter(arrayAdapter);
				Button button = (Button) dialog.findViewById(R.id.initial_dialog_button);
				button.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						CalendarView cal = (CalendarView) ((Activity) context).findViewById(R.id.main_calendar);
						cal.fillGrid();
						if (settings.getBoolean("firstDayofWeek", true))
							myInfo.setText(options[0]);
						else
							myInfo.setText(options[1]);
						dialog.dismiss();
					}
				});
				dialog.show();
			}
		});
	}

	private void setPillType() {
		LinearLayout globalLayout = (LinearLayout) preferencesLayout.findViewById(R.id.preferences_pill_type);
		LinearLayout textLayout = (LinearLayout) globalLayout.getChildAt(0);
		TextView title = (TextView) textLayout.getChildAt(0);
		title.setText(textIni[0]);
		final TextView info = (TextView) textLayout.getChildAt(1);
		int pillTakingDays = settings.getInt("pillTakingDays", -1);
		int pillRestDays = settings.getInt("pillRestDays", -1);
		String text = getResources().getString(R.string.pill_name);
		text += " " + String.valueOf(pillTakingDays);
		if (pillRestDays != 0)
			text += "/" + String.valueOf(pillRestDays);
		info.setText(text);
		final String[] data = getResources().getStringArray(R.array.pill_types);
		globalLayout.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {

				final Dialog dialog = new Dialog(context,R.style.NoTitleDialog);
				dialog.setContentView(R.layout.dialog_select);
				TextView text = (TextView) dialog.findViewById(R.id.select_dialog_text);
				text.setText(R.string.select_pill_type_dialog_title);
				ListView lv = (ListView) dialog.findViewById(R.id.initial_dialog_listview);
				PillSelectAdapter arrayAdapter = 
						new PillSelectAdapter(context,R.layout.dialog_select, data);
				lv.setAdapter(arrayAdapter);
				Button button = (Button) dialog.findViewById(R.id.initial_dialog_button);
				button.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						int pillTakingDays = settings.getInt("pillTakingDays", -1);
						int pillRestDays = settings.getInt("pillRestDays", -1);
						String text = getResources().getString(R.string.pill_name);
						text += " " + String.valueOf(pillTakingDays);
						if (pillRestDays != 0)
							text += "/" + String.valueOf(pillRestDays);
						info.setText(text);
						CalendarView cal = (CalendarView) ((Activity) context).findViewById(R.id.main_calendar);
						cal.fillGrid();
						dialog.dismiss();
						launchAlarm();
					}
				});
				dialog.show();
			}
		});
	}
	private void launchAlarm() {
		Intent i = new Intent(context, SetAlarms.class);
		i.setAction("com.jsolutionssp.pill.updateAlarm");
		context.sendBroadcast(i);
	}*/
}
