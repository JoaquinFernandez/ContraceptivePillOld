package com.jsolutionssp.pill.preference;

import java.text.DateFormat;
import java.util.Date;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;
import com.jsolutionssp.pill.ContraceptivePill;
import com.jsolutionssp.pill.R;
import com.jsolutionssp.pill.db.DayStorageDB;
import com.jsolutionssp.pill.gui.PreferenceTourDialog;
import com.jsolutionssp.pill.service.SetAlarms;

/**
 * 
 * @author Joaquin Fernandez Moreno
 *
 */
public class PreferencesActivityV7 extends SherlockPreferenceActivity implements OnSharedPreferenceChangeListener {

	SharedPreferences sp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		sp = getPreferenceScreen().getSharedPreferences();
		for(int i = 0; i < getPreferenceScreen().getPreferenceCount(); i++){
			checkPreference(getPreferenceScreen().getPreference(i));
		}
		//Support for going back in the preference activity on the action bar
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		if (isFirstTime())
			new PreferenceTourDialog(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		sp.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		sp.unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	/**
	 * Support for going back in the preference activity on the action bar
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			startActivity(new Intent(this.getApplicationContext(), ContraceptivePill.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		preferenceCallbacks(key);
		Preference pref = findPreference(key);
		checkPreference(pref);
	}

	private void preferenceCallbacks(String key) {
		if (key.equalsIgnoreCase("pill_type")) {
			DayStorageDB db = new DayStorageDB(this);
			db.invalidateDatabase();
		}
		if (key.equalsIgnoreCase("start_pack_date")) {
			DayStorageDB db = new DayStorageDB(this);
			db.invalidateDatabase();
		}
		//Just need to check if cycle_alarm or diary_alarm strings are in the key of the preference changed
		//because any preference relative to the same alarm start with the same prefix
		if (key.indexOf("cycle_alarm") != -1 || key.indexOf("diary_alarm") != -1) {
			Intent i = new Intent(this, SetAlarms.class);
			i.setAction("com.jsolutionssp.pill.updateAlarm");
			sendBroadcast(i);
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
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		return settings.getBoolean("first_time_used_preference", true);
	}
}