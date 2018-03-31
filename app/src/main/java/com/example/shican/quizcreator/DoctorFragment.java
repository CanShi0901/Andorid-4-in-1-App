package com.example.shican.quizcreator;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class DoctorFragment extends Fragment {

    String surgery, allergy;
    EditText surgeryText, allergyText;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            String[] data = ((RegistrationFormActivity) getActivity()).getDataToFragment();
            View view = inflater.inflate(R.layout.activity_doctor_fragment_detail,
                    container, false);
            surgeryText = (EditText) view.findViewById(R.id.surgeries);
            allergyText = (EditText) view.findViewById(R.id.allergies);
            if (data != null) {
                surgeryText.setText(data[0]);
                allergyText.setText(data[1]);
            }
            return view;
        }

        public String[] getData (){
            surgery = surgeryText.getText().toString();
            allergy = allergyText.getText().toString();
            return (new String[]{surgery, allergy});
        }

        public void setData(String s, String a){
  //          surgeryText.setText(s);
    //        allergyText.setText(a);
        }
}
