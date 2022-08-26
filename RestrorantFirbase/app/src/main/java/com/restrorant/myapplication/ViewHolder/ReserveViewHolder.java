package com.restrorant.myapplication.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.restrorant.myapplication.Common;
import com.restrorant.myapplication.Interface.ItemClicklistener;
import com.restrorant.myapplication.R;

public class ReserveViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
        ,
        View.OnCreateContextMenuListener
{
     public TextView order_id,order_status,order_phone,order_People,order_Price,order_Name,order_Date,order_Time,orderLocation;
     private ItemClicklistener itemClicklistener;
    public ReserveViewHolder(@NonNull View itemView) {
        super(itemView);
        order_id=(TextView)itemView.findViewById(R.id.order_id);
        order_status=(TextView)itemView.findViewById(R.id.order_status);
        order_phone=(TextView)itemView.findViewById(R.id.order_Phone);
        itemView.setOnClickListener(this);
        order_People=(TextView)itemView.findViewById(R.id.order_People);
        order_Price=(TextView)itemView.findViewById(R.id.order_Price);
        order_Name=(TextView)itemView.findViewById(R.id.order_Name);
        order_Date=(TextView)itemView.findViewById(R.id.order_Date);
        order_Time=(TextView)itemView.findViewById(R.id.order_Time);
        orderLocation=(TextView)itemView.findViewById(R.id.orderLocation);
        itemView.setOnCreateContextMenuListener(this);
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
//        menu.setHeaderTitle("Select the Action");
//        menu.add(0,0,getAdapterPosition(), Common.COMPLETED);
//        menu.add(0,1,getAdapterPosition(), Common.PROCESSING);
//        menu.add(0,1,getAdapterPosition(), Common.CANCEL);
    }
}
