package com.rodikenya.rodiseriou;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.rodikenya.rodiseriou.Adaptor.CartAdaptor;
import com.rodikenya.rodiseriou.Adaptor.FavouriteAdaptor;
import com.rodikenya.rodiseriou.Interface.RecyclerItemTouchHelper;
import com.rodikenya.rodiseriou.Interface.RecyclerItemTouchHelperListener;
import com.rodikenya.rodiseriou.RoomDatabase.Model.Cart;
import com.rodikenya.rodiseriou.RoomDatabase.Model.Favourite;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FavouriteListActivity extends AppCompatActivity implements RecyclerItemTouchHelperListener {
    RecyclerView recyclerView_fav;
    CompositeDisposable compositeDisposable;
    RelativeLayout rootLayout;
    FavouriteAdaptor favAdaptor;
    List<Favourite> localFavourite=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_list);

        rootLayout=(RelativeLayout)findViewById(R.id.rootLayout);
        compositeDisposable=new CompositeDisposable();
        recyclerView_fav=(RecyclerView)findViewById(R.id.recycleFav);
        recyclerView_fav.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_fav.setHasFixedSize(true);
        ItemTouchHelper.SimpleCallback simpleCallback=new RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView_fav);
        LoadFavourite();
    }

    private void LoadFavourite() {
        compositeDisposable.add(Common.FavouriteRepository.getFavItems()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Favourite>>() {
                    @Override
                    public void accept(List<Favourite> favourites) throws Exception {
                        displayFavouriteItems(favourites);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(FavouriteListActivity.this,"Failed "+throwable.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }));
    }

    private void displayFavouriteItems(List<Favourite> favourites) {
        localFavourite=favourites;
         favAdaptor=new FavouriteAdaptor(this,favourites);
        recyclerView_fav.setAdapter(favAdaptor);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadFavourite();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
         if(viewHolder instanceof  FavouriteAdaptor.FavouriteViewHolder)
         {
             String name=localFavourite.get(viewHolder.getAdapterPosition()).name;
             final Favourite deletedItem=localFavourite.get(viewHolder.getAdapterPosition());
             final int deletedIndex=viewHolder.getAdapterPosition();
             favAdaptor.removeItem(deletedIndex);
             DeletefromRoom(deletedItem);
             Snackbar snackbar=Snackbar.make(rootLayout,new StringBuilder(name).append(" removed from Favourites list").toString(),Snackbar.LENGTH_LONG);
             snackbar.setAction("UNDO", new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     favAdaptor.restoreItem(deletedItem,deletedIndex);
                     RecovertoRoomDatabase(deletedItem);
                 }
             });
             snackbar.setActionTextColor(Color.YELLOW);
             snackbar.show();
         }
    }

    private void RecovertoRoomDatabase(final Favourite favourite) {
        Disposable disposable=io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                Common.FavouriteRepository.insertFav(favourite);
                emitter.onComplete();
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        //Toast.makeText(FavouriteListActivity.this,"Added to Cart Succcessfully",Toast.LENGTH_LONG).show();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {


                        //Toast.makeText(FavouriteListActivity.this,"Failed "+throwable.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //LoadData();
                    }
                });
    }

    private void DeletefromRoom(final Favourite favourite) {
        Disposable disposable=io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                Common.FavouriteRepository.delete(favourite);
                emitter.onComplete();
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                       // Toast.makeText(FavouriteListActivity.this,"Added to Cart Succcessfully",Toast.LENGTH_LONG).show();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {


                       // Toast.makeText(FavouriteListActivity.this,"Failed "+throwable.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //LoadData();
                    }
                });
    }
}