package com.restrorant.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.restrorant.myapplication.Adaptor.MpesaAdaptor;
import com.restrorant.myapplication.Model.Itemm;
import com.restrorant.myapplication.Model.MpesaPayment;
import com.restrorant.myapplication.Model.stkCallback;
import com.restrorant.myapplication.ViewHolder.RestrourantViewHolder;

import java.util.ArrayList;
import java.util.List;

public class MpesaPayments extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference category;
    RecyclerView recyclerMenu;
    Toolbar toolbar;

    FirebaseRecyclerAdapter<stkCallback, RestrourantViewHolder> adapter;

    String RestorautId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        //init Firebase
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("MpesaPayment");

        database=FirebaseDatabase.getInstance();
        category=database.getReference("MpesaPayment");


        recyclerMenu=(RecyclerView)findViewById(R.id.recyclermenu);
        recyclerMenu.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerMenu.setLayoutManager(layoutManager);


           //loadMenu();
           getData();


    }

//    private void loadMenu() {
//        adapter=new FirebaseRecyclerAdapter<stkCallback, RestrourantViewHolder>(stkCallback.class,R.layout.cart_layout,RestrourantViewHolder.class,
//
//                category
//        ) {
//            @Override
//            protected void populateViewHolder(RestrourantViewHolder menuViewHolder, stkCallback
//                    category, int i) {
//
//
//                 stkCallback clickItem=category;
//                String key = adapter.getRef(i).getKey();
//                System.out.println("Testor :"+adapter.getRef(i).getKey());
//                System.out.println("Testor :"+clickItem.getResultDesc());
//               Toast.makeText(MpesaPayments.this,"Payments: "+clickItem.getResultDesc(),Toast.LENGTH_LONG).show();
//
//            }
//
//        };
//        recyclerMenu.setAdapter(adapter);
//    }

  public void getData()
  {
      DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
      Query query = rootRef.child("MpesaPayment").orderByChild("TransactionDate");
      ValueEventListener eventListener = new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot dataSnapshot) {

              List<Itemm> items=new ArrayList<>();


              for(DataSnapshot get:dataSnapshot.getChildren()){


                      DataSnapshot Item=dataSnapshot.child(get.getKey()).child("Body").child("stkCallback").child("CallbackMetadata").child("Item");
                         int i=0;
                         Long  amount=Item.child("0").child("Value").getValue(Long.class);
                         Long transactionDate=Item.child("3").child("Value").getValue(Long.class);
                         Long phoneNumber=Item.child("4").child("Value").getValue(Long.class);
                         String mpesaReceiptNumber=Item.child("1").child("Value").getValue(String.class);
                         String balance=Item.child("2").child("Value").getValue(String.class);
                        System.out.println("MpesaResults"+mpesaReceiptNumber);
                           if(mpesaReceiptNumber!=null) {
                               items.add(new Itemm(amount, transactionDate, phoneNumber, mpesaReceiptNumber, balance));
                           }else
                           {
                               System.out.println("No Payment");
                           }

                  }
              MpesaAdaptor mpesaAdaptor=new MpesaAdaptor(items,MpesaPayments.this);
              recyclerMenu.setAdapter(mpesaAdaptor);
              System.out.println("MPesa Payment "+items.size());


          }

          @Override
          public void onCancelled(DatabaseError databaseError) {
              System.out.println("what happening");
          }
      };
      query.addListenerForSingleValueEvent(eventListener);
  }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getTitle().equals(Common.RESERVATION))
        {


        }
        return super.onContextItemSelected(item);
    }

}