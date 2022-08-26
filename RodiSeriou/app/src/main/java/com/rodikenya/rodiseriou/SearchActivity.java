package com.rodikenya.rodiseriou;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import com.mancj.materialsearchbar.MaterialSearchBar;
import com.rodikenya.rodiseriou.Adaptor.ProductAdaptor;
import com.rodikenya.rodiseriou.Model.Products;
import com.rodikenya.rodiseriou.Remote.IpService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SearchActivity extends AppCompatActivity {
     List<String> suggestList=new ArrayList<>();
     List<Products> localDataSource=new ArrayList<>();
     MaterialSearchBar searchBar;
     IpService mService;

     RecyclerView recyclerView_search;


     CompositeDisposable compositeDisposable=new CompositeDisposable();
     ProductAdaptor searchAdaptor,adaptor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mService=Common.getIpService();
        recyclerView_search=(RecyclerView)findViewById(R.id.Recycle_search);
        recyclerView_search.setLayoutManager(new GridLayoutManager(this,2));
        searchBar=(MaterialSearchBar)findViewById(R.id.Searchbar);
        searchBar.setHint("Enter your Product");
        loadDrinks();
        searchBar.setCardViewElevation(10);
        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               // Toast.makeText(SearchActivity.this,"before TextChanged",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               List<String> suggest=new ArrayList<>();
               for(String search :suggestList)
               {
                   if(search.toLowerCase().contains(searchBar.getText().toLowerCase()))
                   {
                       suggest.add(search);
                   }
                   searchBar.setLastSuggestions(suggest);
               }
            }

            @Override
            public void afterTextChanged(Editable editable) {
               // Toast.makeText(SearchActivity.this,"afterTextChanged",Toast.LENGTH_LONG).show();
                List<Products> result=new ArrayList<>();
                for(Products products:localDataSource)
                {


                    if(products.getName().contains(editable))
                    {
                        result.add(products);
                        searchAdaptor=new ProductAdaptor(SearchActivity.this,result);
                        recyclerView_search.setAdapter(searchAdaptor);
                    }
                }

            }
        });
        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if(!enabled)
                {
                    recyclerView_search.setAdapter(adaptor);
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                    startSearch(text);
                //Toast.makeText(SearchActivity.this,"onSearchConfirmed",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onButtonClicked(int buttonCode) {
          //Toast.makeText(SearchActivity.this,"onButtonClicked",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void startSearch(CharSequence text) {

        List<Products> result=new ArrayList<>();
        for(Products products:localDataSource)
        {


            if(products.getName().contains(text))
            {
                result.add(products);
                searchAdaptor=new ProductAdaptor(this,result);
                recyclerView_search.setAdapter(searchAdaptor);
            }
        }
    }

    private void loadDrinks() {
        compositeDisposable.add(mService.getAllProducts()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Products>>() {
                    @Override
                    public void accept(List<Products> products) throws Exception {
                        displayListProduct(products);
                        buildSuggestList(products);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(SearchActivity.this,"Failed "+throwable.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }));
    }

    private void buildSuggestList(List<Products> products) {
        for (Products products1:products)
        {
            suggestList.add(products1.getName());
            searchBar.setLastSuggestions(suggestList);
        }
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    private void displayListProduct(List<Products> products) {
        localDataSource=products;
        adaptor=new ProductAdaptor(this,products);
        recyclerView_search.setAdapter(adaptor);
    }
}