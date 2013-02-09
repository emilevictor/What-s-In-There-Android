package com.emilevictor.wit.buses;

import com.emilevictor.wit.MainActivity;
import com.emilevictor.wit.R;

import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
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
		setContentView(R.layout.activity_bus_menu);
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
	
	public void RCHHerstonBus(View view)
	{
		final Intent intent = new Intent(this, LiveBusInfo.class);
		
		//Let the next screen know that we want the uq lakes page.
		intent.putExtra("stop", "herston");
		
		startActivity(intent);
	}
	
	public void GattonBus(View view)
	{
		final Intent intent = new Intent(this, LiveBusInfo.class);
		
		//Let the next screen know that we want the uq lakes page.
		intent.putExtra("stop", "gatton");
		
		startActivity(intent);
	}
	
	public void ipswichBus(View view)
	{
		final Intent intent = new Intent(this, LiveBusInfo.class);
		
		//Let the next screen know that we want the uq lakes page.
		intent.putExtra("stop", "ipswichBus");
		
		startActivity(intent);
	}
	
	public void ipswichTrain(View view)
	{
		final Intent intent = new Intent(this, LiveBusInfo.class);
		
		//Let the next screen know that we want the uq lakes page.
		intent.putExtra("stop", "ipswichTrain");
		
		startActivity(intent);
	}
	
	public void ParkRdTrains(View view)
	{
		final Intent intent = new Intent(this, LiveBusInfo.class);
		
		//Let the next screen know that we want the uq lakes page.
		intent.putExtra("stop", "parkRdTrain");
		
		startActivity(intent);
	}
	
	public void ToowongTrain(View view)
	{
		final Intent intent = new Intent(this, LiveBusInfo.class);
		
		//Let the next screen know that we want the uq lakes page.
		intent.putExtra("stop", "toowongTrain");
		
		startActivity(intent);
	}

}
