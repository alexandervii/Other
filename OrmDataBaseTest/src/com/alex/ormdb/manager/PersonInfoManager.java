package com.alex.ormdb.manager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.alex.ormdb.model.Person;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;

public class PersonInfoManager extends BaseManager {

    private static PersonInfoManager sInstance;
    private List<Person> mPersons = new ArrayList<Person>();
    
    public static PersonInfoManager getInstance(Context context) {
        synchronized (PersonInfoManager.class) {
            if(sInstance == null) {
                sInstance = new PersonInfoManager(context);
            }
        }
        return sInstance;
    }
    
    private PersonInfoManager(Context context) {
        this.mContext = context;
    }
    
    public void clearPersonCache() {
        try {
            Dao<Person, Integer> dao = mDBHelper.getDao(Person.class);
            DeleteBuilder<Person,Integer> deleteBuilder = dao.deleteBuilder();
            deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public List<Person> getPersonsCache() {
        //如果内存中有，则直接取内存的
        if(!mPersons.isEmpty()) {
            return mPersons;
        }
        //如果内存中没有，则从数据库取
        try {
            Dao<Person, Integer> dao = mDBHelper.getDao(Person.class);
            QueryBuilder<Person,Integer> queryBuilder = dao.queryBuilder();
            List<Person> list = queryBuilder.query();
            if(!list.isEmpty() && !mPersons.containsAll(list)) {
                mPersons.addAll(list);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mPersons;
    }
    
    public void savePersonsToCache(List<Person> list) {
        if(list == null || list.isEmpty()) {
            throw new NullPointerException("参数list为空");
        }
        try {
            Dao<Person, Integer> dao = mDBHelper.getDao(Person.class);
            for (Person person : list) {
                dao.create(person);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public List<Person> getPersonsFromServer() {
        //TODO 从服务器获取
        return null;
    }
    
    @Override
    public void destroy() {
        mPersons.clear();
        mPersons = null;
    }
    
}
