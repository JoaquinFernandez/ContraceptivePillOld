package com.jsolutionssp.pill.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.SherlockFragment;
import com.jsolutionssp.pill.R;
import com.jsolutionssp.pill.gui.CalendarView;

public class CalendarFragment extends SherlockFragment {	
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		//Retrieve arguments from bundle
		int month = getArguments().getInt("month", -1);//Theoretically -1 should never happen
		int year = getArguments().getInt("year", -1);
		//Create calendar view and add it to the layout
		LinearLayout calendarLayout = (LinearLayout) inflater.inflate(R.layout.calendar_fragment, container, false);
		calendarLayout.addView(new CalendarView(getActivity(), month, year));
		return calendarLayout;
	}
}