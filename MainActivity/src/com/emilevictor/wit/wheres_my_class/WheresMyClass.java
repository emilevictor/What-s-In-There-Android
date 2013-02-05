package com.emilevictor.wit.wheres_my_class;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.emilevictor.wit.MainActivity;
import com.emilevictor.wit.R;
import com.emilevictor.wit.helpers.Class;
import com.emilevictor.wit.helpers.Network;
import com.emilevictor.wit.helpers.Settings;

public class WheresMyClass extends Activity {

	private TextView progressText;
	private ProgressBar progressBar;
	private TextView enterYourCourseCodeLabel;
	private EditText courseCodeTextField;
	private Button findClassButton;
	private Spinner daySpinner;
	private TextView dayLabel;

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
	    public boolean onOptionsItemSelected(MenuItem menuItem)
	    {       
	        startActivity(new Intent(WheresMyClass.this,MainActivity.class)); 
	        return true;
	    }

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
			this.dayLabel = (TextView) findViewById(R.id.dayLabelWMC);

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
			
			
			Log.w("I got here","Intent");
			//Show progress bar, hide everything else!
			this.progressBar.setVisibility(View.VISIBLE);
			this.progressText.setVisibility(View.VISIBLE);
			this.enterYourCourseCodeLabel.setVisibility(View.GONE);
			this.courseCodeTextField.setVisibility(View.GONE);
			this.findClassButton.setVisibility(View.GONE);
			this.dayLabel.setVisibility(View.GONE);
			this.daySpinner.setVisibility(View.GONE);

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

						Log.w("I get here","1");

						//Get the id of the class given
						EditText courseCode = (EditText) findViewById(R.id.courseCodeTextfield);
						String courseCodeString = courseCode.getText().toString();

						//Get rid of any whitespace.
						courseCodeString = courseCodeString.trim().toUpperCase(Locale.ENGLISH);

						RetreiveCourseIDTask rcidt = new RetreiveCourseIDTask();

						String courseID = null;

						try {
							Log.w("I got here","Running background task");
							courseID = rcidt.execute(generateCourseIDURL(courseCodeString)).get();
							Log.w("I got here","After background task");
						} catch (InterruptedException e) {
							e.printStackTrace();
						} catch (ExecutionException e) {
							e.printStackTrace();
						}

						if (courseID != null)
						{
							//Then using that ID, go a query for that class at:
							//http://rota.eait.uq.edu.au/offering/<id>.xml

							RetreiveCourseTimetableTask rctt = new RetreiveCourseTimetableTask();

							List<com.emilevictor.wit.helpers.Class> classes = new ArrayList<com.emilevictor.wit.helpers.Class>();

							String url = "http://rota.eait.uq.edu.au/offering/" + courseID + ".xml";
							try {
								classes = rctt.execute(url).get();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (ExecutionException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							if (classes != null)
							{
								Log.w("Number of classes", String.valueOf(classes.size()));
							} else {
								Log.w("Number of classes", "NULL");
							}

							//Find this semester's classes - the returned classes may not include the right ones.
							int numberOfClassesToday = 0; //This is used to keep track of the number being sent through intent.

							for (Class cls : classes)
							{

								Log.d("selectedDay, dayMap day, cls.day", ((Integer)selectedDay).toString() + " " +  dayMap.get(cls.day) + " " + cls.day);
								if (dayMap.get(cls.day) == selectedDay)
								{
									intent.putExtra("CLASS"+String.valueOf(numberOfClassesToday), cls);
									numberOfClassesToday += 1;
								}
							}

							//Provide the intent with some of the information that we gathered here.
							intent.putExtra("numberOfClassesToday", numberOfClassesToday);

							//Go to the results page.
							progressBarHandler.removeCallbacks(animationRunnable);

							//Remove the progress bar from the main UI before going to the new page.
							progressBarHandler.post(new Runnable() {

								@Override
								public void run() {
									progressBar.setVisibility(View.GONE);
									progressText.setVisibility(View.GONE);

									enterYourCourseCodeLabel.setVisibility(View.VISIBLE);
									courseCodeTextField.setVisibility(View.VISIBLE);
									findClassButton.setVisibility(View.VISIBLE);
									dayLabel.setVisibility(View.VISIBLE);
									daySpinner.setVisibility(View.VISIBLE);

								}

							});
							

							//Reset the progress bar
							progressStatus = 0;
							progressBar.setProgress(progressStatus);
							
							startActivity(intent);


						} else {
							//The course was not found, return user to the front page.


							progressBarHandler.post(new Runnable() {
								@Override
								public void run() {
									// This gets executed on the UI thread so it can safely modify Views
									progressBar.setVisibility(View.GONE);
									progressText.setVisibility(View.GONE);
									enterYourCourseCodeLabel.setVisibility(View.VISIBLE);
									courseCodeTextField.setVisibility(View.VISIBLE);
									findClassButton.setVisibility(View.VISIBLE);
									dayLabel.setVisibility(View.VISIBLE);
									daySpinner.setVisibility(View.VISIBLE);

									Toast.makeText(getApplicationContext(), R.string.error_course_doesnt_exist, Toast.LENGTH_LONG).show();
								}
							});


						}


					} else {
						//Show the toast telling the user that they need to connect to wifi.
						progressBarHandler.post(new Runnable() {

							@Override
							public void run() {
								progressBar.setVisibility(View.GONE);
								progressText.setVisibility(View.GONE);
								enterYourCourseCodeLabel.setVisibility(View.VISIBLE);
								courseCodeTextField.setVisibility(View.VISIBLE);
								findClassButton.setVisibility(View.VISIBLE);
								dayLabel.setVisibility(View.VISIBLE);
								daySpinner.setVisibility(View.VISIBLE);
								
								Toast.makeText(getApplicationContext(), R.string.waiting_on_connection, Toast.LENGTH_LONG).show();
							}

						});
					}
				}

				private String generateCourseIDURL(String courseCodeString) {
					return Settings.idAPIurlStart + courseCodeString + Settings.idAPIurlEnd;
				}

			}).start();




		}

}
