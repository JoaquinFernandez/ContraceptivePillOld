package com.jsolutionssp.pill.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jsolutionssp.pill.PillDayInfo;
import com.jsolutionssp.pill.R;

public class PillTypeShowBaseAdapter extends BaseAdapter {

	private Context context;

	public PillTypeShowBaseAdapter(Context context) {
		this.context = context;
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
			return text;
		}
		return convertView;
	}
}
