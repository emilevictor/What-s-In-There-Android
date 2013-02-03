package com.emilevictor.wit.uqrota;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.emilevictor.wit.R;
import com.emilevictor.wit.helpers.Network;
import com.emilevictor.wit.helpers.Settings;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

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
		this.superProgress = 25;
		
		this.progressBar.setProgress(this.superProgress);
		this.progressText.setText(R.string.uqRotaProgress1);
		//Animate the progress bar.
		progressBarHandler.postDelayed(animationRunnable,10);
		
		
		
		FetchUQRotaTimetableTask lFetchTask = new FetchUQRotaTimetableTask(this,
				this.progressBarHandler);
		
		
		try {
			this.mRotaClasses = lFetchTask.execute().get();
		} catch (InterruptedException e) {
			e.printStackTrace();
         	Toast.makeText(getApplicationContext(), R.string.InterruptedExceptionRotaFetch, Toast.LENGTH_LONG).show();

		} catch (ExecutionException e) {
			e.printStackTrace();
         	Toast.makeText(getApplicationContext(), R.string.InterruptedExceptionRotaFetch, Toast.LENGTH_LONG).show();
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
		private JSONParser jsonParser;
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
			this.jsonParser = new JSONParser();
			this.httpParams = new BasicHttpParams();
			this.httpParams.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			this.mPersistentCookieStore = new PersistentCookieStore(this.mAppContext);
			this.httpClient = new DefaultHttpClient();
			this.localContext.setAttribute(ClientContext.COOKIE_STORE,this.cookieStore);
			this.isResponse = null;

		}


		@Override
		protected List<RotaClass> doInBackground(Void... params) {

			try {
				mCurrentTimetable = checkForAndLoadCurrentTimetable();
			} catch (NoDefaultTimetableSetYetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//One call to the server to get appropriate group numbers.
			mGroupNumbers = getGroupNumbers();

			//Create a semaphore to stop the thread from continuing until all groups have been fetched.
			final Semaphore sem = new Semaphore(mGroupNumbers.size());

			//Now that we have the right groups (active groups)
			for (Long groupNum : mGroupNumbers)
			{
				try {
					sem.acquire();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				//Specify the group ID (of the specified active groups)
				RequestParams lReqParams = new RequestParams("group_id",groupNum);
				UQRotaRESTClient.get("groups/get",
						lReqParams, new JsonHttpResponseHandler() {
					
					@Override
					public void handleFailureMessage(Throwable e, String responseBody)
					{
						Log.w("Failure:", responseBody);
						e.printStackTrace();
					}
					
					@Override
					public void onFailure(Throwable e, JSONObject errorResponse)
					{
						e.printStackTrace();
					}
					
					@Override
					public void onSuccess(JSONObject lGroupObject)
					{
						publishProgress(75);
						//J S E P T I O N
						try {
							JSONObject lInnerGroupWithIDAndCourseName = (JSONObject) lGroupObject.get("group");
							String courseCodeAndType = (String) lInnerGroupWithIDAndCourseName.get("name");
							String courseType = (String) lInnerGroupWithIDAndCourseName.get("code");

							JSONArray lSessionsArray = (JSONArray)lInnerGroupWithIDAndCourseName.get("sessions");

							for (int i = 0; i < lSessionsArray.length(); i++)
							{
								JSONObject lSessionObject = lSessionsArray.getJSONObject(i);
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

								mRotaClasses.add(cls);
								sem.release();
							}

						} catch (JSONException e)
						{
							e.printStackTrace();
						}


					}

				});

			}
			
			try {
				sem.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return mRotaClasses;
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
				response = httpClient.execute(this.httpGetter,this.localContext);
			} catch (ClientProtocolException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			
			HttpEntity responseEntity = response.getEntity();
			try {
				isResponse = responseEntity.getContent();
			} catch (IllegalStateException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			String responseBody = null;
			try {
				responseBody = Network.convertInputStreamToString(isResponse);
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

			
			return (org.json.simple.JSONObject)JSONValue.parse(responseBody);
		}
		
		/**
		 * Get a list of group numbers for the timetable ID...
		 * @return
		 */
		private List<Long> getGroupNumbers()
		{
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

			//Get all the group tags, put them into a List<Long> of group numbers
			mGroupNumbers.clear();

			for (int i = 0; i < lGroups.length(); i++)
			{
				JSONObject jsonObject = null;
				try {
					jsonObject = lGroups.getJSONObject(i);
					try {
						mGroupNumbers.add(jsonObject.getLong("id"));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
			
			return mGroupNumbers;
		}

	}

}
