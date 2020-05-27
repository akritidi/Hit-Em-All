package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;



public class ScoresActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        TextView[][] data=matchTextViews();
        DatabaseHandler db=new DatabaseHandler(this, null);

        int k;

        //Όταν το μέγεθος της βάσης γίνει 11, διαγράφει την 11η - μικρότερη -βάσει του σκορ- εγγραφή. Κρατάει σταθερό το μέγεθος της βάσης στα 10 - memory efficient
        if(db.getNumberOfDBRows()>10){
            db.deleteScores();
        }

        /*Λούπα για την εμφάνιση των εγγραφών στα text views. Η λούπα θα εκτελεστεί από 0 (άδεια βάση) έως 10 φορές (γεμάτη βάση - σταθερό μέγεθος μετά τις 10 εγγραφές).
        Σε κάθε επανάληψη ανατίθεται στο αντικειμενο η κατάλληλη εγγραφή από την βάση (βλ. highScores), και εμφανίζει στα κατάλληλα text views την τιμή του ονοματος
        και του σκορ της εγγραφής.
        */

            for(k=0;k<db.getNumberOfDBRows();k++){
                PlayerScore playerScore=db.highScores(k);
                data[k][0].setText(playerScore.get_playerName());
                data[k][1].setText(Integer.toString(playerScore.get_playerScore()));
            }

        db.close();
    }



    // Μέθοδος που αρχικοποιεί τον πίνακα data, αναθέτοντας του τα xml πεδία, τα οποία μετά ο κώδικας θα τροποποιήσει
    private TextView[][] matchTextViews() {
        TextView[][] data=new TextView[10][2];
        data[0][0]=findViewById(R.id.textView5);
        data[0][1]=findViewById(R.id.textView6);
        data[1][0]=findViewById(R.id.textView8);
        data[1][1]=findViewById(R.id.textView9);
        data[2][0]=findViewById(R.id.textView11);
        data[2][1]=findViewById(R.id.textView12);
        data[3][0]=findViewById(R.id.textView14);
        data[3][1]=findViewById(R.id.textView15);
        data[4][0]=findViewById(R.id.textView17);
        data[4][1]=findViewById(R.id.textView18);
        data[5][0]=findViewById(R.id.textView20);
        data[5][1]=findViewById(R.id.textView21);
        data[6][0]=findViewById(R.id.textView23);
        data[6][1]=findViewById(R.id.textView24);
        data[7][0]=findViewById(R.id.textView26);
        data[7][1]=findViewById(R.id.textView27);
        data[8][0]=findViewById(R.id.textView29);
        data[8][1]=findViewById(R.id.textView30);
        data[9][0]=findViewById(R.id.textView32);
        data[9][1]=findViewById(R.id.textView33);
        return data;
    }
}

