package com.example.shican.quizcreator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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

    ProgressBar progress;
    int  p = 0;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oc_result_route);
        initToolbar();

        progress = (ProgressBar) findViewById(R.id.inProgress);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(p<100){
                    p++;
                    android.os.SystemClock.sleep(6);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progress.setProgress(p);
                        }
                    });
                }
            }
        }).start();

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
                                "Version number v7.0 \n\nInstructions: \n1.shows stop information and routes at this stop\n" +
                                "2.click on route shows related route informaion\n" )
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
            if(OCMain.adj.contains(savedAdj)){

            }else {
                OCMain.saveRouteValues.put(OCSavedRouteDatabaseHelper.KEY_MESSAGE, savedRoute);
                OCMain.saveRouteValues.put(OCSavedRouteDatabaseHelper.KEY_STAT, savedAdj);
                OCMain.saveRouteDB.insert(OCSavedRouteDatabaseHelper.TABLE_NAME, null, OCMain.saveRouteValues);
                OCMain.saveRoute.add(savedRoute);
                OCMain.adj.add(savedAdj);
            }
        }
    }
}
