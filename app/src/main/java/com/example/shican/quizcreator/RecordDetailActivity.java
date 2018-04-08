package com.example.shican.quizcreator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static com.example.shican.quizcreator.PatientListActivity.dbBuffer;

public class RecordDetailActivity extends Toolbar {
    String  name, address, age, birthday, phoneNumber, healthCardNumber, gender, description, patientType,
            surgeries, allergies, braces, medicalBenefits, glassPurchaseDate, glassPurchaseStore, question1, question2;
    TextView nameText, addressText, ageText, birthdayText, genderText, phoneNumberText, healthCardNumberText, descriptionText,
            patientTypeText, q1Text, q2Text, question1Text, question2Text;
    long id;
    int position;

    @Override
    public boolean onPrepareOptionsMenu (Menu menu){
        MenuItem importItem = (MenuItem) menu.findItem(R.id.import_resource);
        importItem.setVisible(false);
        MenuItem statItem = (MenuItem) menu.findItem(R.id.stats);
        statItem.setVisible(false);
        menu.findItem(R.id.help).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                new AlertDialog.Builder(RecordDetailActivity.this)
                        .setTitle("Help")
                        .setMessage("Activity developped by Nan Jiang "+ "\n" +
                                "Version number: v1.0"+ "\n" +
                                "This activity is designed to register 3 types of patient: doctor, dentist and optometrist. " +
                                "You can view, add, update and delete patient's record." +
                                "You can import multiple patients' records from the Internet. ")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                return true;
            }
        });
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail);

        setTitle(R.string.title_patient_record);
        initToolbar();

        nameText =(TextView)findViewById(R.id.name_input);
        addressText =(TextView)findViewById(R.id.address_input);
        ageText =(TextView)findViewById(R.id.age_input);
        birthdayText = (TextView) findViewById(R.id.birthday_input);
        //       genderText=(TextView)findViewById(R.id.gender_input);
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
                Intent intent = new Intent(RecordDetailActivity.this, RegistrationFormActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("name", name);
                intent.putExtra("address", address);
                intent.putExtra("age", age);
                intent.putExtra("birthday", birthday);
                intent.putExtra("phoneNumber", phoneNumber);
                intent.putExtra("healthCardNumber", healthCardNumber);
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                Toast.makeText(getApplicationContext(),"Back button clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RecordDetailActivity.this, PatientListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                dbBuffer.clear();
                startActivity(intent);

                break;
        }
        return true;
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
                q1Text.setText(R.string.text_surgery);
                q2Text.setText(R.string.text_allergy);

                break;
            case "dentist":
                q1Text.setText(R.string.text_braces);
                q2Text.setText(R.string.text_medical_benefit);
                break;
            case "optometrist":
                q1Text.setText(R.string.text_glasses_date);
                q2Text.setText(R.string.text_glasses_store);
                break;
            default: break;
        }

        question1 = intent.getStringExtra("question 1");
        question1Text.setText(question1);

        question2 = intent.getStringExtra("question 2");
        question2Text.setText(question2);

    }

}
