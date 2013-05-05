
package com.jsolutionssp.pill.gui;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jsolutionssp.pill.ContraceptivePill;
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
	
	/**
	 * Constructor, it initializes this layout information
	 * @param context the context of the activity that created this layout
	 * @param attrs attributes
	 */
	public CalendarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		//Inflate the layout
		LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		calendarLayout = (LinearLayout) layoutInflater.inflate(R.layout.calendar, this);
		//Initialize parameters
		this.context = context;
		months = getResources().getStringArray(R.array.months);
		GregorianCalendar calendar = new GregorianCalendar();
		month = calendar.get(Calendar.MONTH);
		year = calendar.get(Calendar.YEAR);
		//Auxiliary functions
		fillGrid();
		setMonth();
		setListeners();
	}
	
	/**
	 * Auxiliary function that sets the listeners for the next month and previous month buttons 
	 * checking if it is the first month (0) or the last month of the year (11) and if so, updating
	 * the year as well as the month
	 */
	private void setListeners() {
		final ImageView nextMonth = (ImageView) calendarLayout.findViewById(R.id.nextMonth);
		final ImageView prevMonth = (ImageView) calendarLayout.findViewById(R.id.prevMonth);
		nextMonth.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				nextMonth.setAnimation(AnimationUtils.loadAnimation(context, R.anim.right_arrow));
				if (month == 11) {
					month = 0;
					year++;
				}
				else
					month++;
				fillGrid();
				setMonth();				
			}

		});
		prevMonth.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				prevMonth.setAnimation(AnimationUtils.loadAnimation(context, R.anim.left_arrow));
				if (month == 0) {
					month = ContraceptivePill.months;
					year--;
				}
				else
					month--;
				fillGrid();
				setMonth();				
			}
		});
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
