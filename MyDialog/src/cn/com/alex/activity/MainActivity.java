package cn.com.alex.activity;

import cn.com.alex.R;
import cn.com.alex.R.layout;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {

    private Button mListDialog,mAlertDialog; 
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mListDialog = (Button) findViewById(R.id.alert_dialog_list_with_corner_and_click_effect);
        mAlertDialog = (Button) findViewById(R.id.alert_dialog_with_round_corner);
        
        mListDialog.setOnClickListener(this);
        mAlertDialog.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.alert_dialog_list_with_corner_and_click_effect:
            Intent intent = new Intent(this,ListDialogActivity.class);
            startActivity(intent);
            break;

        case R.id.alert_dialog_with_round_corner:
            
            break;
        }
        
    }
}
