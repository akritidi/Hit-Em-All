package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private long backPressedTime;
    private Toast backToast;
    Button playButton, settingsButton, scoresButton, exitButton;


    /**
     * Εδώ κατά την δημιουργια του activity αρχικοποιουνται τα κουμπία και
     * οι listener τους, που οδηγούν στις βασικές δραστηριότητες της εφαρμογής.
     * @param savedInstanceState .
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

        /*
          Καθορίζονται οι εντολές των onClickListeners για κάθε κουμπί
         */
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
     * Μεθοδος, που ενεργοποιείται όταν ο χρηστης πατάει το backPress στην συσκευή του.
     * Στο πρώτο πάτημα εμφανίζει ένα κείμενο της προειδοποιησης εξοδού απο την εφαρμογή.
     * Στο δέυτερο γίνεται έξοδος (αν αυτό γίνει σε διάστημα 2 δευτερολέπτων).
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

    /**
     *  Μέθοδος που ξεκινάει την GameActivity, για να ξεκινήσει το παιχνίδι
     */
    public void openGameActivity(){
        Intent i = new Intent(this,GameActivity.class);
        startActivity(i);
    }

    /**
     * Μέθοδος που ξεκινάει την SettingsActivity (ρυθμίσεις ήχων και μουσικής)
     */
    public void openSettingsActivity(){
        Intent i = new Intent(this,SettingsActivity.class);
        startActivity(i);
    }

    /**
     * Μέθοδος που ξεκινάει την ScoresActivity, που δείχνει όλα τα scores που έχουν καταγραφεί στην βάση
     */
    public void openScoresActivity(){
        Intent i = new Intent(this,ScoresActivity.class);
        startActivity(i);
    }

    /**
     * Μέθοδος που εμφανίζει στον χρήστη ενα exit dialog, για την επιβεβαίωση της εξόδου του από το παιχνίδι
     */
    public void exitMainActivity(){
       ExitDialog exitDialog = new ExitDialog();
       exitDialog.show(getSupportFragmentManager(),"exit dialog");
    }


}
