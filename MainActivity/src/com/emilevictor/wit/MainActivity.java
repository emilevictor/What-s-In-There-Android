package com.emilevictor.wit;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.emilevictor.wit.RoomBuildingParser.InvalidInputException;
import com.emilevictor.wit.buildingXMLParser.Building;

public class MainActivity extends Activity {

	public static final String WIFI = "Wi-Fi";
	public static final String ANY = "Any";
	private static final String buildingsXMLUrl =
			"http://rota.eait.uq.edu.au/buildings.xml";
	
	private Integer room;
	private Integer building;




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
	


	@SuppressWarnings("unchecked")
	public void findRoomButtonPressed(View view) throws IOException, XmlPullParserException, InterruptedException, ExecutionException {
		Intent intent = new Intent(this, FindRoomContentsActivity.class);


		/**
		 * Hit up the API to get the building number.
		 */
		//Check that we are connected.
		Network network = new Network(getApplicationContext());


		if (network.getConnectivityStatus())
		{
			List<Building> buildings = null;
			RetreiveBuildingIdTask retBuild = new RetreiveBuildingIdTask();
			buildings = retBuild.execute(buildingsXMLUrl).get();
			EditText roomInput = (EditText) findViewById(R.id.room_textfield);
			RoomBuildingParser rbp = new RoomBuildingParser(roomInput.getText().toString());
			List<String> roomAndBuilding;
			try {
				roomAndBuilding = rbp.giveMeRoomAndBuildingSeparately();
				String returnedBuildingString = buildingXMLParser.getBuildingIdFromNumber(roomAndBuilding.get(0),buildings);
				
				//The building was not found - oh noes!
				if (returnedBuildingString.equals("NOTFOUND"))
				{
					
				} else {
					this.building = Integer.getInteger(returnedBuildingString);
					this.room = Integer.getInteger(roomAndBuilding.get(1));
					
					
				}
				startActivity(intent);
			} catch (InvalidInputException e) {
				
				e.printStackTrace();
			}

			
			
		} else {
			Toast.makeText(getApplicationContext(), R.string.waiting_on_connection, Toast.LENGTH_LONG).show();
		}



	}

}
