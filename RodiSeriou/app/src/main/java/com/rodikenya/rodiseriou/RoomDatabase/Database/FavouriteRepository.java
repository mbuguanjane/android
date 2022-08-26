package com.rodikenya.rodiseriou.RoomDatabase.Database;

import com.rodikenya.rodiseriou.RoomDatabase.Model.Favourite;

import java.util.List;

import io.reactivex.Flowable;

public class FavouriteRepository implements IFavouriteDataSource {
    private IFavouriteDataSource favouriteDataSource;




    private static FavouriteRepository instance;
    public  static  FavouriteRepository getInstance(IFavouriteDataSource favouriteDataSource)
    {
        if(instance==null) {
            instance = new FavouriteRepository(favouriteDataSource);
        }
         return instance;
    }

    public FavouriteRepository(IFavouriteDataSource favouriteDataSource) {
        this.favouriteDataSource = favouriteDataSource;
    }

    @Override
    public Flowable<List<Favourite>> getFavItems() {
        return favouriteDataSource.getFavItems();
    }

    @Override
    public int isFavourite(int itemId) {
        return favouriteDataSource.isFavourite(itemId);
    }

    @Override
    public void delete(Favourite favourite) {
        favouriteDataSource.delete(favourite);
    }

    @Override
    public void insertFav(Favourite... favourites) {
        favouriteDataSource.insertFav(favourites);
    }

    @Override
    public Flowable<Favourite> getByNameFav(String item) {
        return favouriteDataSource.getByNameFav(item);
    }


}
