package com.emilevictor.wit.wheres_my_class;

import java.util.HashMap;
import java.util.Map;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.emilevictor.wit.R;
import com.emilevictor.wit.helpers.Class;

public class WheresMyClassResults extends Activity {

	private ListView listView;
	private TextView errorMessage;
	private Map<String,Integer> dayMap;
	private String currentCourseCode;
	private String currentDay;

	@Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {       
        startActivity(new Intent(WheresMyClassResults.this,WheresMyClass.class)); 
        return true;
    }
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onStart() {
	    int versionNumber = android.os.Build.VERSION.SDK_INT;
	    if (versionNumber >= 11) {
	            super.onStart();
	            ActionBar actionBar = this.getActionBar();
	            actionBar.setDisplayHomeAsUpEnabled(true);
	    }
	    else {
	            super.onStart();
	    }
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wheres_my_class_results);

		//Initialize variables
		currentCourseCode = "";
		currentDay = "";
		
		
		//Setting up map datatypes for various uses.
		String[] daysStringArray = {"Sunday", "Monday", "Tuesday", "Wednesday",
				"Thursday", "Friday", "Saturday"};
		dayMap = new HashMap<String,Integer>();

		dayMap.put("Sun", 0);
		dayMap.put("Mon",1);
		dayMap.put("Tue",2);
		dayMap.put("Wed", 3);
		dayMap.put("Thu", 4);
		dayMap.put("Fri", 5);
		dayMap.put("Sat", 6);
		
		//Unpack the classes
		Bundle extras = getIntent().getExtras();
		int numberOfClassesToday = extras.getInt("numberOfClassesToday");
		Class[] thisSemestersClasses = new Class[numberOfClassesToday];
		errorMessage = (TextView) findViewById(R.id.errorMessageWMC);
		listView = (ListView)findViewById(R.id.timetableResultsWMC);
		
		boolean courseCodeSet = false;

		//Check that classes were found.
		if (numberOfClassesToday == 0)
		{
			//In this case, there were no classes. Hide the listview,
			//Show an error message.

			errorMessage.setVisibility(View.VISIBLE);

			listView.setVisibility(View.GONE);

			errorMessage.setPadding(15, 100, 15, 15);


		} else {

			errorMessage.setVisibility(View.GONE);
			errorMessage.setPadding(0,0,0,0);

			//Get all of the bundled classes from the main activity.
			for (int i = 0; i < numberOfClassesToday; i++)
			{
				thisSemestersClasses[i] = (Class) getIntent().getSerializableExtra("CLASS"+String.valueOf(i));
			}

			Class[] sortedClasses = new Class[numberOfClassesToday];

			//Use this variable to index the current class.
			int currentSortedClassIndex = 0;

			//Sort each hour, fill in spaces

			String currentHourString = "";
			for (int hour = 8; hour <= 23;)
			{
				if (hour < 12)
				{
					currentHourString = hour + ":00 AM";
				} else if (hour == 12) {
					currentHourString = "12:00 PM";
				}else {

					currentHourString = (hour-12) + ":00 PM";
				}
				
				for (Class currentClass : thisSemestersClasses)
				{
					if (currentClass.startTime.equals(currentHourString)) {
						sortedClasses[currentSortedClassIndex] = currentClass;
						currentSortedClassIndex += 1;
					}
					if (!courseCodeSet)
					{
						courseCodeSet = true;
						currentCourseCode = currentClass.courseCode;
						currentDay = currentClass.day;
					}

				}

				hour += 1;


			}
			

			

			//Create the adapter for our listview

			TimetableResultsAdapterWMC adapter = new TimetableResultsAdapterWMC(this, 
					R.layout.wmc_timetable_results_item_row, sortedClasses);

			ListView listView = (ListView)findViewById(R.id.timetableResultsWMC);

			View header = (View)getLayoutInflater().inflate(R.layout.wmc_timetable_results_header_row, null);
			listView.addHeaderView(header);

			listView.setAdapter(adapter);
			listView.setVisibility(View.VISIBLE);
			
			if (courseCodeSet)
			{
				TextView title = (TextView) findViewById(R.id.txtHeaderWMC);
				
				//Get the day string and place it in the title.
				title.setText(currentCourseCode + ", timetable for " + daysStringArray[dayMap.get(currentDay)]);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater()
		.inflate(R.menu.activity_wheres_my_class_results, menu);
		return true;
	}

}
