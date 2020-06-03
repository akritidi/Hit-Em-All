package com.example.myapplication;

import android.app.Activity;
import android.app.Application;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Η κλάση αυτή είναι κατασκευασμένη για ενημερώσεις μετάβασης,
 * απο το προσκήνιο στο παρασκήνιο και το αντιστροφό αυτής εφαρμογής.
 * Γνωρίζει την διάρκεια ζωής όλων των δραστηριοτήτων
 */
public class MyApplication extends Application   implements Application.ActivityLifecycleCallbacks {
    private static MyService myService;
    static int times = 0;

    private static WeakReference<Activity> currentActivityReference;

    private static final AtomicBoolean applicationBackgrounded = new AtomicBoolean(true);

    private static final long INTERVAL_BACKGROUND_STATE_CHANGE = 750L;

    /**
     * Αυτή η μέθοδος ελέγχει αν μια δραστηριότητα ήρθε στο προσκήνιο.
     * Ελέγχει την λογική ατομική applicationBackgrounded η οποία είναι αληθής, αν το activity ήρθε στο
     * προσκήνιο απο το παρασκήνιο ή αν δημιουργήθηκε τώρα. Προκειμένου να μην παραχθεί σφάλμα.
     */
    private void determineForegroundStatus() {
        if(applicationBackgrounded.get()){
            SharedPreferences sharedPrefs  = getSharedPreferences("state", MODE_PRIVATE);
            boolean musicState = sharedPrefs.getBoolean("music", true);
            MyApplication.onEnterForeground(musicState);
            applicationBackgrounded.set(false);


        }
    }

    /**
     * Αντίστοιχα εδώ ελέγχεται αν η δραστηριότητα εισέρχεται στο παρασκήνιο, μόνο αν πριν ήταν στο προσκήνιο.
     * Ένας handler τρέχει τον παρκάτο κώδικα σε χρονική καθυστέρηση 0.75 δευτερόλεπτα. Επαληθέυει αν η κατάσταση μετάβηκε
     * στο παρασκήνιο με τις δύο μεταλητες applicationBackgrounded currentActivityReference που είναι η αναφορά
     * μας για την κατάσταση. Αν δεν περάσει αυτή η συνθήκη σημαίνει ότι ένα άλλο activity δημιουργήθηκε, άρα δεν
     * είναι η εφαρμογή μας στο παρασκήνιο.
     */
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
        bindService(intent,connect, (Context.BIND_ALLOW_OOM_MANAGEMENT));
    }

    /**
     * Καλείται όταν ξέρουμε ότι η κατάσταση της εφαρμογής μεταβαίνει στο προσκήνιο.
     * Εδώ δημιουργούμε την μουσική απο την κλάση service.
     */
    public static void onEnterForeground(boolean m) {
        if ( times !=0 && m) {
            myService.setPlayer();
        }
    }
    /**
     * Καλείται όταν ξέρουμε ότι η κατάσταση της εφαρμογής είναι στο παρασκήνιο.
     * Εδώ σταματάμε την μουσική απο την κλάση service.
     */
    public static void onEnterBackground()  {
     times ++;
        myService.stop();

    }

    /**
     * Μέθοδος για την απόκηση πρόσβασης στην υπηρεσία της εφαρμογης.
     */
    private final ServiceConnection connect = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyService.LocalBinder binder = (MyService.LocalBinder)service;
            myService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };



    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    /**
     * Η μέθοδος αυτή καλείται όταν μία δραστηριότητα μπαίνει στο προσκήνιο,
     * αποθηκεύουμε μία αναφορά της τρέχουσας δραστηριότητας.
     * @param activity η τρέχουσα δραστηριότητα.
     */
    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        MyApplication.currentActivityReference =
                new WeakReference<>(activity);
        determineForegroundStatus();

    }

    /**
     * Η μέθοδος καλείται όταν η δραστηριότητα εισέρχεται στό προσκήνιο,
     * για αυτό θέτουμε την αναφορά σε κενή.
     * @param activity η τρέχουσα δραστηριότητα.
     */
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
