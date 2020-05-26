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
        DatabaseHandler db=new DatabaseHandler(this, null,null,1);

        int k;

        // Εξήγηση της λογικής του παρακάτω κώδικα. Ξεκινάει η λούπα. Το σημείο λήξης της λούπας, καθορίζεται από το πλήθος των εγγραφών στον πίνακα (έχει τεθεί στην μέθοδο getNumberOFDBRows της DatabaseHandler
        // ως μάξιμουμ το 10 - καθώς θέλουμε na εμφανίζουμε μόνο τα πρώτα 10 High Scores. Η λούπα λοιπόν θα εκτελεστεί από καμία έως το πολύ 10 φορές
        // Λογική λούπας: Σε κάθε επανάληψη
        // 1. Καλείται η highScores με όρισμα το εκάστοτε k. Στην πρώτη επανάληψη θα επιστραφεί το πρώτο αντικείμενο του αντίστοιχου query, στην δεύτερη το δεύτερο κ.ο.κ.
        // αυτό το φροντίζει η μέθοδος highScores λόγω της λούπας που κάνει moveToNext() όσες φορές είναι το k.
        //2. Αφού επιστραφεί το αντικείμενο μέσω του δισδιάσταστου πίνακα data αλλάζουμε τα TextViews που εμφανίζονται στον χρήστη με βάση τα playerName και playerScore, του αντικειμένου που επιστάφηκε.
        for(k=0;k<db.getNumberOfDBRows();k++){
            PlayerScore playerScore=db.highScores(k);
            data[k][0].setText(playerScore.get_playerName());
            data[k][1].setText(Integer.toString(playerScore.get_playerScore()));
        }
    }



    //μια μέθοδος που αρχικοποιεί τον πίνακα data αναθέτοντας του τα xml πεδία, τα οποία μετά ο κώδικας θα τροποποιήσει
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

