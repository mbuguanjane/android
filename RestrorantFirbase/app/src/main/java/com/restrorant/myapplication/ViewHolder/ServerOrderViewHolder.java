package com.restrorant.myapplication.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.restrorant.myapplication.Common;
import com.restrorant.myapplication.Interface.ItemClicklistener;
import com.restrorant.myapplication.R;

public class ServerOrderViewHolder extends RecyclerView.ViewHolder implements
        View.OnClickListener,
        View.OnCreateContextMenuListener
      {
     public TextView order_id,order_status,order_phone,order_Address;
     private ItemClicklistener itemClicklistener;
    public ServerOrderViewHolder(@NonNull View itemView) {
        super(itemView);
        order_id=(TextView)itemView.findViewById(R.id.order_id);
        order_status=(TextView)itemView.findViewById(R.id.order_status);
        order_Address=(TextView)itemView.findViewById(R.id.order_Address);
        order_phone=(TextView)itemView.findViewById(R.id.order_phone);
        itemView.setOnClickListener(this);
        itemView.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View v) {
        this.itemClicklistener.onClick(v,getAdapterPosition(),false);
    }

    public void setItemClicklistener(ItemClicklistener itemClicklistener) {
        this.itemClicklistener = itemClicklistener;
    }

          @Override
          public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
              menu.setHeaderTitle("Select the Action");
              menu.add(0,1,getAdapterPosition(), Common.DELETE);
          }
      }
