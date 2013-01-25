package com.emilevictor.wit.buses;

import com.emilevictor.wit.MainActivity;
import com.emilevictor.wit.R;
import com.emilevictor.wit.R.layout;
import com.emilevictor.wit.R.menu;
import com.emilevictor.wit.computeravailability.ComputerAvailabilityLiveFloorPlans;
import com.emilevictor.wit.computeravailability.LibraryFloorPlan;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class BusMenu extends Activity {
	
	@Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {       
        startActivity(new Intent(BusMenu.this,MainActivity.class)); 
        return true;
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bus_menu);
		
		//Add a back button to the action bar
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_bus_menu, menu);
		return true;
	}
	
	public void chancellorsPlace(View view)
	{
		final Intent intent = new Intent(this, LiveBusInfo.class);
		//Let the next screen know that we want the chancellor's place stop.
		intent.putExtra("stop", "cp");
		
		startActivity(intent);
	}
	
	public void UQLakes(View view)
	{
		final Intent intent = new Intent(this, LiveBusInfo.class);
		
		//Let the next screen know that we want the uq lakes page.
		intent.putExtra("stop", "uql");
		
		startActivity(intent);
	}

}
