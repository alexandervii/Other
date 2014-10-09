package com.alex.manager;

import android.app.Application;
import android.content.Context;

public class LogApplication extends Application {

	private Context mContext;
	private Thread mMainThread;
	private int mMainThreadID;
	
	public LogApplication() {
		mContext = getApplicationContext();
		mMainThreadID = android.os.Process.myPid();
	}
}
