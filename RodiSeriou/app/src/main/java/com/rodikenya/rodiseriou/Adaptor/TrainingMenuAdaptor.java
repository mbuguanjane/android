package com.rodikenya.rodiseriou.Adaptor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rodikenya.rodiseriou.Common;
import com.rodikenya.rodiseriou.Model.TrainingMenuModel;
import com.rodikenya.rodiseriou.R;
import com.rodikenya.rodiseriou.TrainingProducts;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TrainingMenuAdaptor extends RecyclerView.Adapter<TrainingMenuAdaptor.TrainingiewHolder> {
    Context context;
    List<TrainingMenuModel> trainingMenuList;

    public TrainingMenuAdaptor(Context context, List<TrainingMenuModel> trainingMenuList) {
        this.context = context;
        this.trainingMenuList = trainingMenuList;

    }

    public class TrainingiewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        View.OnClickListener itemClickListener;
        TextView trainingname;
        ImageView trainingimage;
        public TrainingiewHolder(@NonNull View itemView) {
            super(itemView);
            trainingname=(TextView)itemView.findViewById(R.id.TrainingMenu);
            trainingimage=(ImageView)itemView.findViewById(R.id.image_product);
            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(View.OnClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view);
        }
    }
    @NonNull
    @Override
    public TrainingiewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //trainingservice_item_layout.xml
        View view= LayoutInflater.from(context).inflate(R.layout.trainingservice_item_layout,parent,false);

        return new TrainingiewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrainingiewHolder holder, final int position) {

        holder.trainingname.setText(trainingMenuList.get(position).getName());
        Picasso.with(context).load(Common.BASE_URL+trainingMenuList.get(position).getLink()).fit().into(holder.trainingimage);
        holder.setItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.CurrentTrainingMenu=trainingMenuList.get(position);
                context.startActivity(new Intent(context, TrainingProducts.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return trainingMenuList.size();
    }



}
