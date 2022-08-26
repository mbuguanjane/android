package com.restrorant.myapplication.ViewHolder;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.restrorant.myapplication.Model.Food;
import com.restrorant.myapplication.Model.Request;
import com.restrorant.myapplication.R;
import com.restrorant.myapplication.RoomDatab.Model.CartModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;




public class OrderDetailAdaptor extends RecyclerView.Adapter<OrderDetailViewHolder> {
    private List<CartModel> Listdata = new ArrayList<>();
    private Context context;
    private Request request;

    public OrderDetailAdaptor(Request request, List<CartModel> listdata, Context context) {
        Listdata = listdata;
        this.context = context;
        this.request=request;
    }

    @NonNull
    @Override
    public OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.orderdetail_dialog, parent, false);

        return new OrderDetailViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailViewHolder holder, int position) {
        Picasso.with(context).load(Listdata.get(position).getImage()).into(holder.foodimage);
        holder.productid.setText("#"+Listdata.get(position).getProductId());
        holder.editPrice.setText("Price Ksh: "+Listdata.get(position).getPrice());
        holder.editQuantity.setText("Product Quantity: "+Listdata.get(position).getQuantity());
        holder.editName.setText("Product Name: "+Listdata.get(position).getProductName());
    }

    @Override
    public int getItemCount() {
        return Listdata.size();
    }
}
