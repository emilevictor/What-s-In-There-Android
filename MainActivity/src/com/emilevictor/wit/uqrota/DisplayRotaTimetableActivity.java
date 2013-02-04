package com.emilevictor.wit.uqrota;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.emilevictor.wit.MainActivity;
import com.emilevictor.wit.R;

public class DisplayRotaTimetableActivity extends Activity {

	private ListView listView;
	private Map<String,Integer> dayMap = new HashMap<String,Integer>();
	
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem)
	{       
		startActivity(new Intent(DisplayRotaTimetableActivity.this,MainActivity.class)); 
		return true;
	}
	
	private Comparator<RotaClass> mComparator = new Comparator<RotaClass>()
			{

				@Override
				public int compare(RotaClass lhs, RotaClass rhs) {
					int compared;
					if ((compared = (dayMap.get(lhs.getDay()).compareTo(dayMap.get(rhs.getDay())))) == 0)
					{
						//same day
						return lhs.getStartTime().compareTo(rhs.getStartTime());
					} else {
						return compared;
					}
				}
		
			};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_uq_rota_timetable_results);
		
		//make daymap available.
		dayMap.put("Sun", 0);
		dayMap.put("Mon",1);
		dayMap.put("Tue",2);
		dayMap.put("Wed", 3);
		dayMap.put("Thu", 4);
		dayMap.put("Fri", 5);
		dayMap.put("Sat", 6);
		
		//Add a back button to the action bar
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		//Unpack the RotaClass
		Bundle extras = getIntent().getExtras();
		int numberOfRotaClass = extras.getInt("numberOfClasses");
		
		//Setting a filter so that we can figure out what to show.
		int filterType = extras.getInt("filterType");
		
		//Unsorted list of rota classes. We need a comparator to sort these.
		List<RotaClass> unsortedRotaClasses = new ArrayList<RotaClass>();
		
		RotaClass[] RotaClass = new RotaClass[numberOfRotaClass];
		
		//Get all of the bundled classes from the main activity.
		for (int i = 0; i < numberOfRotaClass; i++)
		{
			unsortedRotaClasses.add((RotaClass) getIntent().getSerializableExtra("ROTACLASS"+String.valueOf(i)));
		}
		
		//SORT HERE
		Collections.sort(unsortedRotaClasses,mComparator);
		
		
		//Put them back into an array for display.
		
		for (int i = 0; i < numberOfRotaClass; i++)
		{
			RotaClass[i] = unsortedRotaClasses.get(i);
		}
		
	
		
		this.listView = (ListView)findViewById(R.id.computerOverviewList);
		
		//Create the adapter for our listview
		UQRotaTimetableAdapter adapter = new UQRotaTimetableAdapter(this, 
				R.layout.uqrota_item_row, RotaClass);

		ListView listView = (ListView)findViewById(R.id.uqRotaTimetableList);

		View header = (View)getLayoutInflater().inflate(R.layout.uqrota_header_row, null);
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
