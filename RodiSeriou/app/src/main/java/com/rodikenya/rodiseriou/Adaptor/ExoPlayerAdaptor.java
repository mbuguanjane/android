package com.rodikenya.rodiseriou.Adaptor;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.rodikenya.rodiseriou.Common;
import com.rodikenya.rodiseriou.Interface.IItemClickListener;
import com.rodikenya.rodiseriou.Model.Category;
import com.rodikenya.rodiseriou.Model.MediaObject;
import com.rodikenya.rodiseriou.ProductActivity;
import com.rodikenya.rodiseriou.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ExoPlayerAdaptor extends RecyclerView.Adapter<ExoPlayerViewHolder> {
    Context context;
    List<MediaObject> TutorialList;

    public ExoPlayerAdaptor(Context context, List<MediaObject> tutorialList) {
        this.context = context;
        this.TutorialList = tutorialList;
    }

    @NonNull
    @Override
    public ExoPlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.exoplayer_item_layout,null);
        return new ExoPlayerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ExoPlayerViewHolder holder, final int position) {
        holder.Title.setText(TutorialList.get(position).getTutorialName());

        String videopath=TutorialList.get(position).getLink();//"http://192.168.43.28/Rodiproject/drinkshop/Video/testVideo.mp4";
        Uri path=Uri.parse(Common.BASE_URL+videopath);
        final SimpleExoPlayer exoPlayer;
        BandwidthMeter bandwidthMeter=new DefaultBandwidthMeter();
        TrackSelector trackSelector=new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
        exoPlayer= ExoPlayerFactory.newSimpleInstance(context,trackSelector);
        DefaultHttpDataSourceFactory dataSourceFactory=new DefaultHttpDataSourceFactory("Tutorials");
        ExtractorsFactory extractorsFactory=new DefaultExtractorsFactory();
        MediaSource mediaSource=new ExtractorMediaSource(path,dataSourceFactory,extractorsFactory,null,null);
        holder.playerView.setPlayer(exoPlayer);
        exoPlayer.prepare(mediaSource);
        exoPlayer.seekTo(6);
        holder.setiItemClickListener(new IItemClickListener() {
            @Override
            public void onClick(View v) {
                //exoPlayer.setPlayWhenReady(true);

            }
        });
        holder.Like.setText(TutorialList.get(position).getLikes());
        holder.Like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.Like.setText( Integer.parseInt(TutorialList.get(position).getLikes())+1);
                Toast.makeText(context,"You liked the Video",Toast.LENGTH_LONG).show();
            }
        });
        holder.Dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"You disliked the Video",Toast.LENGTH_LONG).show();
                holder.Like.setText( Integer.parseInt(TutorialList.get(position).getLikes())-1);
                context.notify();
            }
        });

    }

    @Override
    public int getItemCount() {
        return TutorialList.size();
    }
}
