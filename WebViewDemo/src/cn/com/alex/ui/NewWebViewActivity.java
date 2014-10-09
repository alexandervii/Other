package cn.com.alex.ui;

import cn.com.alex.R;
import cn.com.alex.R.layout;
import cn.com.alex.R.menu;
import android.os.Bundle;
import android.os.SystemClock;
import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class NewWebViewActivity extends Activity {

	private WebView mWebView;
	private ProgressBar mProgressBar;
	private TextView mFailedText;
	private int mProgress;

	 private static final String mUrl = "http://10.10.90.218:8080/FeinnoBeside/topic.html";
//	private static final String mUrl = "http://www.baidu.com";

	private WebSettings mSettings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_web_view);
		 initView();
		 fakeOperation();
	}
	
	private void fakeOperation() {
		new Thread() {
			public void run() {
				SystemClock.sleep(3000);
				changeNoticeCount(23);
			}
		}.start();   
	}
	
	public void changeNoticeCount(int count) {
		StringBuilder builder = new StringBuilder();
		
		builder.append("javascript:setInformNum(");
		builder.append(count);
		builder.append(")");
		
		Log.i("Alex", "Topic count:"+count);
		
		mWebView.loadUrl(builder.toString());
	}

	private void initView() {
		mWebView = (WebView) findViewById(R.id.web_view_id);
		mProgressBar = (ProgressBar) findViewById(R.id.pb_loading);
		mFailedText = (TextView) findViewById(R.id.tv_failed);
		mFailedText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mWebView.reload();
			}
		});

		mSettings = mWebView.getSettings();
		mSettings.setJavaScriptEnabled(true);
		
		mWebView.setWebViewClient(new MyWebViewClient());
		mWebView.setWebChromeClient(new MyWebChromeClient());

		mWebView.addJavascriptInterface(new TopicJavaScriptInterface(), "besideTopic");
		
		mWebView.loadUrl(mUrl);
	}
	
	class TopicJavaScriptInterface {
		/**
		 * 提供给js获取ckey的接口
		 * @return ckey	
		 */
		public String getCKey() {
//			String ckey = BesideInitUtil.getCKEY();
			String ckey = "hahaha";
			return ckey;
		}
		
		/**
		 * 提供给js获取话题缓存的接口
		 * @return 话题缓存
		 */
		public String getTopicCache() {//需要从本地数据库中获得
//			TopicCache topicCache = new TopicCache(getApplicationContext());
//			List dynamicCache = topicCache.queryDynamicCache(DynamicDB.DYNAMIC_BELONG_LIST_ALL);
//			ArrayList<Topic> broadCastNews = topicCache.makeBroadCastNews(dynamicCache);
//			String  cacheStr = new Gson().toJson(broadCastNews);
//			Log.i("Alex", cacheStr);
//			return cacheStr;
			
			
//			String selection = TopicDB.ISHOT + "=?";
//			String[] selectionArgs = new String[]{"1"};//1表示是热门话题，0表示不是
//			List<Topic> topics = HappySQL.sql2VOList(getContentResolver(), TopicDB.CONTENT_URI, Topic.class, null, selection, selectionArgs, null);
//			String topicCache = new Gson().toJson(topics);
//			return topicCache;
			
			return "hahahh";
		}
	}

	class MyWebChromeClient extends WebChromeClient {

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			if (mProgressBar.getVisibility() == View.VISIBLE) {
				mProgressBar.setProgress(newProgress);
				Log.i("Alex", "web load progress------------->" + newProgress
						+ "");
			}
			super.onProgressChanged(view, newProgress);
		}
	}

	class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			mWebView.loadUrl(url);
			return true;
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			mFailedText.setVisibility(View.GONE);
			mProgressBar.setVisibility(View.VISIBLE);
			mWebView.setVisibility(View.GONE);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			mFailedText.setVisibility(View.GONE);
			mProgressBar.setVisibility(View.GONE);
			mWebView.setVisibility(View.VISIBLE);
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
			mFailedText.setVisibility(View.VISIBLE);
			mProgressBar.setVisibility(View.GONE);
			mWebView.setVisibility(View.GONE);

		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (mWebView != null && mWebView.canGoBack()) {
			mWebView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mWebView.clearCache(true);
	}
}
