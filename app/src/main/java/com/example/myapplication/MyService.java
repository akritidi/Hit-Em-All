package com.example.myapplication;

import android.app.Service;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;


import java.io.IOException;
import java.text.Collator;



public class MyService extends Service {
    private Collator LocalBroadcastManager;

    public MyService() {

    }
  MediaPlayer player ;
    private final IBinder binder=new LocalBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        player = MediaPlayer.create(this, R.raw.song);
        player.setLooping(true);
        player.start();


    }

    public class LocalBinder extends Binder {
        MyService getService(){
            return MyService.this;
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void setPlayer() {

        if (!player.isPlaying() || player == null) {

            player.start();

        }

    }

        public void pause()  {

            player.pause();

        }
  // just  in case,dont delete thise!
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
