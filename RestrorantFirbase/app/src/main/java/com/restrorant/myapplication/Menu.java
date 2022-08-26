package com.restrorant.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.restrorant.myapplication.Interface.ItemClicklistener;
import com.restrorant.myapplication.Model.MenuModel;
import com.restrorant.myapplication.Model.RestrorauntModel;
import com.restrorant.myapplication.ViewHolder.CategoryViewHolder;
import com.restrorant.myapplication.ViewHolder.MenuListViewHolder;
import com.squareup.picasso.Picasso;

public class Menu extends AppCompatActivity {


    FirebaseDatabase database;
    DatabaseReference category;
    RecyclerView recyclerMenu;
    Toolbar toolbar;
    FirebaseRecyclerAdapter<MenuModel, MenuListViewHolder> adapter;

    String RestorautId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //init Firebase
        toolbar=(Toolbar)findViewById(R.id.toolbar);

        database=FirebaseDatabase.getInstance();


        recyclerMenu=(RecyclerView)findViewById(R.id.recyclermenu);
        recyclerMenu.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(Menu.this, LinearLayoutManager.HORIZONTAL, false);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerMenu);
        recyclerMenu.setLayoutManager(layoutManager);

        if(getIntent()!=null)
        {
            RestorautId=getIntent().getStringExtra("RestourantID");
            category=database.getReference("Menu/"+RestorautId);

        }
        if(!RestorautId.isEmpty() && RestorautId!=null)
        {
            loadMenu(RestorautId);

        }

    }

    private void loadMenu(String RestorautId) {
        adapter=new FirebaseRecyclerAdapter<MenuModel, MenuListViewHolder>(MenuModel.class,R.layout.menurowlayout, MenuListViewHolder.class,
                category
        ) {
            @Override
            protected void populateViewHolder(MenuListViewHolder menuViewHolder, MenuModel category, int i) {

                Picasso.with(getBaseContext()).load(category.getImage()).into(menuViewHolder.imageView);
                MenuModel clickItem=category;

                menuViewHolder.setItemClicklistener(new ItemClicklistener() {
                    @Override
                    public void onClick(View view, int position, boolean loongClick) {



                    }
                });
            }
        };
        recyclerMenu.setAdapter(adapter);
    }


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        return super.onContextItemSelected(item);
    }
}