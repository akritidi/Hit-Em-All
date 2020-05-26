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
    public static final String TABLE_SCORES = "scores";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "playerName";
    public static final String COLUMN_SCORE = "playerScore";

    //ο Constructor της ΒΔ
    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
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

    //Μέθοδος που επιστρέφει τον αριθμό των γραμμών που είναι στο query από την βαση δεδομένων.
    //Έχει σημασία το query να έχει limit 10, ώστε να μην κρασάρει η Scores Activity, όταν στην βάση αποθηκευτούν πάνω από 10 εγγραφές
    //Δουλεύει άρτια και για λιγότερες από 10 εγγραφές.
    public int getNumberOfDBRows() {
        int nor;
        String query = "SELECT * FROM " + TABLE_SCORES + " order by " + COLUMN_SCORE + " DESC " + " limit " + 10;
        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle")
        Cursor cursor = db.rawQuery(query, null);
        nor = cursor.getCount();

        return nor;

            }


    //μέθοδος για προσθήκη στην ΒΔ, νέου σκορ. Δέχεται ένα αντικείμενο PlayerScore και βάζει στην βάση δεδομένων
    // μια νέα εγγραφή, βάζοντας στις στήλες COLUMN_NAME και COLUMN_SCORE, τα playerName και playerScore αντίστοιχα.
    //Με την στήλη COLUMN_ID, δεν ασχολούμαστε. Έχει μπει από την OnCreate ως στήλη AUTOINCREMENT (παράγει ID μόνο του)
    public void addScore(PlayerScore playerScore) {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();
        values.put(COLUMN_NAME, playerScore.get_playerName());
        values.put(COLUMN_SCORE, playerScore.get_playerScore());
        db.insert(TABLE_SCORES, null, values);
        db.close();

    }

    // Μέθοδος που δέχεται ως όρισμα έναν ακέραιο επιστρέφει μετά από query στον χρήστη το κατάλληλο αντικείμενο.
    public PlayerScore highScores(int k) {
        String query = "SELECT * FROM " + TABLE_SCORES + " order by " + COLUMN_SCORE + " DESC " + " limit " + 10; //query string για χρήση μέσω cursor. Επιλέγει μάξιμουμ τις 10 πρώτες εγγραφές του πίνακα, οι οποίες έχουν μπει με φθινουσα σειρά
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null); //κάνει την αναζήτηση μέσω cursor

        PlayerScore playerScore = new PlayerScore(); //δημιουργεί το αντικείμενο PlayerScore

        if (cursor.moveToFirst()) {                                               //Αν υπάρχει κάποιο αποτέλεσμα στο query
            int i;
            cursor.moveToFirst();                                               //Πηγαίνει στην πρώτη εγγραφή
            for (i = 0; i < k; i++) {                                                   // Ανοίγει λούπα με σημείο λήξης τον ακέραιο που δώσαμε ως όρισμα (η λογική εξηγείται στο Scores Activity)

                cursor.moveToNext();                                            //Μεταφέρεται σειρά σειρά στην εγγραφή που θέλουμε
            }
            //αφού έχει φτάσει στην σωστή εγγραφή θέτει τα playerName και playerScore με βάση τα αποθηκευμένα δεδομένα κάθε στήλης της εγγραφής
            playerScore.set_playerName(cursor.getString(1));
            playerScore.set_playerScore(cursor.getInt(2));

        }

        cursor.close();
        db.close();
        return playerScore;                                                 // επιστρέφει το αντικείμενο playerScore

    }

}