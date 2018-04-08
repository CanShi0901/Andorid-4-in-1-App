package com.example.shican.quizcreator;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class OCFragmentRecentStops extends Fragment {
    ListView listView;
    ArrayList<String> list= new ArrayList<String>();;
    public OCFragmentRecentStops(){}

    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_oc_recent_stop, container, false);
        listView = (ListView) view.findViewById(R.id.list);
        return view;
    }

    public void createRecentStop(String stop){
        if(list.contains(stop)){}
        else {
            list.add(stop);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list) {
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView textView = (TextView) view.findViewById(android.R.id.text1);
                    textView.setTextColor(Color.BLACK);
                    return view;
                }
            };
            listView.setAdapter(adapter);
        }
    }
}
