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

    EditText questionFiled, answerField, decimals;
    String question, answer, decimal;

    public nuFragment() {
    }

    /**
     * gives edittext fields for user to enter questions and answers
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_nu, container, false);
        questionFiled= (EditText)view.findViewById(R.id.enterQuestion);
        answerField = (EditText)view.findViewById(R.id.enterAnswer);
        decimals = (EditText)view.findViewById(R.id.decimals);

        Bundle bundle = getArguments();
        if (bundle != null){
            String correct = bundle.getString("correct");
            questionFiled.setText(bundle.getString("question"));
            answerField.setText(correct);
            String[] splitter = correct.split("\\.");
            if(splitter.length>1) {
                int decimal = splitter[1].length();
                decimals.setText(Integer.toString(decimal));
            }
            else{
                decimals.setText(Integer.toString(0));
            }
        }
        return view;
    }

    /**
     * returns the values user entered
     * @return
     */
    public String[] getData(){
        String[] data = new String[3];
        question = questionFiled.getText().toString();
        answer = answerField.getText().toString();
        decimal = decimals.getText().toString();
        data[0] = question;
        data[1] = answer;
        data[2] = decimal;
        return data;
    }
}
