package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    private MediaPlayer countdownSound,hitSound,missSound,popSound,jumpSound;
    private boolean countdownFinished,gameFinished;
    private Toast hitToast;
    private Toast missToast;
    private ImageButton[] arrayOfButtons;
    private ImageButton imageButton1;
    private ImageButton imageButton2;
    private ImageButton imageButton3;
    private ImageButton imageButton4;
    private ImageButton imageButton5;
    private ImageButton imageButton6;
    private ImageButton imageButton7;
    private ImageButton imageButton8;
    private ImageButton imageButton9;
    private int lives,r,score,sumOfMoles,arrivalTime,hideTime;
    private TextView countdownText,scoreText,livesText,livesText2,livesText3;



    @SuppressLint({"ClickableViewAccessibility", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        createButtons();

        arrivalTime=1500;
        hideTime=1000;
        score=0;
        sumOfMoles=0;
        scoreText = findViewById(R.id.textScore);

        lives=3;
        livesText = findViewById(R.id.textLives);
        livesText2 = findViewById(R.id.textLives2);
        livesText3 = findViewById(R.id.textLives3);

        gameFinished = false;
        countdownFinished = false;
        countdownSound = MediaPlayer.create(this, R.raw.countdown_sound);
        countdownText = findViewById(R.id.textCountdown);
        Timer countdown = new Timer();
        final Timer moles=new Timer();
        countdownSound.start();
        countdown.schedule(new TimerTask() {
            @Override
            public void run() {
                updateCountdown();
            }
        },1000);

         startRepeatingTask();

        hitSound = MediaPlayer.create(this,R.raw.hit);
        missSound = MediaPlayer.create(this,R.raw.miss);
        popSound = MediaPlayer.create(this,R.raw.pop);
        jumpSound = MediaPlayer.create(this,R.raw.jump);

        ConstraintLayout hitLayout = (ConstraintLayout) findViewById(R.id.conLayout);
        hitLayout.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("SetTextI18n")
            @Override                                               //Εμφάνιση μηνύματος Miss! αφού έχει τελειώσει το countdown όταν γίνεται ταπ σε σημείο που δεν υπάρχει κουμπί
            public boolean onTouch(View v, MotionEvent event) {
                if (countdownFinished){
                    if (event.getAction() == MotionEvent.ACTION_DOWN){
                        missToast();
                        missSound.start();
                        return true;
                    }
                }
                return false;
            }
        });
    }
    public void createButtons(){
        imageButton1 = (ImageButton) findViewById(R.id.imageButton1);
        imageButton2 = (ImageButton) findViewById(R.id.imageButton2);
        imageButton3 = (ImageButton) findViewById(R.id.imageButton3);
        imageButton4 = (ImageButton) findViewById(R.id.imageButton4);
        imageButton5 = (ImageButton) findViewById(R.id.imageButton5);
        imageButton6 = (ImageButton) findViewById(R.id.imageButton6);
        imageButton7 = (ImageButton)findViewById(R.id.imageButton7);
        imageButton8 = (ImageButton)findViewById(R.id.imageButton8);
        imageButton9 = (ImageButton)findViewById(R.id.imageButton9);

        imageButton1.setOnClickListener(this);
        imageButton2.setOnClickListener(this);
        imageButton3.setOnClickListener(this);
        imageButton4.setOnClickListener(this);
        imageButton5.setOnClickListener(this);
        imageButton6.setOnClickListener(this);
        imageButton7.setOnClickListener(this);
        imageButton8.setOnClickListener(this);
        imageButton9.setOnClickListener(this);

        arrayOfButtons = new ImageButton[9];
        arrayOfButtons[0] = imageButton1;
        arrayOfButtons[1] = imageButton2;
        arrayOfButtons[2] = imageButton3;
        arrayOfButtons[3] = imageButton4;
        arrayOfButtons[4] = imageButton5;
        arrayOfButtons[5] = imageButton6;
        arrayOfButtons[6] = imageButton7;
        arrayOfButtons[7] = imageButton8;
        arrayOfButtons[8] = imageButton9;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public int randomMole(){
        int iMole;
        iMole = new Random().ints(0,8).limit(1).findFirst().getAsInt();
        return iMole; }
    public void startRepeatingTask() {
        Handler mHandler=new Handler();
        mHandler.postDelayed(runnableCode, 4000);
    }
    public void hideMole(int r){
        Handler mHandler2=new Handler();
        mHandler2.postDelayed(runnableCode2,hideTime);
    }
    public void nextMole(){
        Handler mHandler3=new Handler();
        mHandler3.postDelayed(runnableCode, arrivalTime);
    }
    private Runnable runnableCode=new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void run() {
            Log.d("Handlers","Called on Main Thread");
            r =  randomMole();
            arrayOfButtons[r].setVisibility(View.VISIBLE);
            popSound.start();
            sumOfMoles++;
            if(sumOfMoles==score){
                if (!gameFinished){
                    nextMole();
                }
            }else{
                hideMole(r);
            }

        }
    };
    private Runnable runnableCode2= new Runnable() {
        @Override
        public void run() {
            Log.d("Handlers","Called on Main Thread");
            arrayOfButtons[r].setVisibility(View.GONE);
            if(score<sumOfMoles){                                //elegxos lives
                jumpSound.start();
                updateLives();
                sumOfMoles=score;
            }
            if (!gameFinished){
                nextMole();
            }
        }
    };
    @SuppressLint("SetTextI18n")
    public void updateScore(){
        score++;
        if(score%5==0 && arrivalTime>200){
            arrivalTime=arrivalTime-250;
        }
        if(score%5==0 && hideTime>500){
            hideTime=hideTime-250;
        }
        scoreText.setText(Integer.toString(score));
    }
    public void updateCountdown(){
        countdownText.setText("2");
        try {
            Thread.sleep(1000);
            countdownText.setText("1");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(1000);
            countdownText.setText(null);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        countdownFinished=true;
    }
    @SuppressLint("SetTextI18n")
    public void missToast(){
        missToast = new Toast(getApplicationContext());
        missToast.setGravity(Gravity.CENTER|Gravity.TOP, 0, 550);

        TextView missTextView = new TextView(GameActivity.this);
        missTextView.setBackgroundColor(Color.TRANSPARENT);
        missTextView.setTextColor(Color.WHITE);
        missTextView.setTextSize(40);
        missTextView.setText("Miss!");
        Typeface missTypeface = Typeface.create("serif",Typeface.BOLD); //or familyName roman

        missTextView.setTypeface(missTypeface);
        missToast.setView(missTextView);
        missToast.show();
    }
    @SuppressLint("SetTextI18n")
    public void hitToast(){
        hitToast = new Toast(getApplicationContext());
        hitToast.setGravity(Gravity.CENTER|Gravity.TOP, 0, 550);

        TextView hitTextView = new TextView(GameActivity.this);
        hitTextView.setBackgroundColor(Color.TRANSPARENT);
        hitTextView.setTextColor(Color.WHITE);
        hitTextView.setTextSize(40);
        hitTextView.setText("Hit!");
        Typeface hitTypeface = Typeface.create("serif",Typeface.BOLD); //or familyName roman

        hitTextView.setTypeface(hitTypeface);
        hitToast.setView(hitTextView);
        hitToast.show();
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        if(countdownFinished){
            hitToast();
            hitSound.start();
            ImageButton btn = (ImageButton) findViewById(v.getId());
            btn.setVisibility(View.GONE);
            updateScore();
        }
    }
    @SuppressLint("SetTextI18n")
    public void updateLives(){
        lives--;
        if(lives==2){
            livesText3.setVisibility(View.GONE);
        }
        else if(lives==1){
            livesText2.setVisibility(View.GONE);
        }
        else if(lives==0){
            livesText.setVisibility(View.GONE);
            gameFinished=true;
            openGameOverActivity();                      //telos paixnidiou
        }
    }
    public void openGameOverActivity(){
        Intent i = new Intent(this,GameOverActivity.class);
        missToast.cancel();
        hitToast.cancel();
        startActivity(i);
        finish();
    }

}
