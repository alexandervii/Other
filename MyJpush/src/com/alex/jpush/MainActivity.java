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
		//�������������ĳһ���ض��û�������������Ϣ
		JPushInterface.setAlias(this, "123456", new TagAliasCallback() {
			@Override
			public void gotResult(int arg0, String arg1, Set<String> arg2) {
				//arg0���Ϊ0��ʾ���ñ����ɹ�
				Log.i(TAG, "set alias : " + arg0);
			}
		});
		//��ǩ���������ĳһ���û������磺���ŷ���Ϊ���������֣��ƾ��ȣ���ͬ�û�����ѡ���õķ���
		Set<String> tags = new HashSet<String>();
		tags.add("news");
		tags.add("sports");
		JPushInterface.setTags(this,tags, new TagAliasCallback() {
			@Override
			public void gotResult(int arg0, String arg1, Set<String> arg2) {
				//arg0���Ϊ0��ʾ���ñ����ɹ�
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
