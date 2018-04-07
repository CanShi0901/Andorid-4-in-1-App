package com.example.shican.quizcreator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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

    private LayoutInflater inflater;
    protected static PatientListAdapter patientListAdapter;

    private ListView patientListView;

    protected static ArrayList<ArrayList<String>> dbBuffer = new ArrayList<>();
    protected static SQLiteDatabase readableDb, writableDb;
    protected ContentValues cValues;
    protected Cursor cursor;
    private ProgressBar progressBar;

    @Override
    public boolean onPrepareOptionsMenu (Menu menu){
        MenuItem importItem = (MenuItem) menu.findItem(R.id.import_resource);
        importItem.setVisible(false);
        MenuItem statItem = (MenuItem) menu.findItem(R.id.stats);
        statItem.setVisible(false);
        menu.findItem(R.id.help).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
         @Override
         public boolean onMenuItemClick(MenuItem item) {
             new AlertDialog.Builder(PatientListActivity.this)
                     .setTitle(R.string.menu_help)
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);
        initToolbar();


        setTitle(R.string.title_patient_list);
        initToolbar();
        dbBuffer.clear();
        progressBar = (ProgressBar) findViewById(R.id.indeterminateBar);
 //       progressBar.setVisibility(View.VISIBLE);
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
                Log.i(null, "import click");
                ImportPatientList importList = new ImportPatientList();
                importList.execute();
                progressBar.setVisibility(View.VISIBLE);

            }
        });

        Button statBtn = (Button) findViewById(R.id.statistic_button);
        statBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (doStatistics()!=null) {
                    new AlertDialog.Builder(PatientListActivity.this)
                            .setTitle("STATISTICS")
                            .setMessage("Minimum age:  " + doStatistics().get(0) + "\n" +
                                    "Maximum age: " + doStatistics().get(1) + "\n" +
                                    "Average age:    " + doStatistics().get(2))
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                } else {
                    new AlertDialog.Builder(PatientListActivity.this)
                            .setTitle("EMPTY PATIENT LIST!")
                            .setMessage("Statistics are not available.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                }
            }

        });
        setListView();
    }

    private List<Integer> doStatistics(){

        ArrayList<Integer> ageList = new ArrayList<>();
        Integer sum = 0;
        if (dbBuffer.size()!=0){
        for (ArrayList<String> record: dbBuffer){
            ageList.add(Integer.valueOf(record.get(3)));
            sum +=Integer.valueOf(record.get(3));
        }
            return Arrays.asList(Collections.min(ageList), Collections.max(ageList), sum/ageList.size());
        } else {

            return null;
        }

    }

    private void setListView() {

        DatabaseHelper myDbHelper = new DatabaseHelper(PatientListActivity.this);
        readableDb = myDbHelper.getReadableDatabase();
        writableDb = myDbHelper.getWritableDatabase();
        dbBuffer.clear();

        cursor = readableDb.query(false, TABLE_NAME,
                new String[]{KEY_ID,
                        KEY_NAME,
                        KEY_ADDRESS,
                        KEY_AGE,
                        KEY_BIRTHDAY,
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
                null, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String id = cursor.getString(cursor.getColumnIndex(KEY_ID));
            String name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
            String address = cursor.getString(cursor.getColumnIndex(KEY_ADDRESS));
            String age = cursor.getString(cursor.getColumnIndex(KEY_AGE));
            String birthday = cursor.getString(cursor.getColumnIndex(KEY_BIRTHDAY));
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

            ArrayList<String> record = new ArrayList<>();
            record.add(id);
            record.add(name);
            record.add(address);
            record.add(age);
            record.add(birthday);
            record.add(phoneNumber);
            record.add(healthCardNumber);
            record.add(description);
            record.add(patientType);

            switch (patientType.toLowerCase()) {
                case "doctor":
                    record.add(surgery);
                    record.add(allergery);
                    break;
                case "dentist":
                    record.add(brace);
                    record.add(medicalBenefit);
                    break;
                case "optometrist":
                    record.add(glassPurchaseDate);
                    record.add(glassPurchaseStore);
                    break;
                default:
                    break;
            }

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
                intent.putExtra("phoneNumber", dbBuffer.get(position).get(5));
                intent.putExtra("healthCardNumber", dbBuffer.get(position).get(6));
                intent.putExtra("description", dbBuffer.get(position).get(7));
                intent.putExtra("patientType", dbBuffer.get(position).get(8));
                intent.putExtra("question 1", dbBuffer.get(position).get(9));
                intent.putExtra("question 2", dbBuffer.get(position).get(10));

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
            readableDb.delete(TABLE_NAME, KEY_ID + " = " + id, null);

        }
    }

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.project_main_actions, menu);
        return true;
    }*/

    private class PatientListAdapter extends ArrayAdapter<String> {

        //constructor
        protected PatientListAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount() {

            return dbBuffer.size();
        }

        public String getItem(int position) {

            String listItem = "Name: " + dbBuffer.get(position).get(1) +
                    "    Birthday: " + dbBuffer.get(position).get(4) + "\n" +
                    "Address: "+dbBuffer.get(position).get(2) + "\n" +
                    "Phone Number: " + dbBuffer.get(position).get(5) + "\n" +
                    "Health Card Number: " + dbBuffer.get(position).get(6);
            return listItem;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            View result = null;
            String type = dbBuffer.get(position).get(8);
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
            patientInfoView.setText(getItem(position));

            return result;
        }

        public long getItemId(int position) {

            return Long.parseLong(dbBuffer.get(position).get(0));
        }
    }

    private class ImportPatientList extends AsyncTask<String, Integer, ArrayList<ArrayList<String>>> {
        ArrayList<ArrayList<String>> patientList = new ArrayList<>();
        ArrayList<String> record= new ArrayList<>();
        @Override
        protected ArrayList<ArrayList<String>> doInBackground(String... args) {

            URL url;


            try {
                //connect to url
                url = new URL("http://torunski.ca/CST2335/PatientList.xml");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                //download contents
                InputStream connInputStream = conn.getInputStream();
                //parse contents
                XmlPullParser parser = Xml.newPullParser();
                parser.setInput(connInputStream, null);

                int progress =0;
                int eventType = parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    String tagName = parser.getName();
                    switch (eventType) {
                        case XmlPullParser.START_TAG:
                            if (tagName.equalsIgnoreCase("patient")) {
                                record.add(parser.getAttributeValue(null, "type"));
                                Log.i(null, "Patient: test" + record.get(0));
                            }
                            if (tagName.equalsIgnoreCase("name")) {
                                parser.next();
                                record.add(parser.getText());
                                Log.i(null, "Name: " + parser.getText());
                            }
                            if (tagName.equalsIgnoreCase("address")) {
                                parser.next();
                                record.add(parser.getText());
                                Log.i(null, "Address: " + parser.getText());
                            }
                            if (tagName.equalsIgnoreCase("birthday")) {
                                parser.next();
                                record.add(parser.getText());
                                Log.i(null, "Birthday: " + parser.getText());
                            }
                            if (tagName.equalsIgnoreCase("phonenumber")) {
                                parser.next();
                                record.add(parser.getText());
                                Log.i(null, "Phone number: " + parser.getText());
                            }
                            if (tagName.equalsIgnoreCase("healthcard")) {
                                parser.next();
                                record.add(parser.getText());
                                Log.i(null, "Health Card: " + parser.getText());
                            }
                            if (tagName.equalsIgnoreCase("description")) {
                                parser.next();
                                record.add(parser.getText());
                                Log.i(null, "Description: " + parser.getText());
                            }
                            if (tagName.equalsIgnoreCase("previoussurgery")) {
                                parser.next();
                                record.add(parser.getText());
                                Log.i(null, "Previous Surgery: " + parser.getText());
                            }
                            if (tagName.equalsIgnoreCase("allergies")) {
                                parser.next();
                                record.add(parser.getText());
                                Log.i(null, "Allegies: " + parser.getText());
                            }
                            if (tagName.equalsIgnoreCase("glassesbought")) {
                                parser.next();
                                record.add(parser.getText());
                                Log.i(null, "Glasses bought: " + parser.getText());
                            }
                            if (tagName.equalsIgnoreCase("glassesstore")) {
                                parser.next();
                                record.add(parser.getText());
                                Log.i(null, "Glasses Store: " + parser.getText());
                            }
                            if (tagName.equalsIgnoreCase("benefits")) {
                                parser.next();
                                record.add(parser.getText());
                                Log.i(null, "Benefits: " + parser.getText());
                            }
                            if (tagName.equalsIgnoreCase("hadbraces")) {
                                parser.next();
                                record.add(parser.getText());
                                Log.i(null, "Had braces: " + parser.getText());
                            }
                            break;
                        case XmlPullParser.TEXT:
                            break;
                        case XmlPullParser.END_TAG:
                            if (tagName.equalsIgnoreCase("patient")) {
                                Log.i(null,record.toString());
                                patientList.add(record);
                                record = new ArrayList<>();
                                progress+=35;
                                publishProgress(progress);
                                try {
                                    // Sleep for 200 milliseconds.
                                    Thread.sleep(800);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            break;
                        default:
                            break;
                    }
                    eventType = parser.next();
                }
                connInputStream.close();

            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return patientList;
        }
        @Override
        protected void onProgressUpdate(Integer ...value){
            super.onProgressUpdate(value[0]);
            progressBar.setProgress(value[0]);
            if (value[0]>100) {
                progressBar.setVisibility(View.INVISIBLE);
                Snackbar.make(findViewById(R.id.patient_list_layout), R.string.snackbar_message,
                        Snackbar.LENGTH_SHORT)
                        .show();
            }

        }
        @Override
        protected void onPostExecute(ArrayList<ArrayList<String>> result) {
            ContentValues cValues = new ContentValues();

            for (int i=0; i<result.size(); i++){

                String name = result.get(i).get(1);
                cValues.put(KEY_NAME, name);
                cValues.put(KEY_ADDRESS, result.get(i).get(2));
                String age = String.valueOf(Calendar.getInstance()
                        .get(Calendar.YEAR)-Integer.valueOf(result.get(i).get(3).substring(result.get(i).get(3).length()-4)));
                cValues.put(KEY_AGE, age);
                cValues.put(KEY_BIRTHDAY, result.get(i).get(3));
                cValues.put(KEY_PHONE_NUMBER, result.get(i).get(4));
                cValues.put(KEY_HEALTH_CARD_NUMBER, result.get(i).get(5));
                cValues.put(KEY_DESCRIPTIOIN, result.get(i).get(6));
                cValues.put(KEY_PATIENT_TYPE, result.get(i).get(0));

                String type = result.get(i).get(0).toLowerCase();

                    if (type.equalsIgnoreCase("doctor")) {
                        cValues.put(KEY_SURGERY, result.get(i).get(7));
                        cValues.put(KEY_ALLERGY, result.get(i).get(8));
                        Log.i(null, "Doctor type!!!");
                    }
                    if (type.equalsIgnoreCase("dentist")) {
                        cValues.put(KEY_BRACE, result.get(i).get(7));
                        cValues.put(KEY_MEDICAL_BENEFIT, result.get(i).get(8));
                        Log.i(null, "Dentist type!!!");
                    }
                    if (type.equalsIgnoreCase("optometrist")) {
                        cValues.put(KEY_GLASS_PURCHASE_DATE, result.get(i).get(7));
                        cValues.put(KEY_GLASS_PURCHASE_STORE, result.get(i).get(8));
                        Log.i(null, "Optometrist type!!!");
                    }
                writableDb.insert(TABLE_NAME, "NullColumn", cValues);
            }
            setListView();
            patientListAdapter.notifyDataSetChanged();
        }
    }
}