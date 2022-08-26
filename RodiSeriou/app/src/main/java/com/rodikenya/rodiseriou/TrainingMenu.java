package com.rodikenya.rodiseriou;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rodikenya.rodiseriou.Adaptor.OrderAdaptor;
import com.rodikenya.rodiseriou.Adaptor.TrainingMenuAdaptor;
import com.rodikenya.rodiseriou.Model.Order;
import com.rodikenya.rodiseriou.Model.Products;
import com.rodikenya.rodiseriou.Model.TrainingMenuModel;
import com.rodikenya.rodiseriou.Remote.IpService;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class TrainingMenu extends AppCompatActivity {
    RecyclerView Training_recycle;
    CompositeDisposable compositeDisposable;
    IpService mService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_menu);
        Training_recycle=(RecyclerView)findViewById(R.id.recycleTraining);
        Training_recycle.setHasFixedSize(true);
        Training_recycle.setLayoutManager(new LinearLayoutManager(this));
        compositeDisposable=new CompositeDisposable();
        mService=Common.getIpService();
        LoadOrders();
    }
    private void LoadOrders() {

            compositeDisposable.add(mService.getTrainingMenu()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Consumer<List<TrainingMenuModel>>() {
                        @Override
                        public void accept(List<TrainingMenuModel> trainingMenuModels) throws Exception {
                            LoadList(trainingMenuModels);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Toast.makeText(TrainingMenu.this, "Failed " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
            );

    }

    private void LoadList(List<TrainingMenuModel> trainingMenus) {
        TrainingMenuAdaptor adaptor=new TrainingMenuAdaptor(TrainingMenu.this,trainingMenus);
        Training_recycle.setAdapter(adaptor);

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