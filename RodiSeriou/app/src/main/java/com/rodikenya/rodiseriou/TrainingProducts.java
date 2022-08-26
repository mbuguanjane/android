package com.rodikenya.rodiseriou;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rodikenya.rodiseriou.Adaptor.OrderAdaptor;
import com.rodikenya.rodiseriou.Adaptor.TrainingProductAdaptor;
import com.rodikenya.rodiseriou.Model.Order;
import com.rodikenya.rodiseriou.Model.TrainingItems;
import com.rodikenya.rodiseriou.Remote.IpService;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class TrainingProducts extends AppCompatActivity {
    RecyclerView TrainingItems_recycle;
    CompositeDisposable compositeDisposable;
    IpService mService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_products);

        TrainingItems_recycle=(RecyclerView)findViewById(R.id.recycleOrder);
        TrainingItems_recycle.setHasFixedSize(true);
        TrainingItems_recycle.setLayoutManager(new LinearLayoutManager(this));
        compositeDisposable=new CompositeDisposable();
        mService=Common.getIpService();
        LoadOrders(Common.CurrentTrainingMenu.getMenuID());
    }
    private void LoadOrders(String status) {

            compositeDisposable.add(mService.getTrainingItems(status)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Consumer<List<TrainingItems>>() {
                        @Override
                        public void accept(List<TrainingItems> trainingItems) throws Exception {
                            loadList(trainingItems);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Toast.makeText(TrainingProducts.this, "Failed " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
            );

    }

    private void loadList(List<TrainingItems> trainingItems) {
        TrainingProductAdaptor adaptor=new TrainingProductAdaptor(TrainingProducts.this,trainingItems);
        TrainingItems_recycle.setAdapter(adaptor);

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