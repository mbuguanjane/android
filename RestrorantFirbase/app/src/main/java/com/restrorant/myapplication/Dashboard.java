package com.restrorant.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.restrorant.myapplication.Interface.ItemClicklistener;
import com.restrorant.myapplication.Model.Category;
import com.restrorant.myapplication.Model.RestrorauntModel;
import com.restrorant.myapplication.Model.Reviews;
import com.restrorant.myapplication.ViewHolder.MenuViewHolder;
import com.restrorant.myapplication.ViewHolder.RestrourantViewHolder;
import com.squareup.picasso.Picasso;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    FloatingActionButton fab;
    FirebaseDatabase database;
    DatabaseReference category;
    TextView fullname; //username
    RecyclerView recyclerMenu;
    FirebaseRecyclerAdapter<RestrorauntModel, RestrourantViewHolder>  adapter;

    String Ratings="",restouranrid="";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //init Firebase
        database=FirebaseDatabase.getInstance();
        category=database.getReference("Restaurants");

        recyclerMenu=(RecyclerView)findViewById(R.id.recyclermenu);
        recyclerMenu.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerMenu.setLayoutManager(layoutManager);



        fab=(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        drawerLayout=(DrawerLayout)findViewById(R.id.drawlayout);
        navigationView=(NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Restorant List");
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        View headerView=navigationView.getHeaderView(0);
        fullname=(TextView)headerView.findViewById(R.id.username);
        if(Common.currentUser!=null)
        {
            fullname.setText(Common.currentUser.getFirstName()+" "+Common.currentUser.getLastName());
            loadMenu();
        }else{
            Intent LogoutActivity=new Intent(Dashboard.this,SignIn.class);
            LogoutActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(LogoutActivity);
        }


    }

    private void loadMenu() {
         adapter=new FirebaseRecyclerAdapter<RestrorauntModel, RestrourantViewHolder>(RestrorauntModel.class,R.layout.restorauntlayout,RestrourantViewHolder.class,category) {
            @Override
            protected void populateViewHolder(RestrourantViewHolder menuViewHolder, RestrorauntModel category, int i) {
                menuViewHolder.menuname.setText(category.getName());
                String key = adapter.getRef(i).getKey();

                    settingRating(key,menuViewHolder);



                Picasso.with(getBaseContext()).load(category.getImage()).into(menuViewHolder.imageView);
                RestrorauntModel clickItem=category;
//                menuViewHolder.viewButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                    }
//                });
//                menuViewHolder.viewReserve.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        String key = adapter.getRef(i).getKey();
//                        Intent Foodlist=new Intent(Dashboard.this,Reservation.class);
//                        Foodlist.putExtra("RestourantID",key);
//                        startActivity(Foodlist);
//                        Toast.makeText(Dashboard.this,"Restorant "+category.getName(),Toast.LENGTH_LONG).show();
//
//                    }
//                });
                menuViewHolder.setItemClicklistener(new ItemClicklistener() {
                    @Override
                    public void onClick(View view, int position, boolean loongClick) {
                        String key = adapter.getRef(i).getKey();
                        Intent Foodlist=new Intent(Dashboard.this,Reservation.class);
                        Foodlist.putExtra("RestourantID",key);
                        startActivity(Foodlist);
                    }
                });
            }
        };
        recyclerMenu.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id=menuItem.getItemId();
        switch (id)
        {


            case R.id.Order:
                Intent OrdrActivity=new Intent(Dashboard.this,ProfileDetails.class);
                startActivity(OrdrActivity);
                break;
            case R.id.Reservation:
                Intent ResActivity=new Intent(Dashboard.this,ReservationStatus.class);
                startActivity(ResActivity);
                break;
            case R.id.Admin:
                Intent AdminActivity=new Intent(Dashboard.this,AdminPanel.class);
                startActivity(AdminActivity);
                break;
            case R.id.LogOut:
                Common.currentUser=null;
                Intent LogoutActivity=new Intent(Dashboard.this,SignIn.class);
                LogoutActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(LogoutActivity);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawers();
        }else
        {
            super.onBackPressed();
        }

    }
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
//        if(item.getTitle().equals(Common.RESERVATION))
//        {
//            Intent Foodlist=new Intent(Dashboard.this,Reservation.class);
//            Foodlist.putExtra("RestourantID",adapter.getRef(item.getOrder()).getKey());
//            startActivity(Foodlist);
//            Toast.makeText(Dashboard.this,"Restorant "+adapter.getItem(item.getOrder()).getName(),Toast.LENGTH_LONG).show();
//
//
//        }
        return super.onContextItemSelected(item);
    }
    public void settingRating(String key, RestrourantViewHolder menuViewHolder)
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
                        if(key.equals(reviews.getRestourantID())) {
                            Ratings = reviews.getRatings();
                            restouranrid = reviews.getRestourantID();
                            //ratingBar.setRating(Float.parseFloat(reviews.getRatings()));
                            menuViewHolder.ratingBar.setRating(Float.parseFloat(Ratings));
//                            menuViewHolder.ratingBar.setFocusable(false);
//                            menuViewHolder.ratingBar.setIsIndicator(true);
                           // Toast.makeText(Dashboard.this, "Rating " + reviews.getComments(), Toast.LENGTH_LONG).show();
                        }
                    }else{

                       // Toast.makeText(Dashboard.this,"Review is Empty ",Toast.LENGTH_LONG).show();
                        makeAlert(Dashboard.this,"Failed","Review is Empty");
                    }
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Toast.makeText(Dashboard.this,"Rating "+databaseError.getDetails(),Toast.LENGTH_LONG).show();
                makeAlert(Dashboard.this,"Failed","Rating error "+databaseError.getMessage());
            }
        });
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