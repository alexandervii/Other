package com.alex.advh;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.alex.advh.adapter.CommonAdapter;
import com.alex.advh.holder.ViewHolder;
import com.alex.advh.model.Data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends Activity {

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.id_activity_main_listview);
        initData();
    }

    private void initData() {
        List<Data> lists = new ArrayList<Data>();
        for(int i=0; i<15; i++) {
            String title = "this is the title " + i;
            String description = "this is the description " + i;
            String phone = (10010+i) + "";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = sdf.format(new Date());
            Data d = new Data(title,description,time,phone);
            lists.add(d);
        }
        MyAdapter adapter = new MyAdapter(this,lists,R.layout.common_adapter_item);
        mListView.setAdapter(adapter);
    }

    private class MyAdapter extends CommonAdapter<Data> {

        private List<Integer> mChecks = new ArrayList<Integer>();

        private MyAdapter(Context context, List<Data> data, int layoutId) {
            super(context, data, layoutId);
        }

        @Override
        public void setData(final ViewHolder holder, Data data) {
            //拿到ViewHolder，设置Data里面的值
            holder.setText(R.id.id_common_adapter_item_title, data.getTitle()).
                    setText(R.id.id_common_adapter_item_desc,data.getDesc()).
                    setText(R.id.id_common_adapter_item_time,data.getTime()).
                    setText(R.id.id_common_adapter_item_phone, data.getPhone());
            final CheckBox cb = holder.getView(R.id.id_common_adapter_item_check);
            //解决复用item后产生的状态复用问题
            cb.setChecked(false);
            if(mChecks.contains(holder.getPosition())) {
                cb.setChecked(true);
            }
            cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(cb.isChecked()) {
                        mChecks.add((Integer)holder.getPosition());
                    } else {
                        mChecks.remove((Integer)holder.getPosition());
                    }
                }
            });
        }
    }

}
