package com.rodikenya.rodiseriou;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rodikenya.rodiseriou.Adaptor.CentreHallAdaptor;
import com.rodikenya.rodiseriou.Model.centrebooking;
import com.rodikenya.rodiseriou.Remote.IpService;


import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CentreHallOrder extends AppCompatActivity {
    RecyclerView order_recycle;
    CompositeDisposable compositeDisposable;
    BottomNavigationView bottomNavigationView;
    IpService mService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centre_hall_order);
        order_recycle=(RecyclerView)findViewById(R.id.recycleCentre);
        order_recycle.setHasFixedSize(true);
        order_recycle.setLayoutManager(new LinearLayoutManager(this));
        compositeDisposable=new CompositeDisposable();
        mService= Common.getIpService();
        Toolbar toolbar=(Toolbar)findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);
        bottomNavigationView=(BottomNavigationView)findViewById(R.id.Bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.new_item:
                        LoadOrders("0");

                        break;
                    case R.id.item_processing:
                        LoadOrders("1");
                        break;
                    case R.id.cancelled_item:
                        LoadOrders("-1");

                        break;
                    case R.id.delivered_item:
                        LoadOrders("3");

                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
        LoadOrders("0");
    }
    private void LoadOrders(String status) {
        if(Common.CurrentUser!=null) {
            compositeDisposable.add(mService.getCentreBooking(status, Common.CurrentUser.getPhone())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Consumer<List<centrebooking>>() {
                        @Override
                        public void accept(List<centrebooking> centrebookings) throws Exception {
                            loadList(centrebookings);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Toast.makeText(CentreHallOrder.this, "Failed " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
            );
        }else
        {
            Toast.makeText(CentreHallOrder.this, "Please login First", Toast.LENGTH_LONG).show();
        }

    }

    private void loadList(List<centrebooking> centrebookings) {
        CentreHallAdaptor adaptor=new CentreHallAdaptor(CentreHallOrder.this,centrebookings);
        order_recycle.setAdapter(adaptor);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    @Override
    protected void onStop() {
        super.onStop();
        compositeDisposable.clear();
    }

}