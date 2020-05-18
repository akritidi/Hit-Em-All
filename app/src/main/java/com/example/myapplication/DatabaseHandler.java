package com.example.myapplication;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.text.Editable;


public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "scoresDB.db";
    public static final String TABLE_SCORES = "scores";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "playerName";
    public static final String COLUMN_SCORE = "playerScore";
    private static final String[] COLUMNS = {COLUMN_ID, COLUMN_NAME, COLUMN_SCORE};


    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        String CREATE_SCORES_TABLE = "CREATE TABLE " + TABLE_SCORES + "(" + COLUMN_ID + "INTEGER PRIMARY KEY," + COLUMN_NAME + "TEXT" + COLUMN_SCORE + "INTEGER" + ")";
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

    public String[] bestScores() {
        String query = "SELECT COLUMN_NAME, COLUMN_SCORE FROM " + TABLE_SCORES + "ORDER BY" + COLUMN_SCORE + "DESC" + "GROUP BY" + COLUMNS + "LIMIT 10";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        int i = 0;
        String[] data = new String[cursor.getCount()];
        while (cursor.moveToNext()) {
            data[i]=cursor.getString(1);
            i++;
        }
        cursor.close();
        db.close();
        return data;
    }
}