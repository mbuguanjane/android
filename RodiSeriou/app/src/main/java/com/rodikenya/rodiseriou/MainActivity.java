package com.rodikenya.rodiseriou;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.rodikenya.rodiseriou.Model.DataMessage;
import com.rodikenya.rodiseriou.Model.MyResponse;
import com.rodikenya.rodiseriou.Remote.IFCMService;
import com.rodikenya.rodiseriou.Remote.IpService;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    Button button;
  private static int SPLASH_TIME_OUT=4000;
  Animation frombottom,fromtop;
  ImageView imageView;
  IpService ipService;
  TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frombottom= AnimationUtils.loadAnimation(this,R.anim.homeanim);
        fromtop= AnimationUtils.loadAnimation(this,R.anim.logoanim);
        button=(Button)findViewById(R.id.opensecond);
        ipService=Common.getIpService();
        textView=(TextView)findViewById(R.id.welcome);
        imageView=(ImageView)findViewById(R.id.logomy);
        textView.setAnimation(fromtop);
        imageView.setAnimation(fromtop);
        button.setAnimation(frombottom);
        button.setText("CONTINUE TO RODI ");
        Autologin();



    }

    private void Autologin() {
        Thread loading = new Thread() {
            public void run() {
                try {
                    sleep(5000);
                    Intent main = new   Intent(MainActivity.this,DashBoard.class);
                    startActivity(main);
                    finish();


                }

                catch (Exception e) {
                    e.printStackTrace();
                }

                finally {
                    finish();
                }
            }
        };

        loading.start();

    }


    boolean isBackButtonClicked=false;

    @Override
    public void onBackPressed() {
        if(isBackButtonClicked) {
            super.onBackPressed();
            return;
        }
        this.isBackButtonClicked=true;
        Toast.makeText(MainActivity.this,"Please Clicked Again To Exit ",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        this.isBackButtonClicked=false;
        super.onResume();
    }
}
