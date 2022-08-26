package com.restrorant.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Rating;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.restrorant.myapplication.Model.Banner;
import com.restrorant.myapplication.Model.Food;
import com.restrorant.myapplication.Model.Request;
import com.restrorant.myapplication.Model.ReservationReq;
import com.restrorant.myapplication.Model.RestrorauntModel;
import com.restrorant.myapplication.Model.Reviews;
import com.restrorant.myapplication.Model.User;
import com.restrorant.myapplication.Retrofit.MpesaInterface;
import com.restrorant.myapplication.RoomDatab.Model.CartModel;
import com.restrorant.myapplication.RoomDatab.RoomDB;
import com.restrorant.myapplication.ViewHolder.RestrourantViewHolder;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Reservation extends AppCompatActivity {
    TextView food_name,food_price,food_description;
    Spinner editLocation,editTime,editPeople;
    ImageView food_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    MaterialEditText editDate;
    Button whatsappBtn,ViewMenu;
    RatingBar ratingBar;
    String imageURl="";
    String foodId="";
    FirebaseDatabase database;
    DatabaseReference Reservationdetail;
    DatabaseReference RestourantDetail;
    RestrorauntModel currentRetrourant;
    SliderLayout sliderlayout;
    //Room database
    RoomDB roomdatabase;
    List<CartModel> dataList=new ArrayList<>();
    MpesaInterface mpesaInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        //init Roomdatabase
        database=FirebaseDatabase.getInstance();
        roomdatabase=RoomDB.getInstance(this);
        RestourantDetail=database.getReference("Restaurants");
        mpesaInterface=Common.getMpesa();
        ratingBar=(RatingBar)findViewById(R.id.RatingBar);

        //slider
        sliderlayout = (SliderLayout) findViewById(R.id.Slider);

        Reservationdetail=database.getReference("Reservation");
        ViewMenu=(Button)findViewById(R.id.ViewMenu);


        whatsappBtn=(Button)findViewById(R.id.whatsappBtn);
        whatsappBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent waIntent = new Intent(Intent.ACTION_SEND);
                waIntent.setType("text/plain");
                waIntent.setPackage("com.whatsapp");
                if (waIntent != null) {
                    waIntent.putExtra(
                            Intent.EXTRA_TEXT,
                            "Welcome to our Restraunt www.eatit.com");
                    startActivity(Intent.createChooser(waIntent, "Share with"));
                } else
                   // Toast.makeText(Reservation.this, "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
                makeAlert(Reservation.this,"Error","Whatsapp Not Installed");
            }
        });

        food_image=(ImageView)findViewById(R.id.Image_food);
        btnCart=(FloatingActionButton)findViewById(R.id.btnCart);
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();

                //Toast.makeText(Reservation.this,"Added to Cart",Toast.LENGTH_LONG).show();

            }
        });
        collapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.collasping);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);
        food_description=(TextView)findViewById(R.id.food_description);
        food_name=(TextView)findViewById(R.id.food_name);
        food_price=(TextView)findViewById(R.id.food_price);

        if(getIntent()!=null) {
            foodId = getIntent().getStringExtra("RestourantID");
           /// Toast.makeText(Reservation.this,"RestourantID "+foodId,Toast.LENGTH_LONG).show();
            if(!foodId.isEmpty())
            {
                getFoodDetail(foodId);

            }

        }
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                //Toast.makeText(Reservation.this,"Rating "+ rating,Toast.LENGTH_LONG).show();

            }
        });
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(fromUser) {
                    showAlertDialog(String.valueOf(ratingBar.getRating()));
                }
            }
        });

        ViewMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Foodlist=new Intent(Reservation.this,Menu.class);
                Foodlist.putExtra("RestourantID",foodId);
                startActivity(Foodlist);

            }
        });
        settingRating();



    }
    private void displayImage(List<Banner> banners) {
        HashMap<String,String> bannerMap=new HashMap<>();
        for(Banner item:banners)
        {
            bannerMap.put(item.getName(),imageURl);
        }
        for(String name:bannerMap.keySet())
        {
            TextSliderView textSliderView=new TextSliderView(this);
            textSliderView.description(name)
                    .image(bannerMap.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);
            sliderlayout.addSlider(textSliderView);
        }

    }
    private void getFoodDetail(String foodId) {
        RestourantDetail.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentRetrourant=snapshot.getValue(RestrorauntModel.class);
                //Picasso.with(getBaseContext()).load(currentRetrourant.getImage()).into(food_image);
                imageURl=currentRetrourant.getImage();
                //Making Slider
                List<Banner> bannerList=new ArrayList<>();
                for(int i=0;i<5;i++)
                {
                    bannerList.add(new Banner(foodId,currentRetrourant.getName(),imageURl));
                }
                displayImage( bannerList);
                //end Slider
                collapsingToolbarLayout.setTitle(currentRetrourant.getName());
                food_price.setText("Reservation Price: "+String.valueOf(currentRetrourant.getPrice()));
                food_name.setText("Restourant name: "+currentRetrourant.getName());
                food_description.setText("Restourant Description: "+currentRetrourant.getDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void showDialog() {
        AlertDialog.Builder alertdialog=new AlertDialog.Builder(Reservation.this);
        alertdialog.setTitle("Add Reservation Details");
        alertdialog.setMessage("Please fill full information");
        LayoutInflater inflater=this.getLayoutInflater();
        View add_menu_layout=inflater.inflate(R.layout.add_reservation_layout,null);
        editDate=add_menu_layout.findViewById(R.id.editDate);
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c=Calendar.getInstance();
                Integer month=c.get(Calendar.MONTH);
                Integer day=c.get(Calendar.DAY_OF_MONTH);
                Integer year=c.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog =new DatePickerDialog(Reservation.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        editDate.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                    }
                },year,month,day);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });
        editTime=add_menu_layout.findViewById(R.id.editTime);
        editPeople=add_menu_layout.findViewById(R.id.editPeople);
        //Spinner
        this.makeSpinnerLocation(add_menu_layout);
        this.makeSpinnerPeople(add_menu_layout);
        this.makeSpinnerTime(add_menu_layout);
        //end spinner


        alertdialog.setView(add_menu_layout);
        alertdialog.setIcon(R.drawable.ic_baseline_add_shopping_cart_24);
        alertdialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MpesaPayment(String.valueOf(currentRetrourant.getPrice()));
                dialog.dismiss();
            }
        });
        alertdialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertdialog.show();

    }

    private void makeSpinnerLocation(View add_menu_layout) {
        String[] arraySpinner = new String[] {
                "Select Location",
                "Indoor",
                "Outdoor",
                "Window",
                "Middle position"
        };
        editLocation = (Spinner) add_menu_layout.findViewById(R.id.Spinner01);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editLocation.setAdapter(adapter);
    }
    private void makeSpinnerTime(View add_menu_layout) {
        String[] arraySpinner = new String[] {
                "Select Time",
                "8:00 AM",
                "9:00 AM",
                "10:00 AM",
                "11:00 AM",
                "12:00 PM",
                "13:00 PM",
                "14:00 PM",
                "15:00 PM",
                "16:00 PM",
                "17:00 PM",
                "18:00 PM",
                "19:00 PM",
                "20:00 PM",
                "21:00 PM",
                "22:00 PM",
        };
        editTime = (Spinner) add_menu_layout.findViewById(R.id.editTime);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editTime.setAdapter(adapter);
    }
    private void makeSpinnerPeople(View add_menu_layout) {
        String[] arraySpinner = new String[] {
                "Select Number of People",
                "1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20"

        };
        editPeople = (Spinner) add_menu_layout.findViewById(R.id.editPeople);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editPeople.setAdapter(adapter);
    }
    private void MpesaPayment(String amount)
    {
        if(Integer.parseInt(amount)>0) {


        mpesaInterface.sendPayment(Common.currentUser.getPhone(),amount,"ReservstionFee")
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        sendRequest();
                        makeAlert(Reservation.this,"Succes","sent successfully");
                       // Toast.makeText(Reservation.this,"sent successfully "+response.body().toString(),Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        makeAlert(Reservation.this,"Failed","Failed to send"+t.getMessage());
                       // Toast.makeText(Reservation.this,"Failed to send "+t.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
        }else
        {
            makeAlert(Reservation.this,"Error","Invalid Amount");
            //Toast.makeText(Reservation.this,"You amount is invalid",Toast.LENGTH_LONG).show();
        }
    }

    private void sendRequest() {
        if(checkEmpty()) {
            ReservationReq requestdata = new ReservationReq(
                    Common.currentUser.getPhone(),
                    Common.currentUser.getFirstName() + " " + Common.currentUser.getLastName(),
                    editDate.getText().toString(),
                    editTime.getSelectedItem().toString(),
                    editPeople.getSelectedItem().toString(),
                    "0",
                    String.valueOf(currentRetrourant.getPrice()),
                    editLocation.getSelectedItem().toString()

            );
            Reservationdetail.child(new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss", Locale.getDefault()).format(new Date()))
                    .setValue(requestdata);
            Intent intent = new Intent(Reservation.this, SuccessReservation.class);
            intent.putExtra("Title", "THANK YOU");
            String Description="Success\n" +
                                       "Date:"+editDate.getText().toString()+"\n"+
                                       "Time:"+editTime.getSelectedItem().toString()+"\n"+
                                        "Number of People: "+ editPeople.getSelectedItem().toString();
            intent.putExtra("Description",Description);
            startActivity(intent);

            mpesaInterface.sendReceipt("+"+Common.currentUser.getPhone(),Description)
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            makeAlert(Reservation.this,"Success","Reservation done!!");
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            makeAlert(Reservation.this,"Failed","Reservation Failed!!"+t.getMessage());
                        }
                    });
        }
    }
    public void sendReview(String comments,String rating,String restourantID) {

        Reviews reviews=new Reviews();
        reviews.setComments(comments);
        reviews.setRatings(rating);
        reviews.setRestourantID(restourantID);
        reviews.setPhone(Common.currentUser.getPhone());

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference tasksRef = rootRef.child("Reviews").push();

        tasksRef.setValue(reviews);
       // Toast.makeText(Reservation.this,"Saved successfully",Toast.LENGTH_LONG).show();
        makeAlert(Reservation.this,"Success","Sent successfully");
    }
    private void showAlertDialog(String rating) {
        //here
        final EditText comments = new EditText(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("One more Step ")
                .setMessage("Add comment")
                .setView(comments)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendReview(comments.getText().toString(), rating,foodId);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();


    }
    //setting rating
    public void settingRating()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference.child("Reviews").orderByChild("phone").equalTo(Common.currentUser.getPhone());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for(DataSnapshot data:dataSnapshot.getChildren())
                {
                    Reviews reviews=dataSnapshot.child(data.getKey()).getValue(Reviews.class);
                    System.out.println("hello there "+data.getKey());
                    if(reviews!=null) {
                        if(foodId.equals(reviews.getRestourantID())) {
                            ratingBar.setRating(Float.parseFloat(reviews.getRatings()));
                           // Toast.makeText(Reservation.this, "Rating " + reviews.getRatings(), Toast.LENGTH_LONG).show();
                        }
                    }else{
                        makeAlert(Reservation.this,"Error","Review is Empty");
                       // Toast.makeText(Reservation.this,"Review is Empty ",Toast.LENGTH_LONG).show();
                    }
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Toast.makeText(Reservation.this,"Rating "+databaseError.getDetails(),Toast.LENGTH_LONG).show();
                makeAlert(Reservation.this,"Error","Rating Error"+databaseError.getMessage());
            }
        });
    }
    public boolean checkEmpty()
    {
        if(editDate.getText().toString().isEmpty())
        {
            makeAlert(Reservation.this, "Error", "Date is Required" );
            return false;
        }else if(editTime.getSelectedItem().toString().isEmpty())
        {
            makeAlert(Reservation.this, "Error", "Time Name is Required" );
            return false;
        }
        else if(editPeople.getSelectedItem().toString().isEmpty())
        {
            makeAlert(Reservation.this, "Error", "Number of People is Required" );
            return false;
        }
        else if( editLocation.getSelectedItem().equals("Select Location"))
        {
            makeAlert(Reservation.this, "Error", "Location is Required" );
            return false;
        }

        return true;
    }

    public void makeAlert(Context context, String Title, String Message )
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle(Title);
        builder1.setMessage(Message);
        builder1.setCancelable(true);
        builder1.setIcon(R.drawable.ic_baseline_info_24);

        builder1.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}