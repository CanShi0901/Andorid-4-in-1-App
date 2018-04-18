package com.example.shican.quizcreator;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class QuizDatabaseHelper extends SQLiteOpenHelper{
    static final String DATABASE_NAME = "myDatabase.db";
    static final int VERSION_NUM = 1;
    static final String TABLE_NAME ="quizTable4";
    static String KEY_ID = "_ID";
    static String KEY_QUIZ = "QUIZ";
    static String KEY_QUIZTP = "QUIZTP";
    static String KEY_ANSWER1 = "ANS1";
    static String KEY_ANSWER2 = "ANS2";
    static String KEY_ANSWER3 = "ANS3";
    static String KEY_ANSWER4 = "ANS4";
    static String KEY_CORRECT_ANS = "CORRECT_ANS";


    public QuizDatabaseHelper(Context ctx){
        super(ctx, DATABASE_NAME, null, VERSION_NUM );
    }

    /**
     * creates a table
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_QUIZ + " TEXT not null," +
                        KEY_QUIZTP + " TEXT," + KEY_ANSWER1 + " TEXT," + KEY_ANSWER2 + " TEXT," +
                        KEY_ANSWER3 + " TEXT," + KEY_ANSWER4 + " TEXT," + KEY_CORRECT_ANS + " TEXT" + ");");
        Log.i("QuizDatabaseHelper", "Calling onCreate");
    }

    /**
     * checks if the table has been updated
     * @param db
     * @param oldVer
     * @param newVer
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        Log.i("QuizDatabaseHelper", "Calling onUprade, oldVersion="
                + oldVer + "newVersion=" + newVer);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");
        onCreate(db);
    }
}
