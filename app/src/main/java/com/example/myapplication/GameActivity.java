package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    private MediaPlayer countdownSound,hitSound,missSound,popSound,jumpSound;
    private boolean countdownFinished,gameFinished,hitToastShown,missToastShown,run,nextMolebool,hideMoleBool,backPressed ;
    private Toast hitToast;
    private Toast missToast;
    private long backPressedTime;
    private Toast backToast;
    private ImageButton[] arrayOfButtons;
    private int lives,r,score,sumOfMoles,arrivalTime,hideTime,pauseCounter;
    private TextView countdownText,scoreText,livesText,livesText2,livesText3;
    private Handler mHandler3;
    private ToggleButton pauseButton;
    static boolean soundsPlaying=true;



    /**
     * Σε αυτή την μέθοδο αρχικοποιούνται οι μεταβλητές που χρησημοποιεί αυτο το activity,αρχηκοποιουνταί οι listeners
     * των κουμπιών και οι mediaplayers.
     * @param savedInstanceState
     */
    @SuppressLint({"ClickableViewAccessibility", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        createButtons();



        pauseCounter=0;
        run = true;
        // αρχικοποιηση των μεταβλητών παιχνιδιού.
        arrivalTime=1000;
        hideTime=1000;
        score=0;
        sumOfMoles=0;
        lives=3;
        hitToastShown=false;
        missToastShown=false;
        backPressed=false;
        scoreText = findViewById(R.id.textScore);


        livesText = findViewById(R.id.textLives);
        livesText2 = findViewById(R.id.textLives2);
        livesText3 = findViewById(R.id.textLives3);

        gameFinished = false;
        countdownFinished = false;
        countdownSound = MediaPlayer.create(this, R.raw.countdown_sound);
        countdownText = findViewById(R.id.textCountdown);
        Timer countdown = new Timer();
        soundsMaker(countdownSound);
        countdown.schedule(new TimerTask() {
            @Override
            public void run() {
                updateCountdown();
            }
        },1000);
        // η μέθοδος αυτή καλείται και ξεκινάει το παιχνίδι.
        startRepeatingTask();
        // Δημιουργία των mediaPlayer που είναι υπέυθυνοι για τους ήχους των moles

        hitSound = MediaPlayer.create(this,R.raw.hit);
        missSound = MediaPlayer.create(this,R.raw.miss);
        popSound = MediaPlayer.create(this,R.raw.pop);
        jumpSound = MediaPlayer.create(this,R.raw.jump);
//Εμφάνιση μηνύματος Miss! αφού έχει τελειώσει το countdown όταν γίνεται ταπ σε σημείο που δεν υπάρχει κουμπί ενεργό.
        ConstraintLayout hitLayout = findViewById(R.id.conLayout);
        hitLayout.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("SetTextI18n")
            @Override                                               //Εμφάνιση μηνύματος Miss! αφού έχει τελειώσει το countdown όταν γίνεται ταπ σε σημείο που δεν υπάρχει κουμπί
            public boolean onTouch(View v, MotionEvent event) {
                if (countdownFinished){
                    if (event.getAction() == MotionEvent.ACTION_DOWN){
                        missToast();
                        soundsMaker(missSound);
                        return true;
                    }
                }
                return false;
            }
        });
         mHandler3=new Handler();
        pauseButton=findViewById(R.id.toggleButton);
        pauseButton.setVisibility(View.INVISIBLE);
    }
    /**
     * μΜεθοδος που καλείται απο την onCreate και αρχικοποιιταί τα image buttons- moles και ο listener τους
     * ακόμα δημιουργείται ένας πίνακας από κουμπία.
     */
    public void createButtons(){
        ImageButton imageButton1 = findViewById(R.id.imageButton1);
        ImageButton imageButton2 = findViewById(R.id.imageButton2);
        ImageButton imageButton3 = findViewById(R.id.imageButton3);
        ImageButton imageButton4 = findViewById(R.id.imageButton4);
        ImageButton imageButton5 = findViewById(R.id.imageButton5);
        ImageButton imageButton6 = findViewById(R.id.imageButton6);
        ImageButton imageButton7 = findViewById(R.id.imageButton7);
        ImageButton imageButton8 = findViewById(R.id.imageButton8);
        ImageButton imageButton9 = findViewById(R.id.imageButton9);
        imageButton1.setOnClickListener(this);
        imageButton2.setOnClickListener(this);
        imageButton3.setOnClickListener(this);
        imageButton4.setOnClickListener(this);
        imageButton5.setOnClickListener(this);
        imageButton6.setOnClickListener(this);
        imageButton7.setOnClickListener(this);
        imageButton8.setOnClickListener(this);
        imageButton9.setOnClickListener(this);

        arrayOfButtons = new ImageButton[9];
        arrayOfButtons[0] = imageButton1;
        arrayOfButtons[1] = imageButton2;
        arrayOfButtons[2] = imageButton3;
        arrayOfButtons[3] = imageButton4;
        arrayOfButtons[4] = imageButton5;
        arrayOfButtons[5] = imageButton6;
        arrayOfButtons[6] = imageButton7;
        arrayOfButtons[7] = imageButton8;
        arrayOfButtons[8] = imageButton9;

        SharedPreferences sharedPrefs  = getSharedPreferences("state", MODE_PRIVATE);
        boolean musicState = sharedPrefs.getBoolean("sht", true);
      setMediaBool(musicState);
    }
    /**
     * Μέθοδος που παράγη με τυχαιότητα έναν αριθμό 0-8 που αντιπροσωπέβει μία θέση στον πίνακα κουμπιών,
     * δηλαδή την ΄θεση του  imageButton-mole που θα εμφανιστεί.
     * @return int την θέση.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public int randomMole(){
        int iMole;
        iMole = new Random().ints(0,8).limit(1).findFirst().getAsInt();
        return iMole; }
    /**Μέθοδος που καλείται απο την onCreate,με καθστέρηση τεσσάρων δευτερολέπτων κάλει τον
     * runnableCode που έμφανίζει τα moles.
     */
    public void startRepeatingTask() {
        Handler mHandler=new Handler();
        mHandler.postDelayed(runnableCode, 4000);

    }
    /**Η Μέθοδος αυτή καλή τον runnableCode 2  που κρύβει τα moles .
     * @param r
     */
    public void hideMole(int r){
        hideMoleBool=true;
        Handler mHandler2=new Handler();
        mHandler2.postDelayed(runnableCode2,hideTime);
    }
    /**Η Μέθοδος αυτή καλή τον runnableCode   που εμφανίζει τα moles .
     */

    public void nextMole(){
        nextMolebool =true;
        backPressed = false;
        mHandler3.postDelayed(runnableCode, arrivalTime);

    }

    /**
     * Ο κώδικας αυτός είναι υπέυθηνος για την έμφανισει των moles,από την randomMole()  έχουμε το κουμπί
     *που θα εμφανιστεί,στο τέλος καλεί την hideMole.Πριν εκτελεστεί ο κώδικας ελέγχει την
     * μεταβλητή run (που ορίζει αν είναι σε πάυση η δραστηριότητα ).
     */
    private Runnable runnableCode=new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void run() {
            pauseButton.setVisibility(View.VISIBLE);
            Log.d("Handlers","Called on Main Thread");
            nextMolebool=false;
          if (run) {
              r = randomMole();
              arrayOfButtons[r].setVisibility(View.VISIBLE);
              soundsMaker(popSound);
              sumOfMoles++;
              hideMole(r);
          }
        }
    };
    /**
     * O κώδικας που κρύβει τα κουμπία,ήτε είναι pause το activity ήτε όχι,κρύβουμε το
     * κουμπί που ήταν πριν ανοικτό,προκειμένου να μην μήνει αν ο παίκτης πατησει το  pause.
     * O κώδικας την συνέχεια πέρα απο την μεταβλητή run (που ορίζει αν είναι σε πάυση η δραστηριότητα )
     * ελέγχει και αν το άθροισμα των κουμπίων-moles που εμφανίστηκαν έιναι ίσω με το score.Αν
     * όχι αφερεί μια ζωή.Τέλος πριν καλέσει την nextMole ελέγχει και αν το παιχνίδι έχει τελειώσει η αν είναι σε πάυση.
     */
    private Runnable runnableCode2= new Runnable() {
        @Override
        public void run() {
            Log.d("Handlers","Called on Main Thread");
            arrayOfButtons[r].setVisibility(View.GONE);
            hideMoleBool=false;

            if(score<sumOfMoles && run){
                soundsMaker(jumpSound);
                updateLives();
                sumOfMoles=score;
            }
            if (!gameFinished && run){
                nextMole();
            }
        }
    };
    /**Αυτή η μέθοδος αυξάνει το score κάθε φορά που ένα κουμπί έχει πατηθεί.
     * Ακόμα κάθε φόρα που το σκορ αυξάνεται κατά 5 μειώνεται ο χρόνος εμφάνισεις και
     * όταν αυξάνεται κατα 10 μειώνεται χρόνος απόκρυψης.
     * Αυτοί οι ΄χρονοι αντιπροσοπέυουν την δυσκολεία,αφού με αυτούς τους χρόνους καλούνται οι συναρτησεις
     * εμφάνισεις και απόκρυψης
     */
    @SuppressLint("SetTextI18n")
    public void updateScore(){
        score++;
        if(score%5==0 && arrivalTime>0){
            arrivalTime=arrivalTime-125;
        }
        if(score%10==0 && hideTime>500){
            hideTime=hideTime-125;
        }
        scoreText.setText(Integer.toString(score));
    }
    /**
     * Μέθοδος η οποία εμφανίζει τα text- τα νούμερα τις εκκινησης 3-2-1.
     */
    public void updateCountdown(){
        countdownText.setText("2");
        try {
            Thread.sleep(1000);
            countdownText.setText("1");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(1000);
            countdownText.setText(null);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        countdownFinished=true;
    }
    /**
     * Μέθοδος που κατασκευάζει το miss-αστοχεια toast και το εμφανίζει.
     */
    @SuppressLint("SetTextI18n")
    public void missToast(){
        missToast = new Toast(getApplicationContext());
        missToast.setGravity(Gravity.CENTER|Gravity.TOP, 0, 550);

        TextView missTextView = new TextView(GameActivity.this);
        missTextView.setBackgroundColor(Color.TRANSPARENT);
        missTextView.setTextColor(Color.WHITE);
        missTextView.setTextSize(35);
        missTextView.setText(R.string.miss_toast);
        Typeface missTypeface = Typeface.create("serif",Typeface.BOLD); //or familyName roman

        missTextView.setTypeface(missTypeface);
        missToast.setView(missTextView);
        missToast.show();
        missToastShown = true;

    }
    /**
     * Μέθοδος που κατασκευάζει το hit-χτύπημα και το εμφανίζει.
     */
    @SuppressLint("SetTextI18n")
    public void hitToast(){
        hitToast = new Toast(getApplicationContext());
        hitToast.setGravity(Gravity.CENTER|Gravity.TOP, 0, 550);

        TextView hitTextView = new TextView(GameActivity.this);
        hitTextView.setBackgroundColor(Color.TRANSPARENT);
        hitTextView.setTextColor(Color.WHITE);
        hitTextView.setTextSize(35);
        hitTextView.setText(R.string.hit_toast);
        Typeface hitTypeface = Typeface.create("serif",Typeface.BOLD); //or familyName roman

        hitTextView.setTypeface(hitTypeface);
        hitToast.setView(hitTextView);
        hitToast.show();
        hitToastShown = true;
    }
    /**
     * Listener για όλα τα κουμπιία-moles η οποία καλει την hitToast,αναπαράγει τον ήχο χτυπήματος εξαφανίζοντας και  το αντιστοιχο  κουμπι.
     * Επίσης καλεί την upDateScore.
     * @param v View
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        if(countdownFinished && run){
            hitToast();
            soundsMaker(hitSound);

            ImageButton btn = findViewById(v.getId());
            btn.setVisibility(View.GONE);
            updateScore();
        }
    }

    /**
     * Μέθοδος που καλείται από τον runableCode2-hideMole όταν ένα κουμπί εξαφανιστεί χώρις να πατηθεί.
     * Αφαιρεί μια ζωή την φορά και εξαφανίζει ένα liveText-εικονηδιο καρδιά.
     */
    @SuppressLint("SetTextI18n")
    public void updateLives(){
        lives--;
        if(lives==2){
            livesText3.setVisibility(View.GONE);
        }
        else if(lives==1){
            livesText2.setVisibility(View.GONE);
        }
        else if(lives==0){
            livesText.setVisibility(View.GONE);
            gameFinished=true;
            openGameOverActivity();                      //telos paixnidiou
        }
    }
    /**Καλείται όταν οι ζωές είναι 0,
     * καλείται το GameOverActivity και καταστρέφει αυτό.
     */

    public void openGameOverActivity(){

        if(missToastShown){                           //gia na mhn uparxei bug an xaseis xwris na arxikopoih8oun ta toasts
            missToast.cancel();
        }
        if(hitToastShown){
            hitToast.cancel();
        }
        Intent i = new Intent(this,GameOverActivity.class);
        i.putExtra("SCORE",score);
        startActivity(i);
        finish();
    }
    /**
     * μέθοδος που καλείται όταν το activity από το παρασκήνιο επιστρέφει στο προσκήνιο.
     * Το pause button πάει στην μορφή πάυσης, η μεταβλητή run γίνεται true προκειμένου να τρέχουν οι κώδικες
     * των hidemole-nextMole.Ακόμα το αθροισμά των moles που εμφανίστηκαν γίνεται ίσο με το σκορ έτσι ώστε  να μην
     * καταμετρηθει αν βγηκε κάποιο την ώρα του pause click.
     */
   @Override
   protected void onResume() {
       super.onResume();
       if (pauseCounter!=0){
        pauseButton.setChecked(true);
           run=true;
           sumOfMoles =score;
           backPressed=true;
       }
   }
    /**
     * H μέθοδος αυτή καλείται όταν η εφαρμογή είναι σε πάυση-not visible.
     * Σταματάει τους ήχους και τα toasts.
     */
    @Override
    protected void onPause() {
        pauseCounter++;
        super.onPause();
        run=false;
        missSound.stop();
        hitSound.stop();
        popSound.stop();
        countdownSound.stop();

        if(missToastShown){
            missToast.cancel();
        }
        if(hitToastShown){
            hitToast.cancel();
        }

        try {
            missSound.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            hitSound.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            popSound.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * θέτει την μεταβλητή για τους ήχους
     * @param x bool η οποία καθορίζει αν θα αναπαράγωνται οι ήχοι των moles.Καλείται από το settings activity
     *          για την αντοιστιχη ρύθμιση.
     */
     public static void setMediaBool(boolean x){
        soundsPlaying=x;
     }

    /**
     * Getter για την μεταβγλητή
     * @return soundPlaying.
     */
     public static boolean getMediaBool(){
        return soundsPlaying;
     }
    /**Ελέγχει αν η μεταβλητή soundsPlaying είναι true η όχι και αναπαράγει τον
     * @param player mediaPlayer ηχο που δέχεται.
     */
    public  static  void soundsMaker(MediaPlayer player){
        if (getMediaBool()){ player.start();}
    }
    /**
     * Μέθοδος που καλείται όταν το pause button έχει πατηθεί.Είναι υπέυθηνη για την πάυση του παιχνιδιού και την ενεργοποιησή του έπειτα.
     * @param view
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void pauseClicked(View view) {
        boolean checked = ((ToggleButton)view).isChecked();
        if (checked){
            run=false;// αποτρέπουμε τους runnableCodes να μπουν στο κύριο τμήμα τους.
            backPressed=true;

        }else {
            {//έλεγχος για το αν κάποιος handler από τους δύο έχει θέσει  τον κώδικα του σε καθηστέρηση και έπειτα σε εκτέλεση
                  if (( nextMolebool && !hideMoleBool )|| (!nextMolebool && hideMoleBool)){
                      run=true;
                      sumOfMoles=score;
                  }else {// άμα δεν θα καλεστεί η δεν έχει καλεστει η nextMole, hideMole τότε καλείται ρητά π κώδικα τις εμφάνισεις-nextMole.

                      run = true;
                      sumOfMoles = score;
                      mHandler3.post(runnableCode);
                  }
            }
        }
    }
    /**
     * Μεθοδος που ενεργοποιείται όταν ο χρηστης πατάει το backPress στην συσκευή του.
     * Στο πρώτο πάτημα εμφανίζει ένα κείμενο της προειδοποιησης εξοδού απο το παιχνίδι,στο
     * δέυτερο βγαίνει απο το activity αν πατηθεί σε χρόνο μικρότερο των δύο δευτερολέπτων.
     */
    @Override
    public void onBackPressed(){

        if (backPressedTime + 2000 > System.currentTimeMillis()){
            backToast.cancel();

            gameFinished=true;
            if(missToastShown){
                missToast.cancel();
            }
            if(hitToastShown){
                hitToast.cancel();
            }
            finish();
        }
        else{
            if(!backPressed){
                pauseButton.performClick();
                backPressed = true;
            }
            backToast = Toast.makeText(getBaseContext(),R.string.exit_game_toast,Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime=System.currentTimeMillis();
    }
}
