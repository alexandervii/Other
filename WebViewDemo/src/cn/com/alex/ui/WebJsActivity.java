package cn.com.alex.ui;

import cn.com.alex.R;
import cn.com.alex.R.drawable;
import cn.com.alex.R.id;
import cn.com.alex.R.layout;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow.OnDismissListener;

public class WebJsActivity extends Activity implements OnCheckedChangeListener, OnClickListener {

	public static final String DEFAULT_URL = "www.baidu.com";
	public static final int STATUS_NORMAL = 0;
	public static final int STATUS_LOADING = 1;
	public static final int STATUS_FINISHED = 2;
	public static final int STATUS_FAILED = 3;

	public static final int FONT_SIZE_SMALL = 10;
	public static final int FONT_SIZE_MIDDLE = 11;
	public static final int FONT_SIZE_BIG = 12;

	/** 当前字体大小对应的状态*/
	private int mCurrentFontSize;

	private ImageView mBackBtnIV, mFontSelectBtnIV;
	private TextView mTitle, mNoMoreData;
	private ProgressBar mLoadingBar;
	private WebView mWebViewContent;
	private RadioGroup mFontSizeSelector;

	private boolean mIsPopupShow = false;

	private SharedPreferences mSharedPreferences;

	/** 选择字体大小的PopupWindow*/
	private PopupWindow mPopupWindow;

	private String[] mMenuItem = new String[] { "刷新", "选择字体", };

	/** WebView加载网页的Url*/
//	private String mUrl = "http://10.10.90.218:8080/FeinnoBeside/topic.html";
	private String mUrl = "http://10.10.90.218:8080/FeinnoBeside/hello.html";
	
	/** WebView加载网页状态*/
	private int mStatus;
	
	private WebSettings settings;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_js);
		initView();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		checkAndSetFont();
	}
	
	/**
	 * 初始化
	 */
	private void initView() {
		mSharedPreferences = getSharedPreferences("config",
				Context.MODE_PRIVATE);
		LayoutInflater inflater = LayoutInflater.from(this);
		View contentView = inflater.inflate(R.layout.popup_font_size, null);
		mPopupWindow = new PopupWindow(contentView, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				checkAndSetFont();
			}
		});
		mPopupWindow.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.beside_alertdialog_button_right_hover));

		mFontSizeSelector = (RadioGroup) contentView
				.findViewById(R.id.font_select);
		mFontSizeSelector.setOnCheckedChangeListener(this);

		mNoMoreData = (TextView) findViewById(R.id.tv_no_more_data);
		mNoMoreData.setOnClickListener(this);

		mLoadingBar = (ProgressBar) findViewById(R.id.progressbar_loading);

		mBackBtnIV = (ImageView) findViewById(R.id.btn_back);
		mBackBtnIV.setOnClickListener(this);

		mFontSelectBtnIV = (ImageView) findViewById(R.id.btn_font_size);
		mFontSelectBtnIV.setOnClickListener(this);

		mTitle = (TextView) findViewById(R.id.tv_title);
		mTitle.setText("WebView");

		mWebViewContent = (WebView) findViewById(R.id.web_view_content);
		mWebViewContent.setVisibility(View.VISIBLE);

		settings = mWebViewContent.getSettings();

		settings.setJavaScriptEnabled(true);
		// 设置缩放
		settings.setBuiltInZoomControls(true);
		settings.setSupportZoom(true);
		mWebViewContent.setInitialScale(70);

		mWebViewContent.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				mStatus = STATUS_LOADING;
				changeStatus(mStatus);
				Log.i("Alex", "WebJsActivity-------------->loading page");
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				mStatus = STATUS_FINISHED;
				changeStatus(mStatus);
				Log.i("Alex", "WebJsActivity-------------->page finished");
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return super.shouldOverrideUrlLoading(view, url);
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				Toast.makeText(WebJsActivity.this, "网页加载失败！",
						Toast.LENGTH_LONG).show();
				mStatus = STATUS_FAILED;
				changeStatus(mStatus);
				Log.i("Alex", "WebJsActivity-------------->loading error");
			}
		});
		// mWebViewContent.setWebChromeClient(new WebChromeClient() {
		//
		// });
		
		mWebViewContent.loadUrl(mUrl);
	}
	
	private void checkAndSetFont() {
		int status = getStatus();
		if(status != mCurrentFontSize) {
			setFontSize(status);
			mCurrentFontSize = status;
		}
	}
	
	/**
	 * 设置字体大小
	 * @param status
	 */
	private void setFontSize(int status) {
		switch (status) {
		case FONT_SIZE_BIG:
			settings.setTextSize(WebSettings.TextSize.LARGER);
			break;
		case FONT_SIZE_MIDDLE:
			settings.setTextSize(WebSettings.TextSize.NORMAL);
			break;
		case FONT_SIZE_SMALL:
			settings.setTextSize(WebSettings.TextSize.SMALLER);
			break;
		}
	}
	
	/**
	 * 获得选中状态
	 * @return
	 */
	private int getStatus() {
		return mSharedPreferences.getInt("fontSize", FONT_SIZE_MIDDLE);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		group.check(checkedId);
		int fontSizeStatus = checkStatus(checkedId);
		updateStatus(fontSizeStatus);
		// TODO 设置文本的字体大小（在popupwindowdismiss后修改最好）
	}
	
	/**
	 * 更新字体选中的状态
	 * @param status
	 */
	private void updateStatus(int status) {
		Editor editor = mSharedPreferences.edit();
		editor.putInt("fontSize", status);
		editor.commit();
	}
	
	/**
	 * 通过RadioButton的id判断选择字体的状态
	 * 
	 * @param chechedId
	 *            RadioButton的id
	 * @return 对应的字体的状态
	 */
	private int checkStatus(int chechedId) {
		int fontStatus = -1;
		switch (chechedId) {
		case R.id.font_size_big:
			fontStatus = FONT_SIZE_BIG;
			break;
		case R.id.font_size_middle:
			fontStatus = FONT_SIZE_MIDDLE;
			break;
		case R.id.font_size_small:
			fontStatus = FONT_SIZE_SMALL;
			break;
		}
		return fontStatus;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			if(mIsPopupShow) {
				mPopupWindow.dismiss();
				mIsPopupShow = false;
				return ;
			}
			goToAnotherActivity(MainActivity.class);
			break;
		case R.id.btn_font_size:
			showFontSizeSelectWindow();
			break;
		case R.id.tv_no_more_data:
			mWebViewContent.reload();
			break;
		default:
			break;
		}
	}
	
	@SuppressLint("NewApi")
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mIsPopupShow) {
				mPopupWindow.dismiss();
				mIsPopupShow = false;
				//设置字体,放在PopupWindow的onDismiss监听里
				return true;
			}
			if (mWebViewContent.canGoBack()) {
				mWebViewContent.goBack();
				return true;
			}
		}
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			// Toast.makeText(getApplicationContext(), "菜单键被点击",
			// Toast.LENGTH_LONG).show();
			// PopupWindow pm = new PopupWindow();
			// WindowSize size = getWindowSize();
			// pm.setContentView(getListView());
			// pm.setWidth(100);
			// pm.setHeight(100);
			// pm.showAtLocation(mWebViewContent, Gravity.CENTER, 20, 30);
		}
		return super.onKeyDown(keyCode, event);
	}

	class WindowSize {
		public int width;
		public int height;

		public WindowSize(int width, int height) {
			this.width = width;
			this.height = height;
		}
	}

	
	/**
	 * 设置字体大小对应的RadioButton
	 * 
	 * @param id
	 *            字体状态id
	 * @return 被选择字体对应RadioButton的id
	 */
	private int setSelected(int status) {
		switch (status) {
		case FONT_SIZE_BIG:
			mFontSizeSelector.check(R.id.font_size_big);
			break;
		case FONT_SIZE_MIDDLE:
			mFontSizeSelector.check(R.id.font_size_middle);
			break;
		case FONT_SIZE_SMALL:
			mFontSizeSelector.check(R.id.font_size_small);
			break;
		}
		return mFontSizeSelector.getCheckedRadioButtonId();
	}
	
	private void showFontSizeSelectWindow() {
		// Toast.makeText(this, "字体设置被点击", Toast.LENGTH_LONG).show();
		setSelected(getStatus());
		if (mIsPopupShow) {
			mPopupWindow.dismiss();
		} else {
			mPopupWindow.showAsDropDown(mFontSelectBtnIV, 35, 13);
		}
		mIsPopupShow = (mIsPopupShow ? false : true);
		// pw.setOnDismissListener(new OnDismissListener() {
		// @Override
		// public void onDismiss() {
		// mIsPopupShow = false;
		// }
		// });
		// if(!mIsPopupShow) {
		// pw.showAsDropDown(mFontSelectBtnIV, 0, 0);
		// mIsPopupShow = true;
		// } else {
		// pw.dismiss();
		// mIsPopupShow = false;
		// }
	}
	
	private void goToAnotherActivity(Class clazz) {
		Intent intent = new Intent(this, clazz);
		startActivity(intent);
		this.finish();
	}
	
	private void changeStatus(int status) {
		switch (status) {
		case STATUS_NORMAL:// 正常情况
		case STATUS_FINISHED:// 加载完成
			mWebViewContent.setVisibility(View.VISIBLE);
			mLoadingBar.setVisibility(View.GONE);
			mNoMoreData.setVisibility(View.GONE);
			break;
		case STATUS_LOADING:// 正在加载
			mWebViewContent.setVisibility(View.GONE);
			mLoadingBar.setVisibility(View.VISIBLE);
			mNoMoreData.setVisibility(View.GONE);
			break;
		case STATUS_FAILED:// 加载失败
			mWebViewContent.setVisibility(View.GONE);
			mLoadingBar.setVisibility(View.GONE);
			mNoMoreData.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}
}
