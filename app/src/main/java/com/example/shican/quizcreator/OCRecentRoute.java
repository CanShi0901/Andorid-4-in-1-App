package com.example.shican.quizcreator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class OCRecentRoute extends Toolbar {
    public ArrayList<String> saveList = new ArrayList<String>();
    public ArrayList<Integer> adj = new ArrayList<Integer>();
    public Integer total = 0;
    public long count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oc_saved_route);
        initToolbar();

        final ListView list = (ListView) findViewById(R.id.list);
        final Button back = (Button) findViewById(R.id.back);

        Intent i = getIntent();
        saveList= i.getStringArrayListExtra("saveRoute");
        adj = i.getIntegerArrayListExtra("adj");
        count=i.getLongExtra("count",1);

        for(int j = 0; j < adj.size(); j ++){
            total += adj.get(j);
            Log.i("", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"+adj.get(j));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(OCRecentRoute.this, android.R.layout.simple_list_item_1, saveList) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.BLACK);
                return view;
            }
        };
        list.setAdapter(adapter);

        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(OCRecentRoute.this, OCMain.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        });
    }

    //help toolbar
    public boolean onPrepareOptionsMenu (Menu menu){
        menu.findItem(R.id.help).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                new AlertDialog.Builder(OCRecentRoute.this)
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

        menu.findItem(R.id.stats).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                new AlertDialog.Builder(OCRecentRoute.this)
                        .setTitle("Help")
                        .setMessage( "average adjustedScheduleTime: " + total/count)
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
