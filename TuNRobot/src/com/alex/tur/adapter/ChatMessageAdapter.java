package com.alex.tur.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.alex.tur.R;
import com.alex.tur.bean.ChatMessage;
import com.alex.tur.bean.ChatMessage.Type;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ChatMessageAdapter extends BaseAdapter {

	private List<ChatMessage> mData;
	private Context mContext;
	private LayoutInflater mInflater;
	
	public ChatMessageAdapter( Context context, List<ChatMessage> data) {
		super();
		this.mData = data;
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		ChatMessage cm = mData.get(position);
		int type = -1;
		if(cm.type == Type.INCOMING) {
			type = 0;
		} else if(cm.type == Type.OUTCOMING) {
			type = 1;
		}
		return type;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null) {
			holder = new ViewHolder();
			int type = getItemViewType(position);
			if(type == 0) {//incoming
				convertView = mInflater.inflate(R.layout.item_from_msg,null);
				holder.mDate = (TextView) convertView.findViewById(R.id.id_from_msg_date);
				holder.mMsg = (TextView) convertView.findViewById(R.id.id_from_msg_info);
			} else if(type == 1) {//outcoming
				convertView = mInflater.inflate(R.layout.item_to_msg,null);
				holder.mDate = (TextView) convertView.findViewById(R.id.id_to_msg_date);
				holder.mMsg = (TextView) convertView.findViewById(R.id.id_to_msg_info);
			}
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ChatMessage cm = (ChatMessage) getItem(position);
		holder.mDate.setText(formatDate(cm.date));
		holder.mMsg.setText(cm.msg);
		return convertView;
	}
	
	private String formatDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}
	
	class ViewHolder {
		TextView mDate,mMsg;
	}

}
