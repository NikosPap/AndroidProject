package com.example.agendaMyAcount;


import com.example.agenda.R;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class AccountMainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.account, menu);
		return true;
	}
	
	/*Called when the user clicks the "Edit Semister Schedule" button */
	public void showScheduleCourses(View view){
		Intent intent = new Intent(this, ScheduleCourses.class);
		startActivity(intent);
		
	}
	
	/*Called when the user clicks the "My Schedule" button */
	public void showMySchedule(View view){
		Intent intent = new Intent(this, MySchedule.class);
		startActivity(intent);
	}
	
	/*Called when the user clicks the "General Infos" button */
	public void showGeneralInfos(View view){
		Intent intent = new Intent(this, GeneralInfos.class);
		startActivity(intent);
	}
	
	/*Called when the user clicks the "My Courses" button */
	public void showCourses(View view){
		Intent intent = new Intent(this, Courses.class);
		startActivity(intent);
	}


}
