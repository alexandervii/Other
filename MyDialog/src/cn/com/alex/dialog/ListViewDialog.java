package cn.com.alex.dialog;


import java.util.ArrayList;
import java.util.List;

import cn.com.alex.R;
import cn.com.alex.adapter.DialogListViewAdapter;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;

public abstract class ListViewDialog extends Dialog {

    private DialogListViewAdapter mAdapter;
    private ListView mListView;
    private List<String> mList;
    public ListViewDialog(Context context, int theme) {
        super(context, theme);
        initView(context);
    }
    
    public abstract List<String> setData();

    private void initView(Context context) {
        View view =  getLayoutInflater().from(context).inflate(R.layout.dialog_list_view, null);
        setContentView(view);
        ListView mListView = (ListView) view.findViewById(R.id.dialog_list_view);
        
        mAdapter = new DialogListViewAdapter(getContext(), setData());
        mListView.setAdapter(mAdapter);
    } 
}
