package com.example.catchtheball;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class StartActivity extends AppCompatActivity {

    Button startButton,settingsButton,trainingButton;
    KonfettiView viewKonfetti;
    TextView bestScoreText,scoreText;
    ConstraintLayout constraintLayout;
    private  boolean isTrainingActivityTested;
    long score=0,bestScore=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        viewKonfetti = findViewById(R.id.viewKonfetti);
        startButton = findViewById(R.id.StartButton);
        bestScoreText = findViewById(R.id.BestScoreText);
        settingsButton = findViewById(R.id.SettingsButton);
        scoreText = findViewById(R.id.scoreText);
        constraintLayout = findViewById(R.id.constraintlayout);
        trainingButton = findViewById(R.id.trainingButton);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            score = bundle.getLong("Score");
        }
        getDataFromSharedPreferences();
        if(bestScore > score){

            bestScoreText.setText("Best Score: " + String.valueOf(bestScore));

        }else {
            bestScore = score;
            bestScoreText.setText("Best Score: " + String.valueOf(bestScore));
             saveDataToSharedPreferences();
            ShowKonfettiView();

        }


        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(!isTrainingActivityTested){

                    Intent intent = new Intent(getApplicationContext(),TrainingActivity.class);
                    startActivity(intent);
                    StartActivity.this.finish();

                }else {


                Intent intent = new Intent(getApplicationContext(),GameActivity.class);
                startActivity(intent);
                StartActivity.this.finish();
                }

            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SettingsActivity.class);
                startActivity(intent);
            }
        });

        scoreText.setText("Score: "+score);

        trainingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isTrainingActivityTested = false;
                Intent intent = new Intent(getApplicationContext(),TrainingActivity.class);
                startActivity(intent);
                StartActivity.this.finish();

            }
        });
    }

    //region SharedPreferendces
    public void saveDataToSharedPreferences(){

        SharedPreferences sharedPref = this.getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putLong("savedBestScore",bestScore);
        editor.commit();

    }

    public void getDataFromSharedPreferences(){//get data from shared preferences
        //if there is no match with data that you want it return default value.
        //For example isBackgroundSoundChecked returns false
        SharedPreferences sharedPref = this.getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        bestScore = sharedPref.getLong("savedBestScore",1);
        isTrainingActivityTested = sharedPref.getBoolean("isTested",false);
    }

    //endregion

    public  void ShowKonfettiView(){

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        viewKonfetti.build()
                .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(2000L)
                .addShapes(Shape.CIRCLE, Shape.RECT)
                .addSizes(new Size(12, 5))
                .setPosition(-width,(float)+width,-height/4,(float)+height/5)
                .streamFor(300, 5000L);
    }



}
