package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.CompoundButton;

import android.widget.Switch;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity  {
  Switch music,sounds;
   Toast switch1,s;
   MyService myService;
   boolean isBound = false;
    SharedPreferences sharedPrefs;
    /**
     * Εδώ  αρχικοποιούνται οι listeners των δυο switch button, που περιέχει αυτή η δραστηριότητα.
     * Ακόμα αποκτάται πρόσβαση στο service της εφαρμογής.
     * @param savedInstanceState .
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = new Intent(this,MyService.class);
        bindService(intent, connect, (Context.BIND_ALLOW_OOM_MANAGEMENT));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        music =  findViewById(R.id.switch1);
        sounds=findViewById(R.id.switch2);
        sharedPrefs = getSharedPreferences("state", MODE_PRIVATE);
        music.setChecked(sharedPrefs.getBoolean("music", true));
        sounds.setChecked(sharedPrefs.getBoolean("sht", true));

        /*
          Listener του music button, όπου αποθηκεύεται η κατάσταση του κουμπιού μόνιμα, μέχρι την επόμενη αλλαγή.
          Καλεί τις αντίστοιχες μεθόδους απο την κλάση myService για την ενεργοποίηση ή απενεργοποίηση της μουσικής.
         */
        music.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked) {
                 myService.stop();
                    switch1 = Toast.makeText(getBaseContext(), R.string.mof, Toast.LENGTH_SHORT);
                    switch1.show();
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putBoolean("music", false);
                    editor.apply();
                    music.setChecked(false);

                }else {
                    myService.setPlayer();
                    switch1 = Toast.makeText(getBaseContext(), R.string.mon, Toast.LENGTH_SHORT);
                    switch1.show();
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putBoolean("music", true);
                    editor.apply();
                    music.setChecked(true);
                }
            }
        });

        /*
          Listener του sound button, όπου αποθηκεύεται η κατάσταση του κουμπιού μόνιμα, μέχρι την επόμενη αλλαγή.
          Καλεί τnν μεθόδο setMediaBool, απο την κλάση myService ,προκειμένου να είναι διαθέσιμοι -ή όχι- οι ήχοι στο Game activity.
        */
        sounds.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){

                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putBoolean("sht", false);
                    editor.apply();
                    sounds.setChecked(false);
                    GameActivity.setMediaBool(false);
                    s=Toast.makeText(getBaseContext(),R.string.sof,Toast.LENGTH_SHORT);
                    s.show();

                }else {

                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putBoolean("sht", true);
                    editor.apply();
                    sounds.setChecked(true);
                    GameActivity.setMediaBool(true);
                    s=Toast.makeText(getBaseContext(),R.string.son,Toast.LENGTH_SHORT);
                    s.show();
                }
            }
        });

         }

    /**
     * Εδω αποκτάται πρόσβαση στο ήδη υπάρχον-τρέχον service
     */
    private ServiceConnection connect = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyService.LocalBinder binder = (MyService.LocalBinder)service;
            myService = binder.getService();
            isBound=true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    /**
     * Όταν καταστρέφεται το activity, αποσυνδέεται το service, για να μην υπάρξει διαρροη σύνδεσης.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connect);
    }
}
