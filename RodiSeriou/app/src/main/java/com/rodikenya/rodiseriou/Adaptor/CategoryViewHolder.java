package com.rodikenya.rodiseriou.Adaptor;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.rodikenya.rodiseriou.Interface.IItemClickListener;
import com.rodikenya.rodiseriou.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {
     ImageView productimage;
     TextView menu_name_txt;
     IItemClickListener itemClickListener;

    public void setiItemClickListener(IItemClickListener iItemClickListener) {
        this.itemClickListener = iItemClickListener;
    }

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        productimage=(ImageView)itemView.findViewById(R.id.image_product);
        menu_name_txt=(TextView) itemView.findViewById(R.id.Text_menu_name);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
     itemClickListener.onClick(v);
    }
}
