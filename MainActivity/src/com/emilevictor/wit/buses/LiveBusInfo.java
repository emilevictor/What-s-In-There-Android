package com.emilevictor.wit.buses;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Picture;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebView.PictureListener;

import com.emilevictor.wit.R;

public class LiveBusInfo extends Activity {
	
	private WebView liveBusInfoWebView;
	private String chancellorsPlaceUrl = "http://mobile.jp.translink.com.au/travel-information/network-information/stops-and-stations/stop/uq-chancellors-place";
	private String UQlakesUrl = "http://mobile.jp.translink.com.au/travel-information/network-information/stops-and-stations/stop/uq-lakes";

	@Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {       
        startActivity(new Intent(LiveBusInfo.this,BusMenu.class)); 
        return true;
    }

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_live_bus_info);
		
		//Add a back button to the action bar
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		this.liveBusInfoWebView = (WebView) findViewById(R.id.liveBusInfoWebView);
		
		
		Bundle extras = getIntent().getExtras();
		String requestedStop = (String) extras.get("stop");
		
		
		
		this.liveBusInfoWebView.getSettings().setJavaScriptEnabled(true);
		
		if (requestedStop.equals("cp"))
		{
			this.liveBusInfoWebView.loadUrl(this.chancellorsPlaceUrl);
			scrollPastHeader();
			
		} else if (requestedStop.equals("uql"))
		{
			this.liveBusInfoWebView.loadUrl(this.UQlakesUrl);
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
		 try {
			 this.liveBusInfoWebView.setWebChromeClient(new WebChromeClient() {

			     @Override
			     public void onProgressChanged(WebView view,
			             int newProgress) {

			         if (newProgress >= 100) {

			             liveBusInfoWebView.scrollTo(0,390);
			         }

			         super.onProgressChanged(view, newProgress);
			     };

			 });
			 } catch (Exception e) {
			 e.printStackTrace();
			 }
	}

}
