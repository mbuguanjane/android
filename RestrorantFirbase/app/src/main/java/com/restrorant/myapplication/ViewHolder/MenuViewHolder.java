package com.restrorant.myapplication.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.restrorant.myapplication.Interface.ItemClicklistener;
import com.restrorant.myapplication.R;

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView menuname;
    public ImageView imageView;


    private ItemClicklistener itemClicklistener;
    public MenuViewHolder(@NonNull View itemView) {
        super(itemView);
        menuname=(TextView)itemView.findViewById(R.id.Menu_name);
        imageView=(ImageView)itemView.findViewById(R.id.menu_image);
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
