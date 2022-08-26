package com.rodikenya.rodiseriou.RoomDatabase.Local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.rodikenya.rodiseriou.RoomDatabase.Model.Cart;
import com.rodikenya.rodiseriou.RoomDatabase.Model.Favourite;

@Database(entities = {Cart.class, Favourite.class} ,version = RodiDatabaseProj.DATABASE_VERSION,exportSchema = false)
public abstract class RodiDatabaseProj extends RoomDatabase {




        public  static  final int DATABASE_VERSION=1;
        public static  final String DATABASENAME="Cartdatabase";

         public abstract CartDAO cartDAO();
         public abstract FavDAO favDAO();
          private  static RodiDatabaseProj mInstance;
          public static RodiDatabaseProj getInstance(Context context)
          {
               if(mInstance==null)
               {
                   mInstance= Room.databaseBuilder(context, RodiDatabaseProj.class,DATABASENAME)
                             .fallbackToDestructiveMigration()
                              .build();
               }
               return mInstance;
          }

}
