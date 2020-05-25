package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class GameOverActivity extends AppCompatActivity {
    private Editable yourName;
    private int yourScore;
    boolean wrongNameToastShown;
    boolean longNameToastShown;
    DatabaseHandler db=new DatabaseHandler(this, null,null,1);
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        Intent intent = getIntent();
        yourScore = intent.getIntExtra("SCORE",0);

        TextView scoreTextView = findViewById(R.id.textView3);
        scoreTextView.setText(String.valueOf(yourScore));

        wrongNameToastShown=false;
        longNameToastShown=false;

        EditText nameEditText=findViewById(R.id.editText);
        yourName=nameEditText.getText();

        final Button submitScore= findViewById(R.id.gameOverSubmit);
        submitScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(yourName.toString().contentEquals("")){
                    wrongNameToast();
                }
                else if(yourName.toString().length()>15){
                    longNameToast();
                }
                else {
                    addScore(yourName,yourScore);
                    submitScore.setEnabled(false);
                    if(db.getNumberOfDBRows()==10){
                        PlayerScore lastPlayerScore=db.highScores(9);
                        if(yourScore<=lastPlayerScore.get_playerScore()){
                            notTopTenToast();
                            Handler myH=new Handler();
                            myH.postDelayed(r,1000);
                        }
                        else {openScoresActivity();}
                    }



                }
            }
        });

        Button playAgain = findViewById(R.id.gameOverPlayAgain);
        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGameActivity();
            }
        });

        Button gameOverExit = findViewById(R.id.gameOverExit);
        gameOverExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void addScore(Editable yourName, int yourScore) {
        DatabaseHandler myDBHandler=new DatabaseHandler(this, null, null, 1);
        PlayerScore playerScore=new PlayerScore(yourName,yourScore);
        myDBHandler.addScore(playerScore);
    }

    private void longNameToast() {
        Toast longNameToast = new Toast(getApplicationContext());
        longNameToast.setGravity(Gravity.CENTER|Gravity.BOTTOM,0,500);

        TextView longNameTextView = new TextView(GameOverActivity.this);
        longNameTextView.setBackgroundColor(Color.GRAY);
        longNameTextView.setTextColor(Color.WHITE);
        longNameTextView.setTextSize(20);
        longNameTextView.setText(R.string.long_name);
        Typeface missTypeface = Typeface.create("serif",Typeface.BOLD); //or familyName roman
        longNameTextView.setTypeface(missTypeface);

        longNameToast.setView(longNameTextView);
        longNameToast.show();
        longNameToastShown = true;
    }

    public void wrongNameToast(){
        Toast wrongNameToast = new Toast(getApplicationContext());
        wrongNameToast.setGravity(Gravity.CENTER|Gravity.BOTTOM,0,500);

        TextView wrongTextView = new TextView(GameOverActivity.this);
        wrongTextView.setBackgroundColor(Color.GRAY);
        wrongTextView.setTextColor(Color.WHITE);
        wrongTextView.setTextSize(20);
        wrongTextView.setText(R.string.wrong_name_toast);
        Typeface missTypeface = Typeface.create("serif",Typeface.BOLD); //or familyName roman

        wrongTextView.setTypeface(missTypeface);
        wrongNameToast.setView(wrongTextView);
        wrongNameToast.show();
        wrongNameToastShown = true;

    }

    public void notTopTenToast(){
        Toast notTopTenToast = new Toast(getApplicationContext());
        notTopTenToast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL,0,500);

        TextView notTopTenView = new TextView(GameOverActivity.this);
        notTopTenView.setBackgroundColor(Color.GRAY);
        notTopTenView.setTextColor(Color.WHITE);
        notTopTenView.setTextSize(20);
        notTopTenView.setText(R.string.notTopTen);
        notTopTenView.setGravity(Gravity.CENTER);
        Typeface missTypeface = Typeface.create("serif",Typeface.BOLD); //or familyName roman

        notTopTenView.setTypeface(missTypeface);
        notTopTenToast.setView(notTopTenView);
        notTopTenToast.show();


    }
    private Runnable r=new Runnable() {
        @Override
        public void run() {
            openScoresActivity();
        }
    };

    public void openGameActivity(){
        Intent i = new Intent(this,GameActivity.class);
        startActivity(i);
        finish();
    }

    private void openScoresActivity() {
        Intent i = new Intent(this,ScoresActivity.class);

        startActivity(i);
        finish();
    }
}
