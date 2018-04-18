package com.example.shican.quizcreator;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/*
@file name: OCSavedStopDatabaseHelper
@author: yuxin zhang
@course: cst 2335
@assignemnt: final projact
@date: April 18, 2018
@professor: eric
@purpose: recent route database helper
 */

public class OCSavedStopDatabaseHelper extends SQLiteOpenHelper {
    public final static String DATABASE_NAME = "db";
    public final static int VERSION_NUM = 9;
    public final static String TABLE_NAME = "savedStopTable";
    public final static String KEY_ID = "ID";
    public final static String KEY_MESSAGE = "Stops";

    public OCSavedStopDatabaseHelper(Context ctx){
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
        //ctx.deleteDatabase(DATABASE_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "+ TABLE_NAME + " ("+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+KEY_MESSAGE+" String)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }
}
