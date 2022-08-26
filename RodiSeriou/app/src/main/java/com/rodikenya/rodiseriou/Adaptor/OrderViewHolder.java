package com.rodikenya.rodiseriou.Adaptor;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rodikenya.rodiseriou.Interface.IItemClickListener;
import com.rodikenya.rodiseriou.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
    public TextView order_id,order_price,order_address,order_comment,order_status;


  IItemClickListener itemClickListener;
    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);
        order_id=(TextView)itemView.findViewById(R.id.order_id);
        order_price=(TextView)itemView.findViewById(R.id.orderPrice_value);
        order_address=(TextView)itemView.findViewById(R.id.text_order_Address);
        order_comment=(TextView)itemView.findViewById(R.id.text_order_comment);
        order_status=(TextView)itemView.findViewById(R.id.text_order_status);
        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(IItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
       itemClickListener.onClick(view);
    }
}
