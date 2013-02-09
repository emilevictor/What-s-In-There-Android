package com.emilevictor.wit.uqrota;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.emilevictor.wit.R;
import com.emilevictor.wit.helpers.FileCache;
import com.emilevictor.wit.helpers.Network;
import com.emilevictor.wit.helpers.Settings;
import com.loopj.android.http.PersistentCookieStore;

public class ShowDefaultTimetable extends Activity {
	private List<RotaClass> mRotaClasses;
	private TextView progressText;
	private ProgressBar progressBar;
	private Integer superProgress;
	private Handler progressBarHandler = new Handler();
	private Runnable animationRunnable = new Runnable() {
		public void run() {
			if (progressBar.getProgress() < superProgress) {
				progressBar.incrementProgressBy(1);
				progressBar.invalidate();
				progressBarHandler.postDelayed(animationRunnable, 10); //run again after 10 ms

			}
		}};

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_show_default_timetable);
			
			

			//Initialize members
			this.progressText = (TextView) findViewById(R.id.uqRotaLoadingTimetableProgressText);
			this.progressBar = (ProgressBar) findViewById(R.id.uqrotaLoadingTimetableProgress);
			this.superProgress = 10;
			this.mRotaClasses = new ArrayList<RotaClass>();

			this.progressBar.setProgress(this.superProgress);
			this.progressText.setText(R.string.uqRotaProgress1);
			//Animate the progress bar.
			progressBarHandler.postDelayed(animationRunnable,10);

			//Check that the cache exists AND that we haven't changed the number of the timetable. If we have, attempt to fetch it.
			if (!FileCache.checkExistenceOfCachedTimetable(this) || !FileCache.checkCacheValidityAgainstCurrentTimetable(this))
			{
				FetchUQRotaTimetableTask lFetchTask = new FetchUQRotaTimetableTask(this,
						this.progressBarHandler);

				lFetchTask.execute();
			} else {
				//Fetch the timetables from the cache instead, hide progress bars, etc.
				File cacheDir = new File(getCacheDir(), "rotaTimetableCaches");
				File cacheFile = new File(cacheDir,"rotaTimetable");
				
				
				if (cacheFile.exists())
				{
					try {
						FileInputStream fis = new FileInputStream(cacheFile.getAbsolutePath());
						
						ObjectInputStream ois = new ObjectInputStream(fis);
						
						Object obj = null;
						
						while (ois.available() != -1)
						{
							obj = ois.readObject();
							if (obj instanceof RotaClass)
							{
								mRotaClasses.add((RotaClass)obj);
							}
						}
//						while ((obj = ois.readObject()) != null) {
//
//						}
						
						ois.close();
						
						
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (OptionalDataException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				
				handleClasses(this.mRotaClasses);
			}

			

		}
	

		private void handleClasses(List<RotaClass> classes)
		{
			this.mRotaClasses = classes;
			List<RotaClass> todaysClasses = new ArrayList<RotaClass>();

			Map<String,Integer> dayMap = new HashMap<String,Integer>();

			dayMap.put("Sun", 0);
			dayMap.put("Mon",1);
			dayMap.put("Tue",2);
			dayMap.put("Wed", 3);
			dayMap.put("Thu", 4);
			dayMap.put("Fri", 5);
			dayMap.put("Sat", 6);


			//Filter Rota Classes to only handle today.

			filterClassesByTodayOnly(todaysClasses, dayMap);

			Intent intent = new Intent(this, DisplayRotaTimetableActivity.class);

			int numberOfClassesToday = 0;
			for (RotaClass rc: todaysClasses)
			{

				intent.putExtra("ROTACLASS"+String.valueOf(numberOfClassesToday),
						rc);
				++numberOfClassesToday;
			}


			//Get filtertype from the bundle
			Bundle extras = getIntent().getExtras();
			int filterType = extras.getInt("filterType");

			intent.putExtra("numberOfClasses",numberOfClassesToday);
			intent.putExtra("filterType", filterType);

			startActivity(intent);
		}

		private void filterClassesByTodayOnly(List<RotaClass> todaysClasses,
				Map<String, Integer> dayMap) {
			Calendar rightNow = Calendar.getInstance();
			rightNow.get(Calendar.DAY_OF_WEEK);
			for (RotaClass rc : mRotaClasses)
			{
				todaysClasses.add(rc);

				//				if (rc.getDay().equals(dayMap.get(day)))
				//				{
				//					todaysClasses.add(rc);
				//				}
			}
		}

		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.activity_show_default_timetable, menu);
			return true;
		}

		protected class FetchUQRotaTimetableTask extends AsyncTask<Void, Integer, List<RotaClass>> {

			private Long mCurrentTimetable;
			private Context mAppContext;
			private List<Long> mGroupNumbers;
			private List<RotaClass> mRotaClasses;
			private Handler progressBarHandler;

			//HTTP stuff
			private HttpGet httpGetter;
			private InputStream isResponse;
			private HttpContext localContext;
			private CookieStore cookieStore;
			private HttpParams httpParams;
			private PersistentCookieStore mPersistentCookieStore;
			private HttpClient httpClient;


			/**
			 * Constructor must be called before execution so that the application context can be
			 * passed in. This is used for shared preferences access.
			 * @param appContext
			 */
			public FetchUQRotaTimetableTask(Context appContext, Handler progressBarHandler)
			{
				this.mAppContext = appContext;
				this.mRotaClasses = new ArrayList<RotaClass>();
				this.progressBarHandler = progressBarHandler;
				this.cookieStore = new BasicCookieStore();
				this.localContext = new BasicHttpContext();
				this.httpParams = new BasicHttpParams();
				this.httpParams.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
				this.mPersistentCookieStore = new PersistentCookieStore(this.mAppContext);
				this.httpClient = new DefaultHttpClient();
				this.localContext.setAttribute(ClientContext.COOKIE_STORE,this.cookieStore);
				this.isResponse = null;

			}

			@Override
			protected void onPostExecute(List<RotaClass> rotaClasses)
			{
				handleClasses(rotaClasses);
			}


			@Override
			protected List<RotaClass> doInBackground(Void... params) {
				
				setBarProgress(20,"Getting current timetable from memory.");

				try {
					mCurrentTimetable = checkForAndLoadCurrentTimetable();
				} catch (NoDefaultTimetableSetYetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				setBarProgress(50,"Getting classes");


				//One call to the server to get appropriate group numbers.
				mGroupNumbers = getGroupNumbers();

				enumerateRotaClasses();

				//CACHE the results as a file


				cacheRotaClasses();

				
				setBarProgress(100,"Done.");


				return mRotaClasses;
			}

			private void setBarProgress(int i,final String message) {
				// TODO Auto-generated method stub
				superProgress = i;
				progressBarHandler.post(new Runnable(){

					@Override
					public void run() {
						progressText.setText(message);
					}
					
				});
				progressBarHandler.postDelayed(animationRunnable,10);
			}


			private void cacheRotaClasses() {
				//Create rotaTimetableCache
				File cacheDir = new File(getCacheDir(), "rotaTimetableCaches");
				cacheDir.mkdirs();
				File cacheFile = new File(cacheDir,"rotaTimetable");
				try {
					cacheFile.createNewFile();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				try {
					ObjectOutputStream lOOS = new ObjectOutputStream(new FileOutputStream(cacheFile));
					for (RotaClass rc : mRotaClasses)
					{
						lOOS.writeObject(rc);
					}
					lOOS.flush();

					lOOS.close();

				} catch (StreamCorruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				//At the same time, write the current timetableId to the cache.
				File cacheDirId = new File(getCacheDir(), "rotaTimetableCaches");
				cacheDirId.mkdirs();
				File cacheFileId = new File(cacheDir,"rotaTimetableId");
				if (!cacheFileId.exists())
				{
					try {
						cacheFileId.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				try {
					DataOutputStream out = 
					        new DataOutputStream(new FileOutputStream(cacheFileId.getAbsolutePath()));
					
					out.writeLong(mCurrentTimetable);
					out.flush();
					out.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}


			protected void onProgressUpdate(Integer... progress)
			{
				super.onProgressUpdate(progress);
				superProgress = progress[0];
				progressBarHandler.postDelayed(animationRunnable,10);
			}

			protected Long checkForAndLoadCurrentTimetable() throws NoDefaultTimetableSetYetException
			{
				SharedPreferences settings = this.mAppContext.getSharedPreferences(Settings.preferencesFilename, 0);
				mCurrentTimetable = settings.getLong("rotaDefaultTimetableId", -1);

				if (mCurrentTimetable == -1)
				{
					throw new NoDefaultTimetableSetYetException();
				} else {
					return mCurrentTimetable;
				}

			}

			private void enumerateRotaClasses()
			{
				//Now that we have the right groups (active groups)
				for (final Long groupNum : mGroupNumbers)
				{
					//PROGRESS BAR
					superProgress += 5;
					progressBarHandler.postDelayed(animationRunnable,10);
					Log.w("progress:",superProgress.toString());


					JSONObject lGroupObject = getJSONObjectFromURL(Settings.UQRotaBaseUrl +
							"groups/get?group_id=" + String.valueOf(groupNum));

					//J S E P T I O N
					try {
						JSONObject lInnerGroupWithIDAndCourseName = (JSONObject) lGroupObject.get("group");
						String courseCodeAndType = (String) lInnerGroupWithIDAndCourseName.get("name");
						String courseType = (String) lInnerGroupWithIDAndCourseName.get("code");

						JSONArray lSessionsArray = (JSONArray)lInnerGroupWithIDAndCourseName.get("sessions");

						for (Object obj : lSessionsArray)
						{
							JSONObject lSessionObject = (JSONObject) obj;
							String day = (String)lSessionObject.get("day");
							Long timeStart = (Long)lSessionObject.get("start");
							Long timeFinish = (Long)lSessionObject.get("finish");
							String buildingNameFull = (String)lSessionObject.get("building");
							String room = (String)lSessionObject.get("room");
							String buildingNumber = (String)lSessionObject.get("building_number");

							//Create new rota class for local storage
							RotaClass cls = new RotaClass();
							cls.setStartTime(timeStart);
							cls.setFinishTime(timeFinish);
							cls.setBuilding(buildingNameFull);
							cls.setBuildingNo(buildingNumber);
							cls.setDay(day);
							cls.setCourseNameAndType(courseCodeAndType);
							cls.setCourseType(courseType);
							cls.setRoom(room);

							mRotaClasses.add(cls);
						}

					} catch (Exception e)
					{
						e.printStackTrace();
					}



				}
			}

			/**
			 * Takes a URL, returns a JSON Object in a synchronous, blocking fashion.
			 * @param url
			 * @return
			 */
			private JSONObject getJSONObjectFromURL(String url)
			{


				//Now use traditional, synchronous method of calling everything to get
				//our timetable.
				this.httpGetter = new HttpGet(url);

				this.cookieStore.addCookie(this.mPersistentCookieStore.getCookies().get(0));

				HttpResponse response = null;
				try {
					response = this.httpClient.execute(this.httpGetter,this.localContext);
				} catch (ClientProtocolException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
					Log.w("url",url);
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
					Log.w("url",url);
				}

				HttpEntity responseEntity = response.getEntity();
				try {
					isResponse = responseEntity.getContent();
				} catch (IllegalStateException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
					Log.w("url",url);
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
					Log.w("url",url);
				}
				String responseBody = null;
				try {
					responseBody = Network.convertInputStreamToString(isResponse);
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
					Log.w("url",url);
				}


				return (JSONObject)JSONValue.parse(responseBody);
			}

			/**
			 * Get a list of group numbers for the timetable ID...
			 * @return
			 */
			private List<Long> getGroupNumbers()
			{
				mGroupNumbers = new ArrayList<Long>();
				JSONObject outerJSONObject = getJSONObjectFromURL(Settings.UQRotaBaseUrl +
						"timetables/get?timetable_id="+String.valueOf(this.mCurrentTimetable));
				JSONObject lInnerTimetableObject = null;

				JSONArray lGroups = null;

				try {
					lInnerTimetableObject = (JSONObject) outerJSONObject.get("timetable");

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


				try {
					lGroups = (JSONArray) lInnerTimetableObject.get("groups");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


				for (Object obj : lGroups)
				{
					JSONObject jsonObject = (JSONObject)obj;


					try {
						mGroupNumbers.add((Long)jsonObject.get("id"));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				return mGroupNumbers;
			}

		}

}
