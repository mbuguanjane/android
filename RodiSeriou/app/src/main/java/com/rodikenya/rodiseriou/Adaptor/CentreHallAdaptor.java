package com.rodikenya.rodiseriou.Adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.rodikenya.rodiseriou.Common;
import com.rodikenya.rodiseriou.Interface.IItemClickListener;
import com.rodikenya.rodiseriou.Model.CentreModel;
import com.rodikenya.rodiseriou.Model.DataMessage;
import com.rodikenya.rodiseriou.Model.MyResponse;
import com.rodikenya.rodiseriou.Model.Token;
import com.rodikenya.rodiseriou.Model.centrebooking;
import com.rodikenya.rodiseriou.R;
import com.rodikenya.rodiseriou.Remote.IFCMService;
import com.rodikenya.rodiseriou.Remote.IpService;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CentreHallAdaptor extends RecyclerView.Adapter<CentreHallViewHolder> {
    Context context;
   List<centrebooking> centrebookingList;
    ArrayAdapter<String> spinnerArrayAdapter;
    String[] spinner_source=new String[]{
            "Cancelled",
            "placed",
            "Processing",
            "Success"

    };

    IpService mService;
    CompositeDisposable compositeDisposable=new CompositeDisposable();

    public CentreHallAdaptor(final Context context, List<centrebooking> centrebookingList) {
        this.context = context;
        this.centrebookingList = centrebookingList;
        mService= Common.getIpService();
         spinnerArrayAdapter=new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,spinner_source);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    }

    @NonNull
    @Override
    public CentreHallViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View views= LayoutInflater.from(context).inflate(R.layout.centre_hall_layout,parent,false);

        return new CentreHallViewHolder(views);
    }

    @Override
    public void onBindViewHolder(@NonNull final CentreHallViewHolder holder, final int position) {
        CentreModel Event = new Gson().fromJson(centrebookingList.get(position).getEventDetails(), CentreModel.class);
       holder.order_id.setText(new StringBuilder("#").append(centrebookingList.get(position).getBookingId()));
       holder.order_address.setText("On Date "+centrebookingList.get(position).getBookingDate()+"\t\t Contacts "+centrebookingList.get(position).getPhone());
       holder.order_comment.setText(Event.getEventName());
       holder.order_price.setText(new StringBuilder("KES ").append(Event.getEventPrice()));
       holder.setItemClickListener(new IItemClickListener() {
                      @Override
           public void onClick(View view) {
                          Common.CurrentCentre=centrebookingList.get(position);
           }
       });



    }



    public void FetchToken(String Phonenumber, final CentreHallViewHolder holder)
    {
        System.out.println("client "+Phonenumber);
        mService.getFireBaseToken(Phonenumber,"0")
                .enqueue(new Callback<Token>() {
                    @Override
                    public void onResponse(Call<Token> call, Response<Token> response) {
                        //send Notification
                        System.out.println("Token Remote"+response.body().getToken());
                        System.out.println("Token Local"+ FirebaseInstanceId.getInstance().getToken());
                        //Toast.makeText(CartActivity.this,"Token "+response.body().getToken(),Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<Token> call, Throwable t) {
                        Toast.makeText(context,"Failed "+t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return centrebookingList.size();
    }
}
