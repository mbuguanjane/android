package com.rodikenya.rodiseriou.Adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.rodikenya.rodiseriou.Common;
import com.rodikenya.rodiseriou.Interface.IItemClickListener;
import com.rodikenya.rodiseriou.Model.CentreModel;
import com.rodikenya.rodiseriou.Model.Conferencepackage;
import com.rodikenya.rodiseriou.Model.Token;
import com.rodikenya.rodiseriou.Model.centrebooking;
import com.rodikenya.rodiseriou.R;
import com.rodikenya.rodiseriou.Remote.IpService;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CentreDetailsAdaptor extends RecyclerView.Adapter<CentreDetailViewHolder> {
    Context context;
   List<Conferencepackage> conferencepackages;




    public CentreDetailsAdaptor(Context context, List<Conferencepackage> conferencepackages) {
        this.context = context;
        this.conferencepackages = conferencepackages;



    }

    @NonNull
    @Override
    public CentreDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View views= LayoutInflater.from(context).inflate(R.layout.centredescription,parent,false);

        return new CentreDetailViewHolder(views);
    }

    @Override
    public void onBindViewHolder(@NonNull final CentreDetailViewHolder holder, final int position) {
       System.out.println("Description"+conferencepackages.get(1).getDescription());
      holder.descrip.setText(conferencepackages.get(position).getDescription());
      holder.descrip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
              if(b) {
                  Common.currentpackage.add(conferencepackages.get(position));
                  Toast.makeText(context,"Contenthere "+Common.currentpackage.size(),Toast.LENGTH_LONG).show();
              }else
              {

                      Common.currentpackage.remove(conferencepackages.get(position));
                  Toast.makeText(context,"Contenthere "+Common.currentpackage.size(),Toast.LENGTH_LONG).show();

              }
          }
      });
      holder.Price.setText("Ksh "+conferencepackages.get(position).getPrice());
       holder.setItemClickListener(new IItemClickListener() {
                      @Override
           public void onClick(View view) {
                        //  Common.CurrentCentrePricing=conferencepackages..get(position);
           }
       });



    }




    @Override
    public int getItemCount() {
        return conferencepackages.size();
    }
}
