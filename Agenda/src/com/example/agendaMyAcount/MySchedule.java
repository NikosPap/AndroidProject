package com.example.agendaMyAcount;

import com.example.agenda.R;


import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MySchedule extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_schedule);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_schedule, menu);
		return true;
	}

}
