package com.example.shican.quizcreator;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
/*
@author yuxin zhang
saved stop fragment
shows saved stop database in listview
click on stop intent stop information activity
 */

public class OCFragmentSavedStop extends Fragment {
    ListView listView;

    public OCFragmentSavedStop(){}

    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_oc_saved_stop, container, false);
        //layout variables
        listView = (ListView) view.findViewById(R.id.list);

        //saved stop listview
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, OCMain.saveArrayList) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.BLACK);
                return view;
            }
        };
        listView.setAdapter(adapter);

        //switch to OCResultRoute activity
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapter, View v, int pos, long a) {
                String enterStop = (String) adapter.getItemAtPosition(pos);
                Intent intent = new Intent(getActivity(), OCResultRoute.class);
                intent.putExtra("enterStop", enterStop);
                startActivity(intent);
            }
        });
        return view;
    }
}