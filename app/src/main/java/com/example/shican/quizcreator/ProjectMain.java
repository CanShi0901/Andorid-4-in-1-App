package com.example.shican.quizcreator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class ProjectMain extends AppCompatActivity {
    /**
     * sets menu items
     * @param menu
     * @return
     */
    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        MenuItem importItem = (MenuItem) menu.findItem(R.id.import_resource);
        importItem.setVisible(false);
        MenuItem statItem = (MenuItem) menu.findItem(R.id.stats);
        statItem.setVisible(false);
        menu.findItem(R.id.help).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                new AlertDialog.Builder(ProjectMain.this)
                        .setTitle(R.string.menu_help)
                        .setMessage("Activity developped by Nan Jiang, Can Shi, Yuxin Zhang "+ "\n" +
                                "Version number: v1.0"+ "\n" +
                                "This project has 3 functions: quiz creator, pation record, and ocTranspo stop search")
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
                intent=new Intent(ProjectMain.this, PatientListActivity.class);
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
