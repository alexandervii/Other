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
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class WebViewActivity extends Activity implements OnClickListener,
		OnCheckedChangeListener {

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
	private String mUrl;
	
	/** WebView加载网页状态*/
	private int mStatus;
	
	private WebSettings settings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_view);
		initView();
		loadPage();
	}

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
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				mStatus = STATUS_FINISHED;
				changeStatus(mStatus);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return super.shouldOverrideUrlLoading(view, url);
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				Toast.makeText(WebViewActivity.this, "网页加载失败！",
						Toast.LENGTH_LONG).show();
				mStatus = STATUS_FAILED;
				changeStatus(mStatus);
			}
		});
		// mWebViewContent.setWebChromeClient(new WebChromeClient() {
		//
		// });
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

	private void loadPage() {
		mUrl = getIntent().getStringExtra("url").trim();
		if (TextUtils.isEmpty(mUrl)) {
			mUrl = DEFAULT_URL;
		}
		mWebViewContent.loadUrl(mUrl);
	}

	private void goToAnotherActivity(Class clazz) {
		Intent intent = new Intent(this, clazz);
		startActivity(intent);
		this.finish();
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
			mWebViewContent.loadUrl(mUrl);
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		checkAndSetFont();
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
	
	private void checkAndSetFont() {
		int status = getStatus();
		if(status != mCurrentFontSize) {
			setFontSize(status);
			mCurrentFontSize = status;
		}
	}

	private void showFontSizeSelectWindow() {
		// Toast.makeText(this, "字体设置被点击", Toast.LENGTH_LONG).show();
		setSelected(getStatus());
		if (mIsPopupShow) {
			mPopupWindow.dismiss();
		} else {
			mPopupWindow.showAsDropDown(mFontSelectBtnIV, 37, 8);
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

	private ListView getListView() {
		ListView lv = new ListView(getApplicationContext());

		ArrayAdapter<String> aa = new ArrayAdapter<String>(
				getApplicationContext(), android.R.layout.simple_list_item_1,
				android.R.id.text1, mMenuItem);
		lv.setAdapter(aa);
		return lv;
	}

	private WindowSize getWindowSize() {
		WindowManager manager = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		// Display display = manager.getDefaultDisplay(); //第一种
		// int width =display.getWidth();
		// int height=display.getHeight();
		// Log.d("width", String.valueOf(width));
		// Log.d("height", String.valueOf(height));
		//
		DisplayMetrics dm = new DisplayMetrics(); // 第二种
		manager.getDefaultDisplay().getMetrics(dm);
		return new WindowSize(dm.widthPixels, dm.heightPixels);
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
	 * 设置字体大小
	 * @param status
	 */
	public void setFontSize(int status) {
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
	
	/**
	 * 获得选中状态
	 * @return
	 */
	private int getStatus() {
		return mSharedPreferences.getInt("fontSize", FONT_SIZE_MIDDLE);
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

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		group.check(checkedId);
		int fontSizeStatus = checkStatus(checkedId);
		updateStatus(fontSizeStatus);
		// TODO 设置文本的字体大小（在popupwindowdismiss后修改最好）
	}
}
