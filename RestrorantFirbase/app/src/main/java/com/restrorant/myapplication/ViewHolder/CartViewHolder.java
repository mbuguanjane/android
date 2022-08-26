package com.restrorant.myapplication.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.restrorant.myapplication.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
     public TextView cartname, cartprice;
     public ImageView cartcount;

     public RelativeLayout viewBackground;
     public LinearLayout viewForeground;

     public CartViewHolder(@NonNull View itemView) {
         super(itemView);
         cartname = (TextView) itemView.findViewById(R.id.cartItemname);
         cartprice = (TextView) itemView.findViewById(R.id.cartItemprice);
         cartcount = (ImageView) itemView.findViewById(R.id.cartitemcount);

         viewBackground = (RelativeLayout) itemView.findViewById(R.id.view_background);
         viewForeground = (LinearLayout) itemView.findViewById(R.id.viewForeground);
     }

     public void setText_cartname(TextView textCartname) {
         this.cartname = textCartname;
     }

     @Override
     public void onClick(View v) {

     }



 }
