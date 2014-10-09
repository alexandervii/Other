package cn.com.alex.client;

import cn.com.alex.server.domain.Person;
import cn.com.alex.server.domain.Person.Gender;
import cn.com.alex.server.inter.IDraftTools;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static final String NAME = "姓名：";
	private static final String AGE = "年龄：";
	private static final String GENDER = "性别：";
	
	private IDraftTools mTools;
	private Intent mIntent;
	private DraftConnection mConn;
	private EditText mDraftId,mComment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initView();
		
		mIntent = new Intent();
		mIntent.setAction("cn.com.alex.Draft");
		mConn = new DraftConnection();
		bindService(mIntent, mConn, Context.BIND_AUTO_CREATE);
	}
	
	/**
	 * 初始化视图
	 */
	private void initView() {
		mDraftId = (EditText) findViewById(R.id.et_id);
		mComment = (EditText) findViewById(R.id.et_comment);
	}
	
	/**
	 * 判断是否输入的是人的信息
	 * @param str
	 * @return
	 */
	private boolean isPerson(String str) {
		return str.split("#").length == 3 ? true : false;
	}

	/**
	 * 将字符串信息转换为Person信息
	 * @param str
	 * @return
	 */
	private Person str2Person(String str) {
		String[] infos = str.split("#");
		Gender gender = infos[2].equals("男") ? Gender.MAN : Gender.WOMEN;
		return new Person(infos[0], Integer.parseInt(infos[1]), gender);
	}
	
	/**
	 * 将保存人信息的字符串转换为格式良好的人信息
	 * @param str
	 * @return
	 */
	private String showInPerson(String str) {
		StringBuilder sb = new StringBuilder();
		String[] infos = str.split("#");
		sb.append(NAME).append(infos[0]).append("\n\r");
		sb.append(AGE).append(infos[1]).append("\n\r");
		sb.append(GENDER).append(infos[2]);
		return sb.toString();
	}
	
	/**
	 * 保存数据到服务中
	 * @param view
	 */
	public void save(View view) {
		if(null == mComment.getText() || TextUtils.isEmpty(mComment.getText().toString())) {
			Toast.makeText(this, "内容不能为空", Toast.LENGTH_SHORT).show();
			return ;
		}
		String content = mComment.getText().toString().trim();
		try {
			if(isPerson(content)) {
				Person person = str2Person(content);
				mTools.setPerson(person);
			}
			mTools.setDraft(content);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 从服务中获取数据
	 * @param view
	 */
	public void fetch(View view) {
		if(null == mDraftId.getText() || TextUtils.isEmpty(mDraftId.getText().toString())) {
			Toast.makeText(this, "id不能为空", Toast.LENGTH_SHORT).show();
			return ;
		}
		String draftId = mDraftId.getText().toString().trim();
		String draft = "";
		try {
			 draft = mTools.getDraft(draftId);
			//如果是人的信息
			if(isPerson(draft)) {
				//转变成人信息显示格式
				draft = showInPerson(draft);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		mComment.setText(draft);
	}
	
	class DraftConnection implements ServiceConnection {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mTools = IDraftTools.Stub.asInterface(service);
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mTools = null;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindService(mConn);
	}
}
