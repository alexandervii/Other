package cn.com.alex.ui;

import cn.com.alex.R;
import cn.com.alex.ui.widget.SingleChoiceDialog;
import cn.com.alex.utils.UIUtils;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class TopicActivity extends Activity implements OnClickListener {
	
	
	/** 字体大小*/
	public static final int FONT_SMALL = 0;
	public static final int FONT_MIDDLE = 1;
	public static final int FONT_BIG = 2;
	
	/** WebView所有加载状态 */
	public static final int STATUS_LOADING = 3;
	public static final int STATUS_FINISHED = 4;
	public static final int STATUS_FAILED = 5;

	/** WebView加载的网页*/
//	private static final String mUrl = "http://10.10.90.218:8080/FeinnoBeside/topic.html";
	private static final String mUrl = "http://www.baidu.com";
	
	/** 字体大小选项*/
	private String[] mFontSizeArray = new String[]{"小号","中号","大号"};
	
	private ImageView mBackBtn, mFontSizeBtn;
	/** 标题文本 */
	private TextView mTopicTitle;
	/** 加载网页失败提示 */
	private TextView mFailedTip;
	/** 展示内容和进度的FrameLayout */
	private FrameLayout mDispFL;
	/** 加载网页的WebView */
	private WebView mWebContent;
	/** 进度条 */
	private ProgressBar mLoadingProgress;
	private WebSettings mSettings;

	/** WebView当前加载网页状态 */
	private int mStatus;
	
	/** 用来记录状态*/
	private SharedPreferences mSharedPreferences;
	
	/** 当前字体大小*/
	private int mCurrFontSize;
	private AlertDialog mFontSizeSelector;
	private AlertDialog.Builder mBuilder;
	
	private Editor mEditor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_topic);
		initView();
	}

	private void initView() {
		
		mSharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
		mEditor = mSharedPreferences.edit();
		
		mTopicTitle = (TextView) findViewById(R.id.tv_title);
		mBackBtn = (ImageView) findViewById(R.id.btn_back);
		mFontSizeBtn = (ImageView) findViewById(R.id.btn_font_size);

		mTopicTitle.setOnClickListener(this);
		mFontSizeBtn.setOnClickListener(this);
		
		mFailedTip = (TextView) findViewById(R.id.topic_tv_failed);
		mFailedTip.setOnClickListener(this);

		mDispFL = (FrameLayout) findViewById(R.id.topic_fl_content);
		mLoadingProgress = (ProgressBar) findViewById(R.id.topic_loading_progress);

		mWebContent = (WebView) findViewById(R.id.topic_wv_content);
		mSettings = mWebContent.getSettings();
		mSettings.setJavaScriptEnabled(true);
		
		mCurrFontSize = mSharedPreferences.getInt("fontSize", FONT_MIDDLE);
		changeFontSize();
		
		mBuilder = new AlertDialog.Builder(this).setSingleChoiceItems(mFontSizeArray, getSavedFontSize(), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//
				mCurrFontSize = which;
			}
		} );
		mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				int preFontSize = mSharedPreferences.getInt("fontSize", FONT_MIDDLE);
				if(preFontSize == mCurrFontSize) {
					return ;
				}
				changeFontSize();
				Toast.makeText(TopicActivity.this, "操作成功!", Toast.LENGTH_SHORT).show();
			}
		} );
		//当dialog设置cancelable为false时，dismiss无效
		mBuilder.setCancelable(false);
		mFontSizeSelector = mBuilder.create();
		
		mWebContent.setWebViewClient(new TopicWebViewClient());
		mWebContent.addJavascriptInterface(new TopicJavaScriptInterface(),
				"topic");
		
		mWebContent.loadUrl(mUrl);
	}
	
	private int getSavedFontSize() {
		return mSharedPreferences.getInt("fontSize", FONT_MIDDLE);
	}
	
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if(KeyEvent.KEYCODE_BACK == keyCode) {
			//如果字体选择菜单没有关闭，则先关闭
//			if(mFontSizeSelector != null && mFontSizeSelector.isShowing()) {
//				mFontSizeSelector.dismiss();
//				return true;
//			}
			if(mWebContent != null && mWebContent.canGoBack()) {
				mWebContent.canGoBack();
			} 
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 改变字体大小
	 */
	private void changeFontSize() {
		
		switch (mCurrFontSize) {
		case FONT_BIG:
			mSettings.setTextSize(WebSettings.TextSize.LARGER);
			break;
		case FONT_MIDDLE:
			mSettings.setTextSize(WebSettings.TextSize.NORMAL);
			break;
		case FONT_SMALL:
			mSettings.setTextSize(WebSettings.TextSize.SMALLER);
			break;
		}
		//将字体大小保存到本地
		mEditor.putInt("fontSize", mCurrFontSize);
		mEditor.commit();
	}

	class TopicJavaScriptInterface {

	}

	class TopicWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			return super.shouldOverrideUrlLoading(view, url);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			mStatus = STATUS_LOADING;
			changeStatus();
			Log.i("Alex", "TopicActivity-------------->loading page");
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			mStatus = STATUS_FINISHED;
			changeStatus();
			Log.i("Alex", "TopicActivity-------------->page finished");
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			mStatus = STATUS_FAILED;
			changeStatus();
			Log.i("Alex", "TopicActivity-------------->loading error");
		}

		@Override
		public void doUpdateVisitedHistory(WebView view, String url,
				boolean isReload) {
			super.doUpdateVisitedHistory(view, url, isReload);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			Intent intent = new Intent();
			//用下面的方法返回到上一个Activity
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			UIUtils.jump(intent);
			break;
		case R.id.btn_font_size:
			mFontSizeSelector.show();
			break;
		case R.id.topic_tv_failed:
			mWebContent.reload();
			break;
		}

	}

	/**
	 * 根据状态改变控件显隐
	 * 
	 * @param status
	 */
	public void changeStatus() {
		switch (mStatus) {
		case STATUS_LOADING:// 正在加载中
			mFailedTip.setVisibility(View.GONE);
			mWebContent.setVisibility(View.GONE);
			mLoadingProgress.setVisibility(View.VISIBLE);
			break;
		case STATUS_FINISHED:// 加载完毕
			mFailedTip.setVisibility(View.GONE);
			mWebContent.setVisibility(View.VISIBLE);
			mLoadingProgress.setVisibility(View.GONE);
			break;
		case STATUS_FAILED:// 加载失败
			mFailedTip.setVisibility(View.VISIBLE);
			mDispFL.setVisibility(View.GONE);
			break;
		}
	}
}
