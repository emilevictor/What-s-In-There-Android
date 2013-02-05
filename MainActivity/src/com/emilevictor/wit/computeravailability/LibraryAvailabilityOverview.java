package com.emilevictor.wit.computeravailability;

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
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.emilevictor.wit.MainActivity;
import com.emilevictor.wit.R;

public class LibraryAvailabilityOverview extends Activity {

	private WebView libraryComputerOverviewWebView;
	
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
		setContentView(R.layout.activity_library_availability_overview);
		
		

		
		this.libraryComputerOverviewWebView = (WebView) findViewById(R.id.libraryComputerAvailabilityWebView);
		
		//Automatically scroll past the translink header.
				final Activity activity = this;
				try {
					this.libraryComputerOverviewWebView.setWebChromeClient(new WebChromeClient() {

						@Override
						public void onProgressChanged(WebView view,
								int newProgress) {

							activity.setTitle("Loading availability... " + String.valueOf(newProgress) + "%");
							activity.setProgress(newProgress * 100);
							if(newProgress == 100)
							{
								activity.setTitle("Computer Availability");
							}

							super.onProgressChanged(view, newProgress);
						};

					});
				} catch (Exception e) {
					e.printStackTrace();
				}
		
		this.libraryComputerOverviewWebView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
		this.libraryComputerOverviewWebView.getSettings().setSupportZoom(true);
		this.libraryComputerOverviewWebView.getSettings().setBuiltInZoomControls(true);
		this.libraryComputerOverviewWebView.loadUrl("http://www.library.uq.edu.au/uqlsm/availablepcsembed.php?q=ask-it/computer-availability");
		
		
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem)
	{       
		startActivity(new Intent(LibraryAvailabilityOverview.this,MainActivity.class)); 
		return true;
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(
				R.menu.activity_library_availability_overview, menu);
		return true;
	}

}
