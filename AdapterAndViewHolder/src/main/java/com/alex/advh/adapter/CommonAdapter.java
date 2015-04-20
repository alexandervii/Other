package com.alex.advh.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.alex.advh.holder.ViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2015/4/20.
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

    private Context mContext;
    private List<T> mData;
    private int mLayoutId;

    public CommonAdapter(Context context, List<T> data, int layoutId) {
        this.mContext = context;
        this.mData = data;
        this.mLayoutId = layoutId;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.getHolder(mContext, position, mLayoutId, convertView, parent);
        setData(holder,mData.get(position));
        return holder.getConvertView();
    }

    public abstract void setData(ViewHolder holder, T t);
}
