package com.alex.downloader;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alex.model.FileInfo;
import com.alex.service.DownloadService;


public class MainActivity extends Activity implements View.OnClickListener {

    private TextView mFileNameTV;
    private ProgressBar mProgress;
    private Button mStartBtn,mPauseBtn;
    private String url = "http://www.imooc.com/mobile/imooc.apk";
    private FileInfo mFileInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        IntentFilter filter = new IntentFilter(DownloadService.PROGRESS_UPDATE);
        registerReceiver(mReceiver,filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(DownloadService.PROGRESS_UPDATE.equals(intent.getAction())) {
                int finished = intent.getIntExtra("finished", -1);
                mProgress.setProgress(finished);
            }
        }
    };

    private void initData() {
        mFileInfo = new FileInfo(0,url,"imooc.apk",0,0);
        mFileNameTV.setText(mFileInfo.getFileName());
    }

    private void initView() {
        mFileNameTV = (TextView) findViewById(R.id.fileName);
        mProgress = (ProgressBar) findViewById(R.id.progressBar);
        mStartBtn = (Button) findViewById(R.id.start);
        mStartBtn.setOnClickListener(this);
        mPauseBtn = (Button) findViewById(R.id.pause);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.start) {
            Intent intent = new Intent(this, DownloadService.class);
            intent.setAction(DownloadService.ACTION_START);
            intent.putExtra("fileInfo",mFileInfo);
            startService(intent);
        } else if(id == R.id.pause) {
            Intent intent = new Intent(this, DownloadService.class);
            intent.setAction(DownloadService.ACTION_PAUSE);
            intent.putExtra("fileInfo",mFileInfo);
            startService(intent);
        }
    }
}
