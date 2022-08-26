package com.restrorant.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.restrorant.myapplication.Interface.ItemClicklistener;
import com.restrorant.myapplication.Model.Request;
import com.restrorant.myapplication.Model.ReservationReq;
import com.restrorant.myapplication.ViewHolder.OrderViewHolder;
import com.restrorant.myapplication.ViewHolder.ReserveViewHolder;

public class ReservationStatus extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager linearLayoutManager;
    FirebaseRecyclerAdapter<ReservationReq, ReserveViewHolder> adapter;
    FirebaseDatabase database;
    DatabaseReference orderStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_status);
        recyclerView=(RecyclerView)findViewById(R.id.orderList);
        linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        database=FirebaseDatabase.getInstance();
        orderStatus=database.getReference("Reservation");
        LoadOrder(Common.currentUser.getPhone());
    }

    private void LoadOrder(String phone) {
        adapter=new FirebaseRecyclerAdapter<ReservationReq, ReserveViewHolder>(
                ReservationReq.class,
                R.layout.reservelayout,
                ReserveViewHolder.class,
                orderStatus.orderByChild("phone").equalTo(phone)
        ) {
            @Override
            protected void populateViewHolder(ReserveViewHolder orderViewHolder, ReservationReq request, int i) {
               // Toast.makeText(ReservationStatus.this,"this is the Model "+request,Toast.LENGTH_LONG).show();
                orderViewHolder.order_id.setText("#"+adapter.getRef(i).getKey());
                orderViewHolder.order_status.setText("Reservation Status: "+convertCodeToStatus(request.getStatus()));
                orderViewHolder.order_phone.setText("phone: "+request.getPhone());
                orderViewHolder.order_Price.setText("Price: "+request.getPrice());
                orderViewHolder.order_People.setText("People: "+request.getPeople());
                orderViewHolder.order_Time.setText("Time: "+request.getTime());
                orderViewHolder.order_Name.setText("Name: "+request.getName());
                orderViewHolder.order_Date.setText("Date: "+request.getDate());

                orderViewHolder.setItemClicklistener(new ItemClicklistener() {
                    @Override
                    public void onClick(View view, int position, boolean loongClick) {
                        //code later
                    }
                });

            }
        };
        recyclerView.setAdapter(adapter);
    }

    private String convertCodeToStatus(String status) {
        if(status.equals("0"))
        {
            return "Placed";
        }else if(status.equals("1"))
        {
            return "On the Way";
        }else if(status.equals("-1"))
        {
            return "Cancelled";
        }else if(status.equals("2"))
        {
            return "Processing";
        }else if(status.equals("3"))
        {
            return "Completed";
        }else{
            return "Delivered";
        }

    }

}