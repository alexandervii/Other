package com.alex.fragmentdemo.fragment;

import com.alex.fragmentdemo.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class FunFragment extends BaseFragment {

	
	
	@Override
	public void initData(Bundle savedInstanceState) {
		
	}

	@Override
	public View initView(LayoutInflater inflater) {
		mContentView = (FrameLayout)inflater.inflate(R.layout.fragment_fun, null);
		TextView text = new TextView(mContext);
		((FrameLayout)mContentView).addView(text);
		return mContentView;
	}
}
