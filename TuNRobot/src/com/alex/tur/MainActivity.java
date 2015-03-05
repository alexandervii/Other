package com.alex.tur;

import java.util.ArrayList;
import java.util.List;

import com.alex.tur.adapter.ChatMessageAdapter;
import com.alex.tur.bean.ChatMessage;
import com.alex.tur.manager.MessageManager;
import com.alex.tur.manager.MessageManager.MessageCallBack;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends Activity implements OnClickListener {

	private ListView mList;
	private ChatMessageAdapter mAdapter;
	private List<ChatMessage> mData;
	private MessageManager mManager;
	private MessageListener mListener;
	
	private EditText mInput;
	private Button mSendBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		initData();
		
	}

	private void initData() {
		mManager = MessageManager.getInstance(this);
		mListener = new MessageListener();
		mData = new ArrayList<ChatMessage>();
		mAdapter = new ChatMessageAdapter(this, mData);
		mList.setAdapter(mAdapter);
	}

	private void initView() {
		mInput = (EditText) findViewById(R.id.id_main_input);
		mSendBtn = (Button) findViewById(R.id.id_main_send);
		mList = (ListView) findViewById(R.id.id_listView);
		mSendBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.id_main_send) {
			String msg = mInput.getText().toString();
			mManager.sendMessage(msg, mListener);
		}
	}
	
	private class MessageListener implements MessageCallBack<ChatMessage> {

		@Override
		public void onSuccess(ChatMessage t) {
			mData.add(t);
			mAdapter.notifyDataSetChanged();
		}

		@Override
		public void onFailure(String tip) {
			
		}
	}
}
