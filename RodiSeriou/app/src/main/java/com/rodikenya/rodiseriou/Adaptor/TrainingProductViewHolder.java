package com.rodikenya.rodiseriou.Adaptor;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rodikenya.rodiseriou.Interface.IItemClickListener;
import com.rodikenya.rodiseriou.R;

public class TrainingProductViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
    public TextView ProductName,ProductDescription,ProductPrice,availability;
    public Button Register,Watch;
    public ImageView ProductLink;


  IItemClickListener itemClickListener;
    public TrainingProductViewHolder(@NonNull View itemView) {
        super(itemView);

        ProductLink=(ImageView) itemView.findViewById(R.id.TrainingLink);
        ProductName=(TextView)itemView.findViewById(R.id.ProductName);
        ProductDescription=(TextView)itemView.findViewById(R.id.Pricedescription);
        ProductPrice=(TextView)itemView.findViewById(R.id.Training_price);
        availability=(TextView)itemView.findViewById(R.id.availability);
        Register=(Button) itemView.findViewById(R.id.Register);
        Watch=(Button) itemView.findViewById(R.id.Watch);
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
