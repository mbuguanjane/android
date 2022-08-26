package com.restrorant.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FailedActivity extends AppCompatActivity {

    Button BtnretrievePass;
    String Title,Message,DirectionClass;
    TextView Failedtxt,Messagetxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_failed);
        Failedtxt=(TextView)findViewById(R.id.successtxt);
        Messagetxt=(TextView)findViewById(R.id.Messagetxt);
        if(getIntent()!=null)
        {
            Title=getIntent().getStringExtra("Title");
            Failedtxt.setText(Title);
            Message=getIntent().getStringExtra("Description");
            Messagetxt.setText(Message);
            DirectionClass=getIntent().getStringExtra("DirectionClass");
        }
        BtnretrievePass=(Button)findViewById(R.id.BtnretrievePass);
        BtnretrievePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FailedActivity.this,DirectionClass.getClass());
            }
        });
    }
}