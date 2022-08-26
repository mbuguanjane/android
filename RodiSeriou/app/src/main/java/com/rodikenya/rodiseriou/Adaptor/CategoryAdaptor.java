package com.rodikenya.rodiseriou.Adaptor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.rodikenya.rodiseriou.Common;
import com.rodikenya.rodiseriou.Interface.IItemClickListener;
import com.rodikenya.rodiseriou.Model.Category;


import com.rodikenya.rodiseriou.ProductActivity;
import com.rodikenya.rodiseriou.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryAdaptor extends RecyclerView.Adapter<CategoryViewHolder> {
    Context context;
    List<Category> categories;

    public CategoryAdaptor(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.menu_item_layout,null);
        return new CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, final int position) {
        if(categories.get(position).getLink().contains("http://192.168.43.28/Rodiproject/drinkshop/"))
        {
            Picasso.with(context)
                    .load(categories.get(position).getLink())
                    .fit()
                    .into(holder.productimage);
        }else {
            Picasso.with(context)
                    .load(Common.BASE_URL + categories.get(position).getLink())
                    .into(holder.productimage);

        }

        holder.menu_name_txt.setText(categories.get(position).getName());
        holder.setiItemClickListener(new IItemClickListener() {
            @Override
            public void onClick(View v) {
                Common.currentCategory=categories.get(position);
                context.startActivity(new Intent(context, ProductActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
