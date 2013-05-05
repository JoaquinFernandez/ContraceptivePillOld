package com.jsolutionssp.pill.preference;
import com.jsolutionssp.pill.R;
import com.michaelnovakjr.numberpicker.NumberPicker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;

/**
 * Custom preference, that lets the user pick form a set of numbers
 * @author Joaquin Fernandez Moreno
 *
 */
public class NumberPickerPreference extends DialogPreference {

	NumberPicker picker;
	
	int initialValue;
	
	/**
	 * Constructor of numberPickerPreference it initializes the parameters
	 * @param context the context of the activity that creates this preference
	 * @param attrs options
	 */
	public NumberPickerPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		setDialogLayoutResource(R.layout.number_picker_preference);
	}
	
	@Override
	/**
	 * When this preference is binded to the dialog, this method sets the range of numbers
	 * the user is going to be able to select from, and sets the value to the previous one if
	 * existing
	 */
	protected void onBindDialogView(View view) {
		super.onBindDialogView(view);
		this.picker = (NumberPicker) view.findViewById(R.id.pref_num_picker);
		picker.setRange(0, 7);
		if ( this.initialValue != -1 ) 
			picker.setCurrent(initialValue);
	}
	@Override
	/**
	 * When clicked, the number is persisted
	 */
	public void onClick(DialogInterface dialog, int which) {
		super.onClick(dialog, which);
		if ( which == DialogInterface.BUTTON_POSITIVE ) {
			this.initialValue = picker.getCurrent();
			persistInt( initialValue );
			callChangeListener( initialValue );
		}
	}
	
	/**
	 * Getter
	 * @return the number this preference is set to (or -1 if none)
	 */
	public Integer getValue() {
		return getPersistedInt(-1);
	}
	
	@Override
	/**
	 * Method called when the preference needs to set the initial value, it looks if there  is
	 * any value stored and if not sets the default one
	 */
	protected void onSetInitialValue(boolean restorePersistedValue,
			Object defaultValue) {
		int def = ( defaultValue instanceof Number ) ? (Integer)defaultValue
				: ( defaultValue != null ) ? Integer.parseInt(defaultValue.toString()) : 1;
				if ( restorePersistedValue ) {
					this.initialValue = getPersistedInt(def);
				}
				else this.initialValue = (Integer)defaultValue;
	}
	
	@Override
	/**
	 * When getting the default value, returns the value or 1 as default
	 */
	protected Object onGetDefaultValue(TypedArray a, int index) {
		return a.getInt(index, 1);
	}
}