package com.restrorant.myapplication.RoomDatab;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.restrorant.myapplication.RoomDatab.Dao.cartDAO;
import com.restrorant.myapplication.RoomDatab.Model.CartModel;

@Database(entities = {CartModel.class},version = 3,exportSchema = false)
public abstract class RoomDB extends RoomDatabase {
    private static  RoomDB database;




    private static String DATABASE_NAME="CartDB";
    public synchronized static RoomDB getInstance(Context context)
    {
        if(database==null)
        {
            database= Room.databaseBuilder(context.getApplicationContext(),RoomDB.class,DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }
    public abstract cartDAO cartDAO();
}
