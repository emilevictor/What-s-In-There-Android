package com.emilevictor.wit.whats_in_there;

import com.emilevictor.wit.R;
import com.emilevictor.wit.R.id;
import com.emilevictor.wit.R.layout;
import com.emilevictor.wit.R.menu;
import com.emilevictor.wit.helpers.Class;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class FindRoomContentsActivity extends Activity {

	private ListView listView;
	private TextView errorMessage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_room_contents);

		//Unpack the classes
		Bundle extras = getIntent().getExtras();
		int numberOfClassesToday = extras.getInt("numberOfClassesToday");
		Class[] thisSemestersClasses = new Class[numberOfClassesToday];
		errorMessage = (TextView) findViewById(R.id.errorMessage);
		listView = (ListView)findViewById(R.id.timetableResults);
		
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

			Class[] todaysClassesSortedWithSpacers = new Class[18];

			//Sort each hour, fill in spaces

			String currentHourString = "";
			String portionOfDay = "";
			Class foundClass = null;
			for (int hour = 8; hour <= 23;)
			{
				if (hour < 12)
				{
					currentHourString = hour + ":00 AM";
					portionOfDay = "AM";
				} else if (hour == 12) {
					portionOfDay = "PM";
					currentHourString = "12:00 PM";
				}else {

					currentHourString = (hour-12) + ":00 PM";
					portionOfDay = "PM";
				}
				boolean weFoundOne = false;
				for (Class currentClass : thisSemestersClasses)
				{
					if (currentClass.startTime.equals(currentHourString)) {
						foundClass = currentClass;
						weFoundOne = true;
						break;
					}

				}
				if (weFoundOne)
				{
					//Calculate one hour ahead string, two hours ahead string and three hours ahead string.
					String oneHourAhead = "";
					String twoHoursAhead = "";
					String threeHoursAhead = "";


					if ((hour+1) < 12)
					{
						oneHourAhead = (hour+1) + ":50 AM";
					} else if ((hour+1) == 12) {
						oneHourAhead = "12:50 PM";
					}else {
						oneHourAhead = ((hour+1)-12) + ":50 PM";
					}

					if ((hour+2) < 12)
					{
						twoHoursAhead = (hour+2) + ":50 AM";
					} else if ((hour+2) == 12) {
						twoHoursAhead = "12:50 PM";
					}else {
						twoHoursAhead = ((hour+2)-12) + ":50 PM";
					}

					if ((hour+3) < 12)
					{
						threeHoursAhead = (hour+3) + ":50 AM";
					} else if ((hour+3) == 12) {
						threeHoursAhead = "12:50 PM";
					}else {
						threeHoursAhead = ((hour+3)-12) + ":50 PM";
					}




					/** Then continue
					 * 
					 */

					todaysClassesSortedWithSpacers[hour-8] = foundClass;

					//If it is only an hour long
					if (foundClass.finishTime.equals(hour + ":50 " + portionOfDay))
					{
						//Do nothing


						//If it is two hours long, add one to the hour and insert it again, but make the start time the new hour.
					} else if (foundClass.finishTime.equals(oneHourAhead)) {
						hour += 1;
						//recalculate the time string
						if (hour < 12)
						{
							currentHourString = hour + ":00 AM";
							portionOfDay = "AM";
						} else if (hour == 12) {
							portionOfDay = "PM";
							currentHourString = "12:00 PM";
						}else {

							currentHourString = (hour-12) + ":00 PM";
							portionOfDay = "PM";
						}

						//Create a new dummy class
						Class blankClass = new Class();
						blankClass.courseCode = "-- " + foundClass.courseCode + " continues";
						blankClass.startTime = currentHourString;
						blankClass.classType = foundClass.classType;
						todaysClassesSortedWithSpacers[hour-8] = blankClass;

						//rarer case, three hour long class.
					} else if (foundClass.finishTime.equals(twoHoursAhead))
					{
						hour += 2;
						//recalculate the time string
						if (hour < 12)
						{
							currentHourString = hour + ":00 AM";
							portionOfDay = "AM";
						} else if (hour == 12) {
							portionOfDay = "PM";
							currentHourString = "12:00 PM";
						}else {

							currentHourString = (hour-12) + ":00 PM";
							portionOfDay = "PM";
						}

						//Create a new dummy class
						Class blankClass = new Class();
						blankClass.courseCode = "-- " + foundClass.courseCode + " continues";
						blankClass.startTime = currentHourString;
						blankClass.classType = foundClass.classType;
						todaysClassesSortedWithSpacers[hour-8] = blankClass;
					}
				} else {
					Class blankClass = new Class();
					blankClass.courseCode = "Room free";
					blankClass.startTime = currentHourString;
					blankClass.classType = "F";
					todaysClassesSortedWithSpacers[hour-8] = blankClass;
				}

				hour += 1;


			}

			for (int i = 0; i < 18 ; i++)
			{
				if (todaysClassesSortedWithSpacers[i] == null)
				{
					Class blankClass = new Class();
					blankClass.courseCode = "Room free";
					blankClass.startTime = currentHourString;
					blankClass.classType = "F";
					todaysClassesSortedWithSpacers[i] = blankClass;
				}
			}

			//Create the adapter for our listview

			TimetableResultsAdapter adapter = new TimetableResultsAdapter(this, 
					R.layout.timetable_results_item_row, todaysClassesSortedWithSpacers);

			ListView listView = (ListView)findViewById(R.id.timetableResults);

			View header = (View)getLayoutInflater().inflate(R.layout.timetable_results_header_row, null);
			listView.addHeaderView(header);

			listView.setAdapter(adapter);
			listView.setVisibility(View.VISIBLE);
		}


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_find_room_contents, menu);
		return true;
	}

}
