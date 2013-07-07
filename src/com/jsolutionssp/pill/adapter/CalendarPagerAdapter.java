package com.jsolutionssp.pill.adapter;

import java.util.GregorianCalendar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.jsolutionssp.pill.ContraceptivePill;
import com.jsolutionssp.pill.calendar.CalendarFragment;

public class CalendarPagerAdapter extends FragmentStatePagerAdapter {

	final private int middleMonth;

	final private int middleYear;

	public final static int MAX_VIEWS = 100; //must be even

	final private static int MIDDLE_VIEW = MAX_VIEWS/2;

	public CalendarPagerAdapter(FragmentManager fm) {
		super(fm);
		GregorianCalendar cal = new GregorianCalendar();
		middleMonth = cal.get(GregorianCalendar.MONTH);
		middleYear = cal.get(GregorianCalendar.YEAR);
	}

	protected int[] setPreviousMonth(int difference, int month, int year) {
		if (month == 0) {
			month = ContraceptivePill.months;
			year--;
		}
		else
			month--;
		//Set aux variable
		int[] aux = {month, year};
		//Check if we're done
		if (difference != 1)
			aux = setPreviousMonth(--difference, month, year);
		//return the month and year
		return aux;


	}

	protected int[] setNextMonth(int difference, int month, int year) {
		if (month == 11) {
			month = 0;
			year++;
		}
		else
			month++;
		//Set aux variable
		int[] aux = {month, year};
		//Check if we're done
		if (difference != 1)
			return setNextMonth(--difference, month, year);
		//return the month and year
		return aux;
	}

	@Override
	public Fragment getItem(int position) {
		int month;
		int year;
		if (position > MIDDLE_VIEW) {
			int [] data = setNextMonth(position - MIDDLE_VIEW, middleMonth, middleYear);
			month = data[0];
			year = data[1];
		}
		else if (position < MIDDLE_VIEW) {
			int [] data = setPreviousMonth(MIDDLE_VIEW - position, middleMonth, middleYear);
			month = data[0];
			year = data[1];
		}
		else {
			month = middleMonth;
			year = middleYear;
		}
		//Add arguments
		Fragment fragment = new CalendarFragment();
		Bundle args = new Bundle();
		args.putInt("month", month);
		args.putInt("year", year);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public int getCount() {
		return MAX_VIEWS;
	}
}
