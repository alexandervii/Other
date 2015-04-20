package com.alex.advh.holder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alex.advh.R;

/**
 * Created by Administrator on 2015/4/20.
 */
public class ViewHolder {

    /**用来保存一个ViewHolder中的控件*/
    private SparseArray<View> mViews;
    private int position;
    private View convertView;
    private ViewGroup parent;

    public ViewHolder(Context context, int position, int layoutId, ViewGroup parent) {
        this.position = position;
        this.parent = parent;
        convertView = LayoutInflater.from(context).inflate(layoutId,parent,false);
        convertView.setTag(this);
        mViews = new SparseArray<View>();
    }

    public static ViewHolder getHolder(Context context, int position,
                                       int layoutId, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder(context, position,layoutId,parent);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.position = position;
        }
        return holder;
    }

    /**
     * 用来返回Adapter中getView方法返回的View对象
     * @return
     */
    public View getConvertView() {
        return convertView;
    }

    /**
     * 获取当前ViewHolder对应的item的position
     * @return
     */
    public int getPosition() {
        return position;
    }

    /**
     * 通过控件的id来获得对象
     * @param resId
     * @return
     */
    public <T extends View> T getView(int resId) {
        View view = null;
        if(convertView != null) {
            view = mViews.get(resId);
            if(view == null) {
                view = convertView.findViewById(resId);
                mViews.put(resId,view);
            }
        }
        return (T)view;
    }

    /**
     *  为TextView设置文本
     * @param resId TextView的id
     * @param text 文本
     */
    public ViewHolder setText(int resId,String text) {
        TextView tv = getView(resId);
        tv.setText(text);
        return this;
    }

    /**
     * 为ImageView设置drawable
     * @param resId ImageView的id
     * @param drawable
     * @return
     */
    public ViewHolder setImageDrawable(int resId,Drawable drawable) {
        ImageView iv = getView(resId);
        iv.setImageDrawable(drawable);
        return this;
    }

    /**
     * 为ImageView设置图片id
     * @param resId
     * @param resourceId
     * @return
     */
    public ViewHolder setImageResource(int resId,int resourceId) {
        ImageView iv = getView(resId);
        iv.setImageResource(resourceId);
        return this;
    }

    /**
     * 为ImageView设置bitmap
     * @param resId
     * @param bitmap
     * @return
     */
    public ViewHolder setImageBitmap(int resId,Bitmap bitmap) {
        ImageView iv = getView(resId);
        iv.setImageBitmap(bitmap);
        return this;
    }
}
