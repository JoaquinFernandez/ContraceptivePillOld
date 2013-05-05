package com.jsolutionssp.pill.gui;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

/**
 * Public class overrided for the week day cells, it extends text view and shows the name
 * of the day that this column is in
 * @author Joaquin Fernandez Moreno
 *
 */
public class WeekDayCell extends TextView {

	/**
	 * Constructor that takes the text we want to display and sets the
	 * displaying options
	 * @param context the context of the activity that creates this weekdaycell
	 * @param text the text we want to display in this textview
	 */
	public WeekDayCell(Context context, String text) {
		super(context);
		setTextColor(Color.WHITE);
		setText(text);
		setSingleLine();
		setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
	}
}

