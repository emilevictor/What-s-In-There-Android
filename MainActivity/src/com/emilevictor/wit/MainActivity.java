package com.emilevictor.wit;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.emilevictor.wit.about_page.WhatIsThis;
import com.emilevictor.wit.buses.BusMenu;
import com.emilevictor.wit.computeravailability.ComputerAvailabilityLiveFloorPlans;
import com.emilevictor.wit.computeravailability.ComputerAvailabilityOverview;
import com.emilevictor.wit.computeravailability.LibraryAvailabilityOverview;
import com.emilevictor.wit.helpers.Settings;
import com.emilevictor.wit.uqrota.ShowDefaultTimetable;
import com.emilevictor.wit.uqrota.UQRotaLogin;
import com.emilevictor.wit.wheres_my_class.WheresMyClass;
import com.loopj.android.http.PersistentCookieStore;

public class MainActivity extends Activity {
	private PersistentCookieStore mPersistentCookies;
	private Button mUqRotaLoginButton;
	private Button mAccessUqRotaButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//By default, hide both buttons
		this.mUqRotaLoginButton = (Button) findViewById(R.id.signInUQRotaButton);
		this.mAccessUqRotaButton = (Button) findViewById(R.id.accessUQRotaButton);
		this.mUqRotaLoginButton.setVisibility(View.GONE);
		this.mAccessUqRotaButton.setVisibility(View.GONE);

		//We need to check whether we have a current UQRota cookie, and whether a default timetable has been set.
		if (userHasSetDefaultTimetableAndValidCookieExists())
		{
			this.mAccessUqRotaButton.setVisibility(View.VISIBLE);
		} else {
			this.mUqRotaLoginButton.setVisibility(View.VISIBLE);
		}


	}

	public boolean userHasSetDefaultTimetableAndValidCookieExists()
	{
		SharedPreferences settings = this.getSharedPreferences(Settings.preferencesFilename, 0);
		Long mCurrentTimetable = settings.getLong("rotaDefaultTimetableId", -1);

		if (mCurrentTimetable == -1)
		{
			//There is no timetable set.
			return false;
		} else {
			//There is a timetable set. Now check for a valid cookie.
			this.mPersistentCookies = new PersistentCookieStore(this);
			//Check that we actually have any cookies.
			if (this.mPersistentCookies.getCookies().size() > 0)
			{
				//If this cookie is valid
				if (this.mPersistentCookies.getCookies().get(0).getExpiryDate().after(new Date()))
				{
					return true;
				} else {
					//Remove cookie
					this.mPersistentCookies.getCookies().remove(0);
					return false;
				}
			} else {
				return false;
			}
			
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void openWIT(View view)
	{
		final Intent intent = new Intent(this, com.emilevictor.wit.whats_in_there.InputWITClassActivity.class);

		startActivity(intent);

	}

	public void whatIsThisPage(View view)
	{
		final Intent intent = new Intent(this, WhatIsThis.class);

		startActivity(intent);
	}

	public void wheresMyClassPage(View view)
	{
		final Intent intent = new Intent(this, WheresMyClass.class);

		startActivity(intent);
	}

	public void computerAvailabilityPage(View view)
	{
		final Intent intent = new Intent(this, ComputerAvailabilityLiveFloorPlans.class);

		startActivity(intent);
	}

	public void computerAvailabilityOverviewPage(View view)
	{
		final Intent intent = new Intent(this, ComputerAvailabilityOverview.class);

		startActivity(intent);
	}

	public void liveBusInfo(View view)
	{
		final Intent intent = new Intent(this, BusMenu.class);

		startActivity(intent);
	}

	public void UQLibraryCAOverview(View view)
	{
		final Intent intent = new Intent(this, LibraryAvailabilityOverview.class);

		startActivity(intent);
	}

	public void openRota(View view)
	{
		final Intent intent = new Intent(this, ShowDefaultTimetable.class);

		startActivity(intent);
	}
	
	public void signInRota(View view)
	{
		final Intent intent = new Intent(this, UQRotaLogin.class);

		startActivity(intent);
	}

}
