package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class GameOverActivity extends AppCompatActivity {
    private Editable yourName;
    private int yourScore;
    final DatabaseHandler db=new DatabaseHandler(this);

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        //μεταφέρεται το τελευταίο σκορ του χρήστη από την GameActivity
        Intent intent = getIntent();
        yourScore = intent.getIntExtra("SCORE",0);

        TextView scoreTextView = findViewById(R.id.textView3);
        scoreTextView.setText(String.valueOf(yourScore));

        EditText nameEditText=findViewById(R.id.editText);
        yourName=nameEditText.getText();

        final Button submitScore= findViewById(R.id.gameOverSubmit);
        submitScore.setOnClickListener(new View.OnClickListener() {                                 //On Click Listener για το κουμπί υποβολής
            @Override
            public void onClick(View v) {                                                      // OnclickListener για το κουμπί υποβολής

                if(yourName.toString().contentEquals("")){                                      // περίπτωση που ο χρήστης δεν πληκτρολογήσει τίποτα
                    wrongNameToast();
                }
                else if(yourName.toString().length()>15){                                           // περίπτωση που το όνομα είναι πολύ μεγάλο
                    longNameToast();
                }
                else {                                                                              // περίπτωση που ο χρήστης έχει δώσει όνομα μικρότερο ή ίσο των 15 χαρακτήρων
                    addScore(yourName,yourScore);                                                   // προσθήκη στην ΒΔ μέσω της μεθόδου addScore()
                    submitScore.setEnabled(false);                                                  // σε περίπτωση που ο χρήστης πατήσει πολλές φορές υποβολή, εξασφαλίζει ότι η εγγραφή θα αποθηκευτεί 1 φορά

                    //περιπτώσεις μεγέθους ΒΔ. Εμφανίζονται τα κατάλληλα μυνήματα
                    if(db.getNumberOfDBRows()==1){                                                  // Μύνημα για την 1η εγγραφή στην ΒΔ. -> Καλύτερο σκορ μέχρι τώρα
                        bestScoreToast();
                    }
                    else if(db.getNumberOfDBRows()>1 && db.getNumberOfDBRows()<=10){                //Περίπτωση που η βάση είναι από 2 εως και 10 σειρές
                        PlayerScore firstPlayerScore=db.highScores(1);
                        if(yourScore>firstPlayerScore.get_playerScore()) {
                            bestScoreToast();
                        }
                        else{topTenToast();}
                    }
                    else if(db.getNumberOfDBRows()>10){                                             //Περίπτωση που η βάση είναι πάνω από 10 εγγραφές
                        PlayerScore firstPlayerScore=db.highScores(1);
                        PlayerScore lastPlayerScore=db.highScores(9);
                        if(yourScore>firstPlayerScore.get_playerScore()) {
                            bestScoreToast();
                        }
                        else if(yourScore<=lastPlayerScore.get_playerScore()){
                            notTopTenToast();
                        }

                        else{topTenToast();}

                    }


                    Handler myHandler= new Handler();
                    myHandler.postDelayed(r, 1000);                                       //καλεί μέσω handler το Runnable Code r, με καθυστέρηση 1 δευτερόλεπτο


                }
            }
        });

        Button playAgain = findViewById(R.id.gameOverPlayAgain);                                     // OnclickListener για το κουμπί "Παίξε Ξανά". Δρομολογεί στην οθόνη του παιχνιδιού, που ξεκινά νέο παιχνίδι
        playAgain.setOnClickListener(new View.OnClickListener() {                                    //On Click Listener για το κουμπί "Παίξε Ξανά"
            @Override
            public void onClick(View v) {
                openGameActivity();
            }
        });

        Button gameOverExit = findViewById(R.id.gameOverExit);                                       //On Click Listener για το κουμπί εξόδου από το παιχνίδι. Δρομολογεί στην αρχική οθόνη
        gameOverExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
    * Μέθοδος για την προσθήκη νέου σκορ στην ΒΔ
    * */
    public void addScore(Editable yourName, int yourScore) {
        DatabaseHandler db=new DatabaseHandler(this);
        PlayerScore playerScore=new PlayerScore(yourName,yourScore);
        db.addScore(playerScore);
    }

    /**
     * Μέθοδος, που μετράει το ύψος της οθόνης της συσκευής και καθορίζει βάσει αυτού, το κατάλληλο (αναλογικά) ύψος για την εμφάνιση των toasts.
     */
    public int rightYOffset(){
        int myHeight;
        DisplayMetrics displayMetrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height=displayMetrics.heightPixels;
        myHeight=height/4;

        return myHeight;
    }


    /**
     * Μέθοδος για την εμφάνιση Toast, για την περίπτωση που ο χρήστης δεν πληκτρολογήσει τίποτα
     * */
    public void wrongNameToast(){
        Toast wrongNameToast;
        wrongNameToast = Toast.makeText(getBaseContext(), R.string.wrong_name_toast, Toast.LENGTH_SHORT);
        int myY=rightYOffset();
        wrongNameToast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.TOP,0,myY);

        wrongNameToast.show();
    }

    /**
     * Μέθοδος για την εμφάνιση Toast, για την περίπτωση που το όνομα που πληκτρολογεί ο χρήστης είναι πολύ μεγάλο (πάνω από 15 χαρακτήρες)
     * */
    private void longNameToast() {
        Toast longNameToast;
        longNameToast = Toast.makeText(getBaseContext(), R.string.long_name, Toast.LENGTH_SHORT);
        int myY=rightYOffset();
        longNameToast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.TOP,0,myY);
        longNameToast.show();
    }

    /**
     * Μέθοδος για την εμφάνιση Toast, για την περίπτωση που γίνει νέο Best High Score
     * */
    public void bestScoreToast(){
        Toast bestScoreToast;
        bestScoreToast = Toast.makeText(getBaseContext(), R.string.bestScore, Toast.LENGTH_SHORT);
        int myY=rightYOffset();
        bestScoreToast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.TOP,0,myY);
        bestScoreToast.show();
    }

    /**
     * Μέθοδος για την εμφάνιση Toast, για την περίπτωση που το σκορ του χρήστη μπαίνει στα Top 10 Best Scores (εκτός της πρώτης θέσης)
     * */
    public void topTenToast(){
        Toast topTenToast;
        topTenToast = Toast.makeText(getBaseContext(), R.string.topTen, Toast.LENGTH_SHORT);
        int myY=rightYOffset();
        topTenToast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.TOP,0,myY);
        topTenToast.show();
    }

    /**
     * Μέθοδος για την εμφάνιση Toast, για την περίπτωση που το σκορ του χρήστη δεν μπει στο Top 10
     * */
    public void notTopTenToast(){
        Toast notTopTenToast;
        notTopTenToast = Toast.makeText(getBaseContext(), R.string.notTopTen, Toast.LENGTH_LONG);
        int myY=rightYOffset();
        notTopTenToast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.TOP,0,myY);
        notTopTenToast.show();
    }


    /**
    * Runnable Code που ανακατευθύνει στην Scores Activity
    * */
    private final Runnable r=new Runnable() {
        @Override
        public void run() {
            openScoresActivity();
        }
    };


    /**
    * Μέθοδοι που ανακατευθύνουν τον χρήστη στην Game Activity και Scores Activity, αντίστοιχα.
    * */

    public void openGameActivity(){
        Intent i = new Intent(this,GameActivity.class);
        db.close();
        startActivity(i);
        finish();
    }

    private void openScoresActivity() {
        Intent i = new Intent(this,ScoresActivity.class);
        db.close();
        startActivity(i);
        finish();
    }

}
