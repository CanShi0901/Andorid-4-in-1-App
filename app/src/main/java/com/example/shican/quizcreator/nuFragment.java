package com.example.shican.quizcreator;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class nuFragment extends Fragment {

    EditText questionFiled, answerField;
    String question, answer;

    public nuFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_nu, container, false);
        questionFiled= (EditText)view.findViewById(R.id.enterQuestion);
        answerField = (EditText)view.findViewById(R.id.enterAnswer);
        return view;
    }

    public String[] getData(){
        String[] data = new String[2];
        question = questionFiled.getText().toString();
        answer = answerField.getText().toString();
        data[0] = question;
        data[1] = answer;
        return data;
    }
}
