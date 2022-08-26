package com.restrorant.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ProfileDetails extends AppCompatActivity {

    Button BtnretrievePass;
    String Title,Message,DirectionClass;
    TextView successtxt,Messagetxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);
        successtxt=(TextView)findViewById(R.id.successtxt);
        Messagetxt=(TextView)findViewById(R.id.Messagetxt);


            successtxt.setText("ProFile \n Name:"+Common.currentUser.getFirstName()+" "+Common.currentUser.getLastName());

            Messagetxt.setText("Details \n Email:"+Common.currentUser.getEmail()+
                                        "\n Phone: "+Common.currentUser.getPhone() +
                    "\nUser Level: "+Common.currentUser.getUserLevel() +
                    "\nPhone: "+Common.currentUser.getPhone());


        BtnretrievePass=(Button)findViewById(R.id.BtnretrievePass);
        BtnretrievePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent=new Intent(SuccessActivity.this,DirectionClass.getClass());
            }
        });
    }
}