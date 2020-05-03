package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    private MediaPlayer countdownSound,hitSound,missSound;
    private TextView countdownText;
    private boolean countdownFinished;
    private Toast hitToast;
    private Toast missToast;
    private int lives;
    private int r;
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
    private int mInterval;
    private Handler mHandler;



    @SuppressLint({"ClickableViewAccessibility", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        createButtons();

        countdownFinished = false;
        lives=3;
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

    public void startRepeatingTask() {
        Handler mHandler=new Handler();
        mHandler.postDelayed(runnableCode, 3000);

    }

    public void hideMole(int r){
        Handler mHandler2=new Handler();
        mHandler2.postDelayed(runnableCode2,1500);

    }

    public void nextMole(){
        Handler mHandler3=new Handler();
        mHandler3.postDelayed(runnableCode, 2000);
    }

    private Runnable runnableCode=new Runnable() {
        @Override
        public void run() {
            Log.d("Handlers","Called on Main Thread");
           r =  randomMole();
            arrayOfButtons[r].setVisibility(View.VISIBLE);
            hideMole(r);
        }
    };

    private Runnable runnableCode2= new Runnable() {
        @Override
        public void run() {
            Log.d("Handlers","Called on Main Thread");
            arrayOfButtons[r].setVisibility(View.GONE);
            nextMole();
        }
    };


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
        }
    }
    public int randomMole(){
        int iMole;
        iMole = new Random().ints(0,8).limit(1).findFirst().getAsInt();
   return iMole; }

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


}
