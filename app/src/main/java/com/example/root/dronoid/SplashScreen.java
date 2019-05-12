package com.example.root.dronoid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreen extends Activity {
    static private final int SPLASH_TIME_OUT = 10000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        Thread myThread = new Thread(){
            @Override
            public void run()
            {
                try {
                    ImageView myImageView= (ImageView)findViewById(R.id.txt);
                    Animation myFadeInAnimation = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.fadein);
                    myImageView.startAnimation(myFadeInAnimation);
                    sleep(3000);
                    Intent intent = new Intent(getApplicationContext(), Menu.class);
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
        myThread.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
}
