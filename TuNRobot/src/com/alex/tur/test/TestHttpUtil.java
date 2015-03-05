package com.alex.tur.test;

import com.alex.tur.utils.HttpUtil;

import android.test.AndroidTestCase;
import android.util.Log;

public class TestHttpUtil extends AndroidTestCase {
	
	private static final String TAG = TestHttpUtil.class.getSimpleName();
	
	public void testSendInfo() {
		String result1 = HttpUtil.doGet("给我讲个笑话");
		Log.i(TAG, "给我讲个笑话:"+result1);
		
		String result2 = HttpUtil.doGet("给我讲个鬼故事");
		Log.i(TAG, "给我讲个鬼故事:"+result2);
		
		String result3 = HttpUtil.doGet("任性的英文怎么说");
		Log.i(TAG, "任性的英文怎么说:"+result3);
	}

}
