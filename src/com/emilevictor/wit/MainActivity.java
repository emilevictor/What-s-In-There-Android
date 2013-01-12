package com.emilevictor.wit;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		/*
		 * Set up the campus spinner
		 */
		
		Spinner campusSpinner = (Spinner) findViewById(R.id.campusChoiceSpinner);
		
		//Create an arrayadapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> campusAdapter = ArrayAdapter.createFromResource(this,
				R.array.campuses, android.R.layout.simple_spinner_item);
		
		//Specify the layout to use when the list of choices appears
		campusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		//Apply the adapter to the spinner
		campusSpinner.setAdapter(campusAdapter);
		
		/*
		 * Set up the day spinner
		 */
		
		//Get the day of the week
		Calendar rightNow = Calendar.getInstance();
		int day = rightNow.get(Calendar.DAY_OF_WEEK);
		day -= 1; //To reflect the fact that we'll be addressing an array index.
		
		Log.d("day of week", "value: " + ((Integer)day).toString());
		
		Spinner daySpinner = (Spinner) findViewById(R.id.daysSpinner);
		
		String[] daysStringArray = {"Sunday", "Monday", "Tuesday", "Wednesday",
									"Thursday", "Friday", "Saturday"};
		
		daysStringArray[day] = daysStringArray[day] + " (today)";
		
		ArrayAdapter<CharSequence> dayAdapter = new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item,
				daysStringArray);

		
		
		dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		daySpinner.setAdapter(dayAdapter);
		
		//Set the spinner to automatically select today.
		daySpinner.setSelection(day);
		
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	public void findRoomButtonPressed(View view) {
    	Intent intent = new Intent(this, FindRoomContentsActivity.class);
    	
    	
    	startActivity(intent);
	}

}
