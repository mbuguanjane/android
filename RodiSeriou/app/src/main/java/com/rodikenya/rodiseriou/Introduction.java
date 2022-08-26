package com.rodikenya.rodiseriou;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.rodikenya.rodiseriou.Adaptor.IntroAdapter;
import com.rodikenya.rodiseriou.Model.Category;
import com.rodikenya.rodiseriou.Model.Intro;
import com.rodikenya.rodiseriou.Remote.IpService;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class Introduction extends AppCompatActivity {
    IpService mService;
    RecyclerView Intro_Recycle;
    CompositeDisposable compositeDisposable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);
        mService=Common.getIpService();
        compositeDisposable=new CompositeDisposable();
        Intro_Recycle=(RecyclerView)findViewById(R.id.RecycleViewIntro);
        Intro_Recycle.setHasFixedSize(true);
        LoadIntroduction();

    }

    private void LoadIntroduction() {
        compositeDisposable.add(mService.getIntroduction()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Intro>>() {
                               @Override
                               public void accept(List<Intro> intros) throws Exception {
                                   displayList(intros);
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Toast.makeText(Introduction.this, "Error occurred " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                               }
                           }

                ));

    }

    private void displayList(List<Intro> intros) {
        IntroAdapter adapter=new IntroAdapter(Introduction.this,intros);
        Intro_Recycle.setAdapter(adapter);
    }
}