package com.alex.ormdb.ui.activity;

import java.util.ArrayList;
import java.util.List;

import com.alex.ormdb.manager.BaseEngine;
import com.alex.ormdb.manager.PersonInfoManager;
import com.alex.ormdb.model.Person;
import com.alex.ormdb.model.Person.Gender;
import com.alex.ormdb.ui.adapter.PersonListAdapter;
import com.example.ormdatabasetest.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends BaseActivity {
    
    public static final String TAG = MainActivity.class.getSimpleName();
    
    private ListView mListView;
    private PersonListAdapter mPersonListAdapter;
    private PersonInfoManager mPersonInfoManager;
    private List<Person> mData;

    /**
     * 初始化视图
     */
    public void init() {
        mListView = (ListView) findViewById(R.id.lv);
        mBaseEngine.init();
        mPersonInfoManager =  mBaseEngine.getManager(PersonInfoManager.class);
        mPersonInfoManager.clearPersonCache();
        fakeOperation();
        mData = mPersonInfoManager.getPersonsCache();
        mPersonListAdapter = new PersonListAdapter(mContext, mData);
        mListView.setAdapter(mPersonListAdapter);
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(mContext, "您点击了<"+mData.get(position).getName()+">", 0).show();
            }
        });
    }
    
    private void fakeOperation() {
        List<Person> persons = new ArrayList<Person>();
        for(int i=0; i<10; i++) {
            Person p = new Person();
            p.setId(i+"");  
            p.setName("哈哈"+i);
            p.setNickName("呵呵"+i);
            p.setAge(11+i);
            p.setBirthday(10+i);
            p.setDescription("我去"+i);
            p.setAddress("北京"+i);
            p.setGender(i%2 == 0 ? Gender.man : Gender.woman);
            persons.add(p);
        }
        mPersonInfoManager.savePersonsToCache(persons);
    }
    
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPersonInfoManager.destroy();
        mBaseEngine.destroy();
    }

    @Override
    public View getContentView() {
        return mInflater.inflate(R.layout.activity_main, null);
    }

}
