package com.emilevictor.wit.computeravailability.uqlibrary;

import com.emilevictor.wit.R;
import com.emilevictor.wit.R.layout;
import com.emilevictor.wit.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ScrapeUQLibraryInfo extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scrape_uqlibrary_info);
		
		//Need to send request to UQ Library hours page first, populate libraries.
		
		ASyncTaskScrapeUQLibraryInfo scraper = new ASyncTaskScrapeUQLibraryInfo();
		scraper.execute();
		
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_scrape_uqlibrary_info, menu);
		return true;
	}

}
