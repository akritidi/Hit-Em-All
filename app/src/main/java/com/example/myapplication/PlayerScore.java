package com.example.myapplication;

public class PlayerScore {
    private int _id;
    private String _playerName;
    private int _playerScore;

    public PlayerScore(){}

    public PlayerScore(String playerName, int playerScore){
        this._playerName=playerName;
        this._playerScore=playerScore;

    }
    //methods that set and return the players name and score. Used for accessing database.

    public void set_playerName(String playerName){
        this._playerName=playerName;
    }

    public String get_playerName(){
        return this._playerName;
    }


    public void set_playerScore(int playerScore){
        this._playerScore=playerScore;
    }

    public int get_playerScore(){
        return this._playerScore;
    }
}

