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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class OCFragmentSearch extends Fragment {
    TextView input;
    Button search;
    ListView listView;
    ArrayList<String> list= new ArrayList<String>();;

    public OCFragmentSearch(){}

    OCFragmentSearchListener searchListener;

    public interface OCFragmentSearchListener{
        public void inputSearch(String enterStop);
    }

    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            searchListener = (OCFragmentSearchListener) activity;
        }catch(ClassCastException e){
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_oc_search, container, false);
        search = (Button) view.findViewById(R.id.search);
        input = (TextView) view.findViewById(R.id.input);
        listView = (ListView) view.findViewById(R.id.list);

        search.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        buttonClicked(v);
                    }
                }
        );
        return view;
    }

    public void buttonClicked(View v){
        searchListener.inputSearch(input.getText().toString());

        if(list.contains(input.getText().toString())){}
        else {
            list.add(input.getText().toString());

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list) {
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView textView = (TextView) view.findViewById(android.R.id.text1);
                    textView.setTextColor(Color.BLACK);
                    return view;
                }
            };
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapter, View v, int pos, long a) {
                    String enterStop = (String) adapter.getItemAtPosition(pos);
                    Intent intent = new Intent(getActivity(), OCResultRoute.class);
                    intent.putExtra("enterStop", enterStop);
                    startActivity(intent);
                }
            });
        }
    }
}