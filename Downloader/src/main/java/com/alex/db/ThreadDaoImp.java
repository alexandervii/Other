package com.alex.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.alex.model.ThreadInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/4/14.
 */
public class ThreadDaoImp implements ThreadDao {

    private DBHelper mDBDbHelper;

    public ThreadDaoImp(Context context) {
        mDBDbHelper = new DBHelper(context);
    }

    @Override
    public void insertThread(ThreadInfo info) {
        SQLiteDatabase db = mDBDbHelper.getWritableDatabase();
        String sql = "insert into thread_info(thread_id,url,start,end,finished) values(?,?,?,?,?)";
        db.execSQL(sql,new Object[] {info.getId(),info.getUrl(),
                info.getStart(),info.getEnd(),info.getFinished()});
        db.close();
    }

    @Override
    public void deleteThread(String url, int threadId) {
        SQLiteDatabase db = mDBDbHelper.getWritableDatabase();
        String sql = "delete from thread_info where url = ? and thread_id = ?";
        db.execSQL(sql,new Object[] {url, threadId});
        db.close();
    }

    @Override
    public void updateThread(String url, int threadId, int finished) {
        SQLiteDatabase db = mDBDbHelper.getWritableDatabase();
        String sql = "update thread_info set finished = ? where url = ? and thread_id = ?";
        db.execSQL(sql,new Object[] {finished, url, threadId});
        db.close();
    }

    @Override
    public List<ThreadInfo> getAllThreads(String url) {
        SQLiteDatabase db = mDBDbHelper.getWritableDatabase();
        String sql = "select * from thread_info where url = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{url});
        List<ThreadInfo> list = new ArrayList<ThreadInfo>();
        while(cursor.moveToNext()) {
            ThreadInfo info = new ThreadInfo();
            info.setId(cursor.getInt(cursor.getColumnIndex("thread_id")));
            info.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            info.setStart(cursor.getInt(cursor.getColumnIndex("start")));
            info.setEnd(cursor.getInt(cursor.getColumnIndex("end")));
            info.setFinished(cursor.getInt(cursor.getColumnIndex("finished")));
            list.add(info);
        }
        cursor.close();
        db.close();
        return list;
    }

    @Override
    public boolean isExists(String url, int threadId) {
        SQLiteDatabase db = mDBDbHelper.getWritableDatabase();
        String sql = "select * from thread_info where url = ? and thread_id = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{url,threadId+""});
        cursor.close();
        db.close();
        return cursor.moveToNext();
    }
}
