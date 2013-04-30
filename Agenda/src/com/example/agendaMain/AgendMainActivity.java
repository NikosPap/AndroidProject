package com.example.agendaMain;

import com.example.agenda.R;
//import com.example.courses.Courses;
import com.example.courses.Courses;
import com.example.scedule.Schedule;

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
	
	/*Called when the user clicks the "Courses" button 
	public void showCourses(View view){
		Intent intent = new Intent(this, Courses.class);
		startActivity(intent);
		
	}
	*/
	/*Called when the user clicks the "Courses" button */
	public void showCourses(View view){
		//Intent intent = new Intent(this, Courses.class);
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
	
	/*Called when the user clicks the "Profile" button */
	public void showProfile(View view){
		Intent intent = new Intent(this, Profile.class);
		startActivity(intent);
	}

}
