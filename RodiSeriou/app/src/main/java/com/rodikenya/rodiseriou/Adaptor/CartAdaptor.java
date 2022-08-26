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
import com.rodikenya.rodiseriou.CartActivity;
import com.rodikenya.rodiseriou.Common;
import com.rodikenya.rodiseriou.DashBoard;
import com.rodikenya.rodiseriou.Interface.OnItemClick;
import com.rodikenya.rodiseriou.Model.Products;
import com.rodikenya.rodiseriou.R;
import com.rodikenya.rodiseriou.Remote.IpService;
import com.rodikenya.rodiseriou.RoomDatabase.Model.Cart;
import com.rodikenya.rodiseriou.RoomDatabase.Model.Favourite;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.UUID;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;

public class CartAdaptor extends  RecyclerView.Adapter<CartAdaptor.CartViewholder> {
    Context context;
    List<Cart> cartlist;
    private OnItemClick mCallback;
    List<Products> AvailableProduct;
    IpService mService;
    public float sumprice;
    int difference=0;
    int Remaining=0;
  CompositeDisposable compositeDisposable=new CompositeDisposable();
    public CartAdaptor(final Context context, List<Cart> cartlist,OnItemClick listener) {
        this.context = context;
        this.cartlist = cartlist;
        this.mCallback = listener;
        mService=Common.getIpService();

    }

    @NonNull
    @Override
    public CartViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.cart_item_layout,parent,false);
        return new CartViewholder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartViewholder holder, final int position) {
        if(cartlist.get(position).getLink().contains("http://192.168.43.28/Rodiproject/drinkshop/"))
        {
            Picasso.with(context)
                    .load( cartlist.get(position).getLink())
                    .into(holder.productimage);
        }else {
            Picasso.with(context)
                    .load(Common.BASE_URL + cartlist.get(position).getLink())
                    .into(holder.productimage);
        }
        holder.productprice.setText("KES "+String.valueOf(cartlist.get(position).getPrice()));
        holder.productname.setText(cartlist.get(position).getName());
        holder.productdescrip.setVisibility(View.GONE);
        holder.counter.setNumber(String.valueOf(cartlist.get(position).getQuantity()));
        final double priceOneProduct=cartlist.get(position).getPrice()/cartlist.get(position).getQuantity();
        System.out.println("Available in Cart"+cartlist.get(position).getAvailable());
        System.out.println("Quantity in Cart"+cartlist.get(position).getQuantity());

        holder.counter.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {


                    Cart cart = cartlist.get(position);
                    cart.setQuantity(newValue);
                    cart.setPrice(Math.round(priceOneProduct * newValue));
                    difference = newValue - oldValue;
                          checkAvailable(holder,cart,position);


                    holder.productprice.setText("KES " + String.valueOf(cartlist.get(position).getPrice()));


            }
        });
    }
    private float getPrice() {
        Disposable disposable=io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {

                //sumprice=Common.cartRepository.sumPrice();
                mCallback.onClick(String.valueOf(Common.cartRepository.sumPrice()));
                emitter.onComplete();
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(context,"Failed "+throwable.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                       // totalbill.setText("Total Bill "+sumprice);

                    }
                });
        return sumprice;
    }
    private void checkAvailable(final CartViewholder holder, final Cart cart, final int position) {
        compositeDisposable.add(mService.getAllProducts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Products>>() {
                               @Override
                               public void accept(List<Products> products) throws Exception {
                                   AvailableProduct=products;
                                   for(Products server:AvailableProduct)
                                   {
                                       if(server.getName().contains(cartlist.get(position).getName()))
                                       {
                                           System.out.println("Found "+server.getName());
                                           if(Integer.parseInt(holder.counter.getNumber())>Integer.parseInt(server.getAvailable()))
                                           {
                                               cartlist.get(position).setStatus(false);
                                               holder.counter.setEnabled(cartlist.get(position).isStatus());
                                               System.out.println("Enough");
                                               Toast.makeText(context,server.getName()+" is Now out of Stock",Toast.LENGTH_LONG).show();

                                           }else
                                           {
                                               cartlist.get(position).setStatus(true);
                                               holder.counter.setEnabled(cartlist.get(position).isStatus());

                                               System.out.println("Continue");
                                               updateCart(cart, position);
                                           }
                                       }
                                   }
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Toast.makeText(context, "Error occurred " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                               }
                           }
                )
        );


        // AvailableProduct
    }


    private void updateCart(final Cart cart, final int position) {
        Disposable disposable=io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                //Common.cartRepository.updateQuantity(cart.getQuantity(),cart.getId());
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

                                  System.out.println("product id "+cart.getName());
                                   getPrice();

                               }
                           }

                );
    }

    @Override
    public int getItemCount() {
        return cartlist.size();
    }

   public  class CartViewholder extends RecyclerView.ViewHolder
    {


         ImageView productimage;
         TextView productprice,productdescrip,productname;
         ElegantNumberButton counter;
         public RelativeLayout view_background ;
         public LinearLayout view_foreground;


        public CartViewholder(@NonNull View itemView) {
            super(itemView);
            productimage=(ImageView)itemView.findViewById(R.id.img_product);
            productname=(TextView)itemView.findViewById(R.id.productname);
            productprice=(TextView)itemView.findViewById(R.id.price);
            productdescrip=(TextView)itemView.findViewById(R.id.description);
            counter=(ElegantNumberButton)itemView.findViewById(R.id.text_count);
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
