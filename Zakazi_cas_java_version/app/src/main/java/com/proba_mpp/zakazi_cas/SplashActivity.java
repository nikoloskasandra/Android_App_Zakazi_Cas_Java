package com.proba_mpp.zakazi_cas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    //in this class i will use -->  findViewById()

    private static int SPLASH_SCREEN = 5000; // 1s = 1000 5s = 5000
    private Animation top_animation, bottom_animation;
    private ImageView logo;
    private TextView title, vision;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        //animations
        top_animation = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottom_animation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        logo = findViewById(R.id.logo);
        title = findViewById(R.id.title);
        vision = findViewById(R.id.vision);

        //setting animation to logo, title and vision
        logo.setAnimation(top_animation);
        title.setAnimation(bottom_animation);
        vision.setAnimation(bottom_animation);

        //splash screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, MainActivity.class);

                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View, String>(logo, "image_logo");
                pairs[1] = new Pair<View, String>(title, "logo_title");

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this);

                    startActivity(i, options.toBundle());
                }
            }
        }, SPLASH_SCREEN); //waiting


    }
}