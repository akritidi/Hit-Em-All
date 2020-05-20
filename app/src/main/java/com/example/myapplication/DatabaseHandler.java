package com.example.myapplication;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;


public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "scoresDB.db";
    private static final String TABLE_SCORES = "scores";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "playerName";
    private static final String COLUMN_SCORE = "playerScore";
    private static final String[] COLUMNS = {COLUMN_ID, COLUMN_NAME, COLUMN_SCORE};


    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        String CREATE_SCORES_TABLE = "CREATE TABLE " + TABLE_SCORES + "(" + COLUMN_ID + "INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME + "TEXT" + COLUMN_SCORE + "INTEGER" + ")";
        db.execSQL(CREATE_SCORES_TABLE);

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORES);
        onCreate(db);

    }

    public void addScore(PlayerScore playerScore) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, playerScore.get_playerName());
        values.put(COLUMN_SCORE, playerScore.get_playerScore());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_SCORES, null, values);
        db.close();

    }

    public PlayerScore[] highScores(){
        String query="SELECT * FROM "+TABLE_SCORES;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        PlayerScore[] highScoresTable= new PlayerScore[10];
        int i=0;
        if(cursor!=null){
            cursor.moveToFirst();
            for (i=0;i<cursor.getCount();i++){
                highScoresTable[i].set_playerName(cursor.getString(0));
                highScoresTable[i].set_playerScore(cursor.getInt(1));
                i++;
                cursor.moveToNext();
            }
            cursor.close();
        }

        db.close();
        return highScoresTable;
    }


}