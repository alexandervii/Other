package com.alex.downloader;

import android.app.Activity;
import android.content.Intent;
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
    private String url = "http://download.kugou.com/download/kugou_pc";
    private FileInfo mFileInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initData() {
        mFileInfo = new FileInfo(0,url,"kugou7684.exe",0,0);
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
