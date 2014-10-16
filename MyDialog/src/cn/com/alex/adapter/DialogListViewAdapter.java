package cn.com.alex.adapter;

import java.util.List;

import cn.com.alex.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DialogListViewAdapter extends BaseAdapter {

    private List<String> mList;
    private Context mContext;
    
    public DialogListViewAdapter(Context context, List<String> list) {
        mList = list;
        mContext = context;
    }
    
    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.dialog_list_view_item, null);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String content = mList.get(position);
        holder.tv = (TextView) convertView.findViewById(R.id.tv_content);
        holder.tv.setText(content);
        return convertView;
    }
    
    class ViewHolder {
        TextView tv;
    }

}
