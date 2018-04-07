package com.example.shican.quizcreator;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;


public class mcFragment extends Fragment {

    String question,answer1,answer2,answer3,answer4;
    EditText questionFiled;
    EditText answerField1;
    EditText answerField2;
    EditText answerField3;
    EditText answerField4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_mc, container, false);
        questionFiled= (EditText)view.findViewById(R.id.enterQuestion);
        answerField1 = (EditText)view.findViewById(R.id.enterAnswer1);
        answerField2 = (EditText)view.findViewById(R.id.enterAnswer2);
        answerField3 = (EditText)view.findViewById(R.id.enterAnswer3);
        answerField4 = (EditText)view.findViewById(R.id.enterAnswer4);
        return view;
    }

    public ArrayList<String> getData(){
        ArrayList<String> data = new ArrayList<>();
        question = questionFiled.getText().toString();
        answer1 = answerField1.getText().toString();
        answer2 = answerField2.getText().toString();
        answer3 = answerField3.getText().toString();
        answer4 = answerField4.getText().toString();
        data.add(question);
        data.add(answer1);
        data.add(answer2);
        data.add(answer3);
        data.add(answer4);
        return data;
    }

}
