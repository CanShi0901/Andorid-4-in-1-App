package com.example.shican.quizcreator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * Created by shican on 2018-03-29.
 */

public class Toolbar extends AppCompatActivity {
    private MenuItem helpMenu;
    protected void onCreate(Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
    }

    /**
     * displays the toolbar
     */
    public void initToolbar(){
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.project_main_actions, menu);
        return true;
    }

    /**
     * when each item is selected, it goes to the corresponding activity
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.quizCreator:
                intent=new Intent(Toolbar.this, QuizMain.class);
                startActivity(intent);
                break;

            case R.id.patient:
                intent=new Intent(Toolbar.this, PatientListActivity.class);
                startActivity(intent);
                break;
            case R.id.octranspo:
                intent=new Intent(Toolbar.this, OCMain.class);
                startActivity(intent);
                break;
            default:

        }
        return true;
    }
}
