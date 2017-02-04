package com.github.jgilfelt.chuck.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ChuckDbOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "chuck.db";
    private static final int VERSION = 2;

    public ChuckDbOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        LocalCupboard.getInstance().withDatabase(db).createTables();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        LocalCupboard.getInstance().withDatabase(db).upgradeTables();
    }
}
