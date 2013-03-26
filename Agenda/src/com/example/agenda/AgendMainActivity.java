package com.example.agenda;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;


public class AgendMainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agenda_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.agend_main, menu);
		return true;
	}
	
	/*Called when the user clicks the "Course" button */
	public void showCourses(View view){
		Intent intent = new Intent(this, Courses.class);
		startActivity(intent);
		
	}
	
	/*Called when the user clicks the "Schedule" button */
	public void showSchedule(View view){
		Intent intent = new Intent(this, Schedule.class);
		startActivity(intent);
	}
	
	/*Called when the user clicks the "News Feed" button */
	public void showNews(View view){
		Intent intent = new Intent(this, ITCutiesReaderAppActivity.class);
		startActivity(intent);
	}
	
	/*Called when the user clicks the "Settings" button */
	public void showSettings(View view){
		Intent intent = new Intent(this, Settings.class);
		startActivity(intent);
	}

}
