package com.emilevictor.wit.buses;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import com.emilevictor.wit.R;

public class LiveBusInfo extends Activity {

	private WebView liveBusInfoWebView;
	private String chancellorsPlaceUrl = "http://mobile.jp.translink.com.au/travel-information/network-information/stops-and-stations/stop/uq-chancellors-place";
	private String UQlakesUrl = "http://mobile.jp.translink.com.au/travel-information/network-information/stops-and-stations/stop/uq-lakes";
	private String herstonUrl = "http://mobile.jp.translink.com.au/travel-information/network-information/stops-and-stations/stop/rch-herston-station";
	private String gattonUrl = "http://mobile.jp.translink.com.au/travel-information/network-information/stops-and-stations/stop/310640";
	private String ipswichTrainUrl = "http://mobile.jp.translink.com.au/travel-information/network-information/stops-and-stations/stop/ipswich-station";
	private String ipswichBusUrl = "http://mobile.jp.translink.com.au/travel-information/network-information/stops-and-stations/stop/310076";
	private String toowongTrainUrl = "http://mobile.jp.translink.com.au/travel-information/network-information/stops-and-stations/stop/toowong-station";
	private String parkRdTrainUrl = "http://mobile.jp.translink.com.au/travel-information/network-information/stops-and-stations/stop/park-road-station";
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem)
	{       
		startActivity(new Intent(LiveBusInfo.this,BusMenu.class)); 
		return true;
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
		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_live_bus_info);


		this.liveBusInfoWebView = (WebView) findViewById(R.id.liveBusInfoWebView);


		Bundle extras = getIntent().getExtras();
		String requestedStop = (String) extras.get("stop");


		this.liveBusInfoWebView.getSettings().setJavaScriptEnabled(true);

		//Show a different URL based on the button clicked.
		if (requestedStop.equals("cp"))
		{
			this.liveBusInfoWebView.loadUrl(this.chancellorsPlaceUrl);
			scrollPastHeader();

		} else if (requestedStop.equals("uql"))
		{
			this.liveBusInfoWebView.loadUrl(this.UQlakesUrl);
			scrollPastHeader();
		} else if (requestedStop.equals("herston"))
		{
			this.liveBusInfoWebView.loadUrl(this.herstonUrl);
			scrollPastHeader();
		} else if (requestedStop.equals("gatton"))
		{
			this.liveBusInfoWebView.loadUrl(this.gattonUrl);
			scrollPastHeader();
		} else if (requestedStop.equals("ipswichTrain"))
		{
			this.liveBusInfoWebView.loadUrl(this.ipswichTrainUrl);
			scrollPastHeader();
		} else if (requestedStop.equals("ipswichBus"))
		{
			this.liveBusInfoWebView.loadUrl(this.ipswichBusUrl);
			scrollPastHeader();
		} else if (requestedStop.equals("toowongTrain"))
		{
			this.liveBusInfoWebView.loadUrl(this.toowongTrainUrl);
			scrollPastHeader();
		} else if (requestedStop.equals("parkRdTrain"))
		{
			this.liveBusInfoWebView.loadUrl(this.parkRdTrainUrl);
			scrollPastHeader();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_live_bus_info, menu);
		return true;
	}

	private void scrollPastHeader()
	{
		//Automatically scroll past the translink header.
		final Activity activity = this;
		try {
			this.liveBusInfoWebView.setWebChromeClient(new WebChromeClient() {

				@Override
				public void onProgressChanged(WebView view,
						int newProgress) {

					activity.setTitle("Loading... " + String.valueOf(newProgress) + "%");
					activity.setProgress(newProgress * 100);
					if(newProgress == 100)
					{
						liveBusInfoWebView.scrollTo(0,390);
						activity.setTitle("Live Bus Information");
					}

					super.onProgressChanged(view, newProgress);
				};

			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
