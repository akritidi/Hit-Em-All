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
    /**Εδώ κατά την δημιουργια του activity αρχηκοποιουνται τα κουμπία και
     * οι listener τους οπού οδηγούν στις βασικές δραστηριότητες τις εφαρμογης.
     * @param savedInstanceState
     */
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

                openGameActivity();
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openSettingsActivity();
            }
        });

        scoresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openScoresActivity();
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                exitMainActivity();
            }
        });


    }



    /**
     * Μεθοδος που ενεργοποιείται όταν ο χρηστης πατάει το backPress στην συσκευή του.
     * Στο πρώτο πάτημα εμφανίζει ένα κείμενο της προειδοποιησης εξοδού απο την εφαρμογή.
     * Στο δέυτερο γίνεται έξοδος.
     */

    @Override
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
    /** Εδώ αρχίζει βασική διαδραστική δραστηριότητα GameActivity. */

    public void openGameActivity(){
        Intent i = new Intent(this,GameActivity.class);
        startActivity(i);
    }
    /** Εδώ αρχίζει η δραστηριότητα που αφορά τις ρυθμίσεις. */

    public void openSettingsActivity(){
        Intent i = new Intent(this,SettingsActivity.class);
        startActivity(i);
    }
    /** Εδώ αρχίζει η δραστηριότητα με τα scores που έχουν καταγραφεί στην βάση */

    public void openScoresActivity(){
        Intent i = new Intent(this,ScoresActivity.class);
        startActivity(i);
    }

    public void exitMainActivity(){
       ExitDialog exitDialog = new ExitDialog();
       exitDialog.show(getSupportFragmentManager(),"exit dialog");
    }


}
