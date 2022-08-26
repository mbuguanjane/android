package com.rodikenya.rodiseriou.RoomDatabase.Local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.rodikenya.rodiseriou.RoomDatabase.Model.Favourite;

import java.util.List;

import io.reactivex.Flowable;
@Dao
public interface FavDAO {



    @Query("SELECT * FROM Favourite")
    Flowable<List<Favourite>> getFavItems();

    @Query("SELECT EXISTS(SELECT * FROM favourite WHERE id=:itemId)")
    int isFavourite(int itemId);

    @Delete
    void delete(Favourite favourite);

    @Insert
    void  insertFav(Favourite...favourites);

    @Query("SELECT * FROM favourite WHERE name=:item")
    Flowable<Favourite> getByNameFav(String item);


}
