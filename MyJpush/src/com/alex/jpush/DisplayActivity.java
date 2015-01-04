package com.alex.jpush;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.alex.jpush.receiver.MessageReceiver;

public class DisplayActivity extends Activity {
	
	public static final String TAG = DisplayActivity.class.getSimpleName();
	
	private TextView mTv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mTv = (TextView) findViewById(R.id.tv);
		Bundle content = getIntent().getBundleExtra(MessageReceiver.MSG_CONTENT);
		String title = content.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
		String type = content.getString(JPushInterface.EXTRA_NOTI_TYPE);
		String id = content.getString(JPushInterface.EXTRA_NOTIFICATION_ID);
		System.out.println(content.toString());
		mTv.setText(content.getString(JPushInterface.EXTRA_ALERT));
	}

}
