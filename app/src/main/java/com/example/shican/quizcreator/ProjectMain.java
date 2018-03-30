package com.example.shican.quizcreator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class ProjectMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_main);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.project_main_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.quizCreator:
                intent=new Intent(ProjectMain.this, QuizMain.class);
                startActivity(intent);
                break;
            case R.id.patient:
                intent=new Intent(ProjectMain.this, QuizMain.class);
                startActivity(intent);
                break;
            case R.id.octranspo:
                intent=new Intent(ProjectMain.this, OCMain.class);
                startActivity(intent);
                break;
            default:

        }
        return true;
    }
}
