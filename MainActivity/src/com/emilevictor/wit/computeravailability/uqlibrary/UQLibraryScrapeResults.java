package com.emilevictor.wit.computeravailability.uqlibrary;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.emilevictor.wit.MainActivity;
import com.emilevictor.wit.R;

public class UQLibraryScrapeResults extends Activity {

	private ListView listView;
	private final List<Library> mLibraries = new ArrayList<Library>();

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem)
	{       
		startActivity(new Intent(UQLibraryScrapeResults.this,MainActivity.class)); 
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
		if (keyCode == KeyEvent.KEYCODE_BACK ) {
			startActivity(new Intent(UQLibraryScrapeResults.this,MainActivity.class)); 
			return true;
		}

		return super.onKeyDown(keyCode, event);
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
		setContentView(R.layout.scraped_uq_library_results);


		//Unpack the rooms
		Bundle extras = getIntent().getExtras();
		int numberOfRooms = extras.getInt("numberOfLibraries");
		Library[] libraries = new Library[numberOfRooms];

		//Get all of the bundled classes from the main activity.
		for (int i = 0; i < numberOfRooms; i++)
		{
			libraries[i] = (Library) getIntent().getSerializableExtra("LIBRARY"+String.valueOf(i));
			this.mLibraries.add((Library) getIntent().getSerializableExtra("LIBRARY"+String.valueOf(i)));
		}

		this.listView = (ListView)findViewById(R.id.UQLibraryOverviewList);

		//Create the adapter for our listview

		UQLibraryScrapeAdapter adapter = new UQLibraryScrapeAdapter(this, 
				R.layout.uq_library_item_row, libraries);

		ListView listView = (ListView)findViewById(R.id.UQLibraryOverviewList);

		View header = (View)getLayoutInflater().inflate(R.layout.uq_library_header_row, null);
		listView.addHeaderView(header);

		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long rowId) {
				if (mLibraries.get(position+1).getExtraComments() != null)
				{
					AlertDialog.Builder adb = new AlertDialog.Builder(
							UQLibraryScrapeResults.this);
					adb.setTitle("Extra comments");
					adb.setMessage(mLibraries.get(position+1).getExtraComments());

					
					adb.setPositiveButton("Ok", null);
					adb.show();    

				}
			}



		});

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
