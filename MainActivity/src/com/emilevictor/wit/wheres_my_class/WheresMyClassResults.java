package com.emilevictor.wit.wheres_my_class;

import com.emilevictor.wit.R;
import com.emilevictor.wit.R.layout;
import com.emilevictor.wit.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class WheresMyClassResults extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wheres_my_class_results);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater()
				.inflate(R.menu.activity_wheres_my_class_results, menu);
		return true;
	}

}
