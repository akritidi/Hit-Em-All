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
     * Εδώ  αρχεικοποιουνται οι listeners των δυο switch button που περιέχει αυη η δραστηριότητα,
     * ακόμα αποκτάται πρόσβαση στο service της εφαρμογής.
     * @param savedInstanceState
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

        /**
         * listener tou music button οπού αποθηκέυει την κατάσταση του κουμπιού μόνιμα μέχρι την επόμενη αλλαγή.
         * Καλέι τις αντοιστηχες μεθόδους απο την κλάσση myService για την ενεργοποείση η απενεργοποιείση τις μουσικής.
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
/**
 *  listener tou sound button οπού αποθηκέυει την κατάσταση του κουμπιού μόνιμα μέχρι την επόμενη αλλαγή.
 *    Καλέι τnν μεθόδο setMediaBool απο την κλάσση myService προκειμένου να είναι διαθέσιμοι η όχι οι ήχοι στο Game activity.
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
    /**Εδω αποκταμε προσβαση στο ηδη υπαρχων-τρεχον service*/
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
     * ¨Οταν καταστρέφεται το activity αποσυνδεέται το service για να μην υπάρξει διαρροη σύνδεσης.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connect);
    }
}
