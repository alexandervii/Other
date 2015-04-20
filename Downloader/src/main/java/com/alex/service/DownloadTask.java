package com.alex.service;

import android.content.Context;
import android.content.Intent;

import com.alex.db.ThreadDao;
import com.alex.db.ThreadDaoImp;
import com.alex.model.FileInfo;
import com.alex.model.ThreadInfo;

import org.apache.http.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 */
public class DownloadTask {

    public static final int MAX_THREADS = 3;
    private Context mContext;
    private FileInfo mFileInfo;
    private ThreadDao mDao;
    private int mFinished = 0;
    private boolean isPaused = false;

    public DownloadTask(Context context, FileInfo fileInfo) {
        this.mContext = context;
        this.mFileInfo = fileInfo;
        mDao = new ThreadDaoImp(context);
    }

    public void download() {
        //读取数据库的线程信息
        List<ThreadInfo> threads = mDao.getAllThreads(mFileInfo.getUrl());
        ThreadInfo info = null;
        if(threads.size() == 0) {
            info = new ThreadInfo(0,mFileInfo.getUrl(),0,mFileInfo.getLength(),mFinished);
        } else {
            info = threads.get(0);
        }
        new DownloadThread(info).start();
    }

    private class DownloadThread extends Thread {
        private ThreadInfo mThreadInfo = null;

        private DownloadThread(ThreadInfo mThreadInfo) {
            this.mThreadInfo = mThreadInfo;
        }

        @Override
        public void run() {
            //想数据库插入线程信息
            if(!mDao.isExists(mThreadInfo.getUrl(),mThreadInfo.getId())) {
                mDao.insertThread(mThreadInfo);
            }
            //设置下载位置
            HttpURLConnection conn = null;
            RandomAccessFile raf = null;
            InputStream input = null;
            try {
                URL url = new URL(mThreadInfo.getUrl());
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(6*1000);
                conn.setRequestMethod("GET");
                int start = mThreadInfo.getStart() + mThreadInfo.getFinished();
                conn.setRequestProperty("Range","bytes="+start+"-"+mThreadInfo.getEnd());
                //设置文件写入位置
                File file = new File(DownloadService.DOWNLOAD_PATH,mFileInfo.getFileName());
                raf = new RandomAccessFile(file,"rwd");
                raf.seek(start);
                //开始下载
                //由于是Range，所以下载成功返回码是HttpStatus.SC_PARTIAL_CONTENT
                if(conn.getResponseCode() == HttpStatus.SC_PARTIAL_CONTENT) {
                    input = conn.getInputStream();
                    byte[] buffer = new byte[1024*4];
                    int length = -1;
                    //读取数据
                    long time = System.currentTimeMillis();
                    while((length = input.read(buffer)) != -1) {
                        //写入文件
                        raf.read(buffer,0,length);
                        mFinished += length;
                        //在暂停下载时，更新数据库的下载进度
                        if(isPaused) {
                            mDao.updateThread(mThreadInfo.getUrl(),mThreadInfo.getId(),mFinished);
                            return ;
                        }
                        //将下载进度发送到activity(延时一段时间)
                        if(System.currentTimeMillis() - time > 500) {
                            time = System.currentTimeMillis();
                            Intent intent = new Intent();
                            intent.putExtra("finished",mFinished*100/mFileInfo.getLength());
                            mContext.sendBroadcast(intent);
                        }
                    }
                    mDao.deleteThread(mThreadInfo.getUrl(),mThreadInfo.getId());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    raf.close();
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                conn.disconnect();
            }
        }
    }
}
