package com.restrorant.myapplication.ViewHolder;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.restrorant.myapplication.OrderStatus;
import com.restrorant.myapplication.R;
import com.restrorant.myapplication.RoomDatab.Model.CartModel;

import java.util.ArrayList;
import java.util.List;

public class CartAdaptor extends RecyclerView.Adapter<CartViewHolder> {
  private List<CartModel> Listdata = new ArrayList<>();
  private Context context;

  public CartAdaptor(List<CartModel> listdata, Context context) {
      Listdata = listdata;
      this.context = context;
  }

  @NonNull
  @Override
  public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      LayoutInflater inflater = LayoutInflater.from(context);
      View itemView = inflater.inflate(R.layout.cart_layout, parent, false);

      return new CartViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
      TextDrawable drawable = TextDrawable.builder()
              .buildRound(Listdata.get(position).getQuantity(), Color.RED);
      holder.cartcount.setImageDrawable(drawable);
      holder.cartprice.setText("Price Ksh: "+Listdata.get(position).getPrice());
      holder.cartname.setText("Product name: "+Listdata.get(position).getProductName());

  }

  @Override
  public int getItemCount() {
      return Listdata.size();
  }

  public void removeItem(int position)
  {
      Listdata.remove(position);
      notifyItemRemoved(position);
  }
    public void restoreItem(CartModel cartModel,int position)
    {
        Listdata.add(position,cartModel);
        notifyItemInserted(position);
    }
    public CartModel getItem(int position)
    {
        return Listdata.get(position);
    }
}
