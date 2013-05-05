
package com.jsolutionssp.pill;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.analytics.tracking.android.EasyTracker;
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
		}
		setContentView(R.layout.main);
		//initialize();
		//widgetUpdate();
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
			PackageManager manager = getApplicationContext().getPackageManager();
			PackageInfo info = null;
			try {
				info = manager.getPackageInfo(getPackageName(), 0);
			} catch (NameNotFoundException e) {
				Log.e("ContraceptivePill", "exception",  e);
			}
			String version;
			if (info == null) {
				version = null;
			}
			else
				version = info.versionName;

			String myVersion = getResources().getString(R.string.app_version);
			//get the TextView from the custom_toast layout

			final Dialog dialog = new Dialog(this,R.style.NoTitleDialog);
			dialog.setContentView(R.layout.about_dialog);

			TextView text = (TextView) dialog.findViewById(R.id.about_dialog_version);
			text.setText(myVersion + " " + version);

			Button button = (Button) dialog.findViewById(R.id.about_dialog_rate);
			button.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						Intent intent = new Intent(Intent.ACTION_VIEW); 
						intent.setData(Uri.parse("market://details?id=com.jsolutionssp.pill")); 
						startActivity(intent);
					} catch (ActivityNotFoundException e) {
						Intent intent = new Intent(Intent.ACTION_VIEW); 
						intent.setData(Uri.parse("http://play.google.com/store/apps/details?id=com.jsolutionssp.pill")); 
						startActivity(intent);
					}
					dialog.dismiss();
					finish();
				}
			});
			Button button2 = (Button) dialog.findViewById(R.id.about_dialog_ok);
			button2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			dialog.show();
			return true;
		case R.id.MnuOpt2:
			final Dialog intructionsDialog = new Dialog(ContraceptivePill.this, R.style.NoTitleDialog);
			intructionsDialog.setContentView(R.layout.instructions_dialog);
			TextView dialogText = (TextView) intructionsDialog.findViewById(R.id.info_dialog_text);
			dialogText.setText(R.string.instructions);
			Button button1 = (Button) intructionsDialog.findViewById(R.id.info_dialog_button);
			button1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					intructionsDialog.dismiss();
				}
			});
			intructionsDialog.show();
			return true;
		case R.id.MnuOpt3:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}