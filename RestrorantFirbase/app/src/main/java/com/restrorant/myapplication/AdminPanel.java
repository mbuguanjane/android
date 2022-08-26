package com.restrorant.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AdminPanel extends AppCompatActivity implements View.OnClickListener{

    private CardView AddMoreItem,Orders,Reservation,Payments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
        AddMoreItem=(CardView)findViewById(R.id.AddMoreItem);
        Orders=(CardView)findViewById(R.id.Orders);
        Payments=(CardView)findViewById(R.id.Payments);
        Reservation=(CardView)findViewById(R.id.Reservation);
        AddMoreItem.setOnClickListener(this);
        Orders.setOnClickListener(this);
        Payments.setOnClickListener(this);
        Reservation.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.AddMoreItem:
                i=new Intent(this,AddRestorant.class);
                startActivity(i);
                break;
            case R.id.Orders:
                i=new Intent(this,ServerOrderStatus.class);
                startActivity(i);
                break;
            case R.id.Reservation:
                i=new Intent(this,ServerReservationStatus.class);
                startActivity(i);
                break;
            case R.id.Payments:
                i=new Intent(this,MpesaPayments.class);
                startActivity(i);
                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       Intent i=new Intent(this,SignIn.class);
        startActivity(i);
        Common.currentUser=null;
        finish();
    }
}