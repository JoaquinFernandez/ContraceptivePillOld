package com.jsolutionssp.pill.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Class that extends SQLiteOpenHelper and that manages the interactions of the application with the database
 * that has only one table called "Users"
 * @author Joaquin Fernandez Moreno
 *
 */
public class DayStorageDB extends SQLiteOpenHelper {

	private static final String TABLE_NAME = "Users";

	private static final String TABLE_COLUMN_VALID = "valid";

	private static final String TABLE_COLUMN_YEAR = "year";

	private static final String TABLE_COLUMN_DAY_OF_YEAR = "day";

	private static final String TABLE_COLUMN_PILL_TYPE = "state";

	private static final String TABLE_COLUMN_NOTE = "note";

	private static final int VALID = 1;

	private static final int INVALID = 0;

	// SQL sentence to create the table users
	private final String sqlCreate = "CREATE TABLE " + TABLE_NAME + " ( " + TABLE_COLUMN_VALID
			+ " INTEGER, " + TABLE_COLUMN_YEAR
			+ " INTEGER, " + TABLE_COLUMN_DAY_OF_YEAR + " INTEGER, "
			+ TABLE_COLUMN_PILL_TYPE + " INTEGER, " + TABLE_COLUMN_NOTE
			+ " TEXT)";

	private static final int version = 1;


	/**
	 * Constructor that initializes the database
	 * @param context the context of the activity that called this constructor
	 */
	public DayStorageDB(Context context) {
		super(context, TABLE_NAME, null, version);
	}

	@Override
	/**
	 * Overrided method that is called when this database is created, it executes the
	 * create command that we had to define before
	 */
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(sqlCreate);
	}

	@Override
	/**
	 * This method is called if the database needs to be upgraded (add columns or remove in different app 
	 * versions)
	 */
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		/*Query to update the db 
		 * String upgradeQuery = "ALTER TABLE " + TABLE_NAME + "ADD COLUMN " + TABLE_COLUMN_VALID + " INTEGER;";
		 * if (oldVersion == 1 && newVersion == 2)
		 * db.execSQL(upgradeQuery);
		 */
	}

	/**
	 * Method to store the pill type of a day, if the row already existed, it updates it
	 * @param year the year of the day we want to store info about
	 * @param dayOfYear the day of the year we want to store info about
	 * @param pillType the pill type we want to store
	 */
	public void setPillType(String year, String dayOfYear, String pillType) {
		SQLiteDatabase db = getWritableDatabase();

		Cursor c = db.rawQuery(" SELECT " + TABLE_COLUMN_PILL_TYPE + " FROM " + TABLE_NAME +
				" WHERE " + TABLE_COLUMN_YEAR + "=" + year + " AND " + TABLE_COLUMN_DAY_OF_YEAR + "=" + dayOfYear + " ", null);
		if  (c.moveToFirst()) {//the row existed
			db.execSQL("UPDATE " + TABLE_NAME + " SET " + TABLE_COLUMN_PILL_TYPE
					+ "=" + pillType + ", " + TABLE_COLUMN_VALID + "=" + VALID 
					+ " WHERE " + TABLE_COLUMN_YEAR + "=" + year
					+ " AND " + TABLE_COLUMN_DAY_OF_YEAR + "=" + dayOfYear + " ");
		}
		else {
			db.execSQL("INSERT INTO " + TABLE_NAME + " (" + TABLE_COLUMN_YEAR
					+ ", " + TABLE_COLUMN_VALID
					+ ", " + TABLE_COLUMN_DAY_OF_YEAR + ", "
					+ TABLE_COLUMN_PILL_TYPE + ") VALUES (" + year + ", "
					+ VALID + ", " + dayOfYear + ", " + pillType + ") ");
		}
		c.close();
		db.close();
	}

	/**
	 * Method to store a note in a previous existing row of the table, so it
	 * should contain a pill type already
	 * 
	 * @param year the year of the day we want to store info about
	 * @param dayOfYear the day of the year we want to store info about
	 * @param note the note the user wants to store for that day
	 */
	public void setNote(String year, String dayOfYear, String note) {
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("UPDATE " + TABLE_NAME + " SET " + TABLE_COLUMN_NOTE + "='"
				+ note + "' WHERE " + TABLE_COLUMN_YEAR + "=" + year + " AND "
				+ TABLE_COLUMN_DAY_OF_YEAR + "=" + dayOfYear + " ");
		db.close();
	}

	/**
	 * Deletes the selected row by year and dayOfYear
	 * @param year year of the row we want to the delete
	 * @param dayOfYear the day of the year of the row we want to the delete
	 */
	public void deleteRow(String year, String dayOfYear) {
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + TABLE_COLUMN_YEAR + "=" + year +
				" AND " + TABLE_COLUMN_DAY_OF_YEAR + "=" + dayOfYear + " ");
		db.close();
	}

	/**
	 * Getter method that retrieves the pill type of a day giving a year and a day of the year
	 * it previously checks that the database is valid
	 * 
	 * @param year the year of the day we want to retrieve the pill type
	 * @param dayOfYear the day of the year we want to retrieve the pill type
	 * @return the pill type of that day
	 */
	public String getPillType(String year, String dayOfYear) {
		SQLiteDatabase db = getWritableDatabase();
		if (isDatabaseValid(db, year, dayOfYear)) {
			Cursor c = db.rawQuery(" SELECT " + TABLE_COLUMN_PILL_TYPE + " FROM " + TABLE_NAME +
					" WHERE " + TABLE_COLUMN_YEAR + "=" + year + " AND " + TABLE_COLUMN_DAY_OF_YEAR + "=" + dayOfYear + " ", null);
			if  (c.moveToFirst()) {
				String pillType = c.getString(c.getColumnIndex(TABLE_COLUMN_PILL_TYPE));
				c.close();
				db.close();
				if (pillType.equalsIgnoreCase("-1"))
					return null;
				return pillType;
			}
			c.close();
		}
		db.close();
		return null;
	}

	/**
	 * Getter method that retrieves the stored note for a day giving a year and a day of the year
	 * @param year the year of the day we want to retrieve the note
	 * @param dayOfYear the day of the year we want to retrieve the note
	 * @return the note that was stored
	 */
	public String getNote(String year, String dayOfYear) {
		SQLiteDatabase db = getWritableDatabase();
		Cursor c = db.rawQuery(" SELECT " + TABLE_COLUMN_NOTE + " FROM " + TABLE_NAME +
				" WHERE " + TABLE_COLUMN_YEAR + "=" + year + " AND " + TABLE_COLUMN_DAY_OF_YEAR + "=" + dayOfYear + " ", null);
		if  (c.moveToFirst()) {
			String note = c.getString(c.getColumnIndex(TABLE_COLUMN_NOTE));
			c.close();
			db.close();
			if (note == null || note.equalsIgnoreCase("-1"))
				return "";
			else
				return note;
		}
		c.close();
		db.close();
		return "";
	}

	/**
	 * It checks if the database is valid (there has been no changes in the start
	 * day of the pack or the pill modality)
	 * 
	 * @param db the database we want to check
	 * @param year the year of the day we want to check if the information is valid
	 * @param dayOfYear the day of the year we want to check if the information is valid
	 * @return
	 */
	public boolean isDatabaseValid(SQLiteDatabase db, String year, String dayOfYear) {
		Cursor c = db.rawQuery(" SELECT " + TABLE_COLUMN_VALID + " FROM " + TABLE_NAME +
				" WHERE " + TABLE_COLUMN_YEAR + "=" + year + " AND " + TABLE_COLUMN_DAY_OF_YEAR + "=" + dayOfYear + " ", null);
		if  (c.moveToFirst()) {
			int isValid = c.getInt(c.getColumnIndex(TABLE_COLUMN_VALID));
			c.close();
			if (isValid == VALID)
				return true;
			return false;
		}
		c.close();
		return false;
	}

	/**
	 * Method that invalidates the whole database (there has been a change in the start
	 * day of the pack or the pill modality)
	 */
	public void invalidateDatabase() {
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("UPDATE " + TABLE_NAME + " SET " + TABLE_COLUMN_VALID
				+ "=" + INVALID + " WHERE " + TABLE_COLUMN_VALID + "=" + VALID + " ");
		db.close();
	}
}