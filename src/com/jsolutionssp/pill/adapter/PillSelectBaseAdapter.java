package com.jsolutionssp.pill.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jsolutionssp.pill.PillDayInfo;
import com.jsolutionssp.pill.R;

public class PillSelectBaseAdapter extends BaseAdapter {

	private Context context;

	private Dialog changePillType;

	private RelativeLayout relativeLayout; 

	public PillSelectBaseAdapter(Context context, RelativeLayout relativeLayout, Dialog changePillType) {
		this.context = context;
		this.relativeLayout = relativeLayout;
		this.changePillType = changePillType;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return PillDayInfo.PILL_TYPES.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			TextView text = (TextView) inflater.inflate(R.layout.select_pill_type_element, null);
			final String[] pillStates = context.getResources().getStringArray(R.array.pill_state_types);
			text.setText(pillStates[position]);
			Drawable elementImage = context.getResources().getDrawable(PillDayInfo.IMAGES[position]);
			text.setCompoundDrawablesWithIntrinsicBounds(elementImage, null, null, null);
			text.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					ImageView imageView = (ImageView) relativeLayout.findViewById(R.id.day_touched_pill_image);
					imageView.setBackgroundResource(PillDayInfo.IMAGES[position]);
					TextView pillTextView = (TextView) relativeLayout.findViewById(R.id.day_touched_pill_text);
					pillTextView.setText(pillStates[position]);
					changePillType.dismiss();
					return true;
				}
				
			});
			return text;
		}
		return convertView;
	}
}
