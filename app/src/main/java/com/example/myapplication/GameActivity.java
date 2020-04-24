package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {
private Timer countdown;
private TextView countdownText;
private MediaPlayer countdownSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        countdownSound = MediaPlayer.create(this,R.raw.countdown_sound);
        countdownText = findViewById(R.id.textCountdown);
        countdown = new Timer();
        countdownSound.start();
        countdown.schedule(new TimerTask() {
            @Override
            public void run() {
                updateCountdown();
            }
        },1000);

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
            countdownText.setText(" ");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
