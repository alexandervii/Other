package com.alex.jpush;

import cn.jpush.android.api.JPushInterface;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);
	}
	
	@Override
	protected void onResume() {
		JPushInterface.onResume(this);
		super.onResume();
	}

}
