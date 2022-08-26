package com.rodikenya.rodiseriou.Adaptor;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.rodikenya.rodiseriou.Interface.IItemClickListener;
import com.rodikenya.rodiseriou.R;


public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    ImageView product_img;
    TextView price,name,available;
    CardView cardviewlayout;
    IItemClickListener iItemClickListener;
    ImageView cart,favourite;

    public void setiItemClickListener(IItemClickListener iItemClickListener) {
        this.iItemClickListener = iItemClickListener;
    }

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        product_img=(ImageView)itemView.findViewById(R.id.image_product);
        cardviewlayout=(CardView)itemView.findViewById(R.id.cardviewlayout);
        name=(TextView)itemView.findViewById(R.id.Text_drink_name);
        available=(TextView)itemView.findViewById(R.id.availability);
        price=(TextView)itemView.findViewById(R.id.Price_value);
        cart=(ImageView) itemView.findViewById(R.id.AddCart);
        favourite=(ImageView) itemView.findViewById(R.id.favourite);
       itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        iItemClickListener.onClick(v);
    }
}
