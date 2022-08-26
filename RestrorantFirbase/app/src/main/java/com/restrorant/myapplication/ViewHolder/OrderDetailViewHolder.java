package com.restrorant.myapplication.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.restrorant.myapplication.Interface.ItemClicklistener;
import com.restrorant.myapplication.R;

public class OrderDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
     public TextView productid,editPrice,editName,editQuantity;
     public ImageView foodimage;
     private ItemClicklistener itemClicklistener;
    public OrderDetailViewHolder(@NonNull View itemView) {
        super(itemView);
        productid=(TextView)itemView.findViewById(R.id.productid);
        editName=(TextView)itemView.findViewById(R.id.editName);
        editPrice=(TextView)itemView.findViewById(R.id.editPrice);
        editQuantity=(TextView)itemView.findViewById(R.id.editQuantity);
        foodimage=(ImageView)itemView.findViewById(R.id.foodimage);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        this.itemClicklistener.onClick(v,getAdapterPosition(),false);
    }

    public void setItemClicklistener(ItemClicklistener itemClicklistener) {
        this.itemClicklistener = itemClicklistener;
    }
}
