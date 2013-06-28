package com.jsolutionssp.pill.gui;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jsolutionssp.pill.R;

public class AboutDialog extends Dialog {

	public AboutDialog(final Context context) {
		super(context, R.style.NoTitleDialog);
		PackageManager manager = context.getPackageManager();
		PackageInfo info = null;
		try {
			info = manager.getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			Log.e("ContraceptivePill", "exception",  e);
		}
		String version;
		if (info == null) {
			version = null;
		}
		else
			version = info.versionName;

		String myVersion = context.getResources().getString(R.string.app_version);
		//get the TextView from the custom_toast layout
		setContentView(R.layout.about_dialog);

		TextView text = (TextView) findViewById(R.id.about_dialog_version);
		text.setText(myVersion + " " + version);

		Button button = (Button) findViewById(R.id.about_dialog_rate);
		button.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					Intent intent = new Intent(Intent.ACTION_VIEW); 
					intent.setData(Uri.parse("market://details?id=com.jsolutionssp.pill")); 
					context.startActivity(intent);
				} catch (ActivityNotFoundException e) {
					Intent intent = new Intent(Intent.ACTION_VIEW); 
					intent.setData(Uri.parse("http://play.google.com/store/apps/details?id=com.jsolutionssp.pill")); 
					context.startActivity(intent);
				}
				dismiss();
			}
		});
		Button button2 = (Button) findViewById(R.id.about_dialog_ok);
		button2.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		show();
	}

}