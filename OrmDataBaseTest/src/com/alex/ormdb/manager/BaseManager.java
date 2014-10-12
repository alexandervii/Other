package com.alex.ormdb.manager;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;

public abstract class BaseManager {
    protected OrmDBHelper mDBHelper;
    protected Context mContext;
    protected BaseEngine mEngine;
    protected static ExecutorService sDBThreadPool = Executors.newFixedThreadPool(5);
    
    protected void init(Context context, BaseEngine engine) {
        this.mContext = context;
        this.mEngine = engine;
        if(mDBHelper == null) {
            mDBHelper = new OrmDBHelper(context);
        }
    }
    
    protected abstract void destroy();

    protected void executeDBOperation(Runnable runnable) {
        sDBThreadPool.execute(runnable);
    }
    
    interface LoadDataListener<T> {
        public void onFinish(List<T> content);
        public void onFailed(int statusCode);
    }
    
}
