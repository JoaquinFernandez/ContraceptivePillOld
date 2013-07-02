
package com.jsolutionssp.pill;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.analytics.tracking.android.EasyTracker;
import com.jsolutionssp.pill.calendar.CalendarFragmentV11;
import com.jsolutionssp.pill.calendar.CalendarFragmentV7;
import com.jsolutionssp.pill.calendar.CalendarTabListenerV11;
import com.jsolutionssp.pill.calendar.CalendarTabListenerV7;
import com.jsolutionssp.pill.gui.AboutDialog;
import com.jsolutionssp.pill.gui.CalendarTourDialog;
import com.jsolutionssp.pill.preference.PreferencesFragmentV11;
import com.jsolutionssp.pill.preference.PreferencesTabListenerV11;
import com.jsolutionssp.pill.preference.PreferencesTabListenerV7;
import com.jsolutionssp.pill.preference.TransferPreferences;

public class ContraceptivePill extends SherlockFragmentActivity {

	public static String PREFS_NAME = "com.jsolutionssp.pill";
	
	public static final int five_week_calendar = 35;

	public static final int six_week_calendar = 42;

	public static final int months = 11; //From 0 to 11

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Transfer old preferences if they have not been transfered yet, else run normally
		SharedPreferences oldPrefs = getSharedPreferences("com.jsolutionssp.pill", 0);
		if (oldPrefs.getInt("pillTakingDays", 0) != 0)
			TransferPreferences.TransferOldPreferences(this);
		//Check what version are we running under
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			//Get a reference of the action bar
			android.app.ActionBar actionBar = getActionBar();
			//Set the navigation mode to tabs
			actionBar.setNavigationMode(
					ActionBar.NAVIGATION_MODE_TABS);
	
			//Create tabs and add listeners
			android.app.ActionBar.Tab tab1 = actionBar.newTab().setText(R.string.calendar_tab_name);
			android.app.ActionBar.Tab tab2 = actionBar.newTab().setText(R.string.preferences_tab_name);
	
			tab1.setTabListener(new CalendarTabListenerV11<CalendarFragmentV11>(this, "CalendarFragmentV11", CalendarFragmentV11.class));
			tab2.setTabListener(new PreferencesTabListenerV11<PreferencesFragmentV11>(this, "PreferencesFragmentV11", PreferencesFragmentV11.class));
	
			//Add tabs to action bar
			actionBar.addTab(tab1);
			actionBar.addTab(tab2);
			if (isFirstPreferenceTime())
				tab2.select();
			else if (isFirstCalendarTime())
				new CalendarTourDialog(this);
		}
		else {
			//Get a reference of the action bar
			ActionBar actionBar = getSupportActionBar();
			//Set the navigation mode to tabs
			actionBar.setNavigationMode( 
					ActionBar.NAVIGATION_MODE_TABS);
	
			//Create tabs and add listeners
			ActionBar.Tab tab1 = actionBar.newTab().setText(R.string.calendar_tab_name);
			ActionBar.Tab tab2 = actionBar.newTab().setText(R.string.preferences_tab_name);
	
			tab1.setTabListener(new CalendarTabListenerV7<CalendarFragmentV7>(this, "CalendarFragmentV7", CalendarFragmentV7.class));
			tab2.setTabListener(new PreferencesTabListenerV7(this));
	
			//Add tabs to action bar
			actionBar.addTab(tab1);
			actionBar.addTab(tab2);
			if (isFirstPreferenceTime())
				tab2.select();
			else if (isFirstCalendarTime())
				new CalendarTourDialog(this);
		}
		setContentView(R.layout.main);
	}
	
	private boolean isFirstCalendarTime() {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		return settings.getBoolean("first_time_used_calendar", true);
	}

	/**
	 * Checks if it is the first time the application (or the new version) is running
	 * @return whether it is or not the first time
	 */
	private boolean isFirstPreferenceTime() {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		return settings.getBoolean("first_time_used_preference", true);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		EasyTracker.getInstance().activityStart(this); // Add this method.
	}

	@Override
	public void onStop() {
		super.onStop();
		EasyTracker.getInstance().activityStop(this); // Add this method.
	}

	@Override
	public void onPause() {
		super.onPause();
		finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.MnuOpt1:
			new AboutDialog(this);
			return true;
		case R.id.MnuOpt2:
			//Create instructions
			return true;
		case R.id.MnuOpt3:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}