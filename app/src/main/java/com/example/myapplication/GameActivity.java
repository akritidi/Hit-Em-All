package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView countdownText;
    private boolean countdownFinished;

    @SuppressLint({"ClickableViewAccessibility", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        countdownFinished = false;
        MediaPlayer countdownSound = MediaPlayer.create(this, R.raw.countdown_sound);
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
                        countdownText.setText("Miss!");
                        return true;
                    }
                    updateCountdownText();
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
    public void updateCountdownText(){
        try {
            Thread.sleep(500);
            countdownText.setText(null);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        if(countdownFinished){
            countdownText.setText("Hit!");
        }
    }
}
