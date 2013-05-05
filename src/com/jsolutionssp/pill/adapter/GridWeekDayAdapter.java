package com.jsolutionssp.pill.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jsolutionssp.pill.R;
import com.jsolutionssp.pill.gui.WeekDayCell;

/**
 * GridWeekDayAdapter, this class extends BaseAdapter, and it manages the interaction between the grid in the 
 * calendar view for the names of the week days and the elements inside it
 * 
 * @author Joaquin Fernandez Moreno
 *
 */
public class GridWeekDayAdapter extends BaseAdapter {
	
	Context context;

	private SharedPreferences settings;

	//A string array with the names of the week days
	private String[] weekDays;
	
	//There are ALWAYS seven days in a week
	private final int weekDaysNumber = 7;

	/**
	 * constructor that initializes the week names
	 * @param context the context of the activity that creates this adapter
	 */
	public GridWeekDayAdapter(Context context)	{
		this.context = context;
		settings = PreferenceManager.getDefaultSharedPreferences(context);
		//THe short names of the days
		weekDays = context.getResources().getStringArray(R.array.weekdays_short);
		//By default the first day of the week is monday if not, we change it
		if (!settings.getString("start_week_day", "Monday").equalsIgnoreCase("monday")) {
			String firstDay = weekDays[weekDaysNumber - 1];
			//For all the days
			for (int i = weekDaysNumber - 1; i >= 0; i--) {
				//if its the first day (monday) change for the last (sunday) 
				if (i == 0)
					weekDays[i] = firstDay;
				//else, each day has to pass to the next one
				else
					weekDays[i] = weekDays[i - 1];
			}
		}
	}

	@Override
	/**
	 * This returns the number of elements in the grid
	 */
	public int getCount() {
		return weekDaysNumber;
	}
	
	@Override
	/**
	 * This overrided method creates WeekDayCEll objects to populate the grid
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView button;
		if (convertView == null) {  // if it's not recycled, initialize some attributes
			button = new WeekDayCell(context, weekDays[position]);
		} else {
			button = (TextView) convertView;
		}
		return button;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
}
