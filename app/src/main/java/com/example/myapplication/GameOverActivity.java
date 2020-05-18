package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class GameOverActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        Intent intent = getIntent();
        final int yourScore = intent.getIntExtra("SCORE",0);

        TextView scoreTextView = findViewById(R.id.textView3);
        scoreTextView.setText(String.valueOf(yourScore));

        final TextView nameMessage=findViewById(R.id.textView4);

        EditText nameEditText=findViewById(R.id.editText);
        final Editable yourName=nameEditText.getText();

        Button submitScore= findViewById(R.id.gameOverSubmit);
        submitScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(yourName.equals("")){
                    nameMessage.setText("Δεν αναγνωρίζω το όνομα. Παρακαλώ γράψε το όνομα σου για να σώσεις το σκορ σου!");
                }
                else {
                    addScore(yourName, yourScore);
//                    showBestScores();
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

    private void showBestScores() {
        DatabaseHandler myDBHandler=new DatabaseHandler(this, null, null, 1);
        myDBHandler.bestScores();
    }

    private void addScore(Editable yourName, int yourScore) {
        DatabaseHandler myDBHandler=new DatabaseHandler(this, null, null, 1);
        PlayerScore playerScore=new PlayerScore(yourName,yourScore);
        myDBHandler.addScore(playerScore);

    }

    public void openGameActivity(){
        Intent i = new Intent(this,GameActivity.class);
        startActivity(i);
        finish();
    }
}
