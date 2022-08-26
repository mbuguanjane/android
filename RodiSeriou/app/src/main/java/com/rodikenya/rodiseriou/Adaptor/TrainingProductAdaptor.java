package com.rodikenya.rodiseriou.Adaptor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rodikenya.rodiseriou.Common;
import com.rodikenya.rodiseriou.DashBoard;
import com.rodikenya.rodiseriou.Interface.IItemClickListener;
import com.rodikenya.rodiseriou.MainActivity;
import com.rodikenya.rodiseriou.Model.DataMessage;
import com.rodikenya.rodiseriou.Model.MyResponse;
import com.rodikenya.rodiseriou.Model.Order;
import com.rodikenya.rodiseriou.Model.Token;
import com.rodikenya.rodiseriou.Model.TrainingItems;
import com.rodikenya.rodiseriou.Model.TrainingOrderModel;
import com.rodikenya.rodiseriou.Model.User;
import com.rodikenya.rodiseriou.OrderDetailActivity;
import com.rodikenya.rodiseriou.R;
import com.rodikenya.rodiseriou.Remote.IFCMService;
import com.rodikenya.rodiseriou.Remote.IpService;
import com.squareup.picasso.Picasso;
import com.szagurskii.patternedtextwatcher.PatternedTextWatcher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;

public class TrainingProductAdaptor extends RecyclerView.Adapter<TrainingProductViewHolder> {
    Context context;
   List<TrainingItems> trainingItems;
    IpService mService;
    public TrainingProductAdaptor(Context context, List<TrainingItems> trainingItems) {
        this.context = context;
        this.trainingItems = trainingItems;
        mService=Common.getIpService();
    }

    @NonNull
    @Override
    public TrainingProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View views= LayoutInflater.from(context).inflate(R.layout.training_item_layout,parent,false);

        return new TrainingProductViewHolder(views);
    }

    @Override
    public void onBindViewHolder(@NonNull TrainingProductViewHolder holder, final int position) {
       holder.ProductPrice.setText("Training Fee KES "+trainingItems.get(position).getPrice());
       holder.ProductName.setText(trainingItems.get(position).getName());
       if(!TextUtils.isEmpty(trainingItems.get(position).getLink())) {
           Picasso.with(context).load(Common.BASE_URL + trainingItems.get(position).getLink()).into(holder.ProductLink);
       }
       holder.ProductDescription.setText(trainingItems.get(position).getDescription());
       holder.Register.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(Integer.parseInt(trainingItems.get(position).getAvailability())>0) {
                   CreateDialog(position);
               }else
               {
                   Toast.makeText(context,"Not Available",Toast.LENGTH_LONG).show();
               }
           }
       });
       holder.ProductPrice.setVisibility(View.GONE);
       holder.Watch.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

           }
       });
        if(Integer.parseInt(trainingItems.get(position).getAvailability())>0) {
            holder.availability.setText("available");
        }else
        {
            holder.availability.setText("Not available");
        }
        holder.setItemClickListener(new IItemClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void CreateDialog(final int position) {
        registertraining(position);
    }

    private void registertraining(final int position) {
        RegisterUser(position);
    }
    private void RegisterUser(final int position) {
        final AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle("Register Training");

        View  register_layout=LayoutInflater.from(context).inflate(R.layout.regitser_training_layout,null);
        final MaterialEditText edt_name=(MaterialEditText)register_layout.findViewById(R.id.edt_name);
        final MaterialEditText edt_address=(MaterialEditText)register_layout.findViewById(R.id.edt_Address);
        final MaterialEditText edt_member=(MaterialEditText)register_layout.findViewById(R.id.edt_member);
        final MaterialEditText edt_phone=(MaterialEditText)register_layout.findViewById(R.id.edt_Phonenumber);
       // edt_birthdate.addTextChangedListener(new PatternedTextWatcher("####-##-##"));
        final CountryCodePicker Countrycode=(CountryCodePicker)register_layout.findViewById(R.id.countrycode);
        builder.setView(register_layout);
        final AlertDialog alertDialog=builder.create();
        Button registerbutton=(Button)register_layout.findViewById(R.id.Registerbtn);
        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();

                if( TextUtils.isEmpty(edt_name.getText().toString()))
                {
                    Toast.makeText(context,"Name missing",Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(edt_address.getText().toString()))
                {
                    Toast.makeText(context,"Address missing",Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(edt_member.getText().toString()))
                {
                    Toast.makeText(context,"Member Number missing",Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(edt_phone.getText().toString()))
                {
                    Toast.makeText(context,"Phone number is missing",Toast.LENGTH_LONG).show();
                }else {
                    mService.registerTraining(Countrycode.getDefaultCountryCodeWithPlus() + edt_phone.getText().toString()
                            ,new Gson().toJson(trainingItems.get(position)),
                            edt_member.getText().toString(),
                            edt_address.getText().toString(),
                            edt_name.getText().toString())
                            .enqueue(new Callback<TrainingOrderModel>() {
                                @Override
                                public void onResponse(Call<TrainingOrderModel> call, Response<TrainingOrderModel> response) {
                                    Toast.makeText(context, "Registered SuccessFully", Toast.LENGTH_LONG).show();
                                    getFirebaseToken(response.body().getTraineeId(),position);
                                }

                                @Override
                                public void onFailure(Call<TrainingOrderModel> call, Throwable t) {
                                    Toast.makeText(context, "Failed " + t.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });
        alertDialog.show();
    }
    private void getFirebaseToken(final String orderid, final int position){



        mService.getFireBaseToken("Server_01","1")
                .enqueue(new Callback<Token>() {
                    @Override
                    public void onResponse(Call<Token> call, Response<Token> response) {
                        //send Notification
                        if(!TextUtils.isEmpty(response.body().getToken())) {

                            SendNotification(response.body().getToken(),orderid,position);
                            System.out.println("Token Remote" + response.body().getToken());
                            System.out.println("Token Local" + FirebaseInstanceId.getInstance().getToken());
                        }
                        //Toast.makeText(CartActivity.this,"Token "+response.body().getToken(),Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<Token> call, Throwable t) {
                        Toast.makeText(context,"Failed "+t.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void SendNotification(String token,String orderid,int position)  {
        Map<String,String> contentSend=new HashMap<>();
        contentSend.put("title","# "+orderid+" You have Training "+trainingItems.get(position).getName()+"   Registration ");
        contentSend.put("message","please check New Registration For Training");
        DataMessage dataMessage=new DataMessage();
        if(!TextUtils.isEmpty(token))
        {
            dataMessage.setTo(token);
            dataMessage.setData(contentSend);
            IFCMService ifcmService=Common.getFCMService();
            ifcmService.sendNotification(dataMessage)
                    .enqueue(new Callback<MyResponse>() {
                        @Override
                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                            Toast.makeText(context,"Notification Sent Successfully"+response.body(),Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Call<MyResponse> call, Throwable t) {
                            Toast.makeText(context,"Failed Notification",Toast.LENGTH_LONG).show();
                        }
                    });
        }else {
            Toast.makeText(context,"Empty Token",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
        return trainingItems.size();
    }
}
