package cn.com.alex;

import cn.com.alex.widget.RadioProgressBar;
import cn.com.alex.widget.RoundProgressBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;

public class MainActivity extends Activity {

	private ProgressBar mProgressBar,mProgressBarV;
	private RoundProgressBar mRoundBar;
	private RadioProgressBar mRadioBar;
	
	private Button mGoBtn;
	private int mProgress;
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int p = (Integer) msg.obj;
			mProgressBar.setProgress(p);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();
	}

	private void initView() {
		
		mGoBtn = (Button) findViewById(R.id.goSelf);
		mGoBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,DefinedProgressBarActivity.class);
				startActivity(intent);
			}
		});
		
		
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
		mProgressBar.setMax(20);
		
		mProgressBarV = (ProgressBar) findViewById(R.id.progressBar_v);
		mProgressBarV.setMax(20);
		
		mRoundBar = (RoundProgressBar) findViewById(R.id.roundProgressBar);
		mRoundBar.setMax(20);
		
		mRadioBar = (RadioProgressBar) findViewById(R.id.radioProgressBar);
		mRadioBar.setMax(20);
		
		new Thread() {
			public void run() {
				for(int i=0; i<=20; i++) {
					SystemClock.sleep(1000);
					mProgress = i;
//					Message message = handler.obtainMessage();
//					message.obj = i;
//					handler.sendMessage(message);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							mProgressBar.setProgress(mProgress);
							mProgressBarV.setProgress(mProgress);
							mRoundBar.setProgress(mProgress);
							mRadioBar.setProgress(mProgress);
						}
					});
				}
			}
		}.start();
	}
}
