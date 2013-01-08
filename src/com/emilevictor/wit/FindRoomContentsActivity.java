package com.emilevictor.wit;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class FindRoomContentsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_room_contents);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_find_room_contents, menu);
		return true;
	}

}
