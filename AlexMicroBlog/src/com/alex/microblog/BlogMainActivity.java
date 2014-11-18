package com.alex.microblog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class BlogMainActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final Intent intent = new Intent();
		findViewById(R.id.auth_button).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				intent.setClass(BlogMainActivity.this, UserAuthActivity.class);
				startActivity(intent);
			}
		});
		
		findViewById(R.id.card_button).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				intent.setClass(BlogMainActivity.this, AttentionFriendsBlogToCards.class);
				startActivity(intent);
			}
		});
	}
}
