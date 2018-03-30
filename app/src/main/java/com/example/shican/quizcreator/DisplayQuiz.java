package com.example.shican.quizcreator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.support.design.widget.Snackbar;
import android.widget.Toast;
import android.widget.TextView;

public class DisplayQuiz extends Activity {
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_quiz);
        final Context context = this;
        builder = new AlertDialog.Builder(this);
        TextView quizDetail = (TextView)findViewById(R.id.quizDetail);
        Bundle infoPassed = getIntent().getExtras();
        String quizQuestion = infoPassed.getString("quiz");
        long quizID = infoPassed.getLong("ID");
        quizDetail.setText("ID:" + quizID + "  " + quizQuestion);

        Button checkAnswer = (Button)findViewById(R.id.checkAnswer);
        Button hint = (Button)findViewById(R.id.hint);

        checkAnswer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                View v = findViewById(R.id.quizDisplay_layout);
                String youAreCorrect = "You are correct!";
                int duration = Snackbar.LENGTH_SHORT;
                Snackbar snackbar = Snackbar.make(v,youAreCorrect,duration);
                snackbar.show();
            }
        });

        hint.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                View dialogLayout = inflater.inflate(R.layout.custom_dialog_layout, null);
                TextView message = (TextView) dialogLayout.findViewById(R.id.nohint);
                builder.setView(dialogLayout);
                builder.setPositiveButton(R.string.like, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(DisplayQuiz.this, "Like button clicked", Toast.LENGTH_LONG).show();
                    }
                });

                builder.setNegativeButton(R.string.dislike, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "Dislike button clicked", Toast.LENGTH_LONG).show();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}
