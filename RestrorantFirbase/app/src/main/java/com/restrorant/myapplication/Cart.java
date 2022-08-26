package com.restrorant.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.restrorant.myapplication.Helper.RecyclerItemTouchHelper;
import com.restrorant.myapplication.Interface.RecyclerItemTouchHelperListener;
import com.restrorant.myapplication.Model.CartData;
import com.restrorant.myapplication.Model.Request;
import com.restrorant.myapplication.Retrofit.MpesaInterface;
import com.restrorant.myapplication.RoomDatab.Model.CartModel;
import com.restrorant.myapplication.RoomDatab.RoomDB;
import com.restrorant.myapplication.ViewHolder.CartAdaptor;
import com.restrorant.myapplication.ViewHolder.CartViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import info.hoang8f.widget.FButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Cart extends AppCompatActivity implements RecyclerItemTouchHelperListener {
    RoomDB roomdatabase;
    List<CartModel> dataList=new ArrayList<>();
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    FButton btnPlace;
    TextView totalPrice;
     int totalPricesum=0;
     FirebaseDatabase database;
     DatabaseReference request;
    CartAdaptor adaptor;
    RelativeLayout rootLayout;
    String name;
    CartModel cartModeldelete;
    int deleteIndex;
    MpesaInterface mpesaInterface;
    String RequestID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        //init Firebase
        database=FirebaseDatabase.getInstance();
        request=database.getReference("Requests");
          RequestID=new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss", Locale.getDefault()).format(new Date());
        recyclerView=(RecyclerView)findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
         rootLayout=(RelativeLayout)findViewById(R.id.rootLayout);
        mpesaInterface=Common.getMpesa();
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback=new RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
         totalPrice=(TextView)findViewById(R.id.Total);

         btnPlace=(FButton)findViewById(R.id.placeorder);
        roomdatabase=RoomDB.getInstance(this);

        dataList= roomdatabase.cartDAO().getAllCart();
        calculateTotal();


        //totalPrice.setText(String.valueOf(totalPricesum));
         btnPlace.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {


                 PaymentConfirm();

             }
         });
         loadCart(dataList);


    }
    private void PaymentConfirm()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setMessage("Mpesa Payment is going to be made using"+Common.currentUser.getPhone()+" ?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog

                dialog.dismiss();
                MpesaPayment(RequestID);
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
   private void MpesaPayment(String Requestid)
   {
       mpesaInterface.sendPayment(Common.currentUser.getPhone(),String.valueOf(totalPricesum),"Eatit")
               .enqueue(new Callback<String>() {
                   @Override
                   public void onResponse(Call<String> call, Response<String> response) {
                       Toast.makeText(Cart.this,"sent successfully "+response.body().toString(),Toast.LENGTH_LONG).show();
                       if(totalPricesum>0) {
                           showAlertDialog(Requestid);
                       }else
                       {
                           Toast.makeText(Cart.this,"You have Nothing in your Cart",Toast.LENGTH_LONG).show();
                       }
                   }

                   @Override
                   public void onFailure(Call<String> call, Throwable t) {
                       Toast.makeText(Cart.this,"Failed to send "+t.getMessage(),Toast.LENGTH_LONG).show();
                   }
               });
   }
    private void calculateTotal() {
        dataList= roomdatabase.cartDAO().getAllCart();
        for(CartModel cart:dataList)
        {
            totalPricesum+=Integer.parseInt(cart.getPrice())*Integer.parseInt(cart.getQuantity());
        }
        totalPrice.setText(String.valueOf(totalPricesum));
        Toast.makeText(Cart.this,"calculate total cart price "+dataList.size(),Toast.LENGTH_LONG).show();
    }

    private void showAlertDialog(String RequestId) {
        //here
        final EditText edtAddress = new EditText(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("One more Step ")
                .setMessage("Enter the Address")
                .setView(edtAddress)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Request requestdata=new Request(
                                Common.currentUser.getPhone(),
                                Common.currentUser.getFirstName()+" "+Common.currentUser.getLastName(),
                                edtAddress.getText().toString(),
                                totalPrice.getText().toString(),
                                "0",
                                dataList
                        );

                        request.child(RequestId)
                                .setValue(requestdata);
                        //Lipa na Mpesa
                       // MpesaPayment();
                        //Lipa na Mpesa
                        Toast.makeText(Cart.this,"Thank you your Order is Placed",Toast.LENGTH_LONG).show();
                        finish();
                        Toast.makeText(Cart.this,"Thank you your Order is Placed "+Common.currentUser.getPhone(),Toast.LENGTH_LONG).show();
                        roomdatabase.cartDAO().reset(dataList);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();


    }

    private void loadCart(List<CartModel> dataList) {
         adaptor=new CartAdaptor(dataList,Cart.this);
        recyclerView.setAdapter(adaptor);
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if(viewHolder instanceof CartViewHolder)
        {

             name=((CartAdaptor)recyclerView.getAdapter()).getItem(viewHolder.getAdapterPosition()).getProductName();
            cartModeldelete=((CartAdaptor)recyclerView.getAdapter()).getItem(viewHolder.getLayoutPosition());
            deleteIndex=viewHolder.getAdapterPosition();
            adaptor.removeItem(deleteIndex);
            roomdatabase.cartDAO().delete(cartModeldelete);

            calculateTotal();

            showSnakBar();

        }
    }

    private void showSnakBar() {
        Snackbar snackbar=Snackbar.make(rootLayout,name+"removed from cart",Snackbar.LENGTH_LONG);
        snackbar.setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adaptor.restoreItem(cartModeldelete,deleteIndex);
                roomdatabase.cartDAO().insert(cartModeldelete);

            }
        });
        snackbar.setActionTextColor(Color.YELLOW);
        snackbar.show();

        adaptor.notifyDataSetChanged();
    }
}