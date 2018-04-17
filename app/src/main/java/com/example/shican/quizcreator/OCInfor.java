package com.example.shican.quizcreator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class OCInfor extends Toolbar {
    protected static final String ACTIVITY_NAME = "OCInfor";
    public ListView list;
    String savedRoute= "";
    int saveAdj = 0;
    int average = 0;

    TextView stopNum;
    TextView stopDes;
    TextView routeNum;
    TextView routeDes;
    ProgressBar progress;

    int  p = 0;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oc_infor);
        initToolbar();

        Intent i = getIntent();
        String stop = i.getExtras().getString("stop");
        String route = i.getExtras().getString("route");

        stopNum = (TextView) findViewById(R.id.stopNum);
        stopDes = (TextView) findViewById(R.id.stopDes);
        routeNum = (TextView) findViewById(R.id.routeNum);
        routeDes = (TextView) findViewById(R.id.routeDes);
        progress = (ProgressBar) findViewById(R.id.inProgress);

        final Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (OCMain.saveRoute.contains(savedRoute)) {
                } else {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("saveAdj", saveAdj);
                    resultIntent.putExtra("savedRoute", savedRoute);
                    setResult(100, resultIntent);
                }
                finish();
            }
        });

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

        getInfor getInfor = new getInfor();
        getInfor.execute("https://api.octranspo1.com/v1.2/GetNextTripsForStop?appID=223eb5c3&&apiKey=ab27db5b435b8c8819ffb8095328e775&routeNo=" + route + "&stopNo="+stop);

        stopNum.setText(stopNum.getText()+stop);
        routeNum.setText(routeNum.getText()+route);
    }

    public class getInfor extends AsyncTask<String, Integer, String> {
        String getStopDes="";
        String getRouteDes="";
        ArrayList<String> distination = new ArrayList<String>();
        ArrayList<String> startTime= new ArrayList<String>();
        ArrayList<String> adjTime= new ArrayList<String>();
        ArrayList<String> gps= new ArrayList<String>();
        ArrayList<String> latiude= new ArrayList<String>();
        ArrayList<String> longitude= new ArrayList<String>();
        ArrayList<String> additem= new ArrayList<String>();

        int count;

        //find data in http
        protected String doInBackground(String... args){
            try {
                URL url = new URL(args[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000 );
                conn.connect();

                Log.i("aaa", args[0]);

                InputStream stream = conn.getInputStream();
                XmlPullParser parser = Xml.newPullParser();
                parser.setInput(stream, null);

                while (parser.next() != XmlPullParser.END_DOCUMENT) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }

                    if (parser.getName().equals("StopLabel")){
                        parser.next();
                        getStopDes = parser.getText();
                    }

                    else if (parser.getName().equals("RouteLabel")) {parser.next();
                        getRouteDes = parser.getText();
                    }

                    else if (parser.getName().equals("TripDestination")){
                        parser.next();
                        distination.add(parser.getText());
                        count++;
                    }

                    else if (parser.getName().equals("TripStartTime")){
                        parser.next();
                        startTime.add(parser.getText());
                    }

                    else if (parser.getName().equals("AdjustedScheduleTime")){
                        parser.next();
                        adjTime.add(parser.getText());
                    }

                    else if (parser.getName().equals("GPSSpeed")){
                        parser.next();
                        gps.add(parser.getText());
                    }

                    else if (parser.getName().equals("Latitude")){
                        parser.next();
                        latiude.add(parser.getText());
                    }

                    else if (parser.getName().equals("Longitude")){
                        parser.next();
                        longitude.add(parser.getText());
                    }
                }

            }catch (XmlPullParserException e) {
            }catch(IOException exception){ }
            return null;
        }

        //diaplay data
        protected void onPostExecute(String result) {
            stopDes.setText(stopDes.getText()+getStopDes);
            routeDes.setText(routeDes.getText()+getRouteDes);

            if(getStopDes==""){
                additem.add("Invalid stop number");
            }else if(count>0) {
                for (int i = 0; i < count; i++) {
                    additem.add("Trip Distination: " + distination.get(i) + "\nTrip Start Time: " + startTime.get(i) + "\nAdjustedScheduleTime: " + adjTime.get(i) +
                            "\nGPS speed: " + gps.get(i) + "\nLatiude: " + latiude.get(i) + "\nLongitude: " + longitude.get(i));
                    average += Integer.valueOf(adjTime.get(i));
                }
                savedRoute = stopNum.getText() +  "\n" + stopDes.getText()  + "\n" + routeNum.getText() + "\n"+ routeDes.getText() + "Trip Distination: " + distination.get(0) + "\nTrip Start Time: " + startTime.get(0) + "\nAdjustedScheduleTime: " + adjTime.get(0) +
                        "\nGPS speed: " + gps.get(0) + "\nLatiude: " + latiude.get(0) + "\nLongitude: " + longitude.get(0);
                saveAdj = Integer.valueOf(adjTime.get(0));
            }else{
                additem.add("No bus schedule");
            }

            ListView list = (ListView) findViewById(R.id.list);
            ArrayAdapter<String> adapter=new ArrayAdapter<String>(OCInfor.this, android.R.layout.simple_list_item_1, additem){
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view =super.getView(position, convertView, parent);
                    TextView textView=(TextView) view.findViewById(android.R.id.text1);
                    textView.setTextColor(Color.BLACK);
                    return view;
                }
            };
            list.setAdapter(adapter);
        }
    }

    //help toolbar
    public boolean onPrepareOptionsMenu (Menu menu){
        menu.findItem(R.id.help).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                new AlertDialog.Builder(OCInfor.this)
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
                new AlertDialog.Builder(OCInfor.this)
                        .setTitle("Stat")
                        .setMessage("average adjustedScheduleTime: " + average/3)
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
