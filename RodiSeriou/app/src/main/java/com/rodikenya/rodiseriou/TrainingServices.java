package com.rodikenya.rodiseriou;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.MediaController;
import android.widget.VideoView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.rodikenya.rodiseriou.Adaptor.ExoPlayerAdaptor;
import com.rodikenya.rodiseriou.Model.MediaObject;
import com.rodikenya.rodiseriou.Remote.IpService;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class TrainingServices extends AppCompatActivity {
     RecyclerView Recycle_tutorial;
     CompositeDisposable compositeDisposable;
     IpService ipService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_services);

        ipService=Common.getIpService();
        compositeDisposable=new CompositeDisposable();
        Recycle_tutorial=(RecyclerView)findViewById(R.id.Tutorials);
        Recycle_tutorial.setHasFixedSize(true);
        Recycle_tutorial.setLayoutManager(new LinearLayoutManager(this));
        getTutorial();



    }

    private void getTutorial() {
        compositeDisposable.add(ipService.FetchTutorials().observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
        .subscribe(new Consumer<List<MediaObject>>() {
            @Override
            public void accept(List<MediaObject> mediaObjects) throws Exception {
                  diplayTutorials(mediaObjects);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                System.out.println("Error "+throwable.getMessage());
            }
        }));

    }

    private void diplayTutorials(List<MediaObject> mediaObjects) {
        ExoPlayerAdaptor adaptor=new ExoPlayerAdaptor(this,mediaObjects);
        Recycle_tutorial.setAdapter(adaptor);
    }
}