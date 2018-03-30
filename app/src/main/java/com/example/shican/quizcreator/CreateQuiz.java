package com.example.shican.quizcreator;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateQuiz extends Activity {
    EditText question;
    Button save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quiz);
        question = (EditText)findViewById(R.id.question);
        save = (Button)findViewById(R.id.save);
        final QuizDatabaseHelper helper = new QuizDatabaseHelper(this);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String q = question.getText().toString();
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put(helper.KEY_QUIZ, q);
                db.insert(helper.TABLE_NAME, "null Replacement Value", cv);
                QuizMain.QuizAdapter adapter = QuizMain.quizAdapter;
                adapter.notifyDataSetChanged();
                Intent intent = new Intent(CreateQuiz.this, QuizMain.class);
                intent.putExtra("question", q);
                startActivity(intent);
            }
        });
    }
}
