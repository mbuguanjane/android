package com.rodikenya.rodiseriou.Adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rodikenya.rodiseriou.Common;
import com.rodikenya.rodiseriou.R;
import com.rodikenya.rodiseriou.RoomDatabase.Model.Favourite;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavouriteAdaptor extends RecyclerView.Adapter<FavouriteAdaptor.FavouriteViewHolder> {
    Context context;
    List<Favourite> favouriteList;

    public FavouriteAdaptor(Context context, List<Favourite> favouriteList) {
        this.context = context;
        this.favouriteList = favouriteList;
    }

    public class FavouriteViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView productname,productprice;
        public RelativeLayout view_background;
        public LinearLayout view_foreground;
        public FavouriteViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.img_Fav);
            productname=(TextView)itemView.findViewById(R.id.productnameFav);
            productprice=(TextView)itemView.findViewById(R.id.priceFav);
            view_background=(RelativeLayout)itemView.findViewById(R.id.view_background);
            view_foreground=(LinearLayout)itemView.findViewById(R.id.view_foreground);
        }
    }
    @NonNull
    @Override
    public FavouriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.fav_item_layout,parent,false);

        return new FavouriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteViewHolder holder, int position) {
        if(favouriteList.get(position).getLink().contains("http://192.168.43.28/Rodiproject/drinkshop/"))
        {
            Picasso.with(context).load(favouriteList.get(position).getLink()).into(holder.imageView);
        }else {
            Picasso.with(context)
                    .load(Common.BASE_URL + favouriteList.get(position).getLink())
                    .into(holder.imageView);
        }

        holder.productprice.setText("KES "+favouriteList.get(position).getPrice());
        holder.productname.setText(favouriteList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return favouriteList.size();
    }

     public  void removeItem(int position)
     {
         favouriteList.remove(position);
         notifyItemRemoved(position);
     }
     public  void restoreItem(Favourite item,int position)
     {
        favouriteList.add(position, item);
        notifyItemInserted(position);
     }

}
