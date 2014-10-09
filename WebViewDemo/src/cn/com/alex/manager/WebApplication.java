package cn.com.alex.manager;

import android.app.Application;
import android.content.Context;

public class WebApplication extends Application {

	private static WebApplication mInstance;

	public static Context getContext() {
		if (mInstance == null) {
			synchronized (mInstance) {
				if(mInstance == null) {
					mInstance = new WebApplication();
				}
			}
		}
		return mInstance;
	}

}
