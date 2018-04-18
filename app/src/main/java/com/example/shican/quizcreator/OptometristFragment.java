package com.example.shican.quizcreator;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
/**
 *this class is a fragment for optometrist type questions
 * @author Nan Jiang
 */
public class OptometristFragment extends Fragment {
    String glassPurchseDate, glassPurchseStore;
    EditText glassPurchaseDateText, glassPurchaseStoreText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String[] data = ((RegistrationFormActivity) getActivity()).getDataToFragment();
        View view = inflater.inflate(R.layout.activity_optometrist_fragment_detail,
                container, false);
        glassPurchaseDateText = (EditText) view.findViewById(R.id.glasses_purchase_date);
        glassPurchaseStoreText = (EditText) view.findViewById(R.id.glasses_purchase_store);
        if (data != null && data[2].equalsIgnoreCase("optometrist")) {
            glassPurchaseDateText.setText(data[0]);
            glassPurchaseStoreText.setText(data[1]);
        } else {
            glassPurchaseDateText.setText("");
            glassPurchaseStoreText.setText("");
        }

        return view;
    }

    public String[] getData (){
        glassPurchseDate = glassPurchaseDateText.getText().toString();
        glassPurchseStore = glassPurchaseStoreText.getText().toString();
        return (new String[]{glassPurchseDate, glassPurchseStore});
    }
}
