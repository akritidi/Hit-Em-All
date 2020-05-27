package com.example.myapplication;

import android.text.Editable;

class PlayerScore {
    private int _id;
    private String _playerName;
    private int _playerScore;

    /**Constructors αντικειμένων. Ένας άδειος-default και ένας που δέχεται ως ορίσματα το όνομα και το σκορ του παίκτη*/
    PlayerScore(){}

    PlayerScore(Editable playerName, int playerScore){
        this._playerName=playerName.toString();
        this._playerScore=playerScore;

    }

    /**
     *    Μέθοδοι για set and get του id, του ονόματος και του σκορ του παίκτη / εγγραφής
     * @param id
     */
    void set_id(int id){ this._id=id; }

    int get_id(){
        return this._id;
    }

    void set_playerName(String playerName){
        this._playerName=playerName;
    }

    String get_playerName(){
        return this._playerName;
    }


    void set_playerScore(int playerScore){
        this._playerScore=playerScore;
    }

    int get_playerScore(){ return this._playerScore;  }
}

