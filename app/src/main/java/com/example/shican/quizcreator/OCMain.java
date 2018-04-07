package com.example.shican.quizcreator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class OCMain extends Toolbar {
    protected static final String ACTIVITY_NAME = "OCMain";
    private static final int ADD_REQUEST_CODE = 50;
    private static final int DEL_REQUEST_CODE = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oc_main);
        initToolbar();

        final Button addStop = (Button) findViewById(R.id.addStop);
        addStop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i(ACTIVITY_NAME, "User clicked Add Stop");
                Intent i = new Intent(OCMain.this, OCAdd.class);
                startActivityForResult(i, ADD_REQUEST_CODE);
            }
        });

        final Button delStop = (Button) findViewById(R.id.delStop);
        delStop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i(ACTIVITY_NAME, "User clicked Delete Stop");
                Intent i = new Intent(OCMain.this, OCDelete.class);
                startActivityForResult(i, DEL_REQUEST_CODE);
            }
        });

        final Button infor = (Button) findViewById(R.id.reroute);
        infor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i(ACTIVITY_NAME, "User clicked Route Information");
                Intent i = new Intent(OCMain.this, OCInfor.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_REQUEST_CODE) {
            Log.i(ACTIVITY_NAME, "Returned to "+ACTIVITY_NAME);

            String text = data.getStringExtra("Response");
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(OCMain.this, text, duration);
            toast.show();
        }

        if (requestCode == DEL_REQUEST_CODE) {
            Log.i(ACTIVITY_NAME, "Returned to "+ACTIVITY_NAME);

            String text = data.getStringExtra("Response");
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(OCMain.this, text, duration);
            toast.show();
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

