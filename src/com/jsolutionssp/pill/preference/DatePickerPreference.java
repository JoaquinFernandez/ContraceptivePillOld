package com.jsolutionssp.pill.preference;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;

/**
 * Custom preference that lets the user pick a date
 * @author Joaquin Fernandez Moreno
 *
 */
public class DatePickerPreference extends DialogPreference implements  
OnDateChangedListener, OnDateSetListener {

	//The date
	private long mDate;
	//The different formats that the date can be returned
	public static final int DATE_SHORT = DateFormat.SHORT;
	public static final int DATE_MEDIUM = DateFormat.MEDIUM;
	public static final int DATE_LONG = DateFormat.LONG;
	public static final int DATE_FULL = DateFormat.FULL;
	public static final int DATE_DEFAULT = DateFormat.DEFAULT;
	
	/**
	 * Constructor of the preference
	 * @param context the context of the activity that creates this preference
	 * @param attrs options
	 */
	public DatePickerPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		mDate = getPersistedLong(System.currentTimeMillis());
	}

	/**
	 *  Constructor of the preference
	 * @param context the context of the activity that creates this preference
	 * @param attrs options
	 * @param defStyle style of this preference
	 */
	public DatePickerPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mDate = getPersistedLong(System.currentTimeMillis());
	}
	
	@Override
	 /**
	  * Creates the dialog for this preference, setting the day that is first displayed to
	  * the actual day
	  */
	protected View onCreateDialogView() {
		DatePicker picker = new DatePicker(getContext());

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(mDate);

		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int month = calendar.get(Calendar.MONTH);
		int year = calendar.get(Calendar.YEAR);

		picker.init(year, month, day, this);
		return picker;
	}

	/**
	 * if the date changes, the field mDate is updated with the new info
	 */
	public void onDateChanged(DatePicker view, int year, int monthOfYear,  
			int dayOfMonth) {
		mDate = (new Date(year - 1900, monthOfYear, dayOfMonth)).getTime();
	}

	/**
	 * When the date is set this method calls onDateChanged()
	 */
	public void onDateSet(DatePicker view, int year, int monthOfYear,  
			int dayOfMonth) {
		onDateChanged(view, year, monthOfYear, dayOfMonth);
	}

	@Override
	/**
	 * Default value is actual time
	 */
	public void setDefaultValue(Object defaultValue) {
		super.setDefaultValue(String.valueOf((  
				new Date(String.valueOf(defaultValue))).getTime()));
	}

	@Override
	/**
	 * When the dialog closes, the field mDate is stored in preferences
	 */
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);

		if(positiveResult) {
			if(isPersistent()) {
				persistLong(mDate);
			}
			callChangeListener(String.valueOf(mDate));
		}
	}
	
	/**
	 * This method sets the date of this time picker to the one given as parameter
	 * @param date the new date we want this preference set to
	 */
	public void setDate(Date date) { 
		mDate = date.getTime(); 
	}
	
	/**
	 * Sets the date of this preference to the given
	 * @param milisseconds time we want this preference set to in milisseconds
	 */
	public void setDate(long milisseconds) { 
		mDate = milisseconds; 
	}

	/**
	 * Getter, it returns the date of this preferences
	 * @return
	 */
	public long getDate() { 
		long persistedTime = getPersistedLong(System.currentTimeMillis());
		return persistedTime; 
	}
}
