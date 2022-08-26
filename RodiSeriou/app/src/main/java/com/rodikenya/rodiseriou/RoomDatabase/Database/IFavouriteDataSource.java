package com.rodikenya.rodiseriou.RoomDatabase.Database;

import androidx.room.Delete;
import androidx.room.Query;

import com.rodikenya.rodiseriou.RoomDatabase.Model.Favourite;

import java.util.List;

import io.reactivex.Flowable;

public interface IFavouriteDataSource {

    Flowable<List<Favourite>> getFavItems();
    int isFavourite(int itemId);



    void delete(Favourite favourite);
    void  insertFav(Favourite...favourites);
    Flowable<Favourite> getByNameFav(String item);
}
