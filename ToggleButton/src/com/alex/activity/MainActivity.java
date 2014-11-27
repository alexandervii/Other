package com.alex.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.alex.R;
import com.alex.widget.ToggleButton;
import com.alex.widget.ToggleButton.OnStatusChangeListener;
import com.alex.widget.ToggleButton.Status;

public class MainActivity extends Activity {

	private ToggleButton mToggleButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
	}

	private void initView() {
		mToggleButton = (ToggleButton) findViewById(R.id.togglebutton);
		mToggleButton.setOnStatusChangeListener(new OnStatusChangeListener() {
			@Override
			public void onChange(Status status) {
				if(status == Status.OFF) {
					Toast.makeText(MainActivity.this, "关闭", 0).show();
				} else if(status == Status.ON) {
					Toast.makeText(MainActivity.this, "打开", 0).show();
				}
			}
		});
	}

}
