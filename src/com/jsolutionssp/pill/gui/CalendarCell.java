package com.jsolutionssp.pill.gui;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jsolutionssp.pill.PillDayInfo;
import com.jsolutionssp.pill.R;
import com.jsolutionssp.pill.adapter.PillSelectBaseAdapter;
import com.jsolutionssp.pill.db.DayStorageDB;

/**
 * CalendarCell extends RelativeLayout and it holds the information of a day showed in the screen, and handles all the
 * interactions with the user, (clicking, change pill type, add a note...)
 *
 * @author Joaquin Fernandez Moreno
 *
 */
public class CalendarCell extends RelativeLayout {

	/** The textView that contains the string with the day of the month of this cell */
	TextView text;

	private Context context;

	/** The pill type of this cell */
	private int pillType;

	/** The month this cell is representing */
	private int cellMonth;

	/** The day of the month this cell is representing */
	private int cellDayOfMonth;

	/** The year this cell is representing */
	private int cellYear;

	/** The day of the year this cell is representing */
	private int cellDayOfYear;

	/** The button with the image representing the pill type as background */
	private ImageButton imageButton;

	/**
	 * Constructor that initializes all the data necessary for this cell to be showed in the screen
	 * @param context the context of the activity that creates this CalendarCell
	 * @param i position of this cell in the grid
	 * @param firstRepresentingDay the first day we're representing in the screen (usually the latests of the previous month)
	 * @param representingYear the year of the month we're showing in the screen
	 * @param representingMonth the month we're showing on the screen
	 */
	public CalendarCell(Context context, int i, int firstRepresentingDay, int representingYear, int representingMonth) {
		super(context);
		this.context = context;

		LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		CalendarCell calendarCell = (CalendarCell) layoutInflater.inflate(R.layout.calendar_cell, this);
		RelativeLayout layout = (RelativeLayout) calendarCell.getChildAt(0);
		text = (TextView) layout.getChildAt(1);
		imageButton = (ImageButton) layout.getChildAt(0);
		imageButton.setOnClickListener(new CalendarCellClickListener());

		//The day we're representing in Day of the year
		cellDayOfYear = i + firstRepresentingDay;

		//we update our calendar
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.set(Calendar.DAY_OF_YEAR, cellDayOfYear);
		calendar.set(Calendar.YEAR, representingYear);
		cellMonth = calendar.get(Calendar.MONTH);
		cellDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		cellYear = calendar.get(Calendar.YEAR);

		//We set the label of the button
		String dayS = String.valueOf(cellDayOfMonth);
		text.setSingleLine();
		text.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
		text.setText(dayS);


		PillDayInfo pillDayInfo = new PillDayInfo(context);
		pillType = pillDayInfo.getPillType(calendar, cellDayOfYear, cellYear);
		imageButton.setBackgroundResource(setDays(representingMonth));
		//Clear animation (so the other month doesn't get animated) and check if it should be animated
		clearAnimation();
		if (isTodayTheDayRepresented())
			imageButton.setAnimation(AnimationUtils.loadAnimation(context, R.anim.today));

	}

	/**
	 * Auxiliary method that checks if the day this cell is representing is the day we're in today
	 * @return true if it is the same day, false otherwise
	 */
	private boolean isTodayTheDayRepresented() {
		GregorianCalendar calendar = new GregorianCalendar();
		int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
		int year = calendar.get(Calendar.YEAR);
		if (cellDayOfYear == dayOfYear && cellYear == year)
			return true;
		return false;
	}

	/**
	 * Auxiliary method that checks which pill type this cell has
	 * @return the background resource that needs to be displayed in this cells
	 */
	private int getDrawable() {
		//Return background resource
		switch (pillType) {
		case PillDayInfo.PILL_TAKEN:
			return R.drawable.pill_taken;

		case PillDayInfo.PILL_NOT_TAKEN:
			return R.drawable.pill_not_taken;

		case PillDayInfo.PILL_TAKEN_LATE:
			return R.drawable.pill_taken_late;

		case PillDayInfo.PILL_PENDING:
			return R.drawable.pill_pending;

		case PillDayInfo.PILL_PLACEBO:
			return R.drawable.pill_placebo;

		default:
			return R.drawable.pill_rest_day;
		}
	}

	/**
	 * Auxiliary method that checks the pill type this cell has
	 * @return the String that represents the pill type of this cell
	 */
	private String getPillTypeText() {
		String[] pillStates = context.getResources().getStringArray(R.array.pill_state_types);
		//Return background resource
		switch (pillType) {
		case PillDayInfo.PILL_TAKEN:
			return pillStates[0];

		case PillDayInfo.PILL_NOT_TAKEN:
			return pillStates[1];

		case PillDayInfo.PILL_TAKEN_LATE:
			return pillStates[2];

		case PillDayInfo.PILL_PENDING:
			return pillStates[3];

		case PillDayInfo.PILL_PLACEBO:
			return pillStates[4];

		default:
			return pillStates[5];
		}
	}

	/**
	 * Auxiliary method that sets the color of the text that shows the day of the cell
	 * (that depending on the background image can be black, white, or gray)
	 * @param representingMonth the month that we're showing in the screen
	 * @return the resource id of the background this cell should use
	 */
	private int setDays(int representingMonth) {

		//Return background resource
		switch (pillType) {
		case PillDayInfo.PILL_TAKEN:

			if (representingMonth == cellMonth)
				text.setTextColor(getResources().getColor(R.color.White));
			else
				text.setTextColor(getResources().getColor(R.color.Gray));
			return R.drawable.pill_taken;

		case PillDayInfo.PILL_NOT_TAKEN:

			if (representingMonth == cellMonth)
				text.setTextColor(getResources().getColor(R.color.Black));
			else
				text.setTextColor(getResources().getColor(R.color.Gray));
			return R.drawable.pill_not_taken;

		case PillDayInfo.PILL_TAKEN_LATE:

			if (representingMonth == cellMonth)
				text.setTextColor(getResources().getColor(R.color.White));
			else
				text.setTextColor(getResources().getColor(R.color.Gray));
			return R.drawable.pill_taken_late;

		case PillDayInfo.PILL_PENDING:

			if (representingMonth == cellMonth)
				text.setTextColor(getResources().getColor(R.color.Black));
			else
				text.setTextColor(getResources().getColor(R.color.Gray));
			return R.drawable.pill_pending;

		case PillDayInfo.PILL_PLACEBO:

			if (representingMonth == cellMonth)
				text.setTextColor(getResources().getColor(R.color.Black));
			else
				text.setTextColor(getResources().getColor(R.color.Gray));
			return R.drawable.pill_placebo;

		default:

			if (representingMonth == cellMonth)
				text.setTextColor(getResources().getColor(R.color.White));
			else
				text.setTextColor(getResources().getColor(R.color.Gray));
			return R.drawable.pill_rest_day;
		}
	}

	/**
	 * Auxiliary class that handles the click on the cell, showing a popup with interactions
	 * @author Joaquin Fernandez Moreno
	 *
	 */
	private class CalendarCellClickListener implements View.OnClickListener {

		@Override
		/**
		 * This method is called when the user clicks on the cell
		 */
		public void onClick(View v) {
			//The dialog that is going to be shown 
			final Dialog dayTouchedDialog = new Dialog(context, R.style.NoTitleDialog);
			dayTouchedDialog.setContentView(R.layout.day_touched_dialog);
			Display display = ((WindowManager) context.getSystemService(Activity.WINDOW_SERVICE)).getDefaultDisplay();
			int width = display.getWidth();
			dayTouchedDialog.getWindow().setLayout(width, android.view.WindowManager.LayoutParams.WRAP_CONTENT);
			//Image view that is going to show the image of the pill type this cell has
			ImageView imageView = (ImageView) dayTouchedDialog.findViewById(R.id.day_touched_pill_image);
			imageView.setBackgroundResource(getDrawable());
			//The text view that represents the pill type of this cell
			TextView pillTextView = (TextView) dayTouchedDialog.findViewById(R.id.day_touched_pill_text);
			pillTextView.setText(getPillTypeText());
			//Change button, shows a new dialog with a list of the pill types so the user can change it
			final RelativeLayout relativeLayout = (RelativeLayout) dayTouchedDialog.findViewById(R.id.day_touched_pill_clickable_layout);
			Button bt = new Button(context);
			relativeLayout.setBackgroundDrawable(bt.getBackground());
			relativeLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					//The dialog that is going to be shown when the user clicks on the change button
					final Dialog changePillType = new Dialog(context, R.style.NoTitleDialog);
					changePillType.setContentView(R.layout.select_pill_type);

					ListView list = (ListView) changePillType.findViewById(R.id.select_pill_type_list);
					list.setAdapter(new PillSelectBaseAdapter(context, relativeLayout, changePillType));
					//Dismiss button
					changePillType.findViewById(R.id.select_pill_type_button).setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							changePillType.dismiss();
						}
					});
					changePillType.show();
				}
			});
			//This edit text shows the note that was stored for this day
			EditText noteText = (EditText) dayTouchedDialog.findViewById(R.id.day_touched_note_text);
			DayStorageDB db = new DayStorageDB(context);
			String note = db.getNote(String.valueOf(cellYear), String.valueOf(cellDayOfYear));
			db.close();
			noteText.setText(note);

			//This button resets the text on the edit text to the previous note that there was stored
			Button cancelButton = (Button) dayTouchedDialog.findViewById(R.id.day_touched_cancel);
			cancelButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dayTouchedDialog.dismiss();
				}
			});

			//Ok button, saves whatever there is in the previous edit text to the database and dismisses the dialog
			Button okButton = (Button) dayTouchedDialog.findViewById(R.id.day_touched_ok);
			okButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String[] pillStates = context.getResources().getStringArray(R.array.pill_state_types);
					TextView pillTextView = (TextView) relativeLayout.findViewById(R.id.day_touched_pill_text);
					for (int i = 0; i < pillStates.length; i++) {
						if (pillTextView.getText().toString().equalsIgnoreCase(pillStates[i])) {
							pillType = PillDayInfo.PILL_TYPES[i];
							break;
						}
					}
					DayStorageDB db = new DayStorageDB(context);
					//Save the note to the database
					EditText noteText = (EditText) dayTouchedDialog.findViewById(R.id.day_touched_note_text);
					Editable text = noteText.getText();
					db.setNote(String.valueOf(cellYear), String.valueOf(cellDayOfYear), text.toString());
					//Save the pill type to the database as well
					db.setPillType(String.valueOf(cellYear), String.valueOf(cellDayOfYear), String.valueOf(pillType));
					db.close();
					//Set a toast that everything worked as expected
					String toastText = context.getResources().getString(R.string.note_saved_text);
					Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();
					imageButton.setBackgroundResource(setDays(cellMonth));
					imageButton.invalidate();//To redraw the button
					dayTouchedDialog.dismiss();
				}
			});

			dayTouchedDialog.show();
		}
	}
}