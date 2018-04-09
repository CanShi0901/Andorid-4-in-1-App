package com.example.shican.quizcreator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
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
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class OCResultRoute extends Toolbar{
    protected static final String ACTIVITY_NAME = "OCResultRoute";
    searchStop s;

    public TextView stopNum;
    public TextView stopDes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oc_result_route);
        initToolbar();

        Intent i = getIntent();
        String enterStop = i.getExtras().getString("enterStop");

        stopNum = (TextView) findViewById(R.id.stopNum);
        stopDes = (TextView) findViewById(R.id.stopDes);

        stopNum.setText(stopNum.getText()+enterStop);
        s = new searchStop();
        s.execute("https://api.octranspo1.com/v1.2/GetRouteSummaryForStop?appID=223eb5c3&&apiKey=ab27db5b435b8c8819ffb8095328e775&stopNo="+enterStop);

        final Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }

    public class searchStop extends AsyncTask<String, Integer, String> {
        String desctiption="";
        ArrayList<String> number= new ArrayList<String>();
        ArrayList<String> directionId= new ArrayList<String>();
        ArrayList<String> direction= new ArrayList<String>();
        ArrayList<String> heading= new ArrayList<String>();
        ArrayList<String> additem= new ArrayList<String>();
        int count;

        protected String doInBackground(String... args){
            Log.i(ACTIVITY_NAME, args[0]);

            try {
                URL url = new URL(args[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000 );
                conn.connect();

                InputStream stream = conn.getInputStream();
                XmlPullParser parser = Xml.newPullParser();
                parser.setInput(stream, null);

                while (parser.next() != XmlPullParser.END_DOCUMENT) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }

                    if (parser.getName().equals("StopDescription")){
                        parser.next();
                        desctiption = parser.getText();
                    }

                    else if (parser.getName().equals("RouteNo")) {
                        parser.next();
                        number.add(parser.getText());
                        count++;
                    }

                    else if (parser.getName().equals("DirectionID")) {
                        parser.next();
                        directionId.add(parser.getText());
                    }

                    else if (parser.getName().equals("Direction")) {
                        parser.next();
                        direction.add(parser.getText());
                    }

                    else if (parser.getName().equals("RouteHeading")) {
                        parser.next();
                        heading.add(parser.getText());
                    }
                }

            }catch (XmlPullParserException e) {
            }catch(IOException exception){ }
            return null;
        }

        protected void onPostExecute(String result) {
            stopDes.setText(stopDes.getText()+desctiption);
            ListView list = (ListView) findViewById(R.id.list);

            if(desctiption==null){
                additem.add("Invalid stop number");
            }else if(count>0) {
                for (int i = 0; i < count; i++) {
                    additem.add("Rount No: " + number.get(i) + "\nDirectionId: " + directionId.get(i) + "\nDirection: " + direction.get(i) + "\nHeading: " + heading.get(i));
                }
            }else{
                additem.add("No bus schedule");
            }

            ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, additem){
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
}
