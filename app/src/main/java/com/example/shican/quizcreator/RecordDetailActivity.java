package com.example.shican.quizcreator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static com.example.jian0080.finalproject.PatientListActivity.dbBuffer;

public class RecordDetailActivity extends Activity {
    String  name, address, age, birthday, phoneNumber, healthCardNumber, gender, description, patientType,
            surgeries, allergies, braces, medicalBenefits, glassPurchaseDate, glassPurchaseStore, question1, question2;
    TextView nameText, addressText, ageText, birthdayText, genderText, phoneNumberText, healthCardNumberText, descriptionText,
            patientTypeText, q1Text, q2Text, question1Text, question2Text;
    long id;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail);

        setTitle("Patient Record");

        nameText =(TextView)findViewById(R.id.name_input);
        addressText =(TextView)findViewById(R.id.address_input);
        ageText =(TextView)findViewById(R.id.age_input);
        birthdayText = (TextView) findViewById(R.id.birthday_input);
        genderText=(TextView)findViewById(R.id.gender_input);
        phoneNumberText = (TextView)findViewById(R.id.phone_number_input);
        healthCardNumberText = (TextView)findViewById(R.id.health_card_number_input);
        descriptionText = (TextView)findViewById(R.id.description_input);
        patientTypeText = (TextView)findViewById(R.id.patient_type_input);
        q1Text = (TextView) findViewById(R.id.question_1);
        q2Text = (TextView) findViewById(R.id.question_2);
        question1Text = (TextView) findViewById(R.id.question_1_input);
        question2Text = (TextView) findViewById(R.id.question_2_input);

        displayRecord();
        Button okBtn = (Button) findViewById(R.id.ok);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecordDetailActivity.this, PatientListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                dbBuffer.clear();
                startActivity(intent);
            }
        });

        Button editBtn = (Button) findViewById(R.id.edit);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecordDetailActivity.this, com.example.jian0080.finalproject.RegistrationFormActivity.class);
                intent.putExtra("id", id);
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

                startActivity(intent);
            }
        });

        Button deleteBtn = (Button) findViewById(R.id.delete);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("position", position);
                intent.putExtra("id", id);
                Log.i(null, "ID:   "+id+"POSITION:   "+position);
                setResult(1, intent);
                finish();
            }
        });

    }

    private void displayRecord () {
        Intent intent=getIntent();

        id = intent.getLongExtra("id", 0);
        position = intent.getIntExtra("position", 0);

        Log.i(null, "ID: "+id+"POSITION: "+position);

        name = intent.getStringExtra("name");
        nameText.setText(name);

        address = intent.getStringExtra("address");
        addressText.setText(address);

        age = intent.getStringExtra("age");
        ageText.setText(age);

        birthday = intent.getStringExtra("birthday");
        birthdayText.setText(birthday);

        gender = intent.getStringExtra("gender");
        genderText.setText(gender);

        phoneNumber = intent.getStringExtra("phoneNumber");
        phoneNumberText.setText(phoneNumber);

        healthCardNumber = intent.getStringExtra("healthCardNumber");
        healthCardNumberText.setText(healthCardNumber);

        description = intent.getStringExtra("description");
        descriptionText.setText(description);

        patientType = intent.getStringExtra("patientType");
        patientTypeText.setText(patientType);

        switch (intent.getStringExtra("patientType").toLowerCase()) {
            case "doctor":
                q1Text.setText("Surgeries:");
                q2Text.setText("Allergies:");

                break;
            case "dentist":
                q1Text.setText("Braces:");
                q2Text.setText("Allergies:");
                break;
            case "optometrist":
                q1Text.setText("Glasses purchase date:");
                q2Text.setText("Glasses purchase date:");
                break;
            default: break;
        }

        question1 = intent.getStringExtra("question 1");
        question1Text.setText(question1);

        question2 = intent.getStringExtra("question 2");
        question2Text.setText(question2);

    }

}
