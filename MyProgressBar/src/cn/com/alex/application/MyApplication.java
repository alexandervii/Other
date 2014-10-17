package cn.com.alex.application;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

    
    private static MyApplication mInstance;
    
    
    public static MyApplication getInstance() {
        synchronized (MyApplication.class) {
            if(mInstance == null) {
                mInstance = new MyApplication();
            }
        }
        return mInstance;
    }
    
    public static Context getContext() {
        return getInstance().getApplicationContext();
    }
}
