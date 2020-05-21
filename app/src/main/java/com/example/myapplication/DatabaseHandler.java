package com.example.myapplication;

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;


public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "scoresDB.db";
    public static final String TABLE_SCORES = "scores";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "playerName";
    public static final String COLUMN_SCORE = "playerScore";



    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        String CREATE_SCORES_TABLE = "CREATE TABLE " + TABLE_SCORES + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NAME + " TEXT," + COLUMN_SCORE + " INTEGER" + ")";
        db.execSQL(CREATE_SCORES_TABLE);

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORES);
        onCreate(db);

    }

    public void addScore(PlayerScore playerScore) {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();
        values.put(COLUMN_NAME, playerScore.get_playerName());
        values.put(COLUMN_SCORE, playerScore.get_playerScore());
        db.insert(TABLE_SCORES, null, values);
        db.close();

    }

    public int getNumberOfDBRows(){
        int nor;
        String query="SELECT * FROM "+ TABLE_SCORES + " order by " + COLUMN_SCORE + " DESC " + " limit " + 10;
        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle")
        Cursor cursor = db.rawQuery(query,null);
        nor=cursor.getCount();

        return nor;
    }


    public PlayerScore highScores(int k){
        String query="SELECT * FROM "+ TABLE_SCORES + " order by " + COLUMN_SCORE + " DESC " + " limit " + 10;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        PlayerScore playerScore= new PlayerScore();

        if(cursor.moveToFirst()){
            int i;
            cursor.moveToFirst();
            for(i=0;i<k;i++){

                cursor.moveToNext();
            }

            playerScore.set_playerName(cursor.getString(1));
            playerScore.set_playerScore(cursor.getInt(2));





        }

        cursor.close();
        db.close();
        return playerScore;

    }


//    public PlayerScore[] highScores(){
//        String query="SELECT * FROM "+TABLE_SCORES;
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(query,null);
//        PlayerScore[] highScoresTable= new PlayerScore[10];
//        try {
//
//            cursor.moveToFirst();
//            int i=0;
//            while(cursor.moveToNext()){
//                    highScoresTable[i].set_playerName(cursor.getString(1));
//                    highScoresTable[i].set_playerScore(cursor.getInt(2));
//                    i++;
//                    cursor.moveToNext();
//                }
//
//        }
//        finally {
//            cursor.close();
//        }
//
//
//
//        db.close();
//        return highScoresTable;
//    }


}