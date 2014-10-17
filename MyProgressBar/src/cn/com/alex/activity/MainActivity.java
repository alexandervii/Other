package cn.com.alex.activity;

import cn.com.alex.R;
import cn.com.alex.R.layout;
import cn.com.alex.R.menu;
import cn.com.alex.widget.MyProgressBar;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {

    private MyProgressBar mProgressBar;
    private int mProgress;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        mProgressBar = (MyProgressBar) findViewById(R.id.progress_bar);
//        mProgressBar.setMaxProgress(50);
//        mProgressBar.setTextSize(30);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <= 22; i++) {
                    mProgress = i;
                    SystemClock.sleep(1000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setProgress(mProgress);
                        }
                    });
                }
            }
        }).start();
        
    }

}
