package cn.com.alex.dialog;


import cn.com.alex.R;
import cn.com.alex.adapter.DialogListViewAdapter;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ListView;

public class ListViewDialog extends Dialog {

    private DialogListViewAdapter mAdapter;
    private ListView mListView;
    public ListViewDialog(Context context, int theme) {
        super(context, theme);
        initView(context);
    }

    private void initView(Context context) {
        View view =  getLayoutInflater().from(context).inflate(R.layout.dialog_list_view, null);
        setContentView(view);
        ListView mListView = (ListView) view.findViewById(R.id.dialog_list_view);
//        mListView.setAdapter(adapter)
    } 
    
    

}
