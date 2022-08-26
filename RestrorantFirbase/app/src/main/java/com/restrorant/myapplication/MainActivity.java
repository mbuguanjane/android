package com.restrorant.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Handler handler;
    Runnable runnable;
   TextView txtSlogan;
   ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ImageView img1;
        TextView img2;
        Animation top,bottom;

            super.onCreate(savedInstanceState);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_main);

            img1=(ImageView)findViewById(R.id.logo);
            img2=(TextView)findViewById(R.id.sublogo);

            top= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.mainlogoanimation);
            bottom= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.sublogoanimation);

            img1.setAnimation(top);
            img2.setAnimation(bottom);
            Typeface face=Typeface.createFromAsset(getAssets(),"fonts/NABILA.TTF");
             img2.setTypeface(face);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(getApplicationContext(),SignIn.class));
                    finish();
                }
            }, 5000);
        }


}