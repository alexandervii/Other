package com.alex.tur;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alex.tur.adapter.ChatMessageAdapter;
import com.alex.tur.bean.ChatMessage;
import com.alex.tur.bean.ChatMessage.Type;
import com.alex.tur.manager.MessageManager;
import com.alex.tur.manager.MessageManager.MessageCallBack;

import android.hardware.input.InputManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private ListView mList;
	private ChatMessageAdapter mAdapter;
	private List<ChatMessage> mData;
	private MessageManager mManager;
	private MessageListener mListener;
	private InputMethodManager mInputManager;
	private int mScreenHeight;
	private int mFirstVisibleItem = 0;
	
	private EditText mInput;
	private Button mSendBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		initView();
		initData();
		initListener();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		mInputManager.hideSoftInputFromWindow(mInput.getWindowToken(), 0);
	}

	private void initListener() {
		mSendBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String msg = mInput.getText().toString();
				if(TextUtils.isEmpty(msg)) {
					Toast.makeText(getApplicationContext(), "发送内容不能为空!", 0).show();
					return;
				}
				ChatMessage cm = new ChatMessage(msg, Type.OUTCOMING, new Date());
				addNewMessage(cm);
				mInput.setText("");
				mManager.sendMessage(msg, mListener);
			}
		});
		mList.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if(mFirstVisibleItem != firstVisibleItem) {
					mInputManager.hideSoftInputFromWindow(mInput.getWindowToken(), 0);
				}
				mFirstVisibleItem = firstVisibleItem;
			}
		});
	}
	
	private void addNewMessage(ChatMessage cm) {
		mData.add(cm);
		mAdapter.notifyDataSetChanged();
		mList.setSelection(mList.getBottom());
	}

	private void initData() {
		mManager = MessageManager.getInstance(this);
		mScreenHeight = getResources().getDisplayMetrics().heightPixels;
		mInputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		mListener = new MessageListener();
		mData = new ArrayList<ChatMessage>();
		mData.add(new ChatMessage("你好，我是小慕",Type.INCOMING,new Date()));
		mAdapter = new ChatMessageAdapter(this, mData);
		mList.setAdapter(mAdapter);
	}

	private void initView() {
		mInput = (EditText) findViewById(R.id.id_main_input);
		mSendBtn = (Button) findViewById(R.id.id_main_send);
		mList = (ListView) findViewById(R.id.id_listView);
	}
	
	private class MessageListener implements MessageCallBack<ChatMessage> {

		@Override
		public void onSuccess(ChatMessage t) {
			mData.add(t);
			mAdapter.notifyDataSetChanged();
		}

		@Override
		public void onFailure(String tip) {
			Toast.makeText(getApplicationContext(), "信息发送失败!", 0).show();
		}
	}
}
