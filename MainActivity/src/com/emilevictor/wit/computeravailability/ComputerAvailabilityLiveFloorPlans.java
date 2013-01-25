package com.emilevictor.wit.computeravailability;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.emilevictor.wit.MainActivity;
import com.emilevictor.wit.R;
import com.emilevictor.wit.helpers.Settings;

public class ComputerAvailabilityLiveFloorPlans extends Activity {

	private Spinner libraryChoiceSpinner;
	private Spinner libraryLevelSpinner;
	private String[] libraryGetCodes;
	private ArrayList<String> levels;
	
	@Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {       
        startActivity(new Intent(ComputerAvailabilityLiveFloorPlans.this,MainActivity.class)); 
        return true;
    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_computer_availability);
		
		/*
		 * Populate the library get codes for insertion into the URL.
		 * These will be filled out as new GET codes are known.
		 */
		
		this.levels = new ArrayList<String>();
		//Because the default library is SS&H, we'll fill it up with this:
		this.levels.add("Lvl1");
		this.levels.add("Lvl2");
		this.levels.add("Lvl3");
		this.levels.add("Lvl4");
		
		String[] libraryGETCodes = {"SSAH",
				"Armus",
				"Dentistry",
				"BSL",
				"HML",
				"Duhig"};
		
		this.libraryGetCodes = libraryGETCodes;
		
		this.libraryChoiceSpinner = (Spinner) findViewById(R.id.libraryChoiceSpinner);
		this.libraryLevelSpinner = (Spinner) findViewById(R.id.levelChoiceSpinner);
		
		/**
		 * Populate the library choice spinner
		 */
		ArrayAdapter<CharSequence> libraryAdapter = ArrayAdapter.createFromResource(this,
				R.array.libraries, android.R.layout.simple_spinner_item);

		//Specify the layout to use when the list of choices appears
		libraryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		//Apply the adapter to the spinner
		this.libraryChoiceSpinner.setAdapter(libraryAdapter);

		
		
		
		/**
		 * Populate the level choice spinner
		 */
		
		ArrayAdapter<String> levelAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, this.levels);
		this.libraryLevelSpinner.setAdapter(levelAdapter);
		
		/**
		 * Provide a click handler
		 * so that we can repopulate librarylevelspinner when
		 * we choose a different library.
		 */
		
		this.libraryChoiceSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

		    	if (position == 0) //SSAH
		    	{
		    		levels.clear();
		    		//Because the default library is SS&H, we'll fill it up with this:
		    		levels.add("Lvl1");
		    		levels.add("Lvl2");
		    		levels.add("Lvl3");
		    		levels.add("Lvl4");
		    		ArrayAdapter<String> levelAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, levels);
		    		libraryLevelSpinner.setAdapter(levelAdapter);
		    	} else if (position == 1) //Armus
		    	{
		    		levels.clear();
		    		//Because the default library is SS&H, we'll fill it up with this:
		    		levels.add("Lvl2");
		    		ArrayAdapter<String> levelAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, levels);
		    		libraryLevelSpinner.setAdapter(levelAdapter);
		    	} else if (position == 2) //Dentistry
		    	{
		    		levels.clear();
		    		//Because the default library is SS&H, we'll fill it up with this:
		    		levels.add("Lvl1");
		    		ArrayAdapter<String> levelAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, levels);
		    		libraryLevelSpinner.setAdapter(levelAdapter);
		    	} else if (position == 3) //BSL
		    	{
		    		levels.clear();
		    		//Because the default library is SS&H, we'll fill it up with this:
		    		levels.add("Lvl1");
		    		levels.add("Lvl2");
		    		levels.add("Lvl3");
		    		levels.add("Lvl4");
		    		ArrayAdapter<String> levelAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, levels);
		    		libraryLevelSpinner.setAdapter(levelAdapter);
		    	} else if (position == 4) //HML
		    	{
		    		levels.clear();
		    		//Because the default library is SS&H, we'll fill it up with this:
		    		levels.add("Lvl1");
		    		ArrayAdapter<String> levelAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, levels);
		    		libraryLevelSpinner.setAdapter(levelAdapter);
		    	} else if (position == 5) //Duhig
		    	{
		    		levels.clear();
		    		//Because the default library is SS&H, we'll fill it up with this:
		    		levels.add("Fryer");
		    		ArrayAdapter<String> levelAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, levels);
		    		libraryLevelSpinner.setAdapter(levelAdapter);
		    	}
		    }

		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {
		        // your code here
		    }

		});
		
		//Add a back button to the action bar
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_computer_availability, menu);
		return true;
	}
	
	public void getFloorplanPage(View view)
	{
		final Intent intent = new Intent(this, LibraryFloorPlan.class);

		//The duhig library has its own room code.
		if (this.libraryGetCodes[this.libraryChoiceSpinner.getSelectedItemPosition()] == "Duhig")
		{
			intent.putExtra("URL", "http://www.library.uq.edu.au/uqlsm/availablepcsembed.php?branch=Duhig&room=Fryer");
		} else {
			intent.putExtra("URL", Settings.libraryFloorPlanUrlStart +
					this.libraryGetCodes[this.libraryChoiceSpinner.getSelectedItemPosition()]
							+ Settings.libraryFloorPlanUrlEnd + this.libraryLevelSpinner.getSelectedItem());
		}
		
		
		//Go to results page
		startActivity(intent);
	}

}
