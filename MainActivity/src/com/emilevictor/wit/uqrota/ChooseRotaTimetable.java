package com.emilevictor.wit.uqrota;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.emilevictor.wit.MainActivity;
import com.emilevictor.wit.R;
import com.emilevictor.wit.helpers.JSONParser;
import com.emilevictor.wit.helpers.Network;
import com.emilevictor.wit.helpers.Settings;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;

public class ChooseRotaTimetable extends Activity {

	protected AsyncHttpClient myAsyncClient;
	protected PersistentCookieStore mPersistentCookieStore;
	private HttpGet httpGetTimetables;
	private HttpContext localContext;
	private CookieStore cookieStore;
	private HttpClient httpClient;
	private HttpParams httpParams;
	private InputStream isResponse;
	private JSONParser jsonParser;
	private Handler UIHandler;
	private LinearLayout thisPageLayout;
	private int idIndex;
	private ProgressBar progressBar;
	private TextView progressText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_rota_timetable);
		
		
		this.UIHandler = new Handler();
		this.idIndex = 0;

		// get the persistent cookie store so that we can easily hit up UQRota
		this.mPersistentCookieStore = new PersistentCookieStore(this);

		this.httpParams = new BasicHttpParams();
		this.httpParams.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
				HttpVersion.HTTP_1_1);
		this.cookieStore = new BasicCookieStore();
		if (this.mPersistentCookieStore.getCookies().size() > 0)
		{
			this.cookieStore.addCookie(this.mPersistentCookieStore.getCookies().get(0));
		}
		this.localContext = new BasicHttpContext();
		this.httpGetTimetables = new HttpGet(Settings.uqRotaTimetableSearchUrl);
		this.jsonParser = new JSONParser();
		this.httpClient = new DefaultHttpClient();

		this.progressBar = (ProgressBar) findViewById(R.id.UQRotaTimetableLoadingStatusProgressBar);
		this.progressText = (TextView) findViewById(R.id.UQRotaTimetableLoadingStatusInformation);

		thisPageLayout = (LinearLayout) findViewById(R.id.chooseRotaTimetableLayout);
		thisPageLayout.setOrientation(LinearLayout.VERTICAL);

		// Bind custom cookie store to the local context.
		this.localContext.setAttribute(ClientContext.COOKIE_STORE,
				this.cookieStore);
		
		
		GetTimetablesAndAddButtonsTask btnTask = new GetTimetablesAndAddButtonsTask();

		btnTask.execute();
		

	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	  super.onConfigurationChanged(newConfig);
	  setContentView(R.layout.activity_choose_rota_timetable);
	}
	
	private void addButtonsToUI(List<Button> buttons)
	{
		progressBar.setVisibility(View.GONE);
		progressText.setVisibility(View.GONE);
		
		for (Button btn : buttons)
		{
			LinearLayout row = new LinearLayout(getApplicationContext());
			row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT));
			row.addView(btn);
			thisPageLayout.addView(row);
			Log.w("Childlayoutcount",String.valueOf(thisPageLayout.getChildCount()));
		}
		
		Log.w("Childlayoutcount",String.valueOf(thisPageLayout.getChildCount()));
		
		
		
		
	}
	
	class GetTimetablesAndAddButtonsTask extends AsyncTask<Void,Void,List<Button>> {
		private String responseBody;
		private List<Button> returnedButtons;

		
		@Override
		protected void onPreExecute()
		{
			returnedButtons = new ArrayList<Button>();
		}
		
		@Override
		protected void onPostExecute(List<Button> buttons)
		{
			addButtonsToUI(buttons);
		}

		@Override
		protected List<Button> doInBackground(Void... params) {
			HttpResponse response;
			try {
				response = httpClient.execute(
						httpGetTimetables, localContext);
				
				isResponse = response.getEntity().getContent();
				
				
				

				responseBody = Network
						.convertInputStreamToString(isResponse);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

			// Parse JSON from login GET check
			JSONObject timetableJSONResponse = (JSONObject) JSONValue
					.parse(responseBody);

			JSONArray timetablesArray = (JSONArray) timetableJSONResponse
					.get("timetables");

			for (Object obj : timetablesArray) {
				JSONObject object = (JSONObject) obj;
				

				Button btn = new Button(getApplicationContext());
				btn.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT));
				
				//Get current default timetable (if it exists)
				SharedPreferences settings = getSharedPreferences(Settings.preferencesFilename, 0);
				Long defaultTimetableFromPrefs = settings.getLong("rotaDefaultTimetableId", -1);

				if (defaultTimetableFromPrefs == -1)
				{
					btn.setText((String) object.get("name"));
				} else if (String.valueOf(defaultTimetableFromPrefs).equals(String.valueOf(object.get("id")))){
					btn.setText(((String) object.get("name")) + " (currently selected)");
				} else {
					btn.setText((String) object.get("name"));
				}
				
				
				btn.setId(idIndex++);
				btn.setTag(R.id.uqRotaTimetableId, object.get("id").toString());
				
				
				//Add listener for button
				btn.setOnClickListener(new View.OnClickListener() {
		             public void onClick(View v) {
		         		// Set the default uqRota timetable ID in sharedpreferences
		         		Button myClickedButton = (Button) v;
		         		// Get the value of the hidden tag for the button.
		         		Long timetableId = Long.valueOf((String)myClickedButton
		         				.getTag(R.id.uqRotaTimetableId));

		         		SharedPreferences settings = getSharedPreferences(
		         				Settings.preferencesFilename, 0);
		         		SharedPreferences.Editor editor = settings.edit();
		         		editor.putLong("rotaDefaultTimetableId", timetableId);
		         		editor.commit();
		         		
		         		
		        		
		         		
		             	Toast.makeText(getApplicationContext(), R.string.uqRotaTimetableSet, Toast.LENGTH_LONG).show();
		             	
		             	final Intent intent = new Intent(getApplicationContext(), MainActivity.class);

		        		startActivity(intent);
		             }
		         });

				returnedButtons.add(btn);

			}
			return returnedButtons;
		}
		
	}

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_choose_rota_timetable, menu);
		return true;
	}

}
