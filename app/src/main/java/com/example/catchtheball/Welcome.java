package com.example.catchtheball;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Path;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class Welcome extends AppCompatActivity {

    private  static  int SPLASH_TIME_OUT = 4000;
    long backPressedTime;
    private Toast backToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        ImageView ballImageView = findViewById(R.id.ballImageView);
        TextView textView = findViewById(R.id.textView);
        ballImageView.requestLayout();
        ballImageView.getLayoutParams().height = height/4;
        ballImageView.getLayoutParams().width = width/4;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Path path = new Path();
            path.arcTo(0f, 300f, 750f, height/2, 270f, -180f, true);
            ObjectAnimator animator = ObjectAnimator.ofFloat(ballImageView, ballImageView.X, ballImageView.Y, path);
            animator.setDuration(2000);
            animator.start();
        }


        textView.setTextSize(width/20);
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.slide_in_top);
        animation.setDuration(2000);
        textView.startAnimation(animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(Welcome.this,StartActivity.class);
                startActivity(intent);
                finish();

            }
        },SPLASH_TIME_OUT);

    }


    @Override
    public void onBackPressed() {
        if(backPressedTime +2000 > System.currentTimeMillis()){

            backToast.cancel();
            super.onBackPressed();
            return;

        }else
        {
            backToast = Toast.makeText(getBaseContext(),"Press back again to exit",Toast.LENGTH_LONG);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}
