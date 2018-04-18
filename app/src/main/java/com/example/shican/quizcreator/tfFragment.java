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

public class tfFragment extends Fragment {

    String question,answer;
    EditText questionFiled;
    RadioGroup radioGroup;

    public tfFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_tf, container, false);
        questionFiled= (EditText)view.findViewById(R.id.enterQuestion);
        radioGroup = (RadioGroup)view.findViewById(R.id.RGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.trueAns){
                    answer = "true";
                }
                else if (i == R.id.falseAns){
                    answer = "false";
                }
            }
        });

        Bundle bundle = getArguments();
        if (bundle != null){
            String correct = bundle.getString("correct");
            questionFiled.setText(bundle.getString("question"));

            if(correct.equalsIgnoreCase("true")){
                radioGroup.check(R.id.trueAns);
            }
            else if (correct.equalsIgnoreCase("false")){
                radioGroup.check(R.id.falseAns);
            }
        }
        return view;
    }

    /**
     * returns the value user entered
     * @return
     */
    public String[] getData(){
        String[] data = new String[2];
        question = questionFiled.getText().toString();
        data[0] = question;
        data[1] = answer;
        return data;
    }

}
