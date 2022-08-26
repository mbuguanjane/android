package com.rodikenya.rodiseriou.Adaptor;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.rodikenya.rodiseriou.CartActivity;
import com.rodikenya.rodiseriou.CentreDetails;
import com.rodikenya.rodiseriou.Common;
import com.rodikenya.rodiseriou.Interface.IItemClickListener;
import com.rodikenya.rodiseriou.Model.CentreModel;
import com.rodikenya.rodiseriou.Model.DataMessage;
import com.rodikenya.rodiseriou.Model.MyResponse;
import com.rodikenya.rodiseriou.Model.Token;
import com.rodikenya.rodiseriou.Model.TrainingItems;
import com.rodikenya.rodiseriou.Model.centrebooking;
import com.rodikenya.rodiseriou.R;
import com.rodikenya.rodiseriou.Remote.IFCMService;
import com.rodikenya.rodiseriou.Remote.IpService;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CentrePricingAdaptor extends RecyclerView.Adapter<CentrePricingViewHolder> {
    Context context;
   List<CentreModel> centreModels;
    IpService mService;
    private int mYear, mMonth, mDay;
    String Bookingdate="";

    public CentrePricingAdaptor(Context context, List<CentreModel> centreModels) {
        this.context = context;
        this.centreModels = centreModels;
        mService=Common.getIpService();
    }

    @NonNull
    @Override
    public CentrePricingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View views= LayoutInflater.from(context).inflate(R.layout.centre_pricing_layout,parent,false);

        return new CentrePricingViewHolder(views);
    }

    @Override
    public void onBindViewHolder(@NonNull final CentrePricingViewHolder holder, final int position) {
       holder.EventPrice.setText("KES "+centreModels.get(position).getEventPrice());
       holder.Eventname.setText(centreModels.get(position).getEventName());
       holder.EventDescription.setText(centreModels.get(position).getEventName());
       if(Integer.parseInt(centreModels.get(position).getAvailable())>0)
       {
           holder.EventAvailable.setText("Available");
       }else
       {
           holder.EventAvailable.setText("Booked");
       }
        Picasso.with(context).load(Common.BASE_URL+centreModels.get(position).getLink()).fit().into(holder.EventImage);
        System.out.println("Centre Image "+Common.BASE_URL+centreModels.get(position).getLink());


        holder.setItemClickListener(new IItemClickListener() {
            @Override
            public void onClick(View v) {
             context.startActivity(new Intent(context, CentreDetails.class));
             Common.CurrentCentrePrice=centreModels.get(position);
            }
        });
    }








    @Override
    public int getItemCount() {
        return centreModels.size();
    }
}
