package com.jsolutionssp.pill.preference;

import java.text.DateFormat;
import java.util.Date;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;

import com.jsolutionssp.pill.R;
import com.jsolutionssp.pill.db.DayStorageDB;

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
	}

	private void preferenceCallbacks(String key) {
		if (key.equalsIgnoreCase("pill_type")) {
			DayStorageDB db = new DayStorageDB(this.getActivity());
			db.invalidateDatabase();
		}
		if (key.equalsIgnoreCase("start_pack_date")) {
			DayStorageDB db = new DayStorageDB(this.getActivity());
			db.invalidateDatabase();
		}
		if (key.indexOf("cycle_alarm") != -1) {
			
		}
		if (key.indexOf("diary_alarm") != -1) {
			
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
}