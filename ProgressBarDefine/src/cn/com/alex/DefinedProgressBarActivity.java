package cn.com.alex;

import cn.com.alex.defined.MyRecProgressBar;
import android.os.Bundle;
import android.os.SystemClock;
import android.app.Activity;
import android.view.Menu;

public class DefinedProgressBarActivity extends Activity {

	private final int mMaxProgress = 20;
	private int mProgress;
	private MyRecProgressBar mRecProgressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_defined_progress_bar);
		initView();
	}

	private void initView() {
		mRecProgressBar = (MyRecProgressBar) findViewById(R.id.recProgressBar);
		mRecProgressBar.setMaxProgress(mMaxProgress);
		new Thread() {
			public void run() {
				for(int i=0; i<=mMaxProgress; i++) {
					mProgress = i;
					SystemClock.sleep(1000);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							mRecProgressBar.setProgress(mProgress);
						}
					});
				}
			}
		}.start();
	}

}
