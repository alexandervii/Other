package com.alex.ormdb.ui.activity;

import com.alex.ormdb.manager.BaseEngine;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public abstract class BaseActivity extends Activity {

    protected BaseEngine mBaseEngine;
    protected Context mContext;
    protected LayoutInflater mInflater;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        mInflater = LayoutInflater.from(mContext);
        mBaseEngine = mBaseEngine.getEngine(mContext);
        setContentView(getContentView());
        init();
    }
    
    public abstract View getContentView();

    public abstract void init();

}
