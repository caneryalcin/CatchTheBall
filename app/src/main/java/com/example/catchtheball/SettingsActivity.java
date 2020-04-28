package com.example.catchtheball;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;

public class SettingsActivity extends Activity implements View.OnClickListener{


    ImageView cancelImage;
    CheckBox backgroundSound;
    CheckBox ballSounds;
    Button easyMode,mediumMode,hardMode;
    public int ballSpeeds;
    boolean isBackgroundSoundChecked;
    boolean isBallSoundsChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        cancelImage = findViewById(R.id.cancelImageView);
        backgroundSound =findViewById(R.id.backgroundSoundCheckBox);
        ballSounds = findViewById(R.id.ballSoundsCheckBox);
        easyMode = (Button)findViewById(R.id.easyModeButton);
        mediumMode = (Button)findViewById(R.id.mediumModeButton);
        hardMode = (Button)findViewById(R.id.hardModeButton);


        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width),(int)(height));
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setFinishOnTouchOutside(false);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity= Gravity.CENTER;
        params.x = 0;
        params.y = -20;
        getWindow().setAttributes(params);


        cancelImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SettingsActivity.this.finish();

            }
        });

        backgroundSound.setOnClickListener(this);
        ballSounds.setOnClickListener(this);
        easyMode.setOnClickListener(this);
        mediumMode.setOnClickListener(this);
        hardMode.setOnClickListener(this);

        getDataFromSharedPreferences();

    }



    public void onClick(View view){


        switch (view.getId()){

            case R.id.easyModeButton:
                ballSpeeds = 1000;
                break;
            case R.id.mediumModeButton:
                ballSpeeds = 750;
                break;
            case R.id.hardModeButton:
                ballSpeeds = 500;
                break;
            case R.id.ballSoundsCheckBox:
                isBallSoundsChecked = ballSounds.isChecked();
            case R.id.backgroundSoundCheckBox:
                isBackgroundSoundChecked = backgroundSound.isChecked();
        }

        saveDataToSharedPreferences();

    }

    public void saveDataToSharedPreferences(){

        SharedPreferences sharedPref = this.getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putBoolean("isBackgroundSoundChecked",isBackgroundSoundChecked);
        editor.putBoolean("isBallSoundsChecked",isBallSoundsChecked);
        editor.putInt("ballSpeed",ballSpeeds);
        editor.commit();
    }


    public void getDataFromSharedPreferences(){//get data from shared preferences
        //if there is no match with data that you want it return default value.
        //For example isBackgroundSoundChecked returns false

        SharedPreferences sharedPref = this.getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        boolean savedBackgroundSoundChecked = sharedPref.getBoolean("isBackgroundSoundChecked",false);
        boolean savedBallSoundsChecked = sharedPref.getBoolean("isBallSoundsChecked",false);
        backgroundSound.setChecked(savedBackgroundSoundChecked);
        ballSounds.setChecked(savedBallSoundsChecked);

    }

}
