package com.jsolutionssp.pill.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.jsolutionssp.pill.R;

public class CalendarFragmentV7 extends SherlockFragment {
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_calendar, container, false);
	}
}