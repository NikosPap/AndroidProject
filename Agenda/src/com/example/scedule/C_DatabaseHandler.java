package com.example.scedule;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class C_DatabaseHandler extends SQLiteOpenHelper {
	 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "ShceduleCoursesManager";
 
    // Contacts table name
    private static final String TABLE_SCOURSES = "ScheduleCourses";
 
    // Contacts Table Columns names
    private static final String KEY_DAY = "day";
    private static final String KEY_NAME = "name";
    private static final String KEY_HOUR = "hour";
    private static final String KEY_CLASS = "class";
 
    public C_DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SCHEDULED_COURSES_TABLE = "CREATE TABLE " + TABLE_SCOURSES + "("
                + KEY_DAY + " TEXT," + KEY_NAME + " TEXT," + KEY_CLASS + " TEXT,"
                + KEY_HOUR + " TEXT" + ")";
        db.execSQL(CREATE_SCHEDULED_COURSES_TABLE);
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCOURSES);
 
        // Create tables again
        onCreate(db);		
	}
	
	// Adding new scheduled course
	public void addSCourse(DatabaseCourse course) {
	    SQLiteDatabase db = this.getWritableDatabase();
	 
	    ContentValues values = new ContentValues();
	    values.put(KEY_NAME, course.getName()); 
	    values.put(KEY_HOUR, course.getHour()); 
	    values.put(KEY_DAY, course.getDay());	
	    values.put(KEY_CLASS, course.get_class());
	 
	    // Inserting Row
	    db.insert(TABLE_SCOURSES, null, values);
	    db.close(); // Closing database connection
	}
	
	public void printAll(){
		String selectQuery = "SELECT  * FROM " + TABLE_SCOURSES;
		 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                System.out.println(c.getString(0)+" "+c.getString(1)+" "+c.getString(2)+" "+ c.getString(3) +"\n");
            } while (c.moveToNext());
        }
	}
}
