package com.example.shican.quizcreator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class OCAddDel extends Toolbar {
    protected static final String ACTIVITY_NAME = "OCAddDel";
    private String enterStop= "";

    private ArrayList<String> addString;
    private ArrayList<String> delString;
    private ArrayList<String> saveList;

    private int addResult;
    private int delResult;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oc_adddel);
        initToolbar();

        addResult = 0;
        delResult = 0;
        addString = new ArrayList<>();
        delString = new ArrayList<>();

        Intent i = getIntent();
        saveList= i.getStringArrayListExtra("saveList");

        final EditText input= (EditText) findViewById(R.id.input);
        final Button addButton = (Button) findViewById(R.id.add_button);
        final Button delButton = (Button) findViewById(R.id.del_button);
        final Button back = (Button) findViewById(R.id.back);


        //add button click, add input stop
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                enterStop = input.getText().toString();
                if ((!enterStop.matches("[-+]?\\d*\\.?\\d+")) && (enterStop != " ")) {
                } else {
                    if(saveList.contains(enterStop)){
                        Snackbar.make(findViewById(android.R.id.content), "Stop Already Added", Snackbar.LENGTH_LONG).show();
                    }else {
                        addResult++;
                        addString.add(enterStop);
                        input.setText("");
                        Snackbar.make(findViewById(android.R.id.content), "Stop Added", Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        });

        //delete button click, delete input stop
        delButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                enterStop = input.getText().toString();
                if ((!enterStop.matches("[-+]?\\d*\\.?\\d+")) && (enterStop != " ")) {
                } else {
                    if(saveList.contains(enterStop)){
                        delResult++;
                        delString.add(enterStop);
                        input.setText("");
                        Snackbar.make(findViewById(android.R.id.content), "Stop Deleted", Snackbar.LENGTH_LONG).show();
                    }else {
                        Snackbar.make(findViewById(android.R.id.content), "Stop Not In Saved Stop", Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        });

        //back button click, send add&delete stop back to main
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                String response = "";

                if(addResult == 0 && delResult == 0){response = "No change";}
                else{response = "Please Press Saved Stop to Refresh List";}

                resultIntent.putExtra("addStop", addString);
                resultIntent.putExtra("addStop", addString);
                resultIntent.putExtra("delStop", delString);
                resultIntent.putExtra("Response", response);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    //help toolbar
    public boolean onPrepareOptionsMenu (Menu menu){
        menu.findItem(R.id.help).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                new AlertDialog.Builder(OCAddDel.this)
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

