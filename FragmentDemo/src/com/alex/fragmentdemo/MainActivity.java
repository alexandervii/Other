package com.alex.fragmentdemo;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainActivity extends Activity {

	private RadioGroup mRadioGroup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
	}

	/**
	 * Init view
	 */
	private void initView() {
		mRadioGroup = (RadioGroup) findViewById(R.id.navigation);
		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.news:

					break;
				case R.id.sports:

					break;
				case R.id.fun:

					break;
				case R.id.game:

					break;
				}
			}
		});
	}
}
