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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    private MediaPlayer countdownSound,hitSound,missSound,popSound,jumpSound;
    private boolean countdownFinished,gameFinished,hitToastShown,missToastShown,run, nextMoleBool,hideMoleBool,backPressed;
    private Toast hitToast;
    private Toast missToast;
    private long backPressedTime;
    private Toast backToast;
    private ImageButton[] arrayOfButtons;
    private int lives,r,score,sumOfMoles,arrivalTime,hideTime,pauseCounter;
    private TextView countdownText,scoreText;
    private ImageView lives1, lives2, lives3;
    private Handler mHandler3;
    private ToggleButton pauseButton;
    static boolean soundsPlaying=true;

    /**
     * Σε αυτή την μέθοδο αρχικοποιούνται οι μεταβλητές που χρησιμοποιεί αυτο το activity, αρχικοποιουνταί οι listeners
     * των κουμπιών και οι Media Players.
     * @param savedInstanceState  Bundle .
     */


    @SuppressLint({"ClickableViewAccessibility", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        createButtons();



        pauseCounter=0;
        run = true;
        // αρχικοποίηση των μεταβλητών παιχνιδιού.
        arrivalTime=1000;
        hideTime=1000;
        score=0;
        sumOfMoles=0;
        lives=3;
        hitToastShown=false;
        missToastShown=false;
        backPressed=false;
        scoreText = findViewById(R.id.textScore);

        lives1=findViewById(R.id.imageView1);
        lives2=findViewById(R.id.imageView2);
        lives3=findViewById(R.id.imageView3);

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

        // Δημιουργία των Media Player, που είναι υπέυθυνοι για τους ήχους των moles
        hitSound = MediaPlayer.create(this,R.raw.hit);
        missSound = MediaPlayer.create(this,R.raw.miss);
        popSound = MediaPlayer.create(this,R.raw.pop);
        jumpSound = MediaPlayer.create(this,R.raw.jump);

        ConstraintLayout hitLayout = findViewById(R.id.conLayout);
        hitLayout.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("SetTextI18n")
            @Override                                               //Εμφάνιση μηνύματος Miss! αφού έχει τελειώσει το countdown όταν γίνεται ταπ σε σημείο που δεν υπάρχει κουμπί
            public boolean onTouch(View v, MotionEvent event) {
                if (countdownFinished && !gameFinished){
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
     * Μέθοδος, που καλείται απο την onCreate και αρχικοποιεί τα image buttons - moles και τους listener τους
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
     * Μέθοδος που παράγει τυχαία έναν αριθμό (εύρος 0-8), που αντιπροσωπέυει μία θέση στον πίνακα κουμπιών,
     * δηλαδή την θέση του imageButton-mole, που θα εμφανιστεί.
     * @return int την θέση.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public int randomMole(){
        int iMole;
        //noinspection OptionalGetWithoutIsPresent
        iMole = new Random().ints(0,8).limit(1).findFirst().getAsInt();
        return iMole; }

    /**
     * Μέθοδος που καλείται απο την onCreate, η οποία μέσω ενός handler εκτελεί με καθυστέρηση τεσσάρων δευτερολέπτων (ωστέ να τελειώσει πρώτα το countdown)
     * τον runnableCode, που έμφανίζει τα moles.
     */
    public void startRepeatingTask() {
        Handler mHandler=new Handler();
        mHandler.postDelayed(runnableCode, 4000);

    }

    /**
     * Η μέθοδος αυτή καλεί τον runnableCode 2  που κρύβει τα moles. (καθυστέρηση καθορισμένη από την τιμή της μεταβλητής hideTime)
     */
    public void hideMole(){
        hideMoleBool=true;
        Handler mHandler2=new Handler();
        mHandler2.postDelayed(runnableCode2,hideTime);
    }

    /**
     * Η μέθοδος αυτή καλεί τον runnableCode, που εμφανίζει τα moles. (καθυστέρηση καθορισμένη από την τιμή της μεταβλητής arrivalTime)
     */
    public void nextMole(){
        nextMoleBool =true;
        backPressed = false;
        mHandler3.postDelayed(runnableCode, arrivalTime);

    }

    /**
     * Ο κώδικας αυτός είναι υπεύθυνος για την τυχαία εμφάνιση των moles (μέσω της randomMole())
     * Στο τέλος καλεί την hideMole. Πριν εκτελεστεί ο κώδικας ελέγχει την
     * μεταβλητή run (που ορίζει αν είναι σε πάυση η δραστηριότητα).
     */
    private Runnable runnableCode=new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void run() {
            pauseButton.setVisibility(View.VISIBLE);
            Log.d("Handlers","Called on Main Thread");
            nextMoleBool =false;
          if (run) {
              r = randomMole();
              arrayOfButtons[r].setVisibility(View.VISIBLE);
              soundsMaker(popSound);
              sumOfMoles++;
              hideMole();
          }
        }
    };

    /**
     * O κώδικας αυτός κρύβει τα κουμπία - moles. Αν το activity είναι paused, το mole κρύβεται (ο παίκτης δεν χάνει ζωή, πρακτικά ο κώδικας λειτουργεί λες και αυτό το mole δεν εμφανίστηκε ποτέ.
     * Αν το activity δεν είναι paused, o κώδικας καλείται για να κρύψει μετά από ένα διάστημα εμφάνισης, το mole.
     * Ελέγχεται η μεταβλητή run (που ορίζει αν είναι σε πάυση η δραστηριότητα) και αν το άθροισμα των κουμπίων-moles, που εμφανίστηκαν, έιναι ίσο με το score.
     * Αν όχι αφαιρεί μια ζωή. Τέλος πριν καλέσει την nextMole, ελέγχει και αν το παιχνίδι έχει τελειώσει ή αν είναι σε πάυση.
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

    /**
     * Αυτή η μέθοδος αυξάνει το score κάθε φορά που ο παίκτης χτυπήσει το mole.
     * Ακόμα, κάθε φορά που το σκορ αυξάνεται κατά 5 μειώνεται ο χρόνος εμφάνισης και κάθε φορά που αυξάνεται κατα 10 μειώνεται χρόνος απόκρυψης.
     * Η μείωση των δύο αυτών χρόνων, που χρησιμοποιούνται κατά την κλήση των συναρτήσεων εμφάνισης και απόκρυψης των moles, αυξάνει την δυσκολία του παιχνιδιού σταδιακά
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
     * Μέθοδος, η οποία εμφανίζει τα text - νούμερα της αντίστροφης μέτρησης, πριν την έναρξη του παιχνιδιού.
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
     * Μέθοδος, που κατασκευάζει και εμφανίζει στον χρήστη το miss toast (αστοχία).
     * χρησιμοποιείται η μεταβλητή myY η οποία καθορίζει το yOffset στο setGravity του Toast. Η myY καθορίζεται αναλογικά με το ύψος της οθόνης της εκάστοτε συσκευής.
     */
    @SuppressLint("SetTextI18n")
    public void missToast(){
        missToast = new Toast(getApplicationContext());
        int myY=rightYOffset();
        missToast.setGravity(Gravity.CENTER|Gravity.TOP, 0, myY);

        TextView missTextView = new TextView(GameActivity.this);
        missTextView.setBackgroundColor(Color.TRANSPARENT);
        missTextView.setTextColor(Color.WHITE);
        missTextView.setTextSize(35);
        missTextView.setText(R.string.miss_toast);
        Typeface missTypeface = Typeface.create("serif",Typeface.BOLD);

        missTextView.setTypeface(missTypeface);
        missToast.setView(missTextView);
        missToast.show();
        missToastShown = true;

    }

    /**
     * Μέθοδος, που κατασκευάζει και εμφανίζει στον χρήστη το hit toast (χτύπημα).
     * χρησιμοποιείται η μεταβλητή myY η οποία καθορίζει το yOffset στο setGravity του Toast. Η myY καθορίζεται αναλογικά με το ύψος της οθόνης της εκάστοτε συσκευής.
     */
    @SuppressLint("SetTextI18n")
    public void hitToast(){
        hitToast = new Toast(getApplicationContext());
        int myY=rightYOffset();
        hitToast.setGravity(Gravity.CENTER|Gravity.TOP, 0, myY);

        TextView hitTextView = new TextView(GameActivity.this);
        hitTextView.setBackgroundColor(Color.TRANSPARENT);
        hitTextView.setTextColor(Color.WHITE);
        hitTextView.setTextSize(35);
        hitTextView.setText(R.string.hit_toast);
        Typeface hitTypeface = Typeface.create("serif",Typeface.BOLD);

        hitTextView.setTypeface(hitTypeface);
        hitToast.setView(hitTextView);
        hitToast.show();
        hitToastShown = true;
    }

    /**
     * Μέθοδος, που μετράει το ύψος της οθόνης της συσκευής και καθορίζει βάσει αυτού, το κατάλληλο (αναλογικά) ύψος για την εμφάνιση των toasts.
     */
    public int rightYOffset(){
        int myHeight;
        DisplayMetrics displayMetrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height=displayMetrics.heightPixels;
        myHeight=height/4;

        return myHeight;
    }

    /**
     * Listener για όλα τα κουμπιά - moles, η οποία καλεί την hitToast, αναπαράγει τον ήχο χτυπήματος, εξαφανίζοντας παράλληλα και το αντίστοιχο κουμπί.
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
     * Μέθοδος, που καλείται από τον runnableCode2-hideMole, όταν ένα mole εξαφανιστεί, χωρίς να πατηθεί.
     * Αφαιρεί μια ζωή την φορά και εξαφανίζει ένα ImageView - εικονιδιο καρδιά.
     * Όταν η ακέραια μεταβλητή lives μηδενιστεί, καλεί την openGameOverActivity
     */
    @SuppressLint("SetTextI18n")
    public void updateLives(){
        lives--;
        if(lives==2){
            lives3.setVisibility(ImageView.GONE);
        }
        else if(lives==1){
            lives2.setVisibility(ImageView.GONE);
        }
        else if(lives==0){
            lives1.setVisibility(ImageView.GONE);
            gameFinished=true;
            openGameOverActivity();
        }
    }

    /**
     * Μέθοδος, που καλείται όταν η ακέραια μεταβλητή lives μηδενιστεί,
     * Μέσω intent, περνάει το σκορ του παίκτη στην GameOverActivity, ξεκινάει την GameOverActivity και τερματίζει την GameActivity.
     */
    public void openGameOverActivity(){

        if(missToastShown){
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
     * Μέθοδος, που καλείται όταν το activity επιστρέφει, από το παρασκήνιο, στο προσκήνιο.
     * Το pause button πάει ξανά στην μορφή πάυσης, η μεταβλητή run γίνεται true προκειμένου να τρέχουν οι κώδικες
     * των hideMole-nextMole. Ακόμα, το αθροισμά των moles που εμφανίστηκαν γίνεται ίσο με το σκορ έτσι ώστε να μην
     * χάσει ο παίκτης ζωή, αν βγήκε κάποιο mole την ώρα του pause click.
     */
   @Override
   protected void onResume() {
       super.onResume();
       if (pauseCounter!=0){
        pauseButton.setChecked(true);
           run=true;
           sumOfMoles=score;
           backPressed=true;
       }
   }

    /**
     * Μέθοδος, που καλείται όταν η εφαρμογή είναι σε πάυση-not visible.
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
     * Θέτει την μεταβλητή για τους ήχους
     * @param x bool η οποία καθορίζει αν θα αναπαράγονται οι ήχοι των moles. Καλείται από το settings activity
     *          για την αντίστοιχη ρύθμιση.
     */
     public static void setMediaBool(boolean x){
        soundsPlaying=x;
     }

     /**
     * Getter για την μεταβλητή
     * @return soundsPlaying.
     */
     public static boolean getMediaBool(){
        return soundsPlaying;
     }

    /**
     * Ελέγχει αν η μεταβλητή soundsPlaying είναι true η όχι και αναπαράγει τον mediaPlayer ηχο που δέχεται.
     * @param player .
     */
    public  static  void soundsMaker(MediaPlayer player){
        if (getMediaBool()){ player.start();}
    }

    /**
     * Μέθοδος, που καλείται όταν το pause button έχει πατηθεί. Είναι υπέυθυνη για την πάυση του παιχνιδιού και την ενεργοποίηση του μετέπειτα.
     * @param view v.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void pauseClicked(View view) {
        boolean checked = ((ToggleButton)view).isChecked();
        if (checked){
            run=false;
            backPressed=true;

        }else {
            {
                  if (( nextMoleBool && !hideMoleBool )|| (!nextMoleBool && hideMoleBool)){
                      run=true;
                      sumOfMoles=score;
                  }else {

                      run = true;
                      sumOfMoles = score;
                      mHandler3.post(runnableCode);
                  }
            }
        }
    }

    /**
     * Μεθοδος, που ενεργοποιείται όταν ο χρηστης πατάει το backPress στην συσκευή του.
     * Στο πρώτο πάτημα εμφανίζει ένα κείμενο προειδοποίησης εξοδού απο το παιχνίδι και στο
     * δέυτερο πάτημα βγαίνει απο το activity (αν πατηθεί 2η φορά σε χρόνο μικρότερο των δύο δευτερολέπτων).
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        hitSound.stop();
        hitSound.release();
        missSound.stop();
        missSound.release();
        countdownSound.stop();
        countdownSound.release();
        popSound.stop();
        popSound.release();
        jumpSound.stop();
        jumpSound.release();
    }
}
