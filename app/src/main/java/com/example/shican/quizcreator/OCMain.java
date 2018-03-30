package com.example.shican.quizcreator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class OCMain extends AppCompatActivity {
    protected static final String ACTIVITY_NAME = "OCMain";
    private static final int ADD_REQUEST_CODE = 50;
    private static final int DEL_REQUEST_CODE = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oc_main);

        final Button addRoute = (Button) findViewById(R.id.addRoute);
        addRoute.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i(ACTIVITY_NAME, "User clicked Add Route");
                Intent i = new Intent(OCMain.this, OCAdd.class);
                startActivityForResult(i, ADD_REQUEST_CODE);
            }
        });

        final Button delRoute = (Button) findViewById(R.id.delRoute);
        delRoute.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i(ACTIVITY_NAME, "User clicked Delete Route");
                Intent i = new Intent(OCMain.this, OCDelete.class);
                startActivityForResult(i, DEL_REQUEST_CODE);
            }
        });

        final Button infor = (Button) findViewById(R.id.routeInfor);
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
}

