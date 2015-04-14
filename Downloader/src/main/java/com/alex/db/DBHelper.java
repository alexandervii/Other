package com.alex.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "download.db";
    private static final int DB_VERSION = 1;

    private static String DB_CREATE = "create table thread_info(_id integer primary key autoincrement," +
            "thread_id integer,url text,start integer,end integer,finished integer)";
    private static String DB_DROP = "drop table if exists thread_info";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2) {
        db.execSQL(DB_DROP);
        db.execSQL(DB_CREATE);
    }
}
