package com.example.catchtheball;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;



public class GameActivity extends AppCompatActivity implements View.OnClickListener {


    //region Definitions

    ImageView[] ballImages;
    TextView scoreText, lifeText;
    ConstraintLayout constraintLayout;
    private DisplayMetrics displayMetrics;
    private Toast backToast;
    Timer timer;
    ImageView pauseImage;
    TimerTask UpdateBallTask;


    public static int coinSound, gameOverSound, bonusSound, advantageSound,lifePointSound;
    int life = 2 , ballSpeed;
    int totalClicks = 0;
    int increaseSpeed = 0;

    long score = 0;

    boolean clicked = false, backgroundSoundChecked = false;

    boolean alertDialogCheck = false;

    boolean totalClickFlag = false;

    private long backPressedTime;


    private Random r;

    public static boolean coinSoundCheck = false, bonusSoundCheck = false,
            gameOverSoundCheck = false, advantageSoundCheck = false;

    public static SoundPool soundPool, soundPoolSecond, soundPoolThird, soundPoolForth;


    SoundPool.Builder soundPoolBuilder;
    AudioAttributes attributes;
    AudioAttributes.Builder attributesBuilder;

    public static MediaPlayer backgroundPlayer;

    AlertDialog.Builder dialogBuilder;
    AlertDialog alertDialog;


    //endregion


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //region Objects

        ballImages = new ImageView[8];

        constraintLayout = (ConstraintLayout) findViewById(R.id.constraintlayout);

        ballImages[0] = findViewById(R.id.CollectPointBall);
        ballImages[1] = findViewById(R.id.collectPointBall3);
        ballImages[2] = findViewById(R.id.collectPointBall4);
        ballImages[3] = findViewById(R.id.collectPointBall5);
        ballImages[4] = findViewById(R.id.RedBall);
        ballImages[5] = findViewById(R.id.BlueBall);
        ballImages[6] = findViewById(R.id.greenBall);
        ballImages[7] = findViewById(R.id.RedBall2);
        timer = new Timer();
        pauseImage = findViewById(R.id.pauseImage);
        scoreText = findViewById(R.id.Points);
        lifeText = findViewById(R.id.lifeTextView);

        attributesBuilder = new AudioAttributes.Builder();
        attributesBuilder.setUsage(AudioAttributes.USAGE_GAME);
        attributesBuilder.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION);
        attributes = attributesBuilder.build();

        soundPoolBuilder = new SoundPool.Builder();
        soundPoolBuilder.setAudioAttributes(attributes);
        soundPool = soundPoolBuilder.build();
        soundPoolSecond = soundPoolBuilder.build();
        soundPoolThird = soundPoolBuilder.build();
        soundPoolForth = soundPoolBuilder.build();


        coinSound = soundPool.load(this, R.raw.coin, 1);
        gameOverSound = soundPool.load(this, R.raw.gameover, 1);
        bonusSound = soundPoolSecond.load(this, R.raw.bonussound, 1);
        advantageSound = soundPoolThird.load(this, R.raw.advantagesound, 1);
        lifePointSound = soundPoolForth.load(this, R.raw.lifepointsound, 1);
        //endregion

        getDataFromSharedPreferences();

        for(int i = 0; i<= 7;i++){//ball click events 0 = first collection ball , 4 == redball , 5 == blueball,6 = greenball, 7 = redabll2
            ballImages[i].setOnClickListener(this);
        }

        //region Methods

        BallMethod(ballImages[0], ballSpeed);
        BallMethod(ballImages[1], ballSpeed);
        BallMethod(ballImages[2], ballSpeed);
        BallMethod(ballImages[3], ballSpeed);
        BallMethod(ballImages[4], ballSpeed);
        BallMethod(ballImages[5], ballSpeed);
        BallMethod(ballImages[6], ballSpeed);
        BallMethod(ballImages[7], ballSpeed);

        BallOnClicks(0);
        BallOnClicks(1);
        BallOnClicks(2);
        BallOnClicks(3);
        BallOnClicks(4);
        BallOnClicks(5);
        BallOnClicks(6);
        BallOnClicks(7);

        BackGroundSound();

        // SaveScore();
        // endregion

        //region VISIBILITIES
        ballImages[1].setVisibility(View.INVISIBLE);
        ballImages[2].setVisibility(View.INVISIBLE);
        ballImages[3].setVisibility(View.INVISIBLE);
        ballImages[5].setVisibility(View.INVISIBLE);
        ballImages[6].setVisibility(View.INVISIBLE);
        ballImages[7].setVisibility(View.INVISIBLE);
        ballImages[7].setClickable(false);
        //endregion

        ImageSizes();
        //updateTimeTast();

    }


    boolean check = false;
    private void BallMethod(final ImageView ball, int period) {

        UpdateBallTask = new TimerTask() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //to finding screen size
                        Point point = new Point();
                        getWindowManager().getDefaultDisplay().getSize(point);
                        int height = point.y;
                        int width = point.x;

                        //These lines generate random numbers for ball position
                        r = new Random();
                        int x = r.nextInt((width - (width/4)));
                        int y = r.nextInt(height - (height/7));
                        ball.setX(x);
                        ball.setY(y);
                    }
                });

            }
        };
        int FPS = 1;
        timer.scheduleAtFixedRate(UpdateBallTask, 0, period / FPS);

    }

    private void BallOnClicks(final int index) {

        ballImages[index].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                totalClicksControl(index);

            }

        });

        if (index == 5) {

            ballImages[5].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (advantageSoundCheck != true) {
                        soundPoolThird.play(advantageSound, 1, 1, 1, 0, 1);
                    }


                    r = new Random();
                    int choseRedBallNumber = r.nextInt(2);// set choseRedBallNumber 0 or 1 and set size of one of the red balls smaller.

                    if(ballImages[7].isClickable() == false){
                        ballImages[4].requestLayout();
                        ballImages[4].getLayoutParams().height -= 25;
                        ballImages[4].getLayoutParams().width -= 25;
                    }
                    else{

                        if(choseRedBallNumber == 0){

                            ballImages[4].requestLayout();
                            ballImages[4].getLayoutParams().height -= 25;
                            ballImages[4].getLayoutParams().width -= 25;

                        }
                        else {

                            ballImages[7].requestLayout();
                            ballImages[7].getLayoutParams().height -= 25;
                            ballImages[7].getLayoutParams().width -= 25;

                        }
                    }
                    ballImages[5].setVisibility(View.INVISIBLE);
                }
            });
        }

        if(index == 6){

            ballImages[6].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    life++;
                    lifeText.setText(" " + life);
                    ballImages[6].setVisibility(View.INVISIBLE);
                    soundPoolForth.play(lifePointSound, 1, 1, 0, 0, 1);
                }
            });

        }

    }

    private void totalClicksControl(int index){

        if (index != 4 && index != 7) {

            totalClicks++;
            score++;
            scoreText.setText("Score: " + score);


            //region make the balls visible

            if(totalClicks %10 ==0){

                ballImages[1].setVisibility(View.VISIBLE);

                ballImages[5].setVisibility(View.VISIBLE);

                new CountDownTimer(3000, 1000) {

                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        ballImages[5].setVisibility(View.INVISIBLE);
                    }

                }.start();


            }
            if(totalClicks %20 ==0){

                ballImages[2].setVisibility(View.VISIBLE);
                constraintLayout.setBackgroundResource(R.drawable.greencloud);
            }
            if(totalClicks %30 ==0){
                ballImages[3].setVisibility(View.VISIBLE);

                ballImages[6].setVisibility(View.VISIBLE);

                if(!ballImages[7].isClickable()) {
                    ballImages[7].setClickable(true);
                    ballImages[7].setVisibility(View.VISIBLE);
                }

                new CountDownTimer(3000, 1000) {

                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        ballImages[6].setVisibility(View.INVISIBLE);
                    }

                }.start();
            }

            //endregion


            if (totalClicks % 5 == 0) {


                if (bonusSoundCheck != true) {
                    soundPoolSecond.play(bonusSound, 1, 1, 0, 0, 1);
                }
                ballImages[index].requestLayout();
                ballImages[index].getLayoutParams().height -= 15;
                ballImages[index].getLayoutParams().width -= 15;

                r = new Random();
                int choseRedBallNumber = r.nextInt(2);// set choseRedBallNumber 0 or 1 and set size of one of the red balls smaller.


                if(ballImages[7].isClickable() == false){
                    ballImages[4].requestLayout();
                    ballImages[4].getLayoutParams().height += 15;
                    ballImages[4].getLayoutParams().width += 15;
                }
                else{

                    if(choseRedBallNumber == 0){

                        ballImages[4].requestLayout();
                        ballImages[4].getLayoutParams().height += 15;
                        ballImages[4].getLayoutParams().width += 15;

                    }
                    else {

                        ballImages[7].requestLayout();
                        ballImages[7].getLayoutParams().height += 15;
                        ballImages[7].getLayoutParams().width += 15;

                    }
                }

            } else {

                if (coinSoundCheck != true) {
                    soundPool.play(coinSound, 1, 1, 0, 0, 1);
                }
            }

            ResizeImageSizes(index);


        } else {
            clicked = true;
            life--;
            lifeText.setText(" " + life);

            if (clicked && life == 0) {

                ballImages[index].setClickable(false);
                if (!backgroundSoundChecked) {
                    backgroundPlayer.stop();
                }
                ShowGameEndDialog(R.layout.dialog_layout);
            }

            if (gameOverSoundCheck != true) {
                soundPool.play(gameOverSound, 1, 1, 0, 0, 1);
            }

        }
        updateBallSpeeds();

    }


    public void pauseGame(View view) {

        if(backgroundPlayer !=null ){
            backgroundPlayer.stop();
        }

        ShowPausePlayAlertDialog(R.layout.pause_layout);
        if(timer !=null){
            timer.cancel();
            timer = null;
        }
        pauseImage.setEnabled(false);
    }

    private void ShowPausePlayAlertDialog(int layout) {

        dialogBuilder = new AlertDialog.Builder(GameActivity.this);
        View layoutView = getLayoutInflater().inflate(layout, null);
        Button restartBtn = layoutView.findViewById(R.id.restartBtn);
        Button mainMenuBtn = layoutView.findViewById(R.id.mainMenuBtn);
        ImageView playImage = layoutView.findViewById(R.id.playImageView);
        dialogBuilder.setCancelable(false);
        dialogBuilder.setView(layoutView);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        alertDialog.show();

        //region Onclick

        restartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                Intent intent = getIntent();
                finish();
                startActivity(intent);

            }
        });

        mainMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                intent.putExtra("Score", score);
                startActivity(intent);
                GameActivity.this.finish();
                alertDialog.dismiss();

            }
        });

        playImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();

                if(!backgroundSoundChecked) {
                    BackGroundSound();
                }

                timer = new Timer();
                BallMethod(ballImages[0], ballSpeed-increaseSpeed);
                BallMethod(ballImages[2], ballSpeed-increaseSpeed);
                BallMethod(ballImages[1], ballSpeed-increaseSpeed);
                BallMethod(ballImages[3], ballSpeed-increaseSpeed);
                BallMethod(ballImages[4], ballSpeed-increaseSpeed);
                BallMethod(ballImages[7], ballSpeed-increaseSpeed);
                Log.d("aa",String.valueOf(ballSpeed-increaseSpeed));
                pauseImage.setEnabled(true);


            }
        });

        //endregion
    }

    private void ShowGameEndDialog(int layout) {

        dialogBuilder = new AlertDialog.Builder(GameActivity.this);
        View layoutView = getLayoutInflater().inflate(layout, null);
        Button dialogButton = layoutView.findViewById(R.id.btnDialog);
        dialogBuilder.setCancelable(false);
        dialogBuilder.setView(layoutView);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        alertDialog.show();
        alertDialogCheck = true;
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                intent.putExtra("Score", score);
                startActivity(intent);
                GameActivity.this.finish();
                alertDialog.dismiss();
                alertDialogCheck = false;
            }
        });


    }


    private void updateBallSpeeds(){


        if((totalClicks % 10 == 0 || totalClicks % 20 == 0 || totalClicks % 30 == 0) && !totalClickFlag){

            increaseSpeed += 25;

            if(timer !=null){
                timer.cancel();
                timer = null;
            }
            timer = new Timer();
            BallMethod(ballImages[0], ballSpeed - increaseSpeed);
            BallMethod(ballImages[2], ballSpeed - increaseSpeed);
            BallMethod(ballImages[1], ballSpeed - increaseSpeed);
            BallMethod(ballImages[3], ballSpeed - increaseSpeed);
            BallMethod(ballImages[4], ballSpeed - increaseSpeed);
            BallMethod(ballImages[5], ballSpeed - increaseSpeed);
            BallMethod(ballImages[6], ballSpeed - increaseSpeed);
            BallMethod(ballImages[7], ballSpeed-increaseSpeed);
           // Toast.makeText(getBaseContext(),String.valueOf(1000-increaseSpeed),Toast.LENGTH_SHORT).show();

        }
        if( totalClicks % 10 == 1){

            totalClickFlag = false;
            //Toast.makeText(getBaseContext(),"1",Toast.LENGTH_SHORT).show();

        }
        else if( totalClicks % 10 == 0 )
        totalClickFlag = true;

    }

    @Override
    public void onClick(View view) {


        switch (view.getId()) {

            case R.id.CollectPointBall:
                BallOnClicks(0);
                break;
            case R.id.collectPointBall3:
                BallOnClicks(1);
                break;
            case R.id.collectPointBall4:
                BallOnClicks(2);
                break;
            case R.id.collectPointBall5:
                BallOnClicks(3);
                break;
            case R.id.RedBall:
                BallOnClicks(4);
                break;
            case R.id.BlueBall:
                BallOnClicks(5);
                break;
            case R.id.greenBall:
                BallOnClicks(6);
                break;

        }
    }

    private void BackGroundSound() {

        if (!backgroundSoundChecked) {
            backgroundPlayer = MediaPlayer.create(getApplicationContext(), R.raw.backgroundsoundddd);
            backgroundPlayer.start();
            backgroundPlayer.setLooping(true);
        }
    }

    private void ImageSizes(){
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        for (int i = 0;i<=7;i++){

            ballImages[i].requestLayout();
            ballImages[i].getLayoutParams().height = height/4;
            ballImages[i].getLayoutParams().width = width/4;

        }

    }

    private void ResizeImageSizes(int index){//When image size get too much small make it big.

        ballImages[index].requestLayout();
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        if(ballImages[index].getLayoutParams().height <= (height/5) && ballImages[index].getLayoutParams().width <= (width/5)){

            ballImages[index].getLayoutParams().height =height/4 ;
            ballImages[index].getLayoutParams().width = width/4;

        }

    }

    private void getDataFromSharedPreferences() {//get data from shared preferences
        //if there is no match with data that you want it return default value.
        //For example isBackgroundSoundChecked returns false

        SharedPreferences sharedPref = this.getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        boolean savedBackgroundSoundChecked = sharedPref.getBoolean("isBackgroundSoundChecked", false);
        boolean savedBallSoundsChecked = sharedPref.getBoolean("isBallSoundsChecked", false);
        ballSpeed = sharedPref.getInt("ballSpeed",1000);
        backgroundSoundChecked = savedBackgroundSoundChecked;
        if (savedBallSoundsChecked) {

            coinSoundCheck = true;
            advantageSoundCheck = true;
            bonusSoundCheck = true;
            gameOverSoundCheck = true;

        } else {
            coinSoundCheck = false;
            advantageSoundCheck = false;
            bonusSoundCheck = false;
            gameOverSoundCheck = false;
        }
    }

    @Override
    public void onBackPressed() {

        if(backPressedTime +2000 > System.currentTimeMillis()){
            backToast.cancel();
            if(backgroundPlayer !=null ){
                backgroundPlayer.stop();
            }
            Intent intent = new Intent(getApplicationContext(), StartActivity.class);
            intent.putExtra("Score", score);
            startActivity(intent);
            return;

        }else
        {
            backToast = Toast.makeText(getBaseContext(),"Press back again to exit",Toast.LENGTH_LONG);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();

    }

    @Override
    protected void onStop() {
        if(backgroundPlayer !=null ){
            backgroundPlayer.stop();
        }
        super.onStop();
    }

    @SuppressLint("ResourceType")
    @Override
    protected void onRestart() {
        if(backgroundSoundChecked){
            return;

        }else if (!alertDialogCheck)
        {
            BackGroundSound();
        }
        super.onRestart();
    }


}