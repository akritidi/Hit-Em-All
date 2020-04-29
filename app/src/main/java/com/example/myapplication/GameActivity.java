package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    private MediaPlayer countdownSound;
    private TextView countdownText;
    private boolean countdownFinished;
    private Toast hitToast;
    private Toast missToast;

    @SuppressLint({"ClickableViewAccessibility", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        countdownFinished = false;
        countdownSound = MediaPlayer.create(this, R.raw.countdown_sound);
        countdownText = findViewById(R.id.textCountdown);
        Timer countdown = new Timer();
        countdownSound.start();
        countdown.schedule(new TimerTask() {
            @Override
            public void run() {
                updateCountdown();
            }
        },1000);

        ConstraintLayout hitLayout = (ConstraintLayout) findViewById(R.id.conLayout);
        hitLayout.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("SetTextI18n")
            @Override                                               //Εμφάνιση μηνύματος Miss! αφού έχει τελειώσει το countdown όταν γίνεται ταπ σε σημείο που δεν υπάρχει κουμπί
            public boolean onTouch(View v, MotionEvent event) {
                if (countdownFinished){
                    if (event.getAction() == MotionEvent.ACTION_DOWN){
                        missToast();
                        return true;
                    }
                }
                return false;
            }
        });

        ImageButton imageButton1 = findViewById(R.id.imageButton1);
        ImageButton imageButton2 = findViewById(R.id.imageButton2);
        ImageButton imageButton3 = findViewById(R.id.imageButton3);
        ImageButton imageButton4 = findViewById(R.id.imageButton4);
        ImageButton imageButton5 = findViewById(R.id.imageButton5);
        ImageButton imageButton6 = findViewById(R.id.imageButton6);
        ImageButton imageButton7 = findViewById(R.id.imageButton7);
        ImageButton imageButton8 = findViewById(R.id.imageButton8);
        ImageButton imageButton9 = findViewById(R.id.imageButton9);

        imageButton1.setOnClickListener(this);
        imageButton2.setOnClickListener(this);
        imageButton3.setOnClickListener(this);
        imageButton4.setOnClickListener(this);
        imageButton5.setOnClickListener(this);
        imageButton6.setOnClickListener(this);
        imageButton7.setOnClickListener(this);
        imageButton8.setOnClickListener(this);
        imageButton9.setOnClickListener(this);
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
        }
    }
}
