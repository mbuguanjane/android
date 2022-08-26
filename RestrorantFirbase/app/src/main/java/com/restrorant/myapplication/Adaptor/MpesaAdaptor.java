package com.restrorant.myapplication.Adaptor;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.restrorant.myapplication.Model.Itemm;
import com.restrorant.myapplication.Model.stkCallback;
import com.restrorant.myapplication.R;
import com.restrorant.myapplication.RoomDatab.Model.CartModel;
import com.restrorant.myapplication.ViewHolder.CartViewHolder;
import com.restrorant.myapplication.ViewHolder.MpesaViewHolder;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MpesaAdaptor extends RecyclerView.Adapter<MpesaViewHolder> {
    private List<Itemm> itemms = new ArrayList<>();
    private Context context;

    public MpesaAdaptor(List<Itemm> itemms, Context context) {
        this.itemms = itemms;
        this.context = context;
    }

    @NonNull
    @Override
    public MpesaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.mpesalayout, parent, false);
        return new MpesaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MpesaViewHolder holder, int position) {
      holder.Amount.setText("Amount: "+String.valueOf(itemms.get(position).getAmount()));
        holder.MpesaReceiptNumber.setText("ReceiptNumber: "+itemms.get(position).getMpesaReceiptNumber());
        holder.PhoneNumber.setText("PhoneNumber: "+String.valueOf(itemms.get(position).getPhoneNumber()));
        holder.TransactionDate.setText("Date: "+getDateFromTimeStamp(itemms.get(position).getTransactionDate()));

    }

    @Override
    public int getItemCount() {
        return itemms.size();
    }


    private String getDateFromTimeStamp(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("EEE MMM dd hh:mm:ss  ", cal).toString();
        return date;
    }
}
