package com.emilevictor.wit.computeravailability;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import com.emilevictor.wit.R;

public class LibraryFloorPlan extends Activity {
	private String url;
	private WebView floorPlanWebView;

	@Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {       
        startActivity(new Intent(LibraryFloorPlan.this,ComputerAvailabilityLiveFloorPlans.class)); 
        return true;
    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_library_floor_plan);
		
		this.floorPlanWebView = (WebView) findViewById(R.id.libraryFloorPlan);
		
		
		Bundle extras = getIntent().getExtras();
		this.url = (String) extras.get("URL");
		
		this.floorPlanWebView.getSettings().setJavaScriptEnabled(true);
		this.floorPlanWebView.loadUrl(this.url);
		
		
		//Add a back button to the action bar
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_library_floor_plan, menu);
		return true;
	}

}
