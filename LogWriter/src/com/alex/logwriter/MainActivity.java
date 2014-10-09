package com.alex.logwriter;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.alex.service.LogService;

public class MainActivity extends Activity {

	private Intent mLogIntent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mLogIntent = new Intent(this, LogService.class);
		
	}
	
	public void start(View view) {
		startService(mLogIntent);
	}
	
	public void stop(View view) {
		stopService(mLogIntent);
	}
}
