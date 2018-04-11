package com.example.shican.quizcreator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.support.design.widget.Snackbar;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.EditText;

public class DisplayQuiz extends Toolbar {
    AlertDialog.Builder builder;
    String quizQuestion,quizAnswer, type, quizAns1,quizAns2,quizAns3,quizAns4,enteredAns,prompt;
    long quizID;
    TextView quizDetail;
    EditText enterAnswer;
    Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_quiz);
        initToolbar();

        final Context context = this;
        builder = new AlertDialog.Builder(this);
        enterAnswer = (EditText)findViewById(R.id.enterAnswer);
        quizDetail = (TextView)findViewById(R.id.quizDetail);
        Bundle infoPassed = getIntent().getExtras();
        quizID = infoPassed.getLong("ID");
        quizQuestion = infoPassed.getString("quiz");
        type = infoPassed.getString("type");
        quizAnswer = infoPassed.getString("correctAns");
        if(type.equalsIgnoreCase("mc")){
            quizAns1 = infoPassed.getString("ans1");
            quizAns2 = infoPassed.getString("ans2");
            quizAns3 = infoPassed.getString("ans3");
            quizAns4 = infoPassed.getString("ans4");
        }
        quizDetail.setText("ID:" + quizID + "  " + quizQuestion);

        Button checkAnswer = (Button)findViewById(R.id.checkAnswer);
        checkAnswer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                View v = findViewById(R.id.quizDisplay_layout);
                enteredAns = enterAnswer.getText().toString();
                int duration = Snackbar.LENGTH_SHORT;
                if(enteredAns.equalsIgnoreCase(quizAnswer)){
                    prompt = "You are correct!";
                } else {
                    prompt = "You didn't select the correct answer.";
                }
                snackbar = Snackbar.make(v,prompt,duration);
                snackbar.show();
            }
        });

        /*
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
        */
    }
}
