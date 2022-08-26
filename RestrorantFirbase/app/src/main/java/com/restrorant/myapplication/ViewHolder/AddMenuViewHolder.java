package com.restrorant.myapplication.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.restrorant.myapplication.Common;
import com.restrorant.myapplication.Interface.ItemClicklistener;
import com.restrorant.myapplication.R;

public class AddMenuViewHolder extends RecyclerView.ViewHolder implements
        View.OnClickListener,
         View.OnCreateContextMenuListener
  {
    public ImageView imageView;


    private ItemClicklistener itemClicklistener;
    public AddMenuViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView=(ImageView)itemView.findViewById(R.id.menuImage);
        itemView.setOnClickListener(this::onClick);
        itemView.setOnCreateContextMenuListener(this);
    }

    public void setItemClicklistener(ItemClicklistener itemClicklistener) {
        this.itemClicklistener = itemClicklistener;
    }

    @Override
    public void onClick(View v) {
        itemClicklistener.onClick(v,getAdapterPosition(),false);
    }

      @Override
      public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
          menu.setHeaderTitle("Select the Action");
          menu.add(0,0,getAdapterPosition(), Common.UPDATE);
          menu.add(0,1,getAdapterPosition(), Common.DELETE);

      }
  }
