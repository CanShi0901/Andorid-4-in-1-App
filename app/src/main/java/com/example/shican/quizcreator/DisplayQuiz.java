package com.example.shican.quizcreator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.support.design.widget.Snackbar;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.EditText;

public class DisplayQuiz extends Toolbar {
    AlertDialog.Builder builder;
    String quizQuestion, quizAnswer, type, quizAns1, quizAns2, quizAns3, quizAns4, enteredAns, prompt;
    long quizID;
    Button delete, modify,checkAnswer;
    TextView quizDetail, yourAnswerHere;
    EditText enterAnswer;
    Snackbar snackbar;
    FragmentManager fm;
    FragmentTransaction ft;
    mcFragment mcF;
    tfFragment tfF;
    nuFragment nuF;
    mcDisplayFragment mcDis;
    tfDisplayFragment tfDis;
    nuDisplayFragment nuDis;
    int position;
    LinearLayout editButtons;

    /**
     * sets the layout, displays the quiz depending on its type
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_quiz);
        initToolbar();

        final Context context = this;
        fm = getFragmentManager();
        ft = fm.beginTransaction();
        mcF = new mcFragment();
        tfF = new tfFragment();
        nuF = new nuFragment();
        mcDis = new mcDisplayFragment();
        tfDis = new tfDisplayFragment();
        nuDis = new nuDisplayFragment();
        editButtons = (LinearLayout)findViewById(R.id.edit);
        delete = (Button)findViewById(R.id.delete);
        modify = (Button)findViewById(R.id.modify);
        checkAnswer = (Button) findViewById(R.id.checkAnswer);
        builder = new AlertDialog.Builder(this);
        enterAnswer = (EditText) findViewById(R.id.enterAnswer);
        quizDetail = (TextView) findViewById(R.id.quizDetail);
        yourAnswerHere = (TextView) findViewById(R.id.youAnswerHere);
        final Bundle infoPassed = getIntent().getExtras();
        quizID = infoPassed.getLong("ID");
        position=infoPassed.getInt("position");
        quizQuestion = infoPassed.getString("quiz");
        type = infoPassed.getString("type");
        quizAnswer = infoPassed.getString("correctAns");
        quizDetail.setText("ID:" + quizID + "  " + quizQuestion);

        if (type.equalsIgnoreCase("mc")) {
            quizAns1 = infoPassed.getString("ans1");
            quizAns2 = infoPassed.getString("ans2");
            quizAns3 = infoPassed.getString("ans3");
            quizAns4 = infoPassed.getString("ans4");
            Bundle info = new Bundle();
            info.putString("ans1", quizAns1);
            info.putString("ans2", quizAns2);
            info.putString("ans3", quizAns3);
            info.putString("ans4", quizAns4);
            info.putLong("ID",quizID);
            info.putInt("position", position);
            mcDis.setArguments(info);
            ft.add(R.id.quizContainer, mcDis);
            ft.addToBackStack("");
            ft.commit();
        } else if (type.equalsIgnoreCase("tf")) {
            enterAnswer.setVisibility(View.INVISIBLE);
            yourAnswerHere.setVisibility(View.INVISIBLE);
            ft.add(R.id.quizContainer, tfDis);
            ft.addToBackStack("");
            ft.commit();
        } else if (type.equalsIgnoreCase("nu")) {
            ft.add(R.id.quizContainer, nuDis);
            ft.addToBackStack("");
            ft.commit();
        }

        delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putLong("ID", quizID);
                bundle.putInt("position", position);
                Intent intent = new Intent(DisplayQuiz.this, QuizMain.class);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        modify.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
            Intent intent = new Intent(DisplayQuiz.this, CreateQuiz.class);
                intent.putExtra("ID", quizID);
                intent.putExtra("position", position);
                intent.putExtra("quiz", quizQuestion);
                intent.putExtra("type", type);
                intent.putExtra("correct", quizAnswer );
                intent.putExtra("ans1", quizAns1);
                intent.putExtra("ans2", quizAns2);
                intent.putExtra("ans3", quizAns3);
                intent.putExtra("ans4", quizAns4);
                startActivityForResult(intent, 3);
            }
        });

        checkAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View v = findViewById(R.id.quizDisplay_layout);
                if (type.equalsIgnoreCase("tf")) {
                    enteredAns = tfDis.getChoice();
                } else {
                    enteredAns = enterAnswer.getText().toString();
                }
                int duration = Snackbar.LENGTH_SHORT;
                if (enteredAns.equalsIgnoreCase(quizAnswer)) {
                    prompt = "You are correct!";
                } else {
                    prompt = "You didn't select the correct answer.";
                }
                snackbar = Snackbar.make(v, prompt, duration);
                snackbar.show();
            }
        });
    }

    /**
     * goes back to main with a modify request code
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==3){
            if(resultCode==99){
                setResult(99, data);
                finish();
            }
        }
    }

    /**
     * sets the menu items
     * @param menu
     * @return
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem importItem = (MenuItem) menu.findItem(R.id.import_resource);
        importItem.setVisible(false);
        MenuItem statItem = (MenuItem) menu.findItem(R.id.stats);
        statItem.setVisible(false);
        menu.findItem(R.id.help).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                new AlertDialog.Builder(DisplayQuiz.this)
                        .setTitle("Help")
                        .setMessage("Activity developed by Can Shi " + "\n" +
                                "Version number: v1.0" + "\n" +
                                "MC: type the corresponding answer in full;  TF: select true/false; numeric: enter the number")
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
}
