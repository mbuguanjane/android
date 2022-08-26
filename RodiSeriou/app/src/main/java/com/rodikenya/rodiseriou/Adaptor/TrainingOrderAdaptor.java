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
import com.rodikenya.rodiseriou.Model.DataMessage;
import com.rodikenya.rodiseriou.Model.MyResponse;
import com.rodikenya.rodiseriou.Model.Token;
import com.rodikenya.rodiseriou.Model.Trainee;
import com.rodikenya.rodiseriou.Model.TrainingItems;
import com.rodikenya.rodiseriou.Model.TrainingOrderModel;
import com.rodikenya.rodiseriou.R;
import com.rodikenya.rodiseriou.Remote.IFCMService;
import com.rodikenya.rodiseriou.Remote.IpService;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrainingOrderAdaptor extends RecyclerView.Adapter<TrainingOrderViewHolder> {
    Context context;
   List<Trainee> trainingOrderModelList;
    String[] spinner_source=new String[]{
            "Cancelled",
            "placed",
            "Processing",
            "Delievering",
            "Delievered"

    };
    ArrayAdapter<String> spinnerArrayAdapter;
    IpService mService;
    public TrainingOrderAdaptor(Context context, List<Trainee> trainingOrderModelList) {
        this.context = context;
        this.trainingOrderModelList = trainingOrderModelList;
        spinnerArrayAdapter=new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,spinner_source);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mService= Common.getIpService();
    }

    @NonNull
    @Override
    public TrainingOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View views= LayoutInflater.from(context).inflate(R.layout.training_order_layout,parent,false);

        return new TrainingOrderViewHolder(views);
    }

    @Override
    public void onBindViewHolder(@NonNull final TrainingOrderViewHolder holder, final int position) {
       holder.order_id.setText(new StringBuilder("#").append(trainingOrderModelList.get(position).getTraineeId()));
        TrainingItems Training = new Gson().fromJson(trainingOrderModelList.get(position).getTrainingDetails(), TrainingItems.class);
       holder.order_address.setText(Training.getName());
       holder.order_comment.setText(" Contacts "+trainingOrderModelList.get(position).getPhone());
       holder.order_price.setText(new StringBuilder("KES ").append(Training.getPrice()));
       holder.setItemClickListener(new IItemClickListener() {

           @Override
           public void onClick(View v) {
               Common.CurrentTraining=trainingOrderModelList.get(position);
           }
       });


    }
    public void FetchToken(String Phonenumber, final TrainingOrderViewHolder holder)
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
        return trainingOrderModelList.size();
    }
}
