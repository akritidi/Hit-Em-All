package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
private Button playButton;
private Button settingsButton;
private Button scoresButton;
private Button exitButton;
private long backPressedTime;
private Toast backToast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playButton = (Button) findViewById(R.id.button);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGameActivity();
            }
        });

        settingsButton = (Button) findViewById(R.id.button2);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettingsActivity();
            }
        });

        scoresButton = (Button) findViewById(R.id.button3);
        scoresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openScoresActivity();
            }
        });

        exitButton = (Button) findViewById(R.id.button4);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitMainActivity();
            }
        });

    }

    @Override                       //Άνοιγμα διαλόγου για έξοδο απο την εφαρμογή όταν πατηθεί το "πίσω" 2 φορές
    public void onBackPressed(){
        if (backPressedTime + 2000 > System.currentTimeMillis()){
            backToast.cancel();
            exitMainActivity();
        }
        else{
            backToast = Toast.makeText(getBaseContext(),"Press again to exit",Toast.LENGTH_SHORT);
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

}
