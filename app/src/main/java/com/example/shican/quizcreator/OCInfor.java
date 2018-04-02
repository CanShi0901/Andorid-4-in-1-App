package com.example.shican.quizcreator;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class OCInfor extends Toolbar {
    protected static final String ACTIVITY_NAME = "OCInfor";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oc_infor);
        initToolbar();

        final Button done = (Button) findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(OCInfor.this, OCMain.class);
                startActivity(i);
            }
        });

        ListView list = (ListView) findViewById(R.id.list);
        String[] inforList= {"Destination", "Latitude", "Longitude", "GPS Speed", "Start Time", "Adjusted Schedule Time"};
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, inforList){
            public View getView(int position, View convertView, ViewGroup parent) {
                View view =super.getView(position, convertView, parent);
                TextView textView=(TextView) view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.BLACK);
                return view;
            }
        };
        list.setAdapter(adapter);

        Snackbar.make(findViewById(android.R.id.content), "Detail Route Information shows", Snackbar.LENGTH_LONG).show();
    }
}
