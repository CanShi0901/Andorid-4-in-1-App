package com.example.shican.quizcreator;

import android.annotation.SuppressLint;
import android.support.v7.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.example.shican.quizcreator.QuizDatabaseHelper.TABLE_NAME;
import static org.xmlpull.v1.XmlPullParser.END_TAG;
import static org.xmlpull.v1.XmlPullParser.START_TAG;

public class CreateQuiz extends Toolbar {
    Button save, mc, tf, nu;
    FrameLayout container;
    AlertDialog.Builder builder;
    mcFragment mcF;
    tfFragment tfF;
    nuFragment nuF;
    FragmentManager fm;
    QuizDatabaseHelper helper;
    FragmentTransaction ft;
    String selectedType;
    String[] info;
    ProgressBar importProgress;
    protected static SQLiteDatabase db;
    ImportQuiz importQuiz;
    String type,question, correct, ans1,ans2,ans3,ans4;
    int position, ROW_UPDATED;
    long quizID;
    boolean isEdit;

    /**
     * sets the general layout of the activity, depending on the type of question, loads different fragments,
     * depending if the user is editing an existing quiz, it sets the onClickListener of save button differently
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quiz);
        initToolbar();
        helper = new QuizDatabaseHelper(this);
        isEdit=false;
        ROW_UPDATED=99;
        db = helper.getWritableDatabase();
        save = findViewById(R.id.save);
        mc = findViewById(R.id.mc);
        tf = findViewById(R.id.tf);
        nu = findViewById(R.id.nu);
        container = findViewById(R.id.container);
        importProgress = findViewById(R.id.importProgress);
        mcF = new mcFragment();
        tfF = new tfFragment();
        nuF = new nuFragment();
        helper = new QuizDatabaseHelper(this);
        fm = getFragmentManager();
        ft = fm.beginTransaction();
        builder = new AlertDialog.Builder(this);
        onEdit();
        mc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ft.isEmpty()) {
                    ft = fm.beginTransaction();
                }
                selectedType = "mc";
                ft.replace(R.id.container, mcF);
                ft.addToBackStack("");
                ft.commit();
            }
        });

        tf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ft.isEmpty()) {
                    ft = fm.beginTransaction();
                }
                selectedType = "tf";
                ft.replace(R.id.container, tfF);
                ft.addToBackStack("");
                ft.commit();
            }
        });

        nu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ft.isEmpty()) {
                    ft = fm.beginTransaction();
                }
                selectedType = "nu";
                ft.replace(R.id.container, nuF);
                ft.addToBackStack("");
                ft.commit();
            }
        });

        if(isEdit){
            Bundle infoPassed = getIntent().getExtras();
            quizID = infoPassed.getLong("ID");
            position=infoPassed.getInt("position");
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(CreateQuiz.this, QuizMain.class);
                    intent.putExtra("ID", quizID);
                    intent.putExtra("position", position);
                    if (type.equalsIgnoreCase("mc")) {
                        info = mcF.getData();
                        intent.putExtra("type", "mc");
                        intent.putExtra("question", info[0]);
                        intent.putExtra("ans1", info[1]);
                        intent.putExtra("ans2", info[2]);
                        intent.putExtra("ans3", info[3]);
                        intent.putExtra("ans4", info[4]);
                        intent.putExtra("correctAns", info[5]);
                    } else if (type.equalsIgnoreCase("tf")) {
                        info = tfF.getData();
                        intent.putExtra("type", "tf");
                        intent.putExtra("question", info[0]);
                        intent.putExtra("ans", info[1]);
                    } else if (type.equalsIgnoreCase("nu")) {
                        info = nuF.getData();
                        intent.putExtra("type", "nu");
                        intent.putExtra("question", info[0]);
                        intent.putExtra("ans", info[1]);
                        intent.putExtra("decimal", info[2]);
                    }
                    isEdit=false;
                    setResult(ROW_UPDATED, intent);
                    finish();
                }
            });
        }
        else{
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(CreateQuiz.this, QuizMain.class);
                    if (selectedType.equalsIgnoreCase("mc")) {
                        info = mcF.getData();
                        intent.putExtra("type", "mc");
                        intent.putExtra("question", info[0]);
                        intent.putExtra("ans1", info[1]);
                        intent.putExtra("ans2", info[2]);
                        intent.putExtra("ans3", info[3]);
                        intent.putExtra("ans4", info[4]);
                        intent.putExtra("correctAns", info[5]);
                    } else if (selectedType.equalsIgnoreCase("tf")) {
                        info = tfF.getData();
                        intent.putExtra("type", "tf");
                        intent.putExtra("question", info[0]);
                        intent.putExtra("ans", info[1]);
                    } else if (selectedType.equalsIgnoreCase("nu")) {
                        info = nuF.getData();
                        intent.putExtra("type", "nu");
                        intent.putExtra("question", info[0]);
                        intent.putExtra("ans", info[1]);
                        intent.putExtra("decimal", info[2]);
                    }
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }
    }

    /**
     * sets menu items
     * @param menu
     * @return
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem statItem = menu.findItem(R.id.stats);
        statItem.setVisible(false);
        menu.findItem(R.id.help).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                new AlertDialog.Builder(CreateQuiz.this)
                        .setTitle("Help")
                        .setMessage("Activity developed by Can Shi " + "\n" +
                                "Version number: v1.0" + "\n" +
                                "First select a quiz type by clicking one of the buttons on top."
                                + "Then enter your questions and answers and click 'SAVE QUESTION'.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();
                return true;
            }
        });


        menu.findItem(R.id.import_resource).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                importQuiz = new ImportQuiz();
                LayoutInflater inflater = getLayoutInflater();
                @SuppressLint("InflateParams") View dialogLayout = inflater.inflate(R.layout.dialog_new_message, null);
                builder.setView(dialogLayout);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        importQuiz.execute();
                        Intent intent = new Intent(CreateQuiz.this, QuizMain.class);
                        startActivity(intent);
                        Toast.makeText(CreateQuiz.this, "Quiz imported successfully", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "Did not import quiz", Toast.LENGTH_LONG).show();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        });
        return true;
    }

    /**
     * when user is editing an existing quiz, it loads the text to the fragments
     */
    public void onEdit(){
        Intent intent = getIntent();
        if(intent.getExtras()!=null){
            isEdit=true;
            type = intent.getStringExtra("type");
            question=intent.getStringExtra("quiz");
            correct=intent.getStringExtra("correct");
            quizID=intent.getLongExtra("ID",0);
            ans1=intent.getStringExtra("ans1");
            ans2=intent.getStringExtra("ans2");
            ans3=intent.getStringExtra("ans3");
            ans4=intent.getStringExtra("ans4");
            mc.setVisibility(View.INVISIBLE);
            tf.setVisibility(View.INVISIBLE);
            nu.setVisibility(View.INVISIBLE);
            Bundle bundle = new Bundle();
            bundle.putString("question",question );
            bundle.putString("correct",correct);
            bundle.putLong("ID",quizID);
            bundle.putString("type",type);
            bundle.putInt("position", position);
            if(type.equalsIgnoreCase("mc")){
                bundle.putString("ans1", ans1);
                bundle.putString("ans2", ans2);
                bundle.putString("ans3", ans3);
                bundle.putString("ans4", ans4);
                mcF.setArguments(bundle);
                ft.replace(R.id.container, mcF);
                ft.addToBackStack("");
                ft.commit();
            }
            else if(type.equalsIgnoreCase("tf")){
                tfF.setArguments(bundle);
                ft.replace(R.id.container, tfF);
                ft.addToBackStack("");
                ft.commit();
            }
            else if(type.equalsIgnoreCase("nu")){
                nuF.setArguments(bundle);
                ft.replace(R.id.container, nuF);
                ft.addToBackStack("");
                ft.commit();
            }
        }
    }

    /**
     * class used to parse down values from xml.
     */
    public class ImportQuiz extends AsyncTask<String[], Integer, String[]> {
        HttpURLConnection conn;
        protected String[] doInBackground(String[]... strings) {
            try {
                URL url = new URL("http://torunski.ca/CST2335/QuizInstance.xml");
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser parser = factory.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(conn.getInputStream(), "utf-8");
                int eventType = parser.getEventType();
                String tagName;
                ArrayList<String> result = new ArrayList<>();
                ArrayList<String> answers = new ArrayList<>();

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    parser.next();
                    tagName = parser.getName();
                    if (tagName == null) {
                        tagName = "";
                    }
                    eventType = parser.getEventType();
                    if (eventType == START_TAG) {
                        if (tagName.equalsIgnoreCase("MultipleChoiceQuestion")) {
                            result.add(parser.getAttributeValue(null, "correct"));
                            result.add(parser.getAttributeValue(null, "question"));
                            onProgressUpdate(30);
                        } else if (tagName.equalsIgnoreCase("Answer")) {
                            answers.add(parser.nextText());
                        } else if (tagName.equalsIgnoreCase("NumericQuestion")) {
                            result.add(parser.getAttributeValue(null, "accuracy"));
                            result.add(parser.getAttributeValue(null, "question"));
                            result.add(parser.getAttributeValue(null, "answer"));
                            onProgressUpdate(60);
                        } else if (tagName.equalsIgnoreCase("TrueFalseQuestion")) {
                            result.add(parser.getAttributeValue(null, "question"));
                            result.add(parser.getAttributeValue(null, "answer"));
                            onProgressUpdate(80);
                        }
                    } else if (eventType == END_TAG) {
                        if (tagName.equalsIgnoreCase("MultipleChoiceQuestion")) {
                            result.addAll(answers);
                            answers.clear();//now everything is in result
                        }
                    }
                }
                onProgressUpdate(100);
                conn.disconnect();
                String[] finalResult = new String[result.size()];
                for (int i = 0; i < result.size(); i++) {
                    finalResult[i] = result.get(i);
                }
                return finalResult;
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            importProgress.setVisibility(View.VISIBLE);
            importProgress.setProgress(values[0]);
        }

        /**
         * inserts the downloaded quizzes to the database.
         * @param result
         */
        protected void onPostExecute(String[] result) {
            String mcQuestion, mcCorrect, mcCorrectFormat, mcAns1, mcAns2, mcAns3, mcAns4, nuDecimal, nuQuestion, nuAns, nuFormatedAns, tfQuestion, tfAns;
            ContentValues cv = new ContentValues();
            mcCorrect = result[0];
            mcCorrectFormat=null;
            if(mcCorrect.equalsIgnoreCase("1")){
                mcCorrectFormat="a";
            }
            else if(mcCorrect.equalsIgnoreCase("2")){
                mcCorrectFormat="b";
            }
            else if(mcCorrect.equalsIgnoreCase("3")){
                mcCorrectFormat="c";
            }
            else if(mcCorrect.equalsIgnoreCase("4")){
                mcCorrectFormat="d";
            }
            mcQuestion = result[1];
            mcAns1 = result[2];
            mcAns2 = result[3];
            mcAns3 = result[4];
            mcAns4 = result[5];
            nuDecimal = result[6];
            nuQuestion = result[7];
            nuAns = result[8];
            nuFormatedAns = QuizMain.formatStringNumber(nuAns, nuDecimal);
            tfQuestion = result[9];
            tfAns = result[10];
            QuizMain.quizMessage.add(mcQuestion);
            QuizMain.quizMessage.add(tfQuestion);
            QuizMain.quizMessage.add(nuQuestion);
            cv.put(QuizDatabaseHelper.KEY_QUIZ, mcQuestion);
            cv.put(QuizDatabaseHelper.KEY_QUIZTP, "mc");
            cv.put(QuizDatabaseHelper.KEY_ANSWER1, mcAns1);
            cv.put(QuizDatabaseHelper.KEY_ANSWER2, mcAns2);
            cv.put(QuizDatabaseHelper.KEY_ANSWER3, mcAns3);
            cv.put(QuizDatabaseHelper.KEY_ANSWER4, mcAns4);
            cv.put(QuizDatabaseHelper.KEY_CORRECT_ANS, mcCorrectFormat);
            db.insert(TABLE_NAME, "NullColumn", cv);
            cv.clear();
            cv.put(QuizDatabaseHelper.KEY_QUIZ, nuQuestion);
            cv.put(QuizDatabaseHelper.KEY_QUIZTP, "nu");
            cv.put(QuizDatabaseHelper.KEY_ANSWER1, 0);
            cv.put(QuizDatabaseHelper.KEY_ANSWER2, 0);
            cv.put(QuizDatabaseHelper.KEY_ANSWER3, 0);
            cv.put(QuizDatabaseHelper.KEY_ANSWER4, 0);
            cv.put(QuizDatabaseHelper.KEY_CORRECT_ANS, nuFormatedAns);
            db.insert(TABLE_NAME, "NullColumn", cv);
            cv.clear();
            cv.put(QuizDatabaseHelper.KEY_QUIZ, tfQuestion);
            cv.put(QuizDatabaseHelper.KEY_QUIZTP, "tf");
            cv.put(QuizDatabaseHelper.KEY_ANSWER1, 0);
            cv.put(QuizDatabaseHelper.KEY_ANSWER2, 0);
            cv.put(QuizDatabaseHelper.KEY_ANSWER3, 0);
            cv.put(QuizDatabaseHelper.KEY_ANSWER4, 0);
            cv.put(QuizDatabaseHelper.KEY_CORRECT_ANS, tfAns);
            db.insert(TABLE_NAME, "NullColumn", cv);
            QuizMain.c = db.query(false, helper.TABLE_NAME, new String[]{helper.KEY_ID,
                            helper.KEY_QUIZ, helper.KEY_QUIZTP, helper.KEY_ANSWER1, helper.KEY_ANSWER2,
                            helper.KEY_ANSWER3, helper.KEY_ANSWER4, helper.KEY_CORRECT_ANS},
                    null, null, null, null, null, null);
            QuizMain.c.moveToFirst();
            QuizMain.quizAdapter.notifyDataSetChanged();
            importProgress.setVisibility(View.INVISIBLE);
        }
    }

}
