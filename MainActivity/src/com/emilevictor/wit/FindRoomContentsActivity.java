package com.emilevictor.wit;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

public class FindRoomContentsActivity extends Activity {

	private ListView listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_room_contents);
		
		//Unpack the classes
		Bundle extras = getIntent().getExtras();
		int numberOfClassesToday = extras.getInt("numberOfClassesToday");
		Class[] thisSemestersClasses = new Class[numberOfClassesToday];
		
		//Get all of the bundled classes from the main activity.
		for (int i = 0; i < numberOfClassesToday; i++)
		{
			thisSemestersClasses[i] = (Class) getIntent().getSerializableExtra("CLASS"+String.valueOf(i));
		}
		
		Class[] todaysClassesSortedWithSpacers = new Class[18];
		
		//Sort each hour, fill in spaces
		
		String currentHourString = "";
		String portionOfDay = "";
		for (int hour = 8; hour <= 23;)
		{
			if (hour < 12)
			{
				currentHourString = hour + ":00 AM";
				portionOfDay = "AM";
			} else {
				currentHourString = (hour-12) + ":00 PM";
				portionOfDay = "PM";
			}
			
			for (Class currentClass : thisSemestersClasses)
			{
				if (currentClass.startTime.equals(currentHourString)) {
					todaysClassesSortedWithSpacers[hour-8] = currentClass;
					hour += 1;
				} else if (!currentClass.startTime.equals(currentHourString) && hour == 8)
				{
					Class blankClass = new Class();
					blankClass.courseCode = "Room free";
					blankClass.startTime = currentHourString;
					blankClass.classType = "F";
					todaysClassesSortedWithSpacers[hour-8] = blankClass;
					hour += 1;
				} else {
					
					Class blankClass = new Class();
					blankClass.courseCode = "Room free";
					blankClass.startTime = currentHourString;
					blankClass.classType = "F";
					todaysClassesSortedWithSpacers[hour-8] = blankClass;
					hour +=1;
					
				}
				
				if (currentClass.finishTime.equals(hour + ":50 " + portionOfDay))
				{
					//hour += 1;
				} else {
					//hour +=1;
					
					/*if (hour < 12)
					{
						currentHourString = hour + ":00 AM";
						portionOfDay = "AM";
					} else {
						currentHourString = (hour-12) + ":00 PM";
						portionOfDay = "PM";
					}
					
					Class blankClass = new Class();
					blankClass.courseCode = "Room free";
					blankClass.startTime = currentHourString;
					blankClass.classType = "F";
					todaysClassesSortedWithSpacers[hour-8] = blankClass;*/
					
				}
			}
		}
		
		//Create the adapter for our listview
		
		TimetableResultsAdapter adapter = new TimetableResultsAdapter(this, 
                R.layout.timetable_results_item_row, todaysClassesSortedWithSpacers);
		
		ListView listView = (ListView)findViewById(R.id.timetableResults);
		
		View header = (View)getLayoutInflater().inflate(R.layout.timetable_results_header_row, null);
        listView.addHeaderView(header);
        
        listView.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_find_room_contents, menu);
		return true;
	}

}
