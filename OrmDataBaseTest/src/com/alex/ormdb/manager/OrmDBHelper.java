package com.alex.ormdb.manager;

import java.sql.SQLException;

import com.alex.ormdb.model.Person;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class OrmDBHelper extends OrmLiteSqliteOpenHelper {
    
    private static final String DB_NAME = "contact.db";
    
    private static final int DB_VERSION = 1;
    
    public OrmDBHelper(Context context) {
        super(context,DB_NAME,null,DB_VERSION);
    }
    
    
    interface Tables {
        String Person = "person";
    }


    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Person.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        
    }

}
