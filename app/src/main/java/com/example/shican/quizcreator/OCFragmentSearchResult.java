package com.example.shican.quizcreator;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
/*
@author yuxin zhang
searchresult fragment, shows stop information that get from url
 */

public class OCFragmentSearchResult extends Fragment {
    //layout variables
    public TextView stopNum;
    public TextView stopDes;
    public ListView list;
    public Button back;

    public OCFragmentSearchResult(){}

    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_oc_search_result, container, false);
        stopNum = (TextView) view.findViewById(R.id.stopNum);
        stopDes = (TextView) view.findViewById(R.id.stopDes);

        return view;
    }

    //excute and get from url with input stop number
    public void searchResult(String enterStop) {
        stopNum.setText(stopNum.getText()+enterStop);
        searchStop s = new searchStop();
        s.execute("https://api.octranspo1.com/v1.2/GetRouteSummaryForStop?appID=223eb5c3&&apiKey=ab27db5b435b8c8819ffb8095328e775&stopNo=" + enterStop);
    }

    public class searchStop extends AsyncTask<String, Integer, String> {
        //route information variables
        String stopPass = "";
        String desctiption="";

        ArrayList<String> number= new ArrayList<String>();
        ArrayList<String> directionId= new ArrayList<String>();
        ArrayList<String> direction= new ArrayList<String>();
        ArrayList<String> heading= new ArrayList<String>();
        public ArrayList<String> additem= new ArrayList<String>();

        int count;

        //connect url, search data
        protected String doInBackground(String... args) {
            try {
                URL url = new URL(args[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.connect();

                InputStream stream = conn.getInputStream();
                XmlPullParser parser = Xml.newPullParser();
                parser.setInput(stream, null);

                while (parser.next() != XmlPullParser.END_DOCUMENT) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }

                    if (parser.getName().equals("StopNo")) {
                        parser.next();
                        stopPass = parser.getText();
                    } else if (parser.getName().equals("StopDescription")) {
                        parser.next();
                        desctiption = parser.getText();
                    } else if (parser.getName().equals("RouteNo")) {
                        parser.next();
                        number.add(parser.getText());
                        count++;
                    } else if (parser.getName().equals("DirectionID")) {
                        parser.next();
                        directionId.add(parser.getText());
                    } else if (parser.getName().equals("Direction")) {
                        parser.next();
                        direction.add(parser.getText());
                    } else if (parser.getName().equals("RouteHeading")) {
                        parser.next();
                        heading.add(parser.getText());
                    }
                }

            } catch (XmlPullParserException e) {
            } catch (IOException exception) {
            }
            return null;
        }

        //display data
        protected void onPostExecute(String result) {
            stopDes.setText(stopDes.getText()+desctiption);
            //handle status
            if(desctiption==null){
                additem.add("Invalid stop number");
            }else if(count>0) {
                for (int i = 0; i < count; i++) {
                    additem.add("Rount No: " + number.get(i) + "\nDirectionId: " + directionId.get(i) + "\nDirection: " + direction.get(i) + "\nHeading: " + heading.get(i));
                }
            }else{
                additem.add("No bus schedule");
            }

            //show stop information in listview
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

            //click stop information intent route information
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapter, View v, int pos, long a) {
                    String enterStop = (String) adapter.getItemAtPosition(pos);
                    Intent intent = new Intent(getActivity(), OCInfor.class);
                    intent.putExtra("stop", stopPass);
                    if(count>0) intent.putExtra("route", number.get(pos));
                    startActivityForResult(intent,100);
                }
            });
        }
    }
}