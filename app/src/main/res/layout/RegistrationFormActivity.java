package com.example.jian0080.finalproject;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.example.jian0080.finalproject.DatabaseHelper.KEY_ADDRESS;
import static com.example.jian0080.finalproject.DatabaseHelper.KEY_AGE;
import static com.example.jian0080.finalproject.DatabaseHelper.KEY_ALLERGY;
import static com.example.jian0080.finalproject.DatabaseHelper.KEY_BIRTHDAY;
import static com.example.jian0080.finalproject.DatabaseHelper.KEY_BRACE;
import static com.example.jian0080.finalproject.DatabaseHelper.KEY_DESCRIPTIOIN;
import static com.example.jian0080.finalproject.DatabaseHelper.KEY_GENDER;
import static com.example.jian0080.finalproject.DatabaseHelper.KEY_GLASS_PURCHASE_DATE;
import static com.example.jian0080.finalproject.DatabaseHelper.KEY_GLASS_PURCHASE_STORE;
import static com.example.jian0080.finalproject.DatabaseHelper.KEY_HEALTH_CARD_NUMBER;
import static com.example.jian0080.finalproject.DatabaseHelper.KEY_ID;
import static com.example.jian0080.finalproject.DatabaseHelper.KEY_MEDICAL_BENEFIT;
import static com.example.jian0080.finalproject.DatabaseHelper.KEY_NAME;
import static com.example.jian0080.finalproject.DatabaseHelper.KEY_PATIENT_TYPE;
import static com.example.jian0080.finalproject.DatabaseHelper.KEY_PHONE_NUMBER;
import static com.example.jian0080.finalproject.DatabaseHelper.KEY_SURGERY;
import static com.example.jian0080.finalproject.DatabaseHelper.TABLE_NAME;
import static com.example.jian0080.finalproject.PatientListActivity.dbBuffer;

public class RegistrationFormActivity extends Activity implements AdapterView.OnItemSelectedListener {

    String  name, address, age, birthday, phoneNumber, healthCardNumber, gender, description, patientType,
            surgeries, allergies, braces, medicalBenefits, glassPurchaseDate, glassPurchaseStore, question1, question2;
    long id;
    int position;
    boolean isUpdate = false;
    EditText nameText, addressText, ageText, birthdayText, phoneNumberText, healthCardNumberText, descriptionText;
    RadioGroup radioGender;
    RadioButton maleRadioBtn, femaleRadioBtn;
    Spinner patientTypeText;
    String[] dataToFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_form);

        setTitle("Patient Registration Form");

        Button saveBtn = (Button) findViewById(R.id.save);
        Button cancelBtn = (Button) findViewById(R.id.cancel);
        nameText = (EditText) findViewById(R.id.name);
        addressText = (EditText) findViewById(R.id.address);
        ageText = (EditText) findViewById(R.id.age);
        birthdayText = (EditText) findViewById(R.id.birthday);
        radioGender = (RadioGroup) findViewById(R.id.radioSex);
        maleRadioBtn = (RadioButton) findViewById(R.id.male);
        femaleRadioBtn = (RadioButton) findViewById(R.id.female);
        phoneNumberText = (EditText) findViewById(R.id.phone_number);
        healthCardNumberText = (EditText) findViewById(R.id.health_card_number);
        descriptionText = (EditText) findViewById(R.id.description);
        patientTypeText = (Spinner) findViewById(R.id.patient_type);

        // Spinner click listener
        patientTypeText.setOnItemSelectedListener(this);
        // Spinner Drop down elements
        List<String> type = new ArrayList<String>();
        type.add("Select Patient Type");
        type.add("Doctor");
        type.add("Dentist");
        type.add("Optometrist");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, type);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        patientTypeText.setAdapter(dataAdapter);

        onEditRecord();

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = nameText.getText().toString();
                address = addressText.getText().toString();
                age = ageText.getText().toString();
                birthday = birthdayText.getText().toString();

                int selectedId =radioGender.getCheckedRadioButtonId();
                RadioButton radioSexButton = (RadioButton)findViewById(selectedId);
                gender = radioSexButton.getText().toString();

                phoneNumber = phoneNumberText.getText().toString();
                healthCardNumber = healthCardNumberText.getText().toString();

                description = descriptionText.getText().toString();
                patientType = patientTypeText.getSelectedItem().toString();

                switch (patientType.toLowerCase()) {
                    case "doctor":
                        surgeries = ((DoctorFragment) getFragmentManager().findFragmentById(R.id.FrameLayout)).getData()[0];
                        allergies = ((DoctorFragment) getFragmentManager().findFragmentById(R.id.FrameLayout)).getData()[1];
                        question1 = surgeries;
                        question2 = allergies;
                        break;
                    case "dentist":
                        braces = ((DentistFragment) getFragmentManager().findFragmentById(R.id.FrameLayout)).getData()[0];
                        medicalBenefits = ((DentistFragment) getFragmentManager().findFragmentById(R.id.FrameLayout)).getData()[1];
                        question1 = braces;
                        question2 = medicalBenefits;
                        break;
                    case "optometrist":
                        glassPurchaseDate = ((OptometristFragment) getFragmentManager().findFragmentById(R.id.FrameLayout)).getData()[0];
                        glassPurchaseStore = ((OptometristFragment) getFragmentManager().findFragmentById(R.id.FrameLayout)).getData()[1];
                        question1 = glassPurchaseDate;
                        question2 = glassPurchaseStore;
                        Log.i(null, "Glasses "+question1+" "+question2);
                        break;
                    default: break;
                }

                //data validation
                if (name.isEmpty()){
                    nameText.setError("Please enter your name");
                    nameText.requestFocus();
                }
                else if (address.isEmpty())
                {
                    addressText.setError("Please enter your address");
                    addressText.requestFocus();
                }
                else if (age.isEmpty()){
                    ageText.setError("Please enter your");
                    ageText.requestFocus();
                } else if (birthday.isEmpty()){
                    birthdayText.setError("Please enter your birthday");
                    birthdayText.requestFocus();
                } else if (phoneNumber.isEmpty()) {
                    phoneNumberText.setError("Please enter your phone number");
                    phoneNumberText.requestFocus();
                } else if (healthCardNumber.isEmpty()) {
                    healthCardNumberText.setError("Please enter your health card number");
                    healthCardNumberText.requestFocus();
                } else if (description.isEmpty()){
                    descriptionText.setError("Please enter your description");
                    descriptionText.requestFocus();
                } else if (patientType.equalsIgnoreCase("Select Patient Type")) {
                    Toast.makeText(com.example.jian0080.finalproject.RegistrationFormActivity.this, "Please select patient type", Toast.LENGTH_LONG).show();
                    birthdayText.requestFocus();
                } else {
                    Intent intent = new Intent(com.example.jian0080.finalproject.RegistrationFormActivity.this, RecordDetailActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("address", address);
                    intent.putExtra("age", age);
                    intent.putExtra("birthday", birthday);
                    intent.putExtra("phoneNumber", phoneNumber);
                    intent.putExtra("healthCardNumber", healthCardNumber);
                    intent.putExtra("gender", gender);
                    intent.putExtra("description", description);
                    intent.putExtra("patientType", patientType);
                    intent.putExtra("question 1", question1);
                    intent.putExtra("question 2", question2);

                    DatabaseHelper myDbHelper = new DatabaseHelper(com.example.jian0080.finalproject.RegistrationFormActivity.this);

                    SQLiteDatabase writableDb = myDbHelper.getWritableDatabase();
                    SQLiteDatabase readableDb = myDbHelper.getReadableDatabase();

                    if (isUpdate) {
                        readableDb.delete(TABLE_NAME, KEY_ID + "=" + id, null);
                        dbBuffer.remove(position);

                    }

                    ContentValues cValues = new ContentValues();

                    cValues.put(KEY_NAME, name);
                    cValues.put(KEY_ADDRESS, address);
                    cValues.put(KEY_AGE, age);
                    cValues.put(KEY_BIRTHDAY, birthday);
                    cValues.put(KEY_GENDER, gender);
                    cValues.put(KEY_PHONE_NUMBER, phoneNumber);
                    cValues.put(KEY_HEALTH_CARD_NUMBER, healthCardNumber);
                    cValues.put(KEY_DESCRIPTIOIN, description);
                    cValues.put(KEY_PATIENT_TYPE, patientType);

                    switch (patientType.toLowerCase()) {
                        case "doctor":
                            cValues.put(KEY_SURGERY, question1);
                            cValues.put(KEY_ALLERGY, question2);
                            break;
                        case "dentist":
                            cValues.put(KEY_BRACE, question1);
                            cValues.put(KEY_MEDICAL_BENEFIT, question2);
                            break;
                        case "optometrist":
                            cValues.put(KEY_GLASS_PURCHASE_DATE, question1);
                            cValues.put(KEY_GLASS_PURCHASE_STORE, question2);
                            break;
                        default:
                            break;
                    }

                    String id = String.valueOf(writableDb.insert(TABLE_NAME, "NullColumn", cValues));

                    ArrayList<String> record = new ArrayList<>();

                    record.add(id);
                    record.add(name);
                    record.add(address);
                    record.add(age);
                    record.add(birthday);
                    record.add(gender);
                    record.add(phoneNumber);
                    record.add(healthCardNumber);
                    record.add(description);
                    record.add(patientType);
                    record.add(question1);
                    record.add(question2);

                    dbBuffer.add(record);

                    startActivity(intent);
                }
            }
        });


    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String item = parent.getItemAtPosition(position).toString();

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        switch (item.toLowerCase()) {
            case "doctor":
                DoctorFragment doctorFrag = new DoctorFragment();
                ft.replace(R.id.FrameLayout, doctorFrag);
                ft.commit();
                Toast.makeText(parent.getContext(), "Patient Type Selected: " + item, Toast.LENGTH_LONG).show();
                break;
            case "dentist":
                ft.replace(R.id.FrameLayout, new DentistFragment());
                ft.commit();
                Toast.makeText(parent.getContext(), "Patient Type Selected: " + item, Toast.LENGTH_LONG).show();
                break;
            case "optometrist":
                ft.replace(R.id.FrameLayout, new OptometristFragment());
                ft.commit();
                Toast.makeText(parent.getContext(), "Patient Type Selected: " + item, Toast.LENGTH_LONG).show();
                break;
            default: break;
        }

    }

    private void onEditRecord (){
        Intent intent = getIntent();
        id = intent.getLongExtra("id",0);
        position = intent.getIntExtra("position", 0);
        if (intent.getExtras()!= null){
            isUpdate = true;
            nameText.setText(intent.getStringExtra("name"));
            addressText.setText(intent.getStringExtra("address"));
            ageText.setText(intent.getStringExtra("age"));
            birthdayText.setText(intent.getStringExtra("birthday"));
            if (intent.getStringExtra("gender").equalsIgnoreCase("male"))     {
                maleRadioBtn.setChecked(true);
            } else {
                femaleRadioBtn.setChecked(true);
            }
            phoneNumberText.setText(intent.getStringExtra("phoneNumber"));
            healthCardNumberText.setText(intent.getStringExtra("healthCardNumber"));
            descriptionText.setText(intent.getStringExtra("description"));

            switch (intent.getStringExtra("patientType").toLowerCase()){
                case "doctor":  patientTypeText.setSelection(1);

                    passData(intent.getStringExtra("question 1"), intent.getStringExtra("question 2"));
                    DoctorFragment doctorFrag = new DoctorFragment();
                    getFragmentManager().beginTransaction().replace(R.id.FrameLayout, doctorFrag).commit();
  //                  doctorFrag.setData(intent.getStringExtra("question 1"), intent.getStringExtra("question 2"));
                    break;
                case "dentist": patientTypeText.setSelection(2);
                    passData(intent.getStringExtra("question 1"), intent.getStringExtra("question 2"));
                    DoctorFragment dentistFrag = new DoctorFragment();
                    getFragmentManager().beginTransaction().replace(R.id.FrameLayout, dentistFrag).commit();
                    break;
                case "optometrist": patientTypeText.setSelection(3);
                    passData(intent.getStringExtra("question 1"), intent.getStringExtra("question 2"));
                    DoctorFragment optometristFrag = new DoctorFragment();
                    getFragmentManager().beginTransaction().replace(R.id.FrameLayout, optometristFrag).commit();
                    break;
                default:
                    break;
            }
        }
    }

    public void passData (String s, String a){
        dataToFragment = new String[]{s, a};
    }
    public String[] getDataToFragment(){
        return dataToFragment;
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}
