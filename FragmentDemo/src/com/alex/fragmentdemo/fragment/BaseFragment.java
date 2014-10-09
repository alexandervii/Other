package com.alex.fragmentdemo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {
	
	protected Context mContext;
	protected View mContentView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = initView(inflater);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData(savedInstanceState);
	}
	
	/**
	 * 初始化数据
	 * @param savedInstanceState 
	 */
	public abstract void initData(Bundle savedInstanceState);
	
	/**
	 * 初始化Fragment中显示的控件
	 * @param inflater 
	 * @return
	 */
	public abstract View initView(LayoutInflater inflater);
}
