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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jsolutionssp.pill.PillDayInfo;
import com.jsolutionssp.pill.R;
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
	
	/** Variable to store temporarily the old pill type the user has selected */
	private int oldPillType;

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
	private int getPillTypeText() {
		//Return background resource
		switch (pillType) {
		case PillDayInfo.PILL_TAKEN:
			return R.string.pill_taken;

		case PillDayInfo.PILL_NOT_TAKEN:
			return R.string.pill_not_taken;

		case PillDayInfo.PILL_TAKEN_LATE:
			return R.string.pill_taken_late;

		case PillDayInfo.PILL_PENDING:
			return R.string.pill_pending;

		case PillDayInfo.PILL_PLACEBO:
			return R.string.pill_placebo;

		default:
			return R.string.pill_rest_day;
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
			RelativeLayout relativeLayout = (RelativeLayout) dayTouchedDialog.findViewById(R.id.day_touched_pill_clickable_layout);
			Button bt = new Button(context);
			relativeLayout.setBackgroundDrawable(bt.getBackground());
			relativeLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					//The dialog that is going to be shown when the user clicks on the change button
					final Dialog changePillType = new Dialog(context, R.style.NoTitleDialog);
					changePillType.setContentView(R.layout.select_pill_type);

					//These are the six options of pill type that the user can set for a day
					changePillType.findViewById(R.id.select_pill_type_pill_not_taken_layout).
					setOnClickListener(new PillListClickListener(PillDayInfo.PILL_NOT_TAKEN, dayTouchedDialog, changePillType));

					changePillType.findViewById(R.id.select_pill_type_pill_taken_layout).
					setOnClickListener(new PillListClickListener(PillDayInfo.PILL_TAKEN, dayTouchedDialog, changePillType));

					changePillType.findViewById(R.id.select_pill_type_pill_taken_late_layout).
					setOnClickListener(new PillListClickListener(PillDayInfo.PILL_TAKEN_LATE, dayTouchedDialog, changePillType));

					changePillType.findViewById(R.id.select_pill_type_pill_pending_layout).
					setOnClickListener(new PillListClickListener(PillDayInfo.PILL_PENDING, dayTouchedDialog, changePillType));

					changePillType.findViewById(R.id.select_pill_type_pill_rest_day_layout).
					setOnClickListener(new PillListClickListener(PillDayInfo.PILL_REST_DAY, dayTouchedDialog, changePillType));

					changePillType.findViewById(R.id.select_pill_type_pill_placebo_layout).
					setOnClickListener(new PillListClickListener(PillDayInfo.PILL_PLACEBO, dayTouchedDialog, changePillType));

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
					pillType = oldPillType;
					dayTouchedDialog.dismiss();
				}
			});

			//Ok button, saves whatever there is in the previous edit text to the database and dismisses the dialog
			Button okButton = (Button) dayTouchedDialog.findViewById(R.id.day_touched_ok);
			okButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					oldPillType = pillType;
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
		/**
		 * Listener that is called when te user clicks on any of the change pill type options, and it stores
		 * the new pill type in the database and dismisses the dialog
		 * @author Joaquin Fernandez Moreno
		 *
		 */
		private class PillListClickListener implements View.OnClickListener {

			private int newPillType;
			private Dialog dayTouchedDialog;
			private Dialog changePillType;

			/**
			 * Constructor of the listener, it initializes the values needed
			 * @param newPillType the new pill type that the user selected
			 * @param dayTouchedDialog the dialog that shows the information of the cell (day) selected
			 * @param changePillType the dialog that shows the different pill types 
			 */
			public PillListClickListener(int newPillType, Dialog dayTouchedDialog, Dialog changePillType) {
				this.newPillType = newPillType;
				this.dayTouchedDialog = dayTouchedDialog;
				this.changePillType = changePillType;
			}

			@Override
			/**
			 * When clicked, changes the image of the previous dialog to the new pill type, stores the information
			 * of the new pill type into the database, and dismisses this dialog
			 */
			public void onClick(View v) {
				//Store the old pill type in case the user wants to cancel it late
				oldPillType = pillType;
				pillType = newPillType;
				ImageView imageView = (ImageView) dayTouchedDialog.findViewById(R.id.day_touched_pill_image);
				imageView.setBackgroundResource(getDrawable());
				TextView pillTextView = (TextView) dayTouchedDialog.findViewById(R.id.day_touched_pill_text);
				pillTextView.setText(getPillTypeText());
				changePillType.dismiss();
			}
		}
	}
}

/*else if (event.getAction() == MotionEvent.ACTION_UP) {
if (settings.getBoolean("firstRunDayCell", true)) {
	editor.putInt("startCycleDayofYear", representingDayofYear);
	editor.putInt("startCycleYear", representingYear);
	editor.putBoolean("firstRunDayCell", false);
	editor.commit();
	CalendarView cal = (CalendarView) ((Activity) context).findViewById(R.id.main_calendar);
	cal.fillGrid();
	final Dialog selectDayDialog = new Dialog(context, R.style.NoTitleDialog);
	selectDayDialog.setContentView(R.layout.info_dialog);
	TextView dialogText = (TextView) selectDayDialog.findViewById(R.id.info_dialog_text);
	dialogText.setText(R.string.alarm_info_toast);
	Button button = (Button) selectDayDialog.findViewById(R.id.info_dialog_button);
	button.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			selectDayDialog.dismiss();
		}
	});
	selectDayDialog.show();
	return true;
}
else {
	final Dialog dialog = new Dialog(context);
	dialog.setContentView(R.layout.day_touched_dialog);
	String text = representingDay + "/" + (representingMonth + 1) + "/" + representingYear;
	dialog.setTitle(text);

	//Button buttonOK = (Button) dialog.findViewById(R.id.day_touched_button_ok);
	//buttonOK.setOnClickListener(new OnClickListener() {
		//@Override
		//public void onClick(View v) {
			//editor.putInt("startCycleDayofYear", representingDayofYear);
		//	editor.putInt("startCycleYear", representingYear);
			//editor.commit();
		//	CalendarView cal = (CalendarView) ((Activity) context).findViewById(R.id.main_calendar);
		//	cal.fillGrid();
		//	dialog.dismiss();
		//	Intent i = new Intent(context, SetAlarms.class);
		//	i.setAction("com.jsolutionssp.pill.updateAlarm");
		//	context.sendBroadcast(i);
	//	}
	//});
	//Button buttonNO = (Button) dialog.findViewById(R.id.day_touched_button_no);
	//buttonNO.setOnClickListener(new OnClickListener() {
		//@Override
		//public void onClick(View v) {
			//dialog.dismiss();
	//	}
	//});
	dialog.show();
	return true;
}
}
return super.onTouchEvent(event);	
}*/
