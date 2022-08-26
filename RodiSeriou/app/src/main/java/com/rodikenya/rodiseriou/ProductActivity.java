package com.rodikenya.rodiseriou;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.rodikenya.rodiseriou.Adaptor.ProductAdaptor;
import com.rodikenya.rodiseriou.Model.Products;
import com.rodikenya.rodiseriou.Remote.IpService;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ProductActivity extends AppCompatActivity {
    IpService ipService;
    AlertDialog alertDialog;
    RecyclerView recyclerView;
    CompositeDisposable compositeDisposable=new CompositeDisposable();
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        ipService=Common.getIpService();
        recyclerView=(RecyclerView)findViewById(R.id.Drink_Menu);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setHasFixedSize(true);
        textView=(TextView)findViewById(R.id.Categorytitle);
        textView.setText(Common.currentCategory.getName());
        LoadListDrink();
    }
    private void LoadListDrink() {


        compositeDisposable.add(ipService.getDrinks(Common.currentCategory.getID())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe(new Consumer<List<Products>>() {
                               @Override
                               public void accept(List<Products> drinks) throws Exception {
                                   displayDrinks(drinks);

                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Toast.makeText(ProductActivity.this, "Error occurred " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                               }
                           }
                )

        );
    }

    private void displayDrinks(List<Products> drinks) {
        ProductAdaptor adaptor=new ProductAdaptor(this,drinks);
        recyclerView.setAdapter(adaptor);
    }
}
