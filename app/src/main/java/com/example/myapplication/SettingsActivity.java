package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity  {
  Switch music,sounds;
   Toast swict;
   MyService myService;
   boolean isBound = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = new Intent(this,MyService.class);
    bindService(intent,conect, (Context.BIND_ALLOW_OOM_MANAGEMENT));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        music =  findViewById(R.id.switch1);
        music.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked) {
                 myService.pause();
                    swict = Toast.makeText(getBaseContext(), "The music has turned off ", Toast.LENGTH_SHORT);
                    swict.show();
                }else if (isChecked){
                    myService.setPlayer();
                    swict = Toast.makeText(getBaseContext(), "The music has turned on ", Toast.LENGTH_SHORT);
                    swict.show();
                }


            }
        });
        sounds=findViewById(R.id.switch2);
        sounds.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){

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




}
