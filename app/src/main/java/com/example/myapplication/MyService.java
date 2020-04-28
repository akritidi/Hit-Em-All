package com.example.myapplication;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import java.io.IOException;
import java.text.Collator;

import static java.lang.Integer.parseInt;

public class MyService extends Service {
    private Collator LocalBroadcastManager;
   private boolean playing = true;
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

     public void stop(){
         if(player.isPlaying()){
             player.pause();
             player.release();
         }

     }

    @Override
    public void onDestroy() {
        super.onDestroy();
        player.stop();
    }


    }
