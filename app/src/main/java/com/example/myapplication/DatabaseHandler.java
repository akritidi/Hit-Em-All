package com.example.myapplication;

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;


public class DatabaseHandler extends SQLiteOpenHelper {

    //Οι απαραίτητες σταθερές για την Βάση Δεδομένων (version, όνομα, πίνακας, στήλες)
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "scoresDB.db";
    private static final String TABLE_SCORES = "scores";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "playerName";
    private static final String COLUMN_SCORE = "playerScore";


    //ο Constructor της ΒΔ
    DatabaseHandler(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME , factory, DATABASE_VERSION);
    }

    //Δημιουργία του σχήματος της ΒΔ -> πίνακας scores
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SCORES_TABLE = "CREATE TABLE " + TABLE_SCORES + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NAME + " TEXT," + COLUMN_SCORE + " INTEGER" + ")";
        db.execSQL(CREATE_SCORES_TABLE);

    }

    //Αναβάθμιση της ΒΔ. Η ΒΔ διαγράφεται και επαναδημιουργείται
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORES);
        onCreate(db);

    }

    //Μέθοδος που επιστρέφει τον αριθμό των εγγραφών που υπάρχουν στην ΒΔ.
    int getNumberOfDBRows() {
        String query = "SELECT * FROM " + TABLE_SCORES;
        SQLiteDatabase db = this.getWritableDatabase();

        int nor;
        @SuppressLint("Recycle")
        Cursor cursor = db.rawQuery(query, null);
        nor = cursor.getCount();

        db.close();
        return nor;

            }


    //*Μέθοδος για την προσθήκη νέας εγγραφής στην ΒΔ
        void addScore(PlayerScore playerScore) {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();
        values.put(COLUMN_NAME, playerScore.get_playerName());
        values.put(COLUMN_SCORE, playerScore.get_playerScore());
        db.insert(TABLE_SCORES, null, values);
        db.close();

    }

    /* Μέθοδος που δέχεται ως όρισμα έναν ακέραιο επιστρέφει μετά από query στον χρήστη το κατάλληλο αντικείμενο.
    * Πχ αν δεχτεί k=3 θα επιστρέψει την 3η εγγραφή του πίνακα
    * */
    PlayerScore highScores(int k) {

        String query = "SELECT * FROM " + TABLE_SCORES + " order by " + COLUMN_SCORE + " DESC," + COLUMN_ID + " ASC "; // query string: επιλέγει όλες τις εγγραφές του πίνακα και τις ταξινομεί με φθίνουσα
        SQLiteDatabase db = this.getWritableDatabase();                                                                 // σειρά - βάσει του σκορ (σε περίπτωση ισοβαθμίας, βάσει του αυξανόμενου id)
        Cursor cursor = db.rawQuery(query, null);                                                           // αναζήτηση μέσω cursor

        PlayerScore playerScore = new PlayerScore();

        if (cursor.moveToFirst()) {                                                                                     // Αν to query δεν είναι άδειο
            int i;
            cursor.moveToFirst();                                                                                       // Πηγαίνει στην πρώτη εγγραφή
            for (i = 0; i < k; i++) {

                cursor.moveToNext();                                                                                    // Μεταφέρεται σειρά σειρά στην εγγραφή που θέλουμε
            }
            playerScore.set_playerName(cursor.getString(1));                                                // Αφού έχει φτάσει στην σωστή εγγραφή, θέτει τα playerName και playerScore με βάση τα
            playerScore.set_playerScore(cursor.getInt(2));                                                  // αποθηκευμένα δεδομένα της κατάλληλης στήλης της εγγραφής
        }

        cursor.close();
        db.close();
        return playerScore;                                                                                             // επιστρέφει το αντικείμενο playerScore

    }

    //Μέθοδος για την διαγραφή εγγραφής του πίνακα
    void deleteScores(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_SCORES + " order by " + COLUMN_SCORE + " ASC," + COLUMN_ID + " DESC ";  // query string: επιλέγει όλες τις εγγραφές του πίνακα και τις ταξινομεί με αύξουσα

        @SuppressLint("Recycle")                                                                                        // σειρά - βάσει του σκορ (σε περίπτωση ισοβαθμίας, βάσει του id, με φθίνουσα σειρά)
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){                                                                                       // Αν to query δεν είναι άδειο
            cursor.moveToFirst();                                                                                       // Πηγαίνει στην πρώτη εγγραφή
            PlayerScore playerScore=new PlayerScore();
            playerScore.set_id(Integer.parseInt(cursor.getString(0)));
            db.delete(TABLE_SCORES, COLUMN_ID + " = ? ", new String[]{String.valueOf(playerScore.get_id())});  // Διαγράφει την εγγραφή με το μικρότερο σκορ (σε περίπτωση ισοβαθμίας,
        }                                                                                                                  // διαγράφει την πιο πρόσφατη εγγραφή (id - descendant)
        cursor.close();
        db.close();
        }
    }
