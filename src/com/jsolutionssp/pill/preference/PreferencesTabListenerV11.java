package com.jsolutionssp.pill.preference;

import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.jsolutionssp.pill.R;


public class PreferencesTabListenerV11<T extends Fragment> implements TabListener {

	private Fragment mFragment;
	private final SherlockFragmentActivity mActivity;
	private final String mTag;
	private final Class<T> mClass;
	
	public PreferencesTabListenerV11(SherlockFragmentActivity contraceptivePill,
			String tag, Class<T> clz) {
		mActivity = contraceptivePill;
		mTag = tag;
		mClass = clz;
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ignoredFt) {
		FragmentManager fragMgr = mActivity.getFragmentManager();
		FragmentTransaction ft = fragMgr.beginTransaction();

		// Check if the fragment is already initialized
		if (mFragment == null) {
			// If not, instantiate and add it to the activity
			mFragment = Fragment.instantiate(mActivity, mClass.getName());

			ft.add(R.id.mainLayout, mFragment, mTag);
		    ft.commit();
		} else {
			// If it exists, simply attach it in order to show it
			ft.attach(mFragment);
		    ft.commit();
		}
		
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		if (ft == null) {
			FragmentManager fragMgr = mActivity.getFragmentManager();
			ft = fragMgr.beginTransaction();
		}
		if (mFragment != null) {
			// Detach the fragment, because another one is being attached
			ft.detach(mFragment);
		}
		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
}