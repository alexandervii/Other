package cn.com.alex.activity;

import java.util.ArrayList;
import java.util.List;

import cn.com.alex.R;
import cn.com.alex.dialog.ListViewDialog;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ListDialogActivity extends Activity implements OnClickListener {

	private Button mBtn;
	private ListViewDialog mDialog;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_dialog);
        initView();
    }

	private void initView() {
		mBtn = (Button) findViewById(R.id.btn_show_list_dialog);
		final List<String> mList = new ArrayList<String>();
		mList.add("1");
		mList.add("2");
		mList.add("3");
		mList.add("4");
		mList.add("5");
		
		mDialog = new ListViewDialog(this,R.style.list_dialog_style) {
			@Override
			public List<String> setData() {
				return mList;
			}
		};
		mDialog.setCancelable(true);
		mDialog.setCanceledOnTouchOutside(true);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_show_list_dialog:
			mDialog.show();
			break;

		default:
			break;
		}
	}

}
