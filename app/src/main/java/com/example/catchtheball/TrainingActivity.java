package com.example.catchtheball;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

public class TrainingActivity extends AppCompatActivity implements View.OnClickListener {

    private boolean isTested = false; //check if training activity has tested or not
    private int clickedBalls;//How many ball clicked

    ImageView collectPointBallinTrainingActivity,redBallinTrainingActivity,
            RebBlueBallinTrainingActivity,GreenBallinTrainingActivity;

    KonfettiView viewKonfettiTrainingActivity;

    public static SoundPool soundPool, soundPoolSecond, soundPoolThird, soundPoolForth;
    public static int coinSound, gameOverSound, bonusSound, advantageSound,lifePointSound;

    SoundPool.Builder soundPoolBuilder;
    AudioAttributes attributes;
    AudioAttributes.Builder attributesBuilder;




    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        viewKonfettiTrainingActivity = findViewById(R.id.viewKonfettiTrainingActivity);
        collectPointBallinTrainingActivity = findViewById(R.id.collectPointBallinTrainingActivity);
        redBallinTrainingActivity = findViewById(R.id.redBallinTrainingActivity);
        RebBlueBallinTrainingActivity = findViewById(R.id.RebBlueBallinTrainingActivity);
        GreenBallinTrainingActivity = findViewById(R.id.GreenBallinTrainingActivity);

        redBallinTrainingActivity.setVisibility(View.INVISIBLE);
        RebBlueBallinTrainingActivity.setVisibility(View.INVISIBLE);
        GreenBallinTrainingActivity.setVisibility(View.INVISIBLE);

        collectPointBallinTrainingActivity.setOnClickListener(this);
        redBallinTrainingActivity.setOnClickListener(this);
        RebBlueBallinTrainingActivity.setOnClickListener(this);
        GreenBallinTrainingActivity.setOnClickListener(this);

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

        new MaterialTapTargetPrompt.Builder(TrainingActivity.this)
                .setTarget(R.id.collectPointBallinTrainingActivity)
                .setPrimaryText("You will collect point with brown ball")
                .setSecondaryText("Let's collect some point")
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener()
                {
                    @Override
                    public void onPromptStateChanged(MaterialTapTargetPrompt prompt, int state)
                    {
                        if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED)
                        {
                            soundPool.play(coinSound, 1, 1, 0, 0, 1);
                        }
                    }
                })
                .show();
    }

    public void saveDataToSharedPreferences(){

        SharedPreferences sharedPref = this.getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putBoolean("isTested",isTested);
        editor.commit();

    }

    public void ShowClues(int id){


        if(id == R.id.collectPointBallinTrainingActivity){

            new MaterialTapTargetPrompt.Builder(TrainingActivity.this)
                    .setTarget(R.id.collectPointBallinTrainingActivity)
                    .setPrimaryText("You will collect point with brown ball")
                    .setSecondaryText("Let's collect some point")
                    .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener()
                    {
                        @Override
                        public void onPromptStateChanged(MaterialTapTargetPrompt prompt, int state)
                        {
                            if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED)
                            {

                                soundPool.play(coinSound, 1, 1, 0, 0, 1);
                            }
                        }
                    })
                    .show();
        }

        if(id == R.id.redBallinTrainingActivity){

            new MaterialTapTargetPrompt.Builder(TrainingActivity.this)
                    .setTarget(R.id.redBallinTrainingActivity)
                    .setPrimaryText("If you touch to red ball you will lose one life.Totaly you have 2 life")
                    .setSecondaryText("Let's touch it for now")
                    .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener()
                    {
                        @Override
                        public void onPromptStateChanged(MaterialTapTargetPrompt prompt, int state)
                        {
                            if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED)
                            {
                                soundPool.play(gameOverSound, 1, 1, 0, 0, 1);
                            }
                        }
                    })
                    .show();

        }

        if(id == R.id.RebBlueBallinTrainingActivity) {

            new MaterialTapTargetPrompt.Builder(TrainingActivity.this)
                    .setTarget(R.id.RebBlueBallinTrainingActivity)
                    .setPrimaryText("Whenever you touch this red-blue ball red ball will get smaller.")
                    .setSecondaryText("Let's make red ball small")
                    .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener()
                    {
                        @Override
                        public void onPromptStateChanged(MaterialTapTargetPrompt prompt, int state)
                        {
                            if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED)
                            {

                                soundPoolThird.play(advantageSound, 1, 1, 0, 0, 1);
                            }
                        }
                    })
                    .show();
        }

        if(id == R.id.GreenBallinTrainingActivity) {

            new MaterialTapTargetPrompt.Builder(TrainingActivity.this)
                    .setTarget(R.id.GreenBallinTrainingActivity)
                    .setPrimaryText("Whenever you touch this green ball you will get an extra life.")
                    .setSecondaryText("Let's touch and get your extra life")
                    .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener()
                    {
                        @Override
                        public void onPromptStateChanged(MaterialTapTargetPrompt prompt, int state)
                        {
                            if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED)
                            {
                                soundPoolForth.play(lifePointSound, 1, 1, 0, 0, 1);
                            }
                        }
                    })

                    .show();


        }

    }

    public  void ShowKonfettiView(){

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        viewKonfettiTrainingActivity.build()
                .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(2000L)
                .addShapes(Shape.CIRCLE, Shape.RECT)
                .addSizes(new Size(12, 5))
                .setPosition(-width,(float)+width,-height/4,(float)+height/5)
                .streamFor(500, 3000L);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.collectPointBallinTrainingActivity:
                redBallinTrainingActivity.setVisibility(View.VISIBLE);
                ShowClues(R.id.redBallinTrainingActivity);
                break;
            case R.id.redBallinTrainingActivity:
                RebBlueBallinTrainingActivity.setVisibility(View.VISIBLE);
                ShowClues(R.id.RebBlueBallinTrainingActivity);
                break;
            case R.id.RebBlueBallinTrainingActivity:
                GreenBallinTrainingActivity.setVisibility(View.VISIBLE);
                ShowClues(R.id.GreenBallinTrainingActivity);
                break;

            case R.id.GreenBallinTrainingActivity:
                ShowKonfettiView();
                isTested = true;
                new CountDownTimer(3000, 1000) {

                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        Intent intent = new Intent(getApplicationContext(),GameActivity.class);
                        startActivity(intent);
                        TrainingActivity.this.finish();
                    }

                }.start();

                saveDataToSharedPreferences();
                break;
        }

    }
}
