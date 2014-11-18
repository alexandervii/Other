package com.alex.microblog;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.StatusesAPI;

public class AttentionFriendsBlogToCards extends Activity {
	
	protected static final String TAG = AttentionFriendsBlogToCards.class.getSimpleName();
	private Oauth2AccessToken mAccessToken;
	    /** 用于获取微博信息流等操作的API */
	private StatusesAPI mStatusesAPI;
	private TextView mText;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cards);
		
		 // 获取当前已保存过的 Token
        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        // 对statusAPI实例化
        mStatusesAPI = new StatusesAPI(mAccessToken);
        
        mText = (TextView) findViewById(R.id.all_cards);
		
		findViewById(R.id.get_card_button).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getCards();
			}

		});
	}
	
	private void getCards() {
		mStatusesAPI.friendsTimeline(0, 0, 50, 1, false, 0, false, mListener);
	}
	
	private RequestListener mListener = new RequestListener() {

		@Override
		public void onComplete(String content) {
			if(!TextUtils.isEmpty(content)) {
				Log.i(TAG, "RequestListener-->>onComplete-->>content::"+content);
				mText.setText(content);
			}
		}

		@Override
		public void onWeiboException(WeiboException e) {
			
		}
	};
}
