package com.example.shican.quizcreator;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by shican on 2018-03-28.
 */

public class QuizDatabaseHelper extends SQLiteOpenHelper{
    static final String DATABASE_NAME = "quizzes.db";
    static final int VERSION_NUM = 1;
    static final String TABLE_NAME ="QuizTable";
    static String KEY_ID = "_ID";
    static String KEY_QUIZ = "QUIZ";

    public QuizDatabaseHelper(Context ctx){
        super(ctx, DATABASE_NAME, null, VERSION_NUM );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME +
                "( _ID INTEGER PRIMARY KEY AUTOINCREMENT, QUIZ text);");
        Log.i("ChatDatabaseHelper", "Calling onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        Log.i("ChatDatabaseHelper", "Calling onUprade, oldVersion="
                + oldVer + "newVersion=" + newVer);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
