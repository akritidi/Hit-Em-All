package com.example.myapplication;

import android.app.Service;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;


import java.io.IOException;
import java.text.Collator;

/**
 * Σε αυτήν την κλάσση υλοποιεται το service,η χρήση του είναι η αναπαραγωγή της μουσικης
 * σε όλα τα activities.
 */

public class MyService extends Service {
    private Collator LocalBroadcastManager;

    public MyService() {

    }
  MediaPlayer player ;

    private final IBinder binder=new LocalBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        boolean mState;
   createPLayer();
        SharedPreferences sharedPrefs  = getSharedPreferences("state", MODE_PRIVATE);
        mState=sharedPrefs.getBoolean("music",true);
        if (!mState){
            stop();
        }
    }

    /**
     * Εδώ δημηουργείται ο player και αρχίζει η αναπαραγωγή.
     */
    public void createPLayer(){
        player = MediaPlayer.create(this, R.raw.song);
        player.setLooping(true);

        player.start();

    }
    /**Η μέθοδος αυτή μας επιτρέπει την προσβασή από άλλες κλάσεις.
     */
    public class LocalBinder extends Binder {
        MyService getService(){
            return MyService.this;
        }
    }

    /**H μέθοδος δέχεται
     * @param intent ΙΝΤΕΝΤ και επιστρέφει
     * @return IBinder μεταβλητη.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    /**
     *  Η μέθοδος αυτή θέτει σε αναπαραγωγή τον player και αν έχει προηγηθεί καταστραφεί του,τότε καλει την createPlayer.
     */
    public void setPlayer() {

        if (!player.isPlaying() || player == null) {

            player.start();

        }

    }

        public void pause()  {

            player.pause();

        }

    /**
     * H μέθοδος αυτή σταματάει την μουσικη αφου την κανει παυση,
     * έπειτα την προετοιμάζει για αναπαραγωγή ξανά
     */
     public void stop(){
         if(player.isPlaying()){
             player.pause();

             player.stop();
             try {
                 player.prepare();
             } catch (IOException e) {
                 e.printStackTrace();
             }
         }

     }

    @Override
    public void onDestroy() {
        super.onDestroy();

        player.stop();
    }


    }
