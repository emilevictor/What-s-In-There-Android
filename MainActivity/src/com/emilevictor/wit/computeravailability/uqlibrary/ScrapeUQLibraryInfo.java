package com.emilevictor.wit.computeravailability.uqlibrary;

import java.util.List;
import java.util.concurrent.ExecutionException;

import com.emilevictor.wit.R;
import com.emilevictor.wit.R.layout;
import com.emilevictor.wit.R.menu;
import com.emilevictor.wit.computeravailability.ComputerAvailabilityOverviewResults;
import com.emilevictor.wit.computeravailability.Room;
import com.emilevictor.wit.helpers.Settings;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class ScrapeUQLibraryInfo extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_scrape_uqlibrary_info);
			final Intent intent = new Intent(this, UQLibraryScrapeResults.class);
			

			//Thread is used to allow UI to draw while calling get() below.
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						//Need to send request to UQ Library hours page first, populate libraries.
						ASyncTaskScrapeUQLibraryInfo scraper = new ASyncTaskScrapeUQLibraryInfo();
						List<Library> libraries = scraper.execute().get();
						//Current room
						int thisLibraryIndex = 0;
						//Set the number of rooms
						for (Library lib : libraries)
						{
							intent.putExtra("LIBRARY"+String.valueOf(thisLibraryIndex),lib);
							thisLibraryIndex++;
						}

						intent.putExtra("numberOfLibraries", thisLibraryIndex);

						startActivity(intent);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}).start();
			
			
			
			
			
			
			
		}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_scrape_uqlibrary_info, menu);
		return true;
	}

}
