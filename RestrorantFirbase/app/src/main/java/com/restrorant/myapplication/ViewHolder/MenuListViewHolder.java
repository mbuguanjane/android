package com.restrorant.myapplication.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.restrorant.myapplication.Interface.ItemClicklistener;
import com.restrorant.myapplication.R;

public class MenuListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView imageView;


    private ItemClicklistener itemClicklistener;
    public MenuListViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView=(ImageView)itemView.findViewById(R.id.menuImage);
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
