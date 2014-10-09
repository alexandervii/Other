package cn.com.alex.ui;

import cn.com.alex.R;
import cn.com.alex.R.id;
import cn.com.alex.R.layout;
import cn.com.alex.utils.UIUtils;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	public static final String DEFAULT_URL = "www.baidu.com";
	
	private EditText mInputURL;
	
	private Button mGoDetailBtn,mWebJsBtn,mActiveJsBtn,mNewJsBtn;
	private SharedPreferences sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		String url = sp.getString("url", DEFAULT_URL);
		if(TextUtils.isEmpty(url)) {
			url = "";
		}
		mInputURL.setText(url);
	}
	
	private void initView() {
		sp = getSharedPreferences("config", Activity.MODE_PRIVATE);
		mInputURL = (EditText) findViewById(R.id.et_url);
		mGoDetailBtn = (Button) findViewById(R.id.btn_url);
		mGoDetailBtn.setOnClickListener(this);
		mWebJsBtn = (Button) findViewById(R.id.btn_js);
		mWebJsBtn.setOnClickListener(this);
		mActiveJsBtn = (Button) findViewById(R.id.btn_active_js);
		mActiveJsBtn.setOnClickListener(this);
		mNewJsBtn = (Button) findViewById(R.id.btn_new_js);
		mNewJsBtn.setOnClickListener(this);
	}

	public void goDetail() {
		String url = mInputURL.getText().toString();
		if(TextUtils.isEmpty(url)) {
			Toast.makeText(this, "地址不能为空", Toast.LENGTH_LONG).show();
			return ;
		}
		//将url保存到sp
		Editor editor = sp.edit();
		editor.putString("url", url);
		editor.commit();
		
		Intent intent = new Intent();
		intent.putExtra("url", url);
		intent.setClass(this, WebViewActivity.class);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_url:
			goDetail();
			break;
		case R.id.btn_js:
			goWebJS();
			break;
		case R.id.btn_active_js:
			UIUtils.jump(this, TopicActivity.class);
			break;
		case R.id.btn_new_js:
			UIUtils.jump(this, NewWebViewActivity.class);
			break;
		}
	}

	private void goWebJS() {
		Intent intent = new Intent(this,WebJsActivity.class);//Activity跳转可以写成工具类
		startActivity(intent);
	}
}
