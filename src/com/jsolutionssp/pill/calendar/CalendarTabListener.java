package com.jsolutionssp.pill.calendar;

import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.TabListener;


public class CalendarTabListener<T> implements TabListener {

	/* The following are each of the ActionBar.TabListener callbacks */
	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ignoredFt) {
	}
	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
		
	}
	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
		// User selected the already selected tab. Usually do nothing.
	}
}