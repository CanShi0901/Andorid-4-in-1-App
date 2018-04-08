package com.example.shican.quizcreator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import java.util.ArrayList;

public class OCDelete extends Toolbar {
    protected static final String ACTIVITY_NAME = "OCDelete";
    private static int result = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oc_delete);
        initToolbar();

        final Button del_button = (Button) findViewById(R.id.del_button);
        del_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(OCDelete.this);
                builder.setMessage("Do you want to delete stop?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        result = 1;
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                builder.show();
            }
        });

        final Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                if(result == 1) resultIntent.putExtra("Response","Stop Deleted");
                if(result == 0) resultIntent.putExtra("Response","No stop Deleted");
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        OCMain.saveCu = OCMain.saveDB.query(false,  OCSavedStopDatabaseHelper.TABLE_NAME, new String[]{OCSavedStopDatabaseHelper.KEY_ID, OCSavedStopDatabaseHelper.KEY_MESSAGE},null, null, null, null, null, null);
        OCMain.saveCu.moveToFirst();

        while(!OCMain.saveCu.isAfterLast() ) {
            String newMessage = OCMain.saveCu.getString(OCMain.saveCu.getColumnIndex(OCSavedStopDatabaseHelper.KEY_MESSAGE));
            OCMain.saveArrayList .add(newMessage);
            OCMain.saveCu.moveToNext();
        }


        OCFragmentSavedStop save = ( OCFragmentSavedStop)getSupportFragmentManager().findFragmentById(R.id.fragment2);
        save.showSavedStop(OCMain.saveArrayList);
    }

    //help toolbar
    public boolean onPrepareOptionsMenu (Menu menu){
        menu.findItem(R.id.help).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                new AlertDialog.Builder(OCDelete.this)
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

