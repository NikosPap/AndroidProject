package com.example.agenda;



import java.io.IOException;

import com.itcuties.android.reader.data.DataBaseHelper;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
//import android.database.Cursor;
import android.database.SQLException;
//import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;


@SuppressLint("NewApi")
public class Courses extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_courses);
		//this.deleteDatabase("/data/data/com.example.agenda/databases/MySQLiteDB");

		
		DataBaseHelper myDbHelper = new DataBaseHelper(this);
        try { 
        	myDbHelper.createDataBase();
        } catch (IOException ioe) {
        	throw new Error("Unable to create database");
        }
        try { 
        	myDbHelper.openDataBase();
        }catch(SQLException sqle){
        	throw sqle;
        }
/*        
        SQLiteDatabase db = myDbHelper.getReadableDatabase();
        
        String[] projection = {"Code","Name","Description"};
        String selection = "Code Name Description";		// How you want the results sorted in the resulting Cursor

        Cursor c = db.query(
        		"Subjects",  // The table to query
        	    projection,                               // The columns to return
        	    null,                                // The columns for the WHERE clause
        	    null,                            // The values for the WHERE clause
        	    null,                                     // don't group the rows
        	    null,                                     // don't filter by row groups
        	    null                                 // The sort order
        	    );
        c.moveToFirst();
		while(c.moveToNext()){
			System.out.println(c.getString(0)+" "+c.getString(1)+" "+c.getString(2)+"\n");
		}
		myDbHelper.close();
*/

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_courses, menu);
		return true;
	}

}
