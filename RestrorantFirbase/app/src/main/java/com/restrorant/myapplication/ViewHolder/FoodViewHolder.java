package com.restrorant.myapplication.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.restrorant.myapplication.Interface.ItemClicklistener;
import com.restrorant.myapplication.R;

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView foodname;
    public ImageView foodimage;
    private ItemClicklistener itemClicklistener;
    public FoodViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        foodname=(TextView)itemView.findViewById(R.id.food_name);
        foodimage=(ImageView)itemView.findViewById(R.id.food_image);
        itemView.setOnClickListener(this::onClick);
    }

    public void setItemClicklistener(ItemClicklistener itemClicklistener) {
        this.itemClicklistener = itemClicklistener;
    }

    @Override
    public void onClick(View v) {
        itemClicklistener.onClick(v,getAdapterPosition(),false);
    }
}
