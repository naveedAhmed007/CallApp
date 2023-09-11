package com.itoasis.callingapp.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "callingDataBase.db";
    private static final int DATABASE_VERSION = 1;
    private byte tablevalue;

    // Table creation SQL statement

    private static final String CREATE_TABLE =
            "CREATE TABLE MyTable (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT," + "email TEXT);";

    private static final String CREATE_TABLE1 =
            "CREATE TABLE MyTable1 (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "count INTEGER);";

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_TABLE1);



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+"MyTable");
        db.execSQL("DROP TABLE IF EXISTS "+"MyTable1");

        onCreate(db);

    }
}
