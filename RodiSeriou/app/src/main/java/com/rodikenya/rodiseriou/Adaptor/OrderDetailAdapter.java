package com.rodikenya.rodiseriou.Adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.rodikenya.rodiseriou.Common;
import com.rodikenya.rodiseriou.R;
import com.rodikenya.rodiseriou.RoomDatabase.Model.Cart;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class OrderDetailAdapter extends  RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewholder> {
    Context context;
    List<Cart> cartlist;

    public OrderDetailAdapter(Context context, List<Cart> cartlist) {
        this.context = context;
        this.cartlist = cartlist;
    }

    @NonNull
    @Override
    public OrderDetailAdapter.OrderDetailViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.order_detail_layout,parent,false);
        return new OrderDetailAdapter.OrderDetailViewholder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderDetailAdapter.OrderDetailViewholder holder, final int position) {
        if(cartlist.get(position).getLink().contains("http://192.168.43.28/Rodiproject/drinkshop/"))
        {
            Picasso.with(context)
                    .load(cartlist.get(position).getLink())
                    .into(holder.productimage);
        }else{
            Picasso.with(context)
                    .load(Common.BASE_URL+cartlist.get(position).getLink())
                    .into(holder.productimage);
        }

        holder.productprice.setText("KES "+String.valueOf(cartlist.get(position).getPrice()));
        holder.productname.setText(cartlist.get(position).getName());
        holder.productdescrip.setVisibility(View.GONE);

        final double priceOneProduct=cartlist.get(position).getPrice()/cartlist.get(position).getQuantity();

    }



    private void updateCart(final Cart cart) {
        Disposable disposable=io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                Common.cartRepository.updateCart(cart);
                emitter.onComplete();
            }
        })

                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer() {
                               @Override
                               public void accept(Object o) throws Exception {
                                   Toast.makeText(context, "User added", Toast.LENGTH_LONG).show();
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Toast.makeText(context, "Failed " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                               }
                           }, new Action() {
                               @Override
                               public void run() throws Exception {
                                   Toast.makeText(context, "Quantity updated successfully",Toast.LENGTH_LONG).show();
                               }
                           }

                );
    }

    @Override
    public int getItemCount() {
        return cartlist.size();
    }

    public  class OrderDetailViewholder extends RecyclerView.ViewHolder
    {


        ImageView productimage;
        TextView productprice,productdescrip,productname;
        public RelativeLayout view_background ;
        public LinearLayout view_foreground;


        public OrderDetailViewholder(@NonNull View itemView) {
            super(itemView);
            productimage=(ImageView)itemView.findViewById(R.id.img_product);
            productname=(TextView)itemView.findViewById(R.id.productname);
            productprice=(TextView)itemView.findViewById(R.id.price);
            productdescrip=(TextView)itemView.findViewById(R.id.description);
            view_foreground=(LinearLayout)itemView.findViewById(R.id.view_foreground);
            view_background=(RelativeLayout)itemView.findViewById(R.id.view_background);
        }
    }
    public  void removeItem(int position)
    {
        cartlist.remove(position);
        notifyItemRemoved(position);
    }
    public  void restoreItem(Cart item, int position)
    {
        cartlist.add(position, item);
        notifyItemInserted(position);
    }
}
