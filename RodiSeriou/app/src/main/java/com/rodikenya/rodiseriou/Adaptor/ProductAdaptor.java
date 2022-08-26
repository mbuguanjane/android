package com.rodikenya.rodiseriou.Adaptor;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.rodikenya.rodiseriou.CartActivity;
import com.rodikenya.rodiseriou.Common;
import com.rodikenya.rodiseriou.DashBoard;
import com.rodikenya.rodiseriou.Interface.IItemClickListener;
import com.rodikenya.rodiseriou.Model.Banner;
import com.rodikenya.rodiseriou.Model.Products;
import com.rodikenya.rodiseriou.R;
import com.rodikenya.rodiseriou.RoomDatabase.Model.Cart;
import com.rodikenya.rodiseriou.RoomDatabase.Model.Favourite;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ProductAdaptor extends RecyclerView.Adapter<ProductViewHolder> {

    private CompositeDisposable compositeDisposable;
    Context context;
    List<Products> drinkList;
    public ProductAdaptor(Context context, List<Products> drinkList) {
        this.context = context;
        this.drinkList = drinkList;

        compositeDisposable=new CompositeDisposable();

        //LoadData();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.product_item_layout,null);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductViewHolder holder, final int position) {
            final int  pos=position;
           FavouriteStatus(holder,position);
        if(Integer.parseInt(drinkList.get(position).getAvailable())>0)
        {


            holder.available.setText("In Stock");

        }else
        {
            holder.available.setText("Out Stock");
            holder.available.setTextColor(Color.RED);
        }
           holder.price.setText(new StringBuilder("KES ").append(drinkList.get(position).getPrice()).toString());
           holder.name.setText(drinkList.get(position).getName());

           Picasso.with(context).load(Common.BASE_URL+drinkList.get(position).getLink()).into(holder.product_img);
           holder.setiItemClickListener(new IItemClickListener() {
               @Override
               public void onClick(View v) {

               }
           });


           holder.cart.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   if(Integer.parseInt(drinkList.get(position).getAvailable())>0)
                   {

                       createDialog(holder,position);
                       holder.available.setText("In Stock");

                   }else
                   {
                       Toast.makeText(context,drinkList.get(position).getName()+" is Out of Stock",Toast.LENGTH_LONG).show();
                       holder.available.setText("Out Stock");
                       holder.available.setTextColor(Color.RED);
                   }

               }
           });

           holder.favourite.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {

                       saveFav(position);



               }
           });
    }


    private void createDialog(final ProductViewHolder holder, final int position)
     {
         holder.cardviewlayout.setCardBackgroundColor(Color.WHITE);
         AlertDialog.Builder builder=new AlertDialog.Builder(context);
         View itemView= LayoutInflater.from(context).inflate(R.layout.add_to_cart_layout,null);
         ImageView imageView=(ImageView)itemView.findViewById(R.id.prodimage);
         Picasso.with(context)
                 .load(Common.BASE_URL+drinkList.get(position).getLink())
                 .into(imageView);
         System.out.println("Hello "+Common.BASE_URL + drinkList.get(position).getLink());
         final TextView  price=(TextView)itemView.findViewById(R.id.Price_value);
         price.setText(new StringBuilder("KES ").append(drinkList.get(position).getPrice()).toString());
         TextView  Name=(TextView)itemView.findViewById(R.id.nameproduct);
         Name.setText(drinkList.get(position).getName());
         TextView description=(TextView)itemView.findViewById(R.id.Description);
         description.setText(drinkList.get(position).getDescription());
         final ElegantNumberButton count=(ElegantNumberButton)itemView.findViewById(R.id.countbutton);

         count.setNumber("1");
         count.setVisibility(View.GONE);
         count.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
             @Override
             public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {

                     float totalbill = Float.valueOf(drinkList.get(position).getPrice()) * Integer.parseInt(count.getNumber());
                     price.setText("KES "+String.valueOf(totalbill));


             }
         });
         builder.setView(itemView);
         builder.setPositiveButton("Add to Cart", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 saveCart(holder,drinkList.get(position).getName(),Float.valueOf(drinkList.get(position).getPrice()), Integer.valueOf(count.getNumber()),drinkList.get(position).getName(),drinkList.get(position).getLink(),position,Integer.parseInt(drinkList.get(position).getID()),Integer.parseInt(drinkList.get(position).getAvailable()));

             }
         });
         builder.setView(itemView);
         builder.show();

     }



    private void saveCart(final ProductViewHolder holder, final String name, final float price, final int quantity, final String description, final String link, final int position, final int available, final int productid) {


        Disposable disposable=io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                Cart cart=new Cart(name,price,description,link ,quantity,available,productid);
                Common.cartRepository.insertCart(cart);
                emitter.onComplete();
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Toast.makeText(context,"Added to Cart Succcessfully",Toast.LENGTH_LONG).show();

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {


                        //Toast.makeText(context,"Failed "+throwable.getMessage(),Toast.LENGTH_LONG).show();
                        Toast.makeText(context,drinkList.get(position).getName()+" is Already in the Cart ",Toast.LENGTH_LONG).show();
                        int backgroundColor = ContextCompat.getColor(context, R.color.Cardviewbackground);
                        // Color backgroundColor = ... (Don't do this. The color is just an int.)

                        holder.cardviewlayout.setCardBackgroundColor(backgroundColor);
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //LoadData();
                        Toast.makeText(context,"Added to Cart Succcessfully",Toast.LENGTH_LONG).show();
                        int backgroundColor = ContextCompat.getColor(context, R.color.Cardviewbackground);
                        // Color backgroundColor = ... (Don't do this. The color is just an int.)

                        holder.cardviewlayout.setCardBackgroundColor(backgroundColor);

                    }
                });
    }

    private int  FavouriteStatus(final ProductViewHolder holder, final int position)
    {
        final int[] results = new int[1];
        Disposable disposable=io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {

                if (Common.FavouriteRepository.isFavourite(Integer.parseInt(drinkList.get(position).getID()))==1)
                {
                    holder.favourite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    results[0] =Common.FavouriteRepository.isFavourite(Integer.parseInt(drinkList.get(position).getID()));
                }else
                {
                    holder.favourite.setImageResource(R.drawable.ic_baseline_favorite_24);
                    results[0] =Common.FavouriteRepository.isFavourite(Integer.parseInt(drinkList.get(position).getID()));
                }

                emitter.onComplete();
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Toast.makeText(context,"Added to Fav Succcessfully",Toast.LENGTH_LONG).show();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {


                        Toast.makeText(context,"Failed "+throwable.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                       // Toast.makeText(context,"Favourite Added successfully ",Toast.LENGTH_LONG).show();
                    }
                });
        return results[0];
    }
    private void getPrice() {
        Disposable disposable=io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {

                Common.cartRepository.sumPrice();

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

                    }
                });
    }
    private void saveFav(final int position) {


        Disposable disposable=io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                Favourite fav=new Favourite();
                fav.menuId=drinkList.get(position).getMenuid();
                fav.price=drinkList.get(position).getPrice();
                fav.name=drinkList.get(position).getName();
                fav.link=drinkList.get(position).getLink();
                Common.FavouriteRepository.insertFav(fav);
                emitter.onComplete();
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Toast.makeText(context,"Added to Fav Succcessfully",Toast.LENGTH_LONG).show();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {


                        //Toast.makeText(context,"Failed "+throwable.getMessage(),Toast.LENGTH_LONG).show();
                        Toast.makeText(context,drinkList.get(position).getName()+" is Already in Favourite "+throwable.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        Toast.makeText(context,"Favourite Added successfully ",Toast.LENGTH_LONG).show();

                    }
                });
    }

    @Override
    public int getItemCount() {
        return drinkList.size();
    }
}
