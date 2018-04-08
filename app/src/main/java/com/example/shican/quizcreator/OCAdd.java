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

public class OCAdd extends Toolbar {
    protected static final String ACTIVITY_NAME = "OCAdd";
    private static int result = 0;
    private String enterStop= "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oc_add);
        initToolbar();


        final EditText add = (EditText)findViewById(R.id.add);
        final Button addButton = (Button) findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                enterStop = add.getText().toString();
                if((!enterStop.matches("[-+]?\\d*\\.?\\d+")) && (enterStop != " ")) {
                }else {
                    OCMain.saveValues.put(OCSavedStopDatabaseHelper.KEY_MESSAGE, enterStop);
                    OCMain.saveDB.insert(OCSavedStopDatabaseHelper.TABLE_NAME, null,OCMain.saveValues);
                }
            }
        });

        final Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                if(result >= 1) resultIntent.putExtra("Response","Stop Added");
                if(result == 0) resultIntent.putExtra("Response","No stop Added");
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        final Button search = (Button)findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                enterStop = add.getText().toString();

                if((!enterStop.matches("[-+]?\\d*\\.?\\d+")) && (enterStop != " ")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(OCAdd.this);
                    builder.setMessage("Please enter valid stop number");

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });

                    builder.show();
                }else {
                    createRecentStop(enterStop);

                    Intent intent = new Intent(OCAdd.this, OCResultRoute.class);
                    intent.putExtra("enterStop", enterStop);
                    startActivity(intent);
                }
            }
        });
    }

    public void createRecentStop(String stop){
        OCFragmentRecentStops recent = (OCFragmentRecentStops)getSupportFragmentManager().findFragmentById(R.id.fragment);
        recent.createRecentStop(stop);
    }

    //help toolbar
    public boolean onPrepareOptionsMenu (Menu menu){
        menu.findItem(R.id.help).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                new AlertDialog.Builder(OCAdd.this)
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

