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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.restrorant.myapplication.Interface.ItemClicklistener;
import com.restrorant.myapplication.Model.Request;
import com.restrorant.myapplication.Model.User;
import com.restrorant.myapplication.RoomDatab.Model.CartModel;
import com.restrorant.myapplication.ViewHolder.OrderDetailAdaptor;
import com.restrorant.myapplication.ViewHolder.OrderViewHolder;
import com.restrorant.myapplication.ViewHolder.ServerOrderViewHolder;

import java.util.List;

public class ServerOrderStatus extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager linearLayoutManager;
    FirebaseRecyclerAdapter<User, ServerOrderViewHolder> adapter;
    FirebaseDatabase database;
    DatabaseReference orderStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_order_status);
        recyclerView=(RecyclerView)findViewById(R.id.orderList);
        linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        database=FirebaseDatabase.getInstance();
        orderStatus=database.getReference("User");
        LoadOrder(Common.currentUser.getPhone());
    }

    private void LoadOrder(String phone) {
        adapter=new FirebaseRecyclerAdapter<User, ServerOrderViewHolder>(
                User.class,
                R.layout.orderlayout,
                ServerOrderViewHolder.class,
                orderStatus
        ) {
            @Override
            protected void populateViewHolder(ServerOrderViewHolder orderViewHolder, User user, int i) {
                //Toast.makeText(ServerOrderStatus.this,"this is the Model "+request,Toast.LENGTH_LONG).show();
                orderViewHolder.order_id.setText("Phone: "+adapter.getRef(i).getKey());
                orderViewHolder.order_status.setText("UserLevel: "+user.getUserLevel());
                orderViewHolder.order_Address.setText("Name: "+user.getFirstName()+" "+user.getSecondName()+" "+user.getLastName());
                orderViewHolder.order_phone.setText("Email: "+user.getEmail());
                orderViewHolder.setItemClicklistener(new ItemClicklistener() {
                    @Override
                    public void onClick(View view, int position, boolean loongClick) {
                       Toast.makeText(ServerOrderStatus.this,"Hello there",Toast.LENGTH_LONG).show();
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
        AlertDialog.Builder alertdialog=new AlertDialog.Builder(ServerOrderStatus.this);
        //alertdialog.setTitle("Order Details");
        //alertdialog.setMessage("Please fill full information");
        LayoutInflater inflater=this.getLayoutInflater();
        View add_menu_layout=inflater.inflate(R.layout.orderrowlayout,null);
        TextView orderTitle=(TextView)add_menu_layout.findViewById(R.id.cartItemprice);
        orderTitle.setText("Address:"+request.getAddress());
        RecyclerView recyclerFood=(RecyclerView)add_menu_layout.findViewById(R.id.recyclerFood);
        recyclerFood.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(ServerOrderStatus.this);
        recyclerFood.setLayoutManager(linearLayoutManager);
        OrderDetailAdaptor adaptors=new OrderDetailAdaptor(request,cartModelList,ServerOrderStatus.this);
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
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getTitle().equals(Common.UPDATE))
        {
           // showUpdateDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        }else if(item.getTitle().equals(Common.DELETE))
        {
//            deleteCategory(adapter.getRef(item.getOrder()).getKey());
//            String Url=adapter.getItem(item.getOrder()).getImage();
//            storageReference = storage.getReferenceFromUrl(Url);
//            storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//                @Override
//                public void onSuccess(Void aVoid) {
//                    Toast.makeText(AddCategory.this,"Image Deleted successfully "+saveURI,Toast.LENGTH_LONG).show();
//                }
//            });
        }
        return super.onContextItemSelected(item);
    }
}