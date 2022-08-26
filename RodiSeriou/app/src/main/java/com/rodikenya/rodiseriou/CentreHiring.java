package com.rodikenya.rodiseriou;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rodikenya.rodiseriou.Adaptor.CentrePricingAdaptor;
import com.rodikenya.rodiseriou.Adaptor.TrainingProductAdaptor;
import com.rodikenya.rodiseriou.Model.Banner;
import com.rodikenya.rodiseriou.Model.CentreModel;
import com.rodikenya.rodiseriou.Model.DataMessage;
import com.rodikenya.rodiseriou.Model.MyResponse;
import com.rodikenya.rodiseriou.Model.Token;
import com.rodikenya.rodiseriou.Model.TrainingItems;
import com.rodikenya.rodiseriou.Remote.IFCMService;
import com.rodikenya.rodiseriou.Remote.IpService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CentreHiring extends AppCompatActivity {
    RecyclerView Centre_Prining_recycle;
    CompositeDisposable compositeDisposable;
    IpService mService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centre_hiring);
        Centre_Prining_recycle=(RecyclerView)findViewById(R.id.recycleCentrehiring);
        Centre_Prining_recycle.setHasFixedSize(true);
        Centre_Prining_recycle.setLayoutManager(new LinearLayoutManager(this));
        compositeDisposable=new CompositeDisposable();
        mService=Common.getIpService();
        LoadOrders();
    }
    private void LoadOrders() {

        compositeDisposable.add(mService.FetchCentrePricing()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<CentreModel>>() {
                    @Override
                    public void accept(List<CentreModel> centreModels) throws Exception {
                        loadList(centreModels);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(CentreHiring.this, "Failed " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
        );

    }

    private void loadList(List<CentreModel> centreModels) {
        CentrePricingAdaptor adaptor=new CentrePricingAdaptor(CentreHiring.this,centreModels);
        Centre_Prining_recycle.setAdapter(adaptor);

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