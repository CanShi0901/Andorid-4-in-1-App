package com.example.shican.quizcreator;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.support.v4.app.Fragment;
import java.util.ArrayList;
/*
@file name: OCMain
@author: yuxin zhang
@course: cst 2335
@assignemnt: final projact
@date: April 18, 2018
@professor: eric
@purpose:main for OCTranspo, contians add/delete, search, saved stop, recent route
*/

public class OCMain extends Toolbar implements OCFragmentSearch.OCFragmentSearchListener {
    protected static final String ACTIVITY_NAME = "OCMain";
    private static final int ADDDEL_REQUEST_CODE = 50;

    //saved database
    public static OCSavedStopDatabaseHelper saveHelper;
    public static SQLiteDatabase saveStopDB;
    public static ContentValues saveStopValues;
    public static Cursor saveCu;
    public static ArrayList<String> saveArrayList = new ArrayList<String>();

    //recent database
    public static OCSavedRouteDatabaseHelper saveRouteHelper;
    public static SQLiteDatabase saveRouteDB;
    public static ContentValues saveRouteValues;
    public static Cursor saveRouteCu;
    public static ArrayList<String> saveRoute = new ArrayList<String>();
    public static ArrayList<Integer> adj = new ArrayList<>();

    public static Fragment f = null;
    public ProgressBar progress;
    int p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oc_main);
        initToolbar();

        //saved stop database
        saveHelper = new OCSavedStopDatabaseHelper(this);
        saveStopDB = saveHelper.getWritableDatabase();
        saveStopValues = new ContentValues();

        saveCu = saveStopDB.query(false, OCSavedStopDatabaseHelper.TABLE_NAME, new String[]{OCSavedStopDatabaseHelper.KEY_ID, OCSavedStopDatabaseHelper.KEY_MESSAGE}, null, null, null, null, null, null);
        saveCu.moveToFirst();

        //store database to saved arraylist
        while (!saveCu.isAfterLast()) {
            String newMessage = saveCu.getString(saveCu.getColumnIndex(OCSavedStopDatabaseHelper.KEY_MESSAGE));
            saveArrayList.add(newMessage);
            saveCu.moveToNext();
        }

        //saved route database
        saveRouteHelper = new OCSavedRouteDatabaseHelper(this);
        saveRouteDB = saveRouteHelper.getWritableDatabase();
        saveRouteValues = new ContentValues();

        saveRouteCu = saveRouteDB.query(false, OCSavedRouteDatabaseHelper.TABLE_NAME, new String[]{OCSavedRouteDatabaseHelper.KEY_ID, OCSavedRouteDatabaseHelper.KEY_MESSAGE, OCSavedRouteDatabaseHelper.KEY_STAT}, null, null, null, null, null, null);
        saveRouteCu.moveToFirst();

        //store database to recent database
        while (!saveRouteCu.isAfterLast()) {
            String newMessage = saveRouteCu.getString(saveRouteCu.getColumnIndex(OCSavedRouteDatabaseHelper.KEY_MESSAGE));
            adj.add(saveRouteCu.getInt(saveRouteCu.getColumnIndex(OCSavedRouteDatabaseHelper.KEY_STAT)));
            saveRoute.add(newMessage);
            saveRouteCu.moveToNext();
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

        //recent activity
        final Button infor = (Button) findViewById(R.id.reroute);
        infor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                long count = DatabaseUtils.queryNumEntries(saveRouteDB, OCSavedRouteDatabaseHelper.TABLE_NAME);
                Intent i = new Intent(OCMain.this, OCRecentRoute.class);
                i.putStringArrayListExtra("saveRoute", saveRoute);
                i.putIntegerArrayListExtra("adj", adj);
                i.putExtra("count", count);
                startActivity(i);
            }
        });
    }

    //update saved arraylist and saved database with add/delete variables
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADDDEL_REQUEST_CODE) {
            String text = data.getStringExtra("Response");
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(OCMain.this, text, duration);
            toast.show();

            ArrayList<String> addTemp = data.getStringArrayListExtra("addStop");
            for (String temp : addTemp) {
                saveStopValues.put(OCSavedStopDatabaseHelper.KEY_MESSAGE, temp);
                saveStopDB.insert(OCSavedStopDatabaseHelper.TABLE_NAME, null, OCMain.saveStopValues);
                saveArrayList.add(temp);
            }

            ArrayList<String> delTemp = data.getStringArrayListExtra("delStop");
            for (String temp : delTemp) {
                saveStopDB.delete(OCSavedStopDatabaseHelper.TABLE_NAME, OCSavedStopDatabaseHelper.KEY_MESSAGE + "=" + temp, null);
                saveArrayList.remove(temp);
            }
        }
    }

    //search/save fragment switch
    public void fragmentSwitch(Fragment f) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment, f, "currentFragment");
        transaction.commit();
    }

    //search fragment override
    public void inputSearch(String enterStop) {
        if ((!enterStop.matches("[-+]?\\d*\\.?\\d+")) && (enterStop != " ")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(OCMain.this);
            builder.setMessage("Please enter valid stop number");

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });

            builder.show();
        } else {
            Intent intent = new Intent(OCMain.this, OCResultRoute.class);
            intent.putExtra("enterStop", enterStop);
            startActivity(intent);
        }
    }

    //help toolbar
    public boolean onPrepareOptionsMenu (Menu menu) {
        menu.findItem(R.id.help).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                new AlertDialog.Builder(OCMain.this)
                        .setTitle("Help")
                        .setMessage("Activity developped by Yuxin Zhang " + "\n" +
                                "Version number v7.0 \n\nInstructions: \n1.search with stop number to see stop information and related route information" +
                                "\n2.search has recent search, it shows user search history but it does not have database" +
                                "\n3.add/del saved stop\n4.saved stop shows saved stop database \n" +
                                "5.recent route shows user recent viewed routes database that related to search/saved stop.")
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

