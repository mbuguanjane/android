package com.rodikenya.rodiseriou.RoomDatabase.Local;

import com.rodikenya.rodiseriou.RoomDatabase.Database.IFavouriteDataSource;
import com.rodikenya.rodiseriou.RoomDatabase.Model.Favourite;

import java.util.List;

import io.reactivex.Flowable;

public class FavouriteDatasource implements IFavouriteDataSource {
    private FavDAO favDAO;


    private static FavouriteDatasource instance;

    public FavouriteDatasource(FavDAO favDAO) {
        this.favDAO = favDAO;
    }
    public static FavouriteDatasource getInstance(FavDAO favDAO)
    {
        if(instance==null)
        {
            instance=new FavouriteDatasource(favDAO);
        }
        return instance;
    }
    @Override
    public Flowable<List<Favourite>> getFavItems() {
        return favDAO.getFavItems();
    }

    @Override
    public int isFavourite(int itemId) {
        return favDAO.isFavourite(itemId);
    }

    @Override
    public void delete(Favourite favourite) {
        favDAO.delete(favourite);
    }

    @Override
    public void insertFav(Favourite... favourites) {
        favDAO.insertFav(favourites);
    }

    @Override
    public Flowable<Favourite> getByNameFav(String item) {
        return favDAO.getByNameFav(item);
    }


}
