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

public class ListViewDialog extends Dialog {

    private DialogListViewAdapter mAdapter;
    private ListView mListView;
    private List<String> mList;
    public ListViewDialog(Context context, int theme) {
        super(context, theme);
        initView(context);
    }
    
    public void setData(List<String> list) {
        
    }

    private void initView(Context context) {
        View view =  getLayoutInflater().from(context).inflate(R.layout.dialog_list_view, null);
        setContentView(view);
        ListView mListView = (ListView) view.findViewById(R.id.dialog_list_view);
        
        mList = new ArrayList<String>();
        mList.add("一");
        mList.add("二");
        mList.add("三");
        mList.add("四");
        mList.add("五");
        
        mAdapter = new DialogListViewAdapter(getContext(), mList);
        mListView.setAdapter(mAdapter);
    } 
    
    

}
