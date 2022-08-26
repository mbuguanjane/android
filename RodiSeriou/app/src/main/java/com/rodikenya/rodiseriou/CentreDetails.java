package com.rodikenya.rodiseriou;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rodikenya.rodiseriou.Adaptor.CentreDetailsAdaptor;
import com.rodikenya.rodiseriou.Adaptor.CentreHallAdaptor;
import com.rodikenya.rodiseriou.Model.Conferencepackage;
import com.rodikenya.rodiseriou.Model.DataMessage;
import com.rodikenya.rodiseriou.Model.MyResponse;
import com.rodikenya.rodiseriou.Model.Token;
import com.rodikenya.rodiseriou.Model.centrebooking;
import com.rodikenya.rodiseriou.Remote.IFCMService;
import com.rodikenya.rodiseriou.Remote.IpService;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CentreDetails extends AppCompatActivity {
    RecyclerView details_recycle;
    CompositeDisposable compositeDisposable;
    IpService mService;
    MaterialEditText people;
    private int mYear, mMonth, mDay;
    String Bookingdate="";
    TextView provisions;
    Button Register,date;
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centre_details);
        details_recycle=(RecyclerView) findViewById(R.id.CentrerateList);
        people=(MaterialEditText)findViewById(R.id.edt_member);
        details_recycle.setHasFixedSize(true);
        details_recycle.setLayoutManager(new LinearLayoutManager(this));
        provisions=(TextView)findViewById(R.id.provision);
        provisions.setText(Common.CurrentCentrePrice.getProvision());
        img=(ImageView)findViewById(R.id.centreimage);
        Picasso.with(CentreDetails.this).load(Common.BASE_URL+Common.CurrentCentrePrice.getLink()).into(img);
         compositeDisposable=new CompositeDisposable();
        Register=(Button) findViewById(R.id.Register);
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                CreateDialog();

            }
        });
        date=(Button)findViewById(R.id.setdate);
        Register.setEnabled(false);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ChooseDate();
                Register.setEnabled(true);
            }
        });
         mService= Common.getIpService();
        LoadPackages();
    }
    private void ChooseDate() {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(CentreDetails.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        //txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        Bookingdate=String.valueOf(year +"-" + (monthOfYear + 1) + "-"+dayOfMonth);


                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void CreateDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(CentreDetails.this);
        builder.setTitle("Booking");
        builder.setMessage("Are you sure you want to Book");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(Integer.parseInt(Common.CurrentCentrePrice.getAvailable())>0) {
                            registertraining();
                            Bookingdate="";
                        }else
                        {
                            Toast.makeText(CentreDetails.this,"Not available ",Toast.LENGTH_LONG).show();
                            Common.currentpackage.clear();
                            Bookingdate="";
                        }


                    }
                });
        builder.show();
    }
    private void registertraining() {

        if(Common.CurrentUser!=null)
        {
            if(!TextUtils.isEmpty(Bookingdate) && !TextUtils.isEmpty(people.getText().toString())) {
                mService.BookNow(new Gson().toJson(Common.CurrentCentrePrice), Common.CurrentUser.getPhone(), Bookingdate,Common.CurrentCentrePrice.getEventID(),new Gson().toJson(Common.currentpackage),people.getText().toString())
                        .enqueue(new Callback<centrebooking>() {
                            @Override
                            public void onResponse(Call<centrebooking> call, Response<centrebooking> response) {
                                Toast.makeText(CentreDetails.this, "Booked SuccessFully ", Toast.LENGTH_LONG).show();
                                Bookingdate="";
                                getFirebaseToken(response.body().getBookingId());
                                Common.currentpackage.clear();

                            }

                            @Override
                            public void onFailure(Call<centrebooking> call, Throwable t) {
                                Toast.makeText(CentreDetails.this, "Failed " + t.getMessage(), Toast.LENGTH_LONG).show();
                                Bookingdate="";
                            }
                        }  );



            }else
            {
                Toast.makeText(CentreDetails.this, "Please Select the Date and people", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            Toast.makeText(CentreDetails.this, "Please login First", Toast.LENGTH_LONG).show();
        }
    }

    private void getFirebaseToken(final String eventID){



        mService.getFireBaseToken("Server_01","1")
                .enqueue(new Callback<Token>() {
                    @Override
                    public void onResponse(Call<Token> call, Response<Token> response) {
                        //send Notification
                        if(!TextUtils.isEmpty(response.body().getToken())) {

                            SendNotification(response.body().getToken(),eventID);
                            System.out.println("Token Remote" + response.body().getToken());
                            System.out.println("Token Local" + FirebaseInstanceId.getInstance().getToken());
                        }
                        //Toast.makeText(CartActivity.this,"Token "+response.body().getToken(),Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<Token> call, Throwable t) {
                        Toast.makeText(CentreDetails.this,"Failed "+t.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
    }
    private void SendNotification(String token,String orderid)  {
        Map<String,String> contentSend=new HashMap<>();
        contentSend.put("title","# "+orderid+" You have "+Common.CurrentCentrePrice.getEventName()+"  Booking ");
        contentSend.put("message","please check booking");
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
                            Toast.makeText(CentreDetails.this,"Notification Sent Successfully"+response.body(),Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Call<MyResponse> call, Throwable t) {
                            Toast.makeText(CentreDetails.this,"Failed Notification",Toast.LENGTH_LONG).show();
                        }
                    });
        }else {
            Toast.makeText(CentreDetails.this,"Empty Token",Toast.LENGTH_LONG).show();
        }
    }
    private void LoadPackages() {

            compositeDisposable.add(mService.getConferencePackage(Common.CurrentCentrePrice.getOptions())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Consumer<List<Conferencepackage>>() {
                        @Override
                        public void accept(List<Conferencepackage> conferencepackages) throws Exception {
                            loadList(conferencepackages);
                            System.out.println("conference "+conferencepackages.get(0).getDescription()+" size "+conferencepackages.size());
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Toast.makeText(CentreDetails.this, "Failed " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
            );


    }

    private void loadList(List<Conferencepackage> conferencepackages) {
        CentreDetailsAdaptor adaptor=new CentreDetailsAdaptor(CentreDetails.this,conferencepackages);
        details_recycle.setAdapter(adaptor);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    @Override
    protected void onStop() {
        super.onStop();
        compositeDisposable.clear();
    }
}