package com.emilevictor.wit;

import com.emilevictor.wit.about_page.WhatIsThis;
import com.emilevictor.wit.buses.BusMenu;
import com.emilevictor.wit.computeravailability.ComputerAvailabilityLiveFloorPlans;
import com.emilevictor.wit.computeravailability.ComputerAvailabilityOverview;
import com.emilevictor.wit.wheres_my_class.WheresMyClass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	public void openWIT(View view)
	{
		final Intent intent = new Intent(this, com.emilevictor.wit.whats_in_there.InputWITClassActivity.class);
		
		startActivity(intent);

	}
	
	public void whatIsThisPage(View view)
	{
		final Intent intent = new Intent(this, WhatIsThis.class);
		
		startActivity(intent);
	}
	
	public void wheresMyClassPage(View view)
	{
		final Intent intent = new Intent(this, WheresMyClass.class);
		
		startActivity(intent);
	}
	
	public void computerAvailabilityPage(View view)
	{
		final Intent intent = new Intent(this, ComputerAvailabilityLiveFloorPlans.class);
		
		startActivity(intent);
	}
	
	public void computerAvailabilityOverviewPage(View view)
	{
		final Intent intent = new Intent(this, ComputerAvailabilityOverview.class);
		
		startActivity(intent);
	}
	
	public void liveBusInfo(View view)
	{
		final Intent intent = new Intent(this, BusMenu.class);
		
		startActivity(intent);
	}

}
