package com.example.shican.quizcreator;


import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class OCFragmentRouteInformation extends Fragment {
    public ListView list;

    TextView stopNum;
    TextView stopDes;
    TextView routeNum;
    TextView routeDes;

    public OCFragmentRouteInformation(){}

    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_oc_route_information, container, false);
        //ListView list = (ListView) view.findViewById(R.id.list);
        stopNum = (TextView) view.findViewById(R.id.stopNum);
        stopDes = (TextView) view.findViewById(R.id.stopDes);
        routeNum = (TextView)view.findViewById(R.id.routeNum);
        routeDes = (TextView) view.findViewById(R.id.routeDes);
        return view;
    }

    //run connection
    public void routeInfor(String s, String r) {
        getInfor getInfor = new getInfor();
        getInfor.execute("https://api.octranspo1.com/v1.2/GetNextTripsForStop?appID=223eb5c3&&apiKey=ab27db5b435b8c8819ffb8095328e775&routeNo=" + r + "&stopNo="+s);

        stopNum.setText(stopNum.getText()+s);
        routeNum.setText(routeNum.getText()+r);
    }

    //connect http
    public class getInfor extends AsyncTask<String, Integer, String> {
        String getStopDes="";
        String getRouteDes="";
        ArrayList<String> distination = new ArrayList<String>();
        ArrayList<String> startTime= new ArrayList<String>();
        ArrayList<String> adjTime= new ArrayList<String>();
        ArrayList<String> gps= new ArrayList<String>();
        ArrayList<String> latiude= new ArrayList<String>();
        ArrayList<String> longitude= new ArrayList<String>();
        public ArrayList<String> additem= new ArrayList<String>();

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
                }
            }else{
                additem.add("No bus schedule");
            }

            ListView list = (ListView) getActivity().findViewById(R.id.list);
            ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, additem){
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
}