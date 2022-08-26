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
import com.restrorant.myapplication.ViewHolder.ServerReserveViewHolder;

public class ServerReservationStatus extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager linearLayoutManager;
    FirebaseRecyclerAdapter<ReservationReq, ServerReserveViewHolder> adapter;
    FirebaseDatabase database;
    DatabaseReference orderStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_reservation_status);
        recyclerView=(RecyclerView)findViewById(R.id.orderList);
        linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        database=FirebaseDatabase.getInstance();
        orderStatus=database.getReference("Reservation");
        LoadOrder();
    }

    private void LoadOrder() {
        adapter=new FirebaseRecyclerAdapter<ReservationReq, ServerReserveViewHolder>(
                ReservationReq.class,
                R.layout.reservelayout,
                ServerReserveViewHolder.class,
                orderStatus
        ) {
            @Override
            protected void populateViewHolder(ServerReserveViewHolder orderViewHolder, ReservationReq request, int i) {
               // Toast.makeText(ServerReservationStatus.this,"this is the Model "+request,Toast.LENGTH_LONG).show();
                orderViewHolder.order_id.setText("#"+adapter.getRef(i).getKey());
                orderViewHolder.order_status.setText("Reservation Status: "+convertCodeToStatus(request.getStatus()));
                orderViewHolder.order_phone.setText("Client phone: "+request.getPhone());
                orderViewHolder.order_Date.setText("Date: "+request.getDate());
                orderViewHolder.order_Name.setText("Name: "+request.getName());
                orderViewHolder.order_Price.setText("Price: "+request.getPrice());
                orderViewHolder.order_People.setText("People: "+request.getPeople());
                orderViewHolder.order_Time.setText("Time: "+request.getTime());
                orderViewHolder.orderLocation.setText("Location: "+request.getLocation());
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
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getTitle().equals(Common.COMPLETED))
        {
            ReservationReq req=adapter.getItem(item.getOrder());
            req.setStatus("3");
            //showUpdateDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
            orderStatus.child(adapter.getRef(item.getOrder()).getKey()).setValue(req);
        }else if(item.getTitle().equals(Common.PROCESSING))
        {
            ReservationReq req=adapter.getItem(item.getOrder());
            req.setStatus("2");
            //showUpdateDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
            orderStatus.child(adapter.getRef(item.getOrder()).getKey()).setValue(req);
        }else if(item.getTitle().equals(Common.CANCEL))
        {
            ReservationReq req=adapter.getItem(item.getOrder());
            req.setStatus("-1");
            //showUpdateDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
            orderStatus.child(adapter.getRef(item.getOrder()).getKey()).setValue(req);
        }
        Toast.makeText(ServerReservationStatus.this,"Helloworld",Toast.LENGTH_LONG).show();
        return super.onContextItemSelected(item);
    }

    private void showUpdateDialog(String key, ReservationReq item) {
    }

}