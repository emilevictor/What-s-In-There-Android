package com.emilevictor.wit.about_page;

import com.emilevictor.wit.R;
import com.emilevictor.wit.R.layout;
import com.emilevictor.wit.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class WhatIsThis extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_what_is_this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_what_is_this, menu);
		return true;
	}

}
