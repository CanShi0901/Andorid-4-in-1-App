package com.example.shican.quizcreator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChooseQuiz extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_quiz);
        Button mc = (Button) findViewById(R.id.multipleChoice);
        Button nu = (Button) findViewById(R.id.numeric);
        Button tf = (Button) findViewById(R.id.trueFalse);

        mc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseQuiz.this, CreateQuiz.class);
                intent.putExtra("type", "mc");
                startActivity(intent);
            }
        });

        nu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseQuiz.this, CreateQuiz.class);
                intent.putExtra("type", "nu");
                startActivity(intent);
            }
        });

        tf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseQuiz.this, CreateQuiz.class);
                intent.putExtra("type", "tf");
                startActivity(intent);
            }
        });
    }
}
