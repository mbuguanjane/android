package com.restrorant.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.restrorant.myapplication.Interface.ItemClicklistener;
import com.restrorant.myapplication.Model.Category;
import com.restrorant.myapplication.Model.Food;
import com.restrorant.myapplication.Model.Request;
import com.restrorant.myapplication.Model.User;
import com.restrorant.myapplication.RoomDatab.Model.CartModel;
import com.restrorant.myapplication.ViewHolder.OrderDetailAdaptor;
import com.restrorant.myapplication.ViewHolder.OrderDetailViewHolder;
import com.restrorant.myapplication.ViewHolder.OrderViewHolder;

import java.util.List;

public class OrderStatus extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView recyclerFood;
    RecyclerView.LayoutManager linearLayoutManager;
    FirebaseRecyclerAdapter<User, OrderViewHolder>  adapter;

    FirebaseDatabase database;
    DatabaseReference orderStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);
        recyclerView=(RecyclerView)findViewById(R.id.orderList);

        linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        database=FirebaseDatabase.getInstance();
        orderStatus=database.getReference("Users");

        LoadOrder(Common.currentUser.getPhone());
    }

    private void LoadOrder(String phone) {
        adapter=new FirebaseRecyclerAdapter<User, OrderViewHolder>(
                User.class,
                R.layout.orderlayout,
                OrderViewHolder.class,
                orderStatus.orderByChild("phone").equalTo(phone)
        ) {
            @Override
            protected void populateViewHolder(OrderViewHolder orderViewHolder, User user, int i) {
               // Toast.makeText(OrderStatus.this,"this is the Model "+request,Toast.LENGTH_LONG).show();
               orderViewHolder.order_id.setText("#"+adapter.getRef(i).getKey());
               orderViewHolder.order_status.setText("Username: "+user.getFirstName()+" "+user.getLastName()+" "+user.getSecondName());
               orderViewHolder.order_Address.setText("Phone: "+user.getPhone());
               orderViewHolder.order_phone.setText("UserLevel: "+user.getUserLevel());
               orderViewHolder.setItemClicklistener(new ItemClicklistener() {
                   @Override
                   public void onClick(View view, int position, boolean loongClick) {
                      // Toast.makeText(OrderStatus.this,"We have Food",Toast.LENGTH_LONG).show();

//                       List<CartModel> listmodel=request.getFoods();
//                       showOrderDetailDialog(request,listmodel);

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
               }else{
                   return "Delivered";
               }

    }
    private void showOrderDetailDialog(Request request, List<CartModel> cartModelList) {
        AlertDialog.Builder alertdialog=new AlertDialog.Builder(OrderStatus.this);
        //alertdialog.setTitle("Order Details");
        //alertdialog.setMessage("Please fill full information");
        LayoutInflater inflater=this.getLayoutInflater();
        View add_menu_layout=inflater.inflate(R.layout.orderrowlayout,null);
        TextView orderTitle=(TextView)add_menu_layout.findViewById(R.id.cartItemprice);
        orderTitle.setText("Address:"+request.getAddress());
        recyclerFood=(RecyclerView)add_menu_layout.findViewById(R.id.recyclerFood);
        recyclerFood.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(OrderStatus.this);
        recyclerFood.setLayoutManager(linearLayoutManager);
        OrderDetailAdaptor adaptors=new OrderDetailAdaptor(request,cartModelList,OrderStatus.this);
        recyclerFood.setAdapter(adaptors);

        alertdialog.setView(add_menu_layout);
        alertdialog.setIcon(R.drawable.ic_baseline_order);
        alertdialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();


            }
        });
        alertdialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertdialog.show();
    }

}