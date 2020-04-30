package com.example.myapplication;

import android.app.Activity;
import android.app.Application;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicBoolean;

public class MyApplication extends Application   implements Application.ActivityLifecycleCallbacks {
    private static MyService myService;

     static int times = 0;

    boolean isBound = false;

    private static WeakReference<Activity>
            currentActivityReference;

    private static final AtomicBoolean applicationBackgrounded = new AtomicBoolean(true);

    private static final long INTERVAL_BACKGROUND_STATE_CHANGE = 750L;

    private void determineForegroundStatus() {
        if(applicationBackgrounded.get()){
            MyApplication.onEnterForeground();
            applicationBackgrounded.set(false);
        }
    }


    private void determineBackgroundStatus() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!applicationBackgrounded.get() &&
                        currentActivityReference == null) {

                    applicationBackgrounded.set(true);
                    onEnterBackground();
                }
            }
        }, INTERVAL_BACKGROUND_STATE_CHANGE);
    }

    @Override
    public void onCreate() {
        super.onCreate();


        this.registerActivityLifecycleCallbacks(this);
        Intent intent = new Intent(this,MyService.class);
        bindService(intent,conect, (Context.BIND_ALLOW_OOM_MANAGEMENT));
    }

    public static void onEnterForeground() {
        if (times != 0) {
            myService.setPlayer();
        }
    }
    public static void onEnterBackground()  {
     times ++;
        myService.pause();
    }

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
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        MyApplication.currentActivityReference =
                new WeakReference<>(activity);
        determineForegroundStatus();
      //myService.setPlayer();
    }


    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        MyApplication.currentActivityReference = null;
        determineBackgroundStatus();

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }
}
