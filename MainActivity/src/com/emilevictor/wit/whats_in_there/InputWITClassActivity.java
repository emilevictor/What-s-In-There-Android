package com.emilevictor.wit.whats_in_there;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.xmlpull.v1.XmlPullParserException;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.emilevictor.wit.whats_in_there.RoomBuildingParser.InvalidInputException;
import com.emilevictor.wit.whats_in_there.buildingXMLParser.Building;

public class InputWITClassActivity extends Activity {

	public static final String WIFI = "Wi-Fi";
	public static final String ANY = "Any";
	private static final String buildingsXMLUrl =
			"http://rota.eait.uq.edu.au/buildings.xml";

	private String room;
	private String building;
	private ProgressBar progressBar;
	private TextView progressText;
	private TextView campusLabel;
	public Spinner campusChoiceSpinner;
	private TextView buildingRoomInstructionLabel;
	private EditText roomTextfield;
	private TextView dayLabel;
	private Spinner daysSpinner;
	private Button buttonSend;

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
	    public boolean onOptionsItemSelected(MenuItem menuItem)
	    {       
	        startActivity(new Intent(InputWITClassActivity.this,MainActivity.class)); 
	        return true;
	    }



		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);


			setContentView(R.layout.input_wit_class);

			//Add a back button to the action bar
			ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
			
			//Make all objects available for 
			this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
			this.progressText = (TextView) findViewById(R.id.progressText);
			this.campusLabel = (TextView) findViewById(R.id.campusLabel);
			this.campusChoiceSpinner = (Spinner) findViewById(R.id.campusChoiceSpinner);
			this.buildingRoomInstructionLabel = (TextView) findViewById(R.id.buildingRoomInstructionLabel);
			this.roomTextfield = (EditText) findViewById(R.id.room_textfield);
			this.dayLabel = (TextView) findViewById(R.id.dayLabel);
			this.daysSpinner = (Spinner) findViewById(R.id.daysSpinner);
			this.buttonSend = (Button) findViewById(R.id.button_send);


			//Hide the progress bar and its text.
			this.progressBar.setVisibility(View.INVISIBLE);
			this.progressText.setVisibility(View.INVISIBLE);

			/*
			 * Set up the campus spinner
			 */

			Spinner campusSpinner = (Spinner) findViewById(R.id.campusChoiceSpinner);

			//Create an arrayadapter using the string array and a default spinner layout
			ArrayAdapter<CharSequence> campusAdapter = ArrayAdapter.createFromResource(this,
					R.array.campuses, android.R.layout.simple_spinner_item);

			//Specify the layout to use when the list of choices appears
			campusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			//Apply the adapter to the spinner
			campusSpinner.setAdapter(campusAdapter);

			/*
			 * Set up the day spinner
			 */

			//Get the day of the week
			Calendar rightNow = Calendar.getInstance();
			day = rightNow.get(Calendar.DAY_OF_WEEK);
			day -= 1; //To reflect the fact that we'll be addressing an array index.

			Log.d("day of week", "value: " + ((Integer)day).toString());

			Spinner daySpinner = (Spinner) findViewById(R.id.daysSpinner);

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
			getMenuInflater().inflate(R.menu.activity_main, menu);
			return true;
		}



		public void findRoomButtonPressed(View view) throws IOException, XmlPullParserException, InterruptedException, ExecutionException {
			final Intent intent = new Intent(this, FindRoomContentsActivity.class);
			//Show progress bar, hide everything else!
			this.progressBar.setVisibility(View.VISIBLE);
			this.progressText.setVisibility(View.VISIBLE);
			this.campusLabel.setVisibility(View.INVISIBLE);
			this.campusChoiceSpinner.setVisibility(View.INVISIBLE);
			this.buildingRoomInstructionLabel.setVisibility(View.INVISIBLE);
			this.roomTextfield.setVisibility(View.INVISIBLE);
			this.dayLabel.setVisibility(View.INVISIBLE);
			this.daysSpinner.setVisibility(View.INVISIBLE);
			this.buttonSend.setVisibility(View.INVISIBLE);

			//Hide the keyboard
			InputMethodManager imm = (InputMethodManager)getSystemService(
					Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(this.roomTextfield.getWindowToken(), 0);


			selectedDay = this.daysSpinner.getSelectedItemPosition();
			//Progress bar animation runnable



			//Start a thread so that the UI can be updated in real time.
			new Thread(new Runnable() {
				public void run() {

					/**
					 * Hit up the API to get the building number.
					 */
					//Check that we are connected.
					Network network = new Network(getApplicationContext());


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

						List<Building> buildings = null;
						RetreiveBuildingIdTask retBuild = new RetreiveBuildingIdTask();

						try {
							buildings = retBuild.execute(buildingsXMLUrl).get();
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (ExecutionException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}


						// Update the progress bar
						progressStatus = 25;
						progressBarHandler.post(new Runnable() {
							@Override
							public void run() {
								// This gets executed on the UI thread so it can safely modify Views
								progressText.setText("Feeding bush turkeys...");
							}
						});
						progressBarHandler.postDelayed(animationRunnable,10);

						EditText roomInput = (EditText) findViewById(R.id.room_textfield);
						RoomBuildingParser rbp = new RoomBuildingParser(roomInput.getText().toString());
						// Update the progress bar

						progressBarHandler.post(new Runnable() {
							@Override
							public void run() {
								// This gets executed on the UI thread so it can safely modify Views
								progressText.setText("Finding your room...");
							}
						});
						progressStatus = 40;
						progressBarHandler.postDelayed(animationRunnable,10);

						List<String> roomAndBuilding;
						try {
							// Update the progress bar
							progressStatus = 50;
							progressBarHandler.post(new Runnable() {
								@Override
								public void run() {
									// This gets executed on the UI thread so it can safely modify Views
									progressText.setText("Waiting for mysi-net to load, as usual...");
								}
							});

							progressBarHandler.postDelayed(animationRunnable,10);

							roomAndBuilding = rbp.giveMeRoomAndBuildingSeparately();

							//Campus code generation

							String[] campusCodes = {"STLUC","IPSWC", "GATTN", "HERST"};

							String currentCampus = campusCodes[campusChoiceSpinner.getSelectedItemPosition()];

							String returnedBuildingString = buildingXMLParser.getBuildingIdFromNumber(roomAndBuilding.get(0),
									currentCampus,buildings);

							//The building was not found - oh noes!
							if (returnedBuildingString.equals("NOTFOUND"))
							{
								throw new NoBuildingFoundException("The building that you entered was incorrect.");
							} else {
								//The building was successfully found.

								building = returnedBuildingString;
								room = roomAndBuilding.get(1);

								Log.d("The building id that was requested",building.toString());
								Log.d("The room that was requested",room.toString());

								// Update the progress bar
								progressStatus = 70;
								progressBarHandler.post(new Runnable() {
									@Override
									public void run() {
										// This gets executed on the UI thread so it can safely modify Views
										progressText.setText("Hitting up UQ APIs...");
									}
								});
								progressBarHandler.postDelayed(animationRunnable,10);

								//Here we need to execute the task to hit the API for room contents.
								List<Class> classes = null;
								RetreiveClassesTask retClasses = new RetreiveClassesTask();
								//Construct the custom API url for the GET request
								String classesAPIUrl = "http://rota.eait.uq.edu.au/building/"+building+"/room/"+room+"/sessions.xml";

								//TODO REMOVE ME
								Log.d("API URL", classesAPIUrl);



								classes = retClasses.execute(classesAPIUrl).get();
								// Update the progress bar
								progressStatus = 85;
								progressBarHandler.post(new Runnable() {
									@Override
									public void run() {
										// This gets executed on the UI thread so it can safely modify Views
										progressText.setText("Finding the right class...");
									}
								});
								progressBarHandler.postDelayed(animationRunnable,10);

								//Find this semester's classes - the returned classes may not include the right ones.
								int numberOfClassesToday = 0; //This is used to keep track of the number being sent through intent.

								for (Class cls : classes)
								{

									Log.d("selectedDay, dayMap day, cls.day", ((Integer)selectedDay).toString() + " " +  dayMap.get(cls.day) + " " + cls.day);
									if (cls.semesterId.equals(Settings.currentSemesterNumber)
											&& dayMap.get(cls.day) == selectedDay
											)
									{

										intent.putExtra("CLASS"+String.valueOf(numberOfClassesToday), cls);
										numberOfClassesToday += 1;
									}
								}

								Log.d("Number of classes", String.valueOf(numberOfClassesToday));

								//Provide the intent with some of the information that we gathered here.
								intent.putExtra("numberOfClassesToday", numberOfClassesToday);



								// Update the progress bar
								progressStatus = 100;
								progressBarHandler.post(new Runnable() {
									@Override
									public void run() {
										// This gets executed on the UI thread so it can safely modify Views
										progressText.setText("And we're done here!");
									}
								});
								progressBarHandler.postDelayed(animationRunnable,10);


							}

							//Go to the results page.
							progressBarHandler.removeCallbacks(animationRunnable);

							//Remove the progress bar from the main UI before going to the new page.
							progressBarHandler.post(new Runnable() {

								@Override
								public void run() {
									progressBar.setVisibility(View.INVISIBLE);
									progressText.setVisibility(View.INVISIBLE);
									campusLabel.setVisibility(View.VISIBLE);
									campusChoiceSpinner.setVisibility(View.VISIBLE);
									buildingRoomInstructionLabel.setVisibility(View.VISIBLE);
									roomTextfield.setVisibility(View.VISIBLE);
									dayLabel.setVisibility(View.VISIBLE);
									daysSpinner.setVisibility(View.VISIBLE);
									buttonSend.setVisibility(View.VISIBLE);

								}

							});


							//Reset the progress bar
							progressStatus = 0;
							progressBar.setProgress(progressStatus);

							//Finally, go to the results page.
							startActivity(intent);
						} catch (InvalidInputException e) {

							e.printStackTrace();
						} catch (NoBuildingFoundException e) {
							// TODO add logic to warn user that the building that they entered was invalid.

							e.printStackTrace();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ExecutionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}



					} else {
						//Show the toast telling the user that they need to connect to wifi.
						progressBarHandler.post(new Runnable() {

							@Override
							public void run() {
								Toast.makeText(getApplicationContext(), R.string.waiting_on_connection, Toast.LENGTH_LONG).show();
							}

						});
						
					}



				}
			}).start();






		}

}
