package com.example.shican.quizcreator;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import java.util.ArrayList;

public class CreateQuiz extends Toolbar {
    Button save,mc,tf,nu;
    FrameLayout container;
    mcFragment mcF;
    tfFragment tfF;
    nuFragment nuF;
    FragmentManager fm;
    QuizDatabaseHelper helper;
    FragmentTransaction ft;
    String selectedType;
    ArrayList<String> info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quiz);
        initToolbar();
        save = (Button)findViewById(R.id.save);
        mc = (Button)findViewById(R.id.mc);
        tf = (Button)findViewById(R.id.tf);
        nu = (Button)findViewById(R.id.nu);
        container = (FrameLayout)findViewById(R.id.container);
        mcF = new mcFragment();
        tfF = new tfFragment();
        nuF = new nuFragment();
        helper = new QuizDatabaseHelper(this);
        fm = getFragmentManager();
        ft =fm.beginTransaction();

        mc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!ft.isEmpty()){
                    ft=fm.beginTransaction();
                }
                selectedType="mc";
                ft.replace(R.id.container, mcF);
                ft.addToBackStack("");
                ft.commit();
            }
        });

        tf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!ft.isEmpty()){
                    ft=fm.beginTransaction();
                }
                selectedType="tf";
                ft.replace(R.id.container, tfF);
                ft.addToBackStack("");
                ft.commit();
            }
        });

        nu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!ft.isEmpty()){
                    ft=fm.beginTransaction();
                }
                selectedType="nu";
                ft.replace(R.id.container, nuF);
                ft.addToBackStack("");
                ft.commit();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateQuiz.this, QuizMain.class);
                if(selectedType.equalsIgnoreCase("mc")){
                    info = mcF.getData();
                    intent.putExtra("type","mc");
                    intent.putExtra("question",info.get(0));
                    intent.putExtra("ans1", info.get(1));
                    intent.putExtra("ans2", info.get(2));
                    intent.putExtra("ans3", info.get(3));
                    intent.putExtra("ans4", info.get(4));
                }
                else if (selectedType.equalsIgnoreCase("tf")){
                    info = tfF.getData();
                    intent.putExtra("type","tf");
                    intent.putExtra("question",info.get(0));
                    intent.putExtra("ans", info.get(1));
                }
                else if(selectedType.equalsIgnoreCase("nu")){
                    info = nuF.getData();
                    intent.putExtra("type", "nu");
                    intent.putExtra("question", info.get(0));
                    intent.putExtra("ans", info.get(1));
                }
                setResult(RESULT_OK,intent);
                finish();
            }
        });

    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu){
        menu.findItem(R.id.help).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                new AlertDialog.Builder(CreateQuiz.this)
                        .setTitle("Help")
                        .setMessage("Activity developed by Can Shi "+ "\n" +
                                "Version number: v1.0"+ "\n" +
                                "First select a quiz type by clicking one of the buttons on top."
                        +"Then enter your questions and answers and click 'SAVE QUESTION'.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();
                return true;
            }
        });
        return true;
    }

}
