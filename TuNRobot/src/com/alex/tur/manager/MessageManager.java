package com.alex.tur.manager;

import com.alex.tur.bean.ChatMessage;
import com.alex.tur.utils.HttpUtil;

import android.content.Context;
import android.os.Handler;

public class MessageManager {
	
	private static MessageManager sManager;
	private static Handler mHandler;
	private static Context mContext;

	private MessageManager() {
	}
	
	public static MessageManager getInstance(Context context) {
		synchronized (MessageManager.class) {
			if(sManager == null) {
				sManager = new MessageManager();
			}
		}
		mContext = context;
		mHandler = new Handler();
		return sManager;
	}
	
	public void sendMessage(final String msg,final MessageCallBack<ChatMessage> callBack) {
		new Thread() {
			@Override
			public void run() {
				final ChatMessage cm = HttpUtil.sendMessage(msg);
				if(cm != null && cm.msg != null) {
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							callBack.onSuccess(cm);
						}
					});
				} else {
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							callBack.onFailure("");
						}
					});
				}
			}
		}.start();
	}
	
	public interface MessageCallBack<T> {
		public void onSuccess(T t);
		public void onFailure(String tip);
	}
}
