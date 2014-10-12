package com.alex.ormdb.manager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.content.Context;
import android.os.DropBoxManager.Entry;

public class BaseEngine {
    
    private static BaseEngine sInstance;
    private static Context mContext;
    private Map<Class<? extends BaseManager>,BaseManager> mManagerPool ;
    
    private BaseEngine(Context context) {
        this.mContext = context;
    }
    
    public static final BaseEngine getEngine(Context context) {
        synchronized (BaseEngine.class) {
            if(sInstance == null) {
                sInstance = new BaseEngine(context);
            }
        }
        return sInstance;
    }
    
    public void init() {
        mManagerPool = new HashMap<Class<? extends BaseManager>, BaseManager>();
        registerManager();
    }

    private void registerManager() {
        mManagerPool.put(PersonInfoManager.class, PersonInfoManager.getInstance(mContext));
        for(Iterator<java.util.Map.Entry<Class<? extends BaseManager>, BaseManager>> iterator = mManagerPool.entrySet().iterator();iterator.hasNext();) {
            java.util.Map.Entry<Class<? extends BaseManager>, BaseManager> entry = iterator.next();
            entry.getValue().init(mContext, this);
        }
    }
    
    public <T extends BaseManager> T getManager(Class<T> clazz) {
        if(mManagerPool == null) {
            init();
        }
        T t = (T) mManagerPool.get(clazz);
        return t;
    }
    
    public void destroy() {
        mManagerPool.clear();
        mManagerPool = null;
    }
}
