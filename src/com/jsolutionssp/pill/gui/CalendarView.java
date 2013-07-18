
package com.jsolutionssp.pill.gui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jsolutionssp.pill.R;
import com.jsolutionssp.pill.adapter.GridCalendarAdapter;
import com.jsolutionssp.pill.adapter.GridWeekDayAdapter;

/**
 * Calendar view, this linear layout holds the calendar
 * @author Joaquin Fernandez Moreno
 *
 */
public class CalendarView extends LinearLayout {

	private Context context;

	/** String array with the months */
	private final String[] months;

	/** The layout child of this one */
	private LinearLayout calendarLayout;

	/** Month that we're showing in the screen */
	private int month;
	
	/** Year that we're showing in the screen */
	private int year;

	ImageView nextMonth;
	
	ImageView prevMonth;
	
	/**
	 * Constructor, it initializes this layout information
	 * @param context the context of the activity that created this layout
	 * @param attrs attributes
	 */
	public CalendarView(final Context context, int month, int year) {
		super(context);
		//Inflate the layout
		LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		calendarLayout = (LinearLayout) layoutInflater.inflate(R.layout.calendar, this);
		//Retrieve month ImageViews
		nextMonth = (ImageView) calendarLayout.findViewById(R.id.nextMonth);
		prevMonth  = (ImageView) calendarLayout.findViewById(R.id.prevMonth);
		nextMonth.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Toast.makeText(context, R.string.calendar_month_arrow_text, Toast.LENGTH_LONG).show();
			}
		});
		//Initialize parameters
		this.context = context;
		months = getResources().getStringArray(R.array.months);
		this.month = month;
		this.year = year;
		//Auxiliary functions
		fillGrid();
		setMonth();
	}

	/**
	 * Auxiliary method that sets the text of the layout button to display which month we're representing
	 * on the screen
	 */
	private void setMonth() {
		String monthString = months[month];
		Button currentMonth = (Button) calendarLayout.findViewById(R.id.currentMonth);
		currentMonth.setText(monthString + " " + year);
	}

	/**
	 * Auxiliary method that fill the grids (the week days and the days of the month)
	 */
	public void fillGrid() {
		GridView calendarGrid = (GridView) calendarLayout.findViewById(R.id.grid_calendar);
		calendarGrid.removeAllViewsInLayout();
		calendarGrid.setAdapter(new GridCalendarAdapter(context, month, year));
		GridView calendarWeekDayGrid = (GridView) calendarLayout.findViewById(R.id.grid_days);
		calendarWeekDayGrid.setAdapter(new GridWeekDayAdapter(context));
	}
}
