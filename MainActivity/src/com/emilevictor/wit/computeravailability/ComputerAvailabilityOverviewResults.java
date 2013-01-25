package com.emilevictor.wit.computeravailability;

import com.emilevictor.wit.MainActivity;
import com.emilevictor.wit.R;
import com.emilevictor.wit.R.layout;
import com.emilevictor.wit.R.menu;
import com.emilevictor.wit.helpers.Class;
import com.emilevictor.wit.whats_in_there.TimetableResultsAdapter;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class ComputerAvailabilityOverviewResults extends Activity {

	private ListView listView;
	
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem)
	{       
		startActivity(new Intent(ComputerAvailabilityOverviewResults.this,MainActivity.class)); 
		return true;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_computer_availability_overview_results);
		
		//Add a back button to the action bar
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		//Unpack the rooms
		Bundle extras = getIntent().getExtras();
		int numberOfRooms = extras.getInt("numberOfRooms");
		Room[] rooms = new Room[numberOfRooms];
		
		//Get all of the bundled classes from the main activity.
		for (int i = 0; i < numberOfRooms; i++)
		{
			rooms[i] = (Room) getIntent().getSerializableExtra("ROOM"+String.valueOf(i));
		}
		
		this.listView = (ListView)findViewById(R.id.computerOverviewList);
		
		//Create the adapter for our listview

		ComputerAvailabilityOverviewAdapter adapter = new ComputerAvailabilityOverviewAdapter(this, 
				R.layout.availability_overview_item_row, rooms);

		ListView listView = (ListView)findViewById(R.id.computerOverviewList);

		View header = (View)getLayoutInflater().inflate(R.layout.availability_overview_header_row, null);
		listView.addHeaderView(header);

		listView.setAdapter(adapter);
		listView.setVisibility(View.VISIBLE);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(
				R.menu.activity_computer_availability_overview_results, menu);
		return true;
	}

}
