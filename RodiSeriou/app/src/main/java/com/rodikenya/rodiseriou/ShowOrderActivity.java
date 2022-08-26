package com.rodikenya.rodiseriou;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rodikenya.rodiseriou.Adaptor.OrderAdaptor;
import com.rodikenya.rodiseriou.Model.Order;
import com.rodikenya.rodiseriou.Remote.IpService;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowOrderActivity extends AppCompatActivity {
     RecyclerView order_recycle;
     CompositeDisposable compositeDisposable;
    BottomNavigationView bottomNavigationView;
     IpService mService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_order);

        order_recycle=(RecyclerView)findViewById(R.id.recycleOrder);
        order_recycle.setHasFixedSize(true);
        order_recycle.setLayoutManager(new LinearLayoutManager(this));
        compositeDisposable=new CompositeDisposable();
        mService=Common.getIpService();
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
            compositeDisposable.add(mService.getMyOrder(Common.CurrentUser.getPhone(), status)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Consumer<List<Order>>() {
                        @Override
                        public void accept(List<Order> orders) throws Exception {
                            loadList(orders);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Toast.makeText(ShowOrderActivity.this, "Failed " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
            );
        }else
        {
            Toast.makeText(ShowOrderActivity.this, "Please login First", Toast.LENGTH_LONG).show();
        }
    }

    private void loadList(List<Order> orders) {
        OrderAdaptor adaptor=new OrderAdaptor(ShowOrderActivity.this,orders);
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