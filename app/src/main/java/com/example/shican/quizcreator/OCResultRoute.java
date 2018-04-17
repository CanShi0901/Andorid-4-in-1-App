package com.example.shican.quizcreator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import java.util.ArrayList;

public class OCResultRoute extends Toolbar {
    protected static final String ACTIVITY_NAME = "OCResultRoute";

    String route = null;
    public TextView stopNum;
    public TextView stopDes;
    int savedAdj;
    String savedRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oc_result_route);
        initToolbar();

        Intent i = getIntent();
        String enterStop = i.getExtras().getString("enterStop");

        final Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        OCFragmentSearchResult result = (OCFragmentSearchResult) getSupportFragmentManager().findFragmentById(R.id.fragment2);
        result.searchResult(enterStop);
    }


    //help toolbar
    public boolean onPrepareOptionsMenu (Menu menu){
        menu.findItem(R.id.help).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                new AlertDialog.Builder(OCResultRoute.this)
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 100){
            savedAdj = data.getIntExtra("saveAdj",1);
            savedRoute = data.getStringExtra("savedRoute");

            OCMain.saveRouteValues.put(OCSavedRouteDatabaseHelper.KEY_MESSAGE, savedRoute);
            OCMain.saveRouteValues.put(OCSavedRouteDatabaseHelper.KEY_STAT, savedAdj);
            OCMain.saveRouteDB.insert(OCSavedRouteDatabaseHelper.TABLE_NAME, null, OCMain.saveRouteValues);
            OCMain.saveRoute.add(savedRoute);
            OCMain.adj.add(savedAdj);

            Log.i(ACTIVITY_NAME, "ADJ search result"+ savedAdj);
        }
    }
}
