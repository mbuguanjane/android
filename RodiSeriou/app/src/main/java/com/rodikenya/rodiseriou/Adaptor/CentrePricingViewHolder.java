package com.rodikenya.rodiseriou.Adaptor;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rodikenya.rodiseriou.Interface.IItemClickListener;
import com.rodikenya.rodiseriou.R;

public class CentrePricingViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
    public TextView Eventname,EventPrice,EventDescription,EventAvailable;
    public ImageView EventImage;



  IItemClickListener itemClickListener;
    public CentrePricingViewHolder(@NonNull View itemView) {
        super(itemView);

        EventImage=(ImageView) itemView.findViewById(R.id.EventImage);
        Eventname=(TextView)itemView.findViewById(R.id.Eventname);
        EventPrice=(TextView)itemView.findViewById(R.id.PriceEvent);
        EventDescription=(TextView)itemView.findViewById(R.id.Pricedescription);
        EventAvailable=(TextView)itemView.findViewById(R.id.availability);

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
