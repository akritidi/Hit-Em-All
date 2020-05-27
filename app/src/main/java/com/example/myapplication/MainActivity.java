package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;


import android.content.ComponentName;

import android.content.Intent;
import android.content.ServiceConnection;

import android.os.Bundle;
import android.os.IBinder;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private long backPressedTime;
    private Toast backToast;
    Button playButton, settingsButton, scoresButton, exitButton;
MyService myService;

boolean isBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, MyService.class);// εδω αρχιζει το service
        startService(intent);

        playButton = findViewById(R.id.button);
        settingsButton = findViewById(R.id.button2);
        scoresButton = findViewById(R.id.button3);
        exitButton = findViewById(R.id.button4);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableButtons();
                openGameActivity();
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableButtons();
                openSettingsActivity();
            }
        });

        scoresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableButtons();
                openScoresActivity();
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableButtons();
                exitMainActivity();
            }
        });


    }

    private void disableButtons() {
        playButton.setEnabled(false);
        settingsButton.setEnabled(false);
        scoresButton.setEnabled(false);
        exitButton.setEnabled(false);
    }

    @Override                       //Άνοιγμα διαλόγου για έξοδο απο την εφαρμογή όταν πατηθεί το "πίσω" 2 φορές
    public void onBackPressed(){
        if (backPressedTime + 2000 > System.currentTimeMillis()){
            backToast.cancel();
            exitMainActivity();
        }
        else{
            backToast = Toast.makeText(getBaseContext(),R.string.exit_toast,Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime=System.currentTimeMillis();
    }

    public void openGameActivity(){
        Intent i = new Intent(this,GameActivity.class);
        startActivity(i);
    }

    public void openSettingsActivity(){
        Intent i = new Intent(this,SettingsActivity.class);
        startActivity(i);
    }

    public void openScoresActivity(){
        Intent i = new Intent(this,ScoresActivity.class);
        startActivity(i);
    }

    public void exitMainActivity(){
       ExitDialog exitDialog = new ExitDialog();
       exitDialog.show(getSupportFragmentManager(),"exit dialog");
    }

    private ServiceConnection conect = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyService.LocalBinder binder = (MyService.LocalBinder)service;
            myService = binder.getService();
            isBound=true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };
}
