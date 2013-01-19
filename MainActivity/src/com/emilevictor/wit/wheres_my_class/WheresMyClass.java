package com.emilevictor.wit.wheres_my_class;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.emilevictor.wit.R;
import com.emilevictor.wit.helpers.Network;
import com.emilevictor.wit.helpers.Settings;

public class WheresMyClass extends Activity {

	private TextView progressText;
	private ProgressBar progressBar;
	private TextView enterYourCourseCodeLabel;
	private EditText courseCodeTextField;
	private Button findClassButton;
	private Spinner daySpinner;

	public Map<String,Integer> dayMap;
	public int selectedDay;
	private int day;

	private Handler progressBarHandler = new Handler();
	private int progressStatus = 0;

	private Runnable animationRunnable = new Runnable() {
		public void run() {
			if (progressBar.getProgress() < progressStatus) {
				progressBar.incrementProgressBy(1);
				progressBar.invalidate();
				progressBarHandler.postDelayed(animationRunnable, 10); //run again after 10 ms

			}
		}};

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_wheres_my_class);

			this.progressText = (TextView) findViewById(R.id.progressTextWheresMyClass);
			this.progressBar = (ProgressBar) findViewById(R.id.progressBarWheresMyClass);
			this.enterYourCourseCodeLabel = (TextView) findViewById(R.id.enterYourCourseCodeLabel);
			this.courseCodeTextField = (EditText) findViewById(R.id.courseCodeTextfield);
			this.findClassButton = (Button) findViewById(R.id.button_findClass);
			this.daySpinner = (Spinner) findViewById(R.id.daysSpinnerWheresMyClass);
			
			
			this.progressBar.setVisibility(View.INVISIBLE);
			this.progressText.setVisibility(View.INVISIBLE);

			/*
			 * Set up the day spinner
			 */

			//Get the day of the week
			Calendar rightNow = Calendar.getInstance();
			day = rightNow.get(Calendar.DAY_OF_WEEK);
			day -= 1; //To reflect the fact that we'll be addressing an array index.

			Log.d("day of week", "value: " + ((Integer)day).toString());

			Spinner daySpinner = (Spinner) findViewById(R.id.daysSpinnerWheresMyClass);

			//Setting up map datatypes for various uses.
			String[] daysStringArray = {"Sunday", "Monday", "Tuesday", "Wednesday",
					"Thursday", "Friday", "Saturday"};
			dayMap = new HashMap<String,Integer>();

			dayMap.put("Sun", 0);
			dayMap.put("Mon",1);
			dayMap.put("Tue",2);
			dayMap.put("Wed", 3);
			dayMap.put("Thu", 4);
			dayMap.put("Fri", 5);
			dayMap.put("Sat", 6);


			daysStringArray[day] = daysStringArray[day] + " (today)";

			ArrayAdapter<CharSequence> dayAdapter = new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item,
					daysStringArray);



			dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			daySpinner.setAdapter(dayAdapter);

			//Set the spinner to automatically select today.
			daySpinner.setSelection(day);

		}

		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.activity_wheres_my_class, menu);
			return true;
		}

		public void findClass(View view)
		{
			final Intent intent = new Intent(this, WheresMyClassResults.class);

			//Show progress bar, hide everything else!
			this.progressBar.setVisibility(View.VISIBLE);
			this.progressText.setVisibility(View.VISIBLE);
			this.enterYourCourseCodeLabel.setVisibility(View.INVISIBLE);
			this.courseCodeTextField.setVisibility(View.INVISIBLE);
			this.findClassButton.setVisibility(View.INVISIBLE);

			//Hide the keyboard
			InputMethodManager imm = (InputMethodManager)getSystemService(
					Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(this.courseCodeTextField.getWindowToken(), 0);


			selectedDay = this.daySpinner.getSelectedItemPosition();

			//Call the UQRota API to get the ID of the class:
			//http://rota.eait.uq.edu.au/offerings/find.xml?with={%22course_code%22:%22INFS3200%22,%22semester_id%22:%226220%22}

			new Thread(new Runnable() {

				@Override
				public void run() {
					Network network = new Network(getApplicationContext());
					
					//Check the connection
					if (network.getConnectivityStatus())
					{
						// Update the progress bar
						progressStatus = 10;
						progressBarHandler.post(new Runnable() {
							@Override
							public void run() {
								// This gets executed on the UI thread so it can safely modify Views
								progressText.setText("Waiting for eduroam to be reasonable...");
							}
						});
						progressBarHandler.postDelayed(animationRunnable,10);
						
						//Get the id of the class given
						EditText courseCode = (EditText) findViewById(R.id.courseCodeTextfield);
						String courseCodeString = courseCode.getText().toString();
						
						//Get rid of any whitespace.
						courseCodeString.trim();
						
						RetreiveCourseIDTask rcidt = new RetreiveCourseIDTask();
						
						String courseID = null;
						
						try {
							courseID = rcidt.execute(generateCourseIDURL(courseCodeString)).get();
						} catch (InterruptedException e) {
							e.printStackTrace();
						} catch (ExecutionException e) {
							e.printStackTrace();
						}
						
						if (courseID != null)
						{
							
						} else {
							//The course was not found, return user to the front page.
							//TODO fill this in
						}
						
						
					} else {
						Toast.makeText(getApplicationContext(), R.string.waiting_on_connection, Toast.LENGTH_LONG).show();
					}
				}

				private String generateCourseIDURL(String courseCodeString) {
					return Settings.idAPIurlStart + courseCodeString + Settings.idAPIurlEnd;
				}
				
			});
			
			//Then using that ID, go a query for that class at:
			//http://rota.eait.uq.edu.au/offering/<id>.xml


		}

}
