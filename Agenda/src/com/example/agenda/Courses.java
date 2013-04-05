package com.example.agenda;



import java.io.IOException;

import com.itcuties.android.reader.data.DataBaseHelper;

import android.os.Bundle;
import android.app.Activity;
import android.database.SQLException;
import android.view.Menu;


public class Courses extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_courses);
		
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_courses, menu);
		return true;
	}

}
