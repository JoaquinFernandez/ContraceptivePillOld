package com.jsolutionssp.pill.preference;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;


public class PreferencesTabListenerV7 implements TabListener {

	private final SherlockFragmentActivity mActivity;
	
	public PreferencesTabListenerV7(SherlockFragmentActivity contraceptivePill) {
		mActivity = contraceptivePill;
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ignoredFt) {
		mActivity.startActivity(new Intent(mActivity.getApplicationContext(), PreferencesActivityV7.class));
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {	
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
}