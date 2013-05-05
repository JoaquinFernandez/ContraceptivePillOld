package com.jsolutionssp.pill.adapter;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.jsolutionssp.pill.ContraceptivePill;
import com.jsolutionssp.pill.gui.CalendarCell;

/**
 * GridCalendarAdapter, this class extends BaseAdapter, and it manages the interaction between the grid in the
 * calendar view for the days drawing and the elements inside it
 * 
 * @author Joaquin Fernandez Moreno
 *
 */
public class GridCalendarAdapter extends BaseAdapter {

	Context context;

	//numbers of element on the grid
	private int count;

	private SharedPreferences settings;

	//It is the first day of the month (usually the latests of the previous month) that we're showing on the screen
	private int firstRepresentingDay;

	//The year of the month we're showing on the screen
	private int year;

	//The month we're showing on the screen
	private int month;

	/**
	 * The constructor, it initializes the parameters
	 * @param context the context of the activity we're calling from
	 * @param month The month we're showing on the screen
	 * @param year The year of the month we're showing on the screen
	 */
	public GridCalendarAdapter(Context context, int month, int year)	{
		this.context = context;
		settings = PreferenceManager.getDefaultSharedPreferences(context);
		this.year = year;
		this.month = month;

		firstRepresentingDay = getFirstRepresentingDay();
	}

	/**
	 * This method gets the first day we're representing in the screen
	 * @return the first day we're showing in the screen
	 */
	private int getFirstRepresentingDay() {
		int firstRepresentingDay;
		//Initializes a Gregorian calendar with the day set to the first
		GregorianCalendar myCalendar = new GregorianCalendar(year, month, 1);

		//How many days have this month
		int daysofMonth = myCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		// First day of month (relative to the week)
		int firstDayMonth = myCalendar.get(Calendar.DAY_OF_WEEK); 
		int globalFirstDayMonth = myCalendar.get(Calendar.DAY_OF_YEAR);

		//The default option is the week starting on monday
		if (settings.getString("start_week_day", "Monday").equalsIgnoreCase("monday")) { 
			firstDayMonth = firstDayMonth - 1;
			if (firstDayMonth == 0)
				firstDayMonth = 7;
			firstRepresentingDay = globalFirstDayMonth - (firstDayMonth - 1);
		}
		else { //else we start the week on Sunday
			firstRepresentingDay = globalFirstDayMonth - (firstDayMonth - 1);
		}
		//If we've too many days we will need six rows instead of 5
		if (firstDayMonth + daysofMonth < 37)
			count = ContraceptivePill.five_week_calendar;
		else
			count = ContraceptivePill.six_week_calendar;

		return firstRepresentingDay;
	}

	@Override
	/**
	 * 
	 *	@return number of elements you want on the grid
	 */
	public int getCount() {
		return count;
	}

	@Override
	/**
	 * This overrided method creates CalendarCell Objects to populate the grid
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
			return new CalendarCell(context, position, firstRepresentingDay, year, month);
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}
}
