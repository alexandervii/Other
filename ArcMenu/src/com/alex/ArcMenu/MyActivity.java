package com.alex.ArcMenu;

import java.util.ArrayList;
import java.util.List;

import com.alex.ArcMenu.view.ArcMenu;
import com.alex.ArcMenu.view.ArcMenu.OnMenuItemClickListener;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MyActivity extends Activity {
	
	private ListView mListView;
	private List<String> mData;
	private ArcMenu mRightBottomMenu;
	private ArcMenu mLeftBottomMenu;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initView();
        initData();
        initEvent();
    }
    
    

	private void initEvent() {
		mListView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if(mRightBottomMenu.isOpen()) {
					mRightBottomMenu.toggleMenu(600);
				}
				if(mLeftBottomMenu.isOpen()) {
					mLeftBottomMenu.toggleMenu(600);
				}
			}
		});
		mRightBottomMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public void onClick(View view, int position) {
				Toast.makeText(getApplicationContext(), position+":"+view.getTag(), 1).show();
			}
		});
		
		mLeftBottomMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public void onClick(View view, int position) {
				Toast.makeText(getApplicationContext(), position+":"+view.getTag(), 1).show();
			}
		});
	}

	private void initView() {
		mListView = (ListView) findViewById(R.id.id_listview);
		mRightBottomMenu = (ArcMenu) findViewById(R.id.id_right_bottom_menu);
		mLeftBottomMenu = (ArcMenu) findViewById(R.id.id_left_bottom_menu);
	}

	private void initData() {
		mData = new ArrayList<String>();
		for(int i='A'; i<'Z';i++) {
			mData.add((char)i+"");
		}
		mListView.setAdapter(new ArrayAdapter<String>(this, 
				android.R.layout.simple_list_item_1, 
				mData));
	}
}
