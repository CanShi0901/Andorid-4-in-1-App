package com.example.shican.quizcreator;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.example.shican.quizcreator.DatabaseHelper.KEY_ADDRESS;
import static com.example.shican.quizcreator.DatabaseHelper.KEY_AGE;
import static com.example.shican.quizcreator.DatabaseHelper.KEY_ALLERGY;
import static com.example.shican.quizcreator.DatabaseHelper.KEY_BIRTHDAY;
import static com.example.shican.quizcreator.DatabaseHelper.KEY_BRACE;
import static com.example.shican.quizcreator.DatabaseHelper.KEY_DESCRIPTIOIN;
import static com.example.shican.quizcreator.DatabaseHelper.KEY_GENDER;
import static com.example.shican.quizcreator.DatabaseHelper.KEY_GLASS_PURCHASE_DATE;
import static com.example.shican.quizcreator.DatabaseHelper.KEY_GLASS_PURCHASE_STORE;
import static com.example.shican.quizcreator.DatabaseHelper.KEY_HEALTH_CARD_NUMBER;
import static com.example.shican.quizcreator.DatabaseHelper.KEY_ID;
import static com.example.shican.quizcreator.DatabaseHelper.KEY_MEDICAL_BENEFIT;
import static com.example.shican.quizcreator.DatabaseHelper.KEY_NAME;
import static com.example.shican.quizcreator.DatabaseHelper.KEY_PATIENT_TYPE;
import static com.example.shican.quizcreator.DatabaseHelper.KEY_PHONE_NUMBER;
import static com.example.shican.quizcreator.DatabaseHelper.KEY_SURGERY;
import static com.example.shican.quizcreator.DatabaseHelper.TABLE_NAME;


public class PatientListActivity extends Toolbar {

    protected LayoutInflater inflater;
    protected static PatientListAdapter patientListAdapter;

    protected ListView patientListView;

    protected static ArrayList<ArrayList<String>> dbBuffer = new ArrayList<>();
    protected static SQLiteDatabase readableDb, writableDb;
    protected ContentValues cValues ;
    protected Cursor cursor;

 //   private long id;

    String question1 = null, question2 = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);
        initToolbar();

        setTitle("Patient List");
        initToolbar();
        dbBuffer.clear();
        //identify views: ListView
        patientListView = (ListView) findViewById(R.id.patient_list);
        //set up list view
        inflater = PatientListActivity.this.getLayoutInflater();
        patientListAdapter = new PatientListAdapter(PatientListActivity.this);
        patientListView.setAdapter(patientListAdapter);

        Button addBtn = (Button) findViewById(R.id.add_button);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientListActivity.this, RegistrationFormActivity.class);
                startActivity(intent);
            }
        });

        Button importBtn = (Button) findViewById(R.id.import_button);
        importBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImportPatientList importList = new ImportPatientList();
                importList.execute();
            }
        });
        setListView();

        }

        private void setListView(){
            //get patient info from database
            DatabaseHelper myDbHelper = new DatabaseHelper(PatientListActivity.this);

            readableDb = myDbHelper.getReadableDatabase();

            cursor = readableDb.query(false, TABLE_NAME,
                    new String[]{   KEY_ID,
                            KEY_NAME,
                            KEY_ADDRESS,
                            KEY_AGE,
                            KEY_BIRTHDAY,
                            KEY_GENDER,
                            KEY_PHONE_NUMBER,
                            KEY_HEALTH_CARD_NUMBER,
                            KEY_DESCRIPTIOIN,
                            KEY_PATIENT_TYPE,
                            KEY_SURGERY,
                            KEY_ALLERGY,
                            KEY_BRACE,
                            KEY_MEDICAL_BENEFIT,
                            KEY_GLASS_PURCHASE_DATE,
                            KEY_GLASS_PURCHASE_STORE},
                    null, null, null, null, null,null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String id = cursor.getString(cursor.getColumnIndex(KEY_ID));
                String name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
                String address = cursor.getString(cursor.getColumnIndex(KEY_ADDRESS));
                String age = cursor.getString(cursor.getColumnIndex(KEY_AGE));
                String birthday = cursor.getString(cursor.getColumnIndex(KEY_BIRTHDAY));
                String gender = cursor.getString(cursor.getColumnIndex(KEY_GENDER));
                String phoneNumber = cursor.getString(cursor.getColumnIndex(KEY_PHONE_NUMBER));
                String healthCardNumber = cursor.getString(cursor.getColumnIndex(KEY_HEALTH_CARD_NUMBER));
                String description = cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTIOIN));
                String patientType = cursor.getString(cursor.getColumnIndex(KEY_PATIENT_TYPE));
                String surgery = cursor.getString(cursor.getColumnIndex(KEY_SURGERY));
                String allergery = cursor.getString(cursor.getColumnIndex(KEY_ALLERGY));
                String brace = cursor.getString(cursor.getColumnIndex(KEY_BRACE));
                String medicalBenefit = cursor.getString(cursor.getColumnIndex(KEY_MEDICAL_BENEFIT));
                String glassPurchaseDate = cursor.getString(cursor.getColumnIndex(KEY_GLASS_PURCHASE_DATE));
                String glassPurchaseStore = cursor.getString(cursor.getColumnIndex(KEY_GLASS_PURCHASE_STORE));

                switch (patientType.toLowerCase()) {
                    case "doctor":
                        question1 = surgery;
                        question2 = allergery;
                        break;
                    case "dentist":
                        question1 = brace;
                        question2 = medicalBenefit;
                        break;
                    case "optometrist":
                        question1 = glassPurchaseDate;
                        question2 = glassPurchaseStore;
                        break;
                    default: break;
                }

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

                cursor.moveToNext();
        }

        patientListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PatientListActivity.this, RecordDetailActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("id", id);
                intent.putExtra("name", dbBuffer.get(position).get(1));
                intent.putExtra("address", dbBuffer.get(position).get(2));
                intent.putExtra("age", dbBuffer.get(position).get(3));
                intent.putExtra("birthday", dbBuffer.get(position).get(4));
                intent.putExtra("gender", dbBuffer.get(position).get(5));
                intent.putExtra("phoneNumber", dbBuffer.get(position).get(6));
                intent.putExtra("healthCardNumber", dbBuffer.get(position).get(7));
                intent.putExtra("description", dbBuffer.get(position).get(8));
                intent.putExtra("patientType", dbBuffer.get(position).get(9));
                intent.putExtra("question 1", question1);
                intent.putExtra("question 2", question2);

                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == 1) {
            int indexToDelete = data.getIntExtra("position", 0);
            int id = (int) data.getLongExtra("id", 0);
            dbBuffer.remove(indexToDelete);
            patientListAdapter.notifyDataSetChanged();
            readableDb.delete(TABLE_NAME, KEY_ID+" = "+id , null);

        }
    }
/*
    @Override
    protected void onResume() {
    
        super.onResume();
        patientListAdapter.notifyDataSetChanged();
    }
*/

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.project_main_actions, menu);
        return true;
    }*/
/*    @Override
    public void onStop(){
        super.onStop();
        readableDb.close();
//        writableDb.close();
    }*/

    private class PatientListAdapter extends ArrayAdapter<String> {

        //constructor
        public PatientListAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount() {

            return dbBuffer.size();
        }

        public String getItem(int position) {

            String listItem = "Name: " + dbBuffer.get(position).get(1) +
                            "  Birthday: " + dbBuffer.get(position).get(4) + "\n"+
                            dbBuffer.get(position).get(2) +"\n"+
                            "Phone Number: " + dbBuffer.get(position).get(6) +"\n"+
                            "Health Card Number: " + dbBuffer.get(position).get(7);
            return listItem;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            View result = null;
            String type = dbBuffer.get(position).get(9);
            switch (type.toLowerCase()) {
                case "doctor":
                    result = inflater.inflate(R.layout.list_item_doctor, null);
                    break;
                case "dentist":
                    result = inflater.inflate(R.layout.list_item_dentist, null);
                    break;
                case "optometrist":
                    result = inflater.inflate(R.layout.list_item_optometrist, null);
                    break;
                default:
                    throw new IllegalArgumentException();
            }

            TextView patientInfoView = (TextView) result.findViewById(R.id.patient_info_view);
           // patientInfoView.setTextSize(getResources().getDimension(R.dimen.fab_margin));
            patientInfoView.setText(getItem(position));

            return result;
        }

        public long getItemId(int position) {

//            cursor.moveToPosition(position);
//            return cursor.getLong(cursor.getColumnIndex(KEY_ID));
            return Long.parseLong(dbBuffer.get(position).get(0));
        }
    }

    private class ImportPatientList extends AsyncTask<String, Integer, ArrayList<ArrayList<String>>> {

        @Override
        protected ArrayList<ArrayList<String>> doInBackground(String... args) {

            Log.i(null, "called");
            URL url;
            String imageURL;
            ArrayList<ArrayList<String>> patientList = new ArrayList<>();
            ArrayList<String> record = new ArrayList<>();

            try {
                //connect to url
                url = new URL("http://torunski.ca/CST2335/PatientList.xml");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                //download contents
                InputStream connInputStream = conn.getInputStream();

                //parse contents
                XmlPullParser parser = Xml.newPullParser();
                parser.setInput(connInputStream, null);

                int eventType = parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {

                    String tagName = parser.getName();

                    switch (eventType) {
                        case XmlPullParser.START_TAG:
                            if (tagName.equalsIgnoreCase("patient")) {
                                record.add(parser.getAttributeValue(null, "type"));
                            }
                            break;
                        case XmlPullParser.TEXT:
                            if (parser.getText()!= null){
                                record.add(parser.getText());
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            if (tagName.equalsIgnoreCase("patient")) {
                                patientList.add(record);
                                record.clear();
                            }
                            break;
                        default:
                            break;
                    }
                    eventType = parser.next();
                }

            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.i(null, "Size   "+patientList.size());
            return patientList;//return parsed contents as array of string
        }

        /*            @Override
                    protected void onProgressUpdate(Integer ...value){
                        super.onProgressUpdate(value[0]);
        *//*            progressBar.setProgress(value[0]);
            if (value[0]==100) {
                progressBar.setVisibility(View.INVISIBLE);
            }
            Log.i(null, "onProgressUpdate() gets called");*//*
            }*/
        @Override
        protected void onPostExecute(ArrayList<ArrayList<String>> result){


            for (ArrayList<String> record: result){

//                String patientType = record.get(0);

/*                cValues.put(KEY_NAME, record.get(1));
                cValues.put(KEY_ADDRESS, record.get(2));
                String age = String.valueOf(Calendar.getInstance().get(Calendar.YEAR)-Integer.valueOf(record.get(3).substring(6)));
                cValues.put(KEY_AGE, age);
                cValues.put(KEY_BIRTHDAY, record.get(3));
                cValues.put(KEY_PHONE_NUMBER, record.get(4));
                cValues.put(KEY_HEALTH_CARD_NUMBER, record.get(5));
                cValues.put(KEY_DESCRIPTIOIN, record.get(6));
                cValues.put(KEY_PATIENT_TYPE, record.get(0));

                switch (patientType.toLowerCase()) {
                    case "doctor":
                        cValues.put(KEY_SURGERY, record.get(7));
                        cValues.put(KEY_ALLERGY, record.get(8));
                        break;
                    case "dentist":
                        cValues.put(KEY_BRACE, record.get(7));
                        cValues.put(KEY_MEDICAL_BENEFIT, record.get(8));
                        break;
                    case "optometrist":
                        cValues.put(KEY_GLASS_PURCHASE_DATE, record.get(7));
                        cValues.put(KEY_GLASS_PURCHASE_STORE, record.get(8));
                        break;
                    default:
                        break;
                }
                writableDb.insert(TABLE_NAME, "NullColumn", cValues);*/
            }
        }
    }
}