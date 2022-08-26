package com.rodikenya.rodiseriou.Adaptor;

import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rodikenya.rodiseriou.Interface.IItemClickListener;
import com.rodikenya.rodiseriou.R;

public class ExoPlayerViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {
    SimpleExoPlayerView playerView;
     TextView Title,Like,Dislike;
     FrameLayout frameLayout;
     Button send;
    MaterialEditText comment;
     IItemClickListener itemClickListener;

    public void setiItemClickListener(IItemClickListener iItemClickListener) {
        this.itemClickListener = iItemClickListener;
    }

    public ExoPlayerViewHolder(@NonNull View itemView) {
        super(itemView);
        playerView=(SimpleExoPlayerView) itemView.findViewById(R.id.video_view);
        Title=(TextView) itemView.findViewById(R.id.productname);
        Like=(TextView) itemView.findViewById(R.id.like);
        Dislike=(TextView) itemView.findViewById(R.id.dislike);
        frameLayout=(FrameLayout) itemView.findViewById(R.id.videoViewWrapper);
        send=(Button) itemView.findViewById(R.id.Comentbttn);
        comment=(MaterialEditText) itemView.findViewById(R.id.commentField);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
     itemClickListener.onClick(v);
    }
}
