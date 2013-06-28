package com.jsolutionssp.pill.gui;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jsolutionssp.pill.R;

public class PreferenceTourDialog extends Dialog {

	public PreferenceTourDialog(final Context context) {
		super(context, R.style.NoTitleDialog);
		setContentView(R.layout.tour_dialog);
		TextView dialogText = (TextView) findViewById(R.id.info_dialog_text);
		dialogText.setText(R.string.preferences_instructions);
		Button button = (Button) findViewById(R.id.info_dialog_button);		
		button.setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) {
				SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
				settings.edit().putBoolean("first_time_used_preference", false).commit();
				dismiss();
			}
		});
		show();
	}

}
