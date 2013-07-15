package com.jsolutionssp.pill.preference;

import java.text.DateFormat;
import java.util.Date;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.jsolutionssp.pill.R;
import com.jsolutionssp.pill.db.DayStorageDB;
import com.jsolutionssp.pill.service.SetAlarms;

public class PreferencesFragmentV11 extends PreferenceFragment implements OnSharedPreferenceChangeListener {

	SharedPreferences sp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		sp = getPreferenceScreen().getSharedPreferences();
		//Set the summary for the preferences
		for(int i = 0; i < getPreferenceScreen().getPreferenceCount(); i++){
			checkPreference(getPreferenceScreen().getPreference(i));
		}
		if (isFirstTime())
			preferenceTour(0);
	}

	private void preferenceTour(int intructions) {
		int id;
		String title;
		switch (intructions) {
		case 0 :
			title = getResources().getString(R.string.contraceptive_pill_type_preferences);
			id = R.string.preferences_instructions_first;
			break;
		case 1 :
			title = getResources().getString(R.string.start_pack_date_preferences);
			id = R.string.preferences_instructions_second;
			break;
		case 2 :
			title = getResources().getString(R.string.week_start_day_preferences);
			id = R.string.preferences_instructions_third;
			break;
		default :
			title = getResources().getString(R.string.preferences_alarms_separator);
			id = R.string.preferences_instructions_fourth;
			PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).edit()
			.putBoolean("first_time_used_preference", false).commit();
			break;
		}
		String text = getResources().getString(id);
		new AlertDialog.Builder(getActivity()).setTitle(title).setMessage(text).setIcon(R.drawable.ic_menu_info_details).setNeutralButton("Close", null).show();
	}

	@Override
	public void onResume() {
		super.onResume();
		sp.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		sp.unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		preferenceCallbacks(key);
		Preference pref = findPreference(key);
		checkPreference(pref);
		Context context = getActivity().getApplicationContext();
		Intent i = new Intent(context, SetAlarms.class);
		i.setAction("com.jsolutionssp.pill.updateAlarm");
		context.sendBroadcast(i);
	}

	private void preferenceCallbacks(String key) {
		if (key.equalsIgnoreCase("pill_type")) {
			DayStorageDB db = new DayStorageDB(this.getActivity());
			db.invalidateDatabase();
			if (isFirstTime())
				preferenceTour(1);
		}
		if (key.equalsIgnoreCase("start_pack_date")) {
			DayStorageDB db = new DayStorageDB(this.getActivity());
			db.invalidateDatabase();
			if (isFirstTime())
				preferenceTour(2);
		}
		if (key.equalsIgnoreCase("start_week_day")) {
			if (isFirstTime())
				preferenceTour(3);
		}
	}

	private void checkPreference(Preference pref) {
		if (pref instanceof PreferenceCategory) {
			PreferenceCategory prefCat = (PreferenceCategory) pref;
			for(int i = 0; i < prefCat.getPreferenceCount(); i++){
				setSummary(prefCat.getPreference(i));
			}
		}
		else 
			setSummary(pref);
	}

	private void setSummary(Preference pref) {
		if (pref instanceof ListPreference) {
			ListPreference listPref = (ListPreference) pref;
			pref.setSummary(listPref.getEntry());
		}
		if (pref instanceof NumberPickerPreference) {
			NumberPickerPreference numPref = (NumberPickerPreference) pref;
			int savedValue = numPref.getValue();
			if (savedValue != -1)
				pref.setSummary(String.valueOf(savedValue));
		}
		if (pref instanceof DatePickerPreference) {
			DatePickerPreference datePref = (DatePickerPreference) pref;
			long dateInMillis = datePref.getDate();
			if (dateInMillis != 0) {
				String date = DateFormat.getDateInstance(DateFormat.MEDIUM).format(new Date(dateInMillis));
				pref.setSummary(date);
			}
		}
		if (pref instanceof TimePickerPreference) {
			TimePickerPreference timePref = (TimePickerPreference) pref;
			String time = timePref.getTime();
			if (time != "-1")
				pref.setSummary(timePref.getTime());
		}
	}
	
	/**
	 * Checks if it is the first time the application (or the new version) is running
	 * @return whether it is or not the first time
	 */
	private boolean isFirstTime() {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
		return settings.getBoolean("first_time_used_preference", true);
	}
}