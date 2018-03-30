package com.example.shican.quizcreator;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class QuizMain extends Toolbar {
    protected static final String ACTIVITY_NAME = "QuizMain";
    ListView listViewMain;
    Button newQuiz;
    ArrayList<String> quizMessage;

    ContentValues cv;
    static QuizAdapter quizAdapter;
    QuizDatabaseHelper helper;
    SQLiteDatabase db;
    Cursor c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_main);
        initToolbar();

        listViewMain = (ListView) findViewById(R.id.listviewMain);
        newQuiz = (Button) findViewById(R.id.newQuiz);
        quizMessage = new ArrayList<>();
        cv = new ContentValues();
        quizAdapter = new QuizAdapter(this);
        listViewMain.setAdapter(quizAdapter);
        helper = new QuizDatabaseHelper(this);
        db = helper.getWritableDatabase();

        try {
            helper.onCreate(db);
        } catch (SQLiteException e) {}

        c = db.query(false, helper.TABLE_NAME, new String[]{helper.KEY_ID, helper.KEY_QUIZ},
                null,null,null,null,null,null);
        c.moveToFirst();
        if(c!=null&&c.moveToFirst()) {
            while (!c.isLast()) {
                quizMessage.add(c.getString(c.getColumnIndex(helper.KEY_QUIZ)));
                Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + c.getString(c.getColumnIndex(helper.KEY_QUIZ)));
                c.moveToNext();
            }
        }
        Log.i(ACTIVITY_NAME, "Cursor's column count = " + c.getColumnCount());
        for(int i=0; i<c.getColumnCount(); i++){
            Log.i(ACTIVITY_NAME,c.getColumnName(i));
        }
        listViewMain.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putLong("ID", quizAdapter.getItemId(i));
                bundle.putString("quiz", quizAdapter.getItem(i));
                bundle.putInt("position", i);

                Intent displayQuiz = new Intent(QuizMain.this, DisplayQuiz.class);
                displayQuiz.putExtras(bundle);
                startActivity(displayQuiz);
            }
        });

        newQuiz.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent chooseQuiz = new Intent(QuizMain.this, ChooseQuiz.class);
                startActivityForResult(chooseQuiz, 1);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        quizMessage.clear();
        c = db.query(false, helper.TABLE_NAME, new String[]{helper.KEY_ID, helper.KEY_QUIZ},
                null,null,null,null,null,null);
        c.moveToFirst();
        if(c!=null&&c.moveToFirst()) {
            while (!c.isLast()) {
                quizMessage.add(c.getString(c.getColumnIndex(helper.KEY_QUIZ)));
                Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + c.getString(c.getColumnIndex(helper.KEY_QUIZ)));
                c.moveToNext();
            }
        }
        quizAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        db.close();
    }

    public class QuizAdapter extends ArrayAdapter<String> {
        QuizAdapter(Context ctx) {super(ctx, 0);}
        public int getCount(){return quizMessage.size();}

        public String getItem(int position){return quizMessage.get(position);}

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = QuizMain.this.getLayoutInflater();
            View result = null;
            result = inflater.inflate(R.layout.quiz_row, null);

            TextView quiz_row = (TextView)result.findViewById(R.id.quizRow);
            quiz_row.setText(getItem(position));
            return result;
        }

        public long getId(int position){
            return position;
        }

        public long getItemId(int position){
            c.moveToPosition(position);
            return c.getLong(c.getColumnIndex(helper.KEY_ID));
        }
    }

}


