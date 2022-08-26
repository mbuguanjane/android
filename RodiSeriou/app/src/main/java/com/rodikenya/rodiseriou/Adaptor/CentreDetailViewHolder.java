package com.rodikenya.rodiseriou.Adaptor;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rodikenya.rodiseriou.Interface.IItemClickListener;
import com.rodikenya.rodiseriou.R;


public class CentreDetailViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
    public TextView Price;
    public CheckBox descrip;


  IItemClickListener itemClickListener;
    public CentreDetailViewHolder(@NonNull View itemView) {
        super(itemView);
        Price=(TextView)itemView.findViewById(R.id.descripprice);
        descrip=(CheckBox) itemView.findViewById(R.id.particular);


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
