package com.rodikenya.rodiseriou.Adaptor;

import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rodikenya.rodiseriou.Interface.IItemClickListener;
import com.rodikenya.rodiseriou.R;


public class CentreHallViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
    public TextView order_id,order_price,order_address,order_comment;


  IItemClickListener itemClickListener;
    public CentreHallViewHolder(@NonNull View itemView) {
        super(itemView);
        order_id=(TextView)itemView.findViewById(R.id.order_id);
        order_price=(TextView)itemView.findViewById(R.id.orderPrice_value);
        order_address=(TextView)itemView.findViewById(R.id.text_order_Address);
        order_comment=(TextView)itemView.findViewById(R.id.text_order_comment);

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
