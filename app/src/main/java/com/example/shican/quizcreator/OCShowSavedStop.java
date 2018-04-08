package com.example.shican.quizcreator;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class OCShowSavedStop extends Toolbar {
    protected static final String ACTIVITY_NAME = "OCShowSavedStop";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oc_show_saved_stop);
        initToolbar();

        final Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                finish();
            }
        });

        OCFragmentSavedStop save = ( OCFragmentSavedStop)getSupportFragmentManager().findFragmentById(R.id.fragment3);
        save.showSavedStop(OCMain.saveArrayList);
   }

    //help toolbar
    public boolean onPrepareOptionsMenu (Menu menu){
        menu.findItem(R.id.help).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                new AlertDialog.Builder(OCShowSavedStop.this)
                        .setTitle("Help")
                        .setMessage("Activity developped by Yuxin Zhang "+ "\n" +
                                "Version number v1.0")
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

