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
   Toast swict;
   MyService myService;
   boolean isBound = false;
    SharedPreferences sharedPrefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = new Intent(this,MyService.class);
    bindService(intent,conect, (Context.BIND_ALLOW_OOM_MANAGEMENT));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        music =  findViewById(R.id.switch1);
        sounds=findViewById(R.id.switch2);
       sharedPrefs = getSharedPreferences("state", MODE_PRIVATE);
        music.setChecked(sharedPrefs.getBoolean("music", true));
        sounds.setChecked(sharedPrefs.getBoolean("sht", true));

        music.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked) {
                 myService.stop();
                    swict = Toast.makeText(getBaseContext(), "The music has turned off ", Toast.LENGTH_SHORT);
                    swict.show();



                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putBoolean("music", false);
                    editor.apply();
                    music.setChecked(false);
                }else if (isChecked){
                    myService.setPlayer();
                    swict = Toast.makeText(getBaseContext(), "The music has turned on ", Toast.LENGTH_SHORT);
                    swict.show();
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putBoolean("music", true);
                    editor.apply();
                    music.setChecked(true);
                }
            }
        });

        sounds.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){

                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putBoolean("sht", false);
                    editor.apply();
                    sounds.setChecked(false);

                    GameActivity.setMediaBool(false);

                }else if (isChecked){

                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putBoolean("sht", true);
                    editor.apply();
                    sounds.setChecked(true);
                    GameActivity.setMediaBool(true);

                }
            }
        });

         }
      // εδω αποκταμε προσβαση στο ηδη υπαρχων-τρεχον service
    private ServiceConnection conect = new ServiceConnection() {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conect);
    }
}
