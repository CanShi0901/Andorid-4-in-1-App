package com.example.shican.quizcreator;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.support.v4.app.Fragment;
import java.util.ArrayList;

public class OCMain extends Toolbar implements OCFragmentSearch.OCFragmentSearchListener {
    protected static final String ACTIVITY_NAME = "OCMain";
    private static final int ADDDEL_REQUEST_CODE = 50;

    //database
    public static OCSavedStopDatabaseHelper saveHelper;
    public static SQLiteDatabase saveDB;
    public static ContentValues saveValues;
    public static Cursor saveCu;
    public static ContentValues saveContent;
    public static ArrayList<String> saveArrayList = new ArrayList<String>();

    public static Fragment f = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oc_main);
        initToolbar();

        //saved stop database
        saveHelper = new OCSavedStopDatabaseHelper(this);
        saveDB = saveHelper.getWritableDatabase();
        saveValues = new ContentValues();

        saveCu = saveDB.query(false,  OCSavedStopDatabaseHelper.TABLE_NAME, new String[]{OCSavedStopDatabaseHelper.KEY_ID, OCSavedStopDatabaseHelper.KEY_MESSAGE},null, null, null, null, null, null);
        saveCu.moveToFirst();

        while(!saveCu.isAfterLast() ) {
            String newMessage = saveCu.getString(saveCu.getColumnIndex(OCSavedStopDatabaseHelper.KEY_MESSAGE));
            saveArrayList.add(newMessage);
            saveCu.moveToNext();
        }

        //save fragment
        final Button viewStop = (Button) findViewById(R.id.viewStop);
        viewStop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                f = new OCFragmentSavedStop();
                fragmentSwitch(f);
            }
        });

        //search fragment
        final Button searchStop = (Button) findViewById(R.id.searchStop);
        searchStop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               f = new OCFragmentSearch();
               fragmentSwitch(f);
            }
        });

        //add delete activity
        final Button adddelStop = (Button) findViewById(R.id.adddelStop);
        adddelStop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(OCMain.this, OCAddDel.class);
                i.putExtra("saveList", saveArrayList);
                startActivityForResult(i, ADDDEL_REQUEST_CODE);
            }
        });

        /*
        //temp
        final Button infor = (Button) findViewById(R.id.reroute);
        infor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(OCMain.this, OCInfor.class);
                startActivity(i);
            }
        });*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADDDEL_REQUEST_CODE) {
            String text = data.getStringExtra("Response");
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(OCMain.this, text, duration);
            toast.show();

            ArrayList<String> addTemp = data.getStringArrayListExtra("addStop");
            for(String temp: addTemp){
                saveValues.put(OCSavedStopDatabaseHelper.KEY_MESSAGE, temp);
                saveDB.insert(OCSavedStopDatabaseHelper.TABLE_NAME, null, OCMain.saveValues);
                saveArrayList.add(temp);
            }

            ArrayList<String> delTemp = data.getStringArrayListExtra("delStop");
            for(String temp: delTemp){
                saveDB.delete(OCSavedStopDatabaseHelper.TABLE_NAME,OCSavedStopDatabaseHelper.KEY_MESSAGE +"=" + temp,null);
                saveArrayList.remove(temp);
            }
        }
    }

    public void fragmentSwitch(Fragment f){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment, f,"currentFragment");
        transaction.commit();
    }

    //search fragment override
    public void inputSearch(String enterStop){
        if((!enterStop.matches("[-+]?\\d*\\.?\\d+")) && (enterStop != " ")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(OCMain.this);
            builder.setMessage("Please enter valid stop number");

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });

            builder.show();
        }else {
            Intent intent = new Intent(OCMain.this, OCResultRoute.class);
            intent.putExtra("enterStop", enterStop);
            startActivity(intent);

        }
    }

    //help toolbar
    public boolean onPrepareOptionsMenu (Menu menu){
        menu.findItem(R.id.help).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                new AlertDialog.Builder(OCMain.this)
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

