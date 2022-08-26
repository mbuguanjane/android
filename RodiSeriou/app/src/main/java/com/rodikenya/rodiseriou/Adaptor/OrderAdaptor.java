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
import com.rodikenya.rodiseriou.Model.Order;
import com.rodikenya.rodiseriou.OrderDetailActivity;
import com.rodikenya.rodiseriou.R;

import java.util.ArrayList;
import java.util.List;

public class OrderAdaptor extends RecyclerView.Adapter<OrderViewHolder> {
    Context context;
   List<Order> orderlist;

    public OrderAdaptor(Context context, List<Order> orderlist) {
        this.context = context;
        this.orderlist = orderlist;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View views= LayoutInflater.from(context).inflate(R.layout.order_layout,parent,false);

        return new OrderViewHolder(views);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, final int position) {
       holder.order_id.setText(new StringBuilder("#").append(orderlist.get(position).getOrderId()));
       holder.order_address.setText(orderlist.get(position).getOrderAddress());
       holder.order_comment.setText(orderlist.get(position).getOrderComment());
       holder.order_price.setText(new StringBuilder("KES ").append(orderlist.get(position).getOrderPrice()));
       holder.order_status.setText(new StringBuilder("order Status: ").append(Common.convertCodeToStatus(orderlist.get(position).getOrderStatus())));
       holder.setItemClickListener(new IItemClickListener() {
           @Override
           public void onClick(View v) {
               Common.CurrentOrder=orderlist.get(position);
               context.startActivity(new Intent(context, OrderDetailActivity.class));
           }
       });
    }

    @Override
    public int getItemCount() {
        return orderlist.size();
    }
}
