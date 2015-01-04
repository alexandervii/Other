package com.alex.jpush;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {
	
	public static final String TAG = MainActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);
		//别名，可以针对某一个特定用户来进行推送消息
		JPushInterface.setAlias(this, "123456", new TagAliasCallback() {
			@Override
			public void gotResult(int arg0, String arg1, Set<String> arg2) {
				//arg0如果为0表示设置别名成功
				Log.i(TAG, "set alias : " + arg0);
			}
		});
		//标签，可以针对某一类用户，例如：新闻分类为体育，娱乐，财经等，不同用户可能选择不用的分类
		Set<String> tags = new HashSet<String>();
		tags.add("news");
		tags.add("sports");
		JPushInterface.setTags(this,tags, new TagAliasCallback() {
			@Override
			public void gotResult(int arg0, String arg1, Set<String> arg2) {
				//arg0如果为0表示设置别名成功
				Log.i(TAG, "set alias : " + arg0);
			}
		});
		
	}
	
	@Override
	protected void onResume() {
		JPushInterface.onResume(this);
		super.onResume();
	}

}
