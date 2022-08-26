package com.rodikenya.rodiseriou.RoomDatabase.Database;

import com.rodikenya.rodiseriou.RoomDatabase.Model.Cart;

import java.util.List;

import io.reactivex.Flowable;

public class CartRepository implements ICartDatasource {
    private ICartDatasource mLocalDataSource;
    private static  CartRepository mInstance;

    public CartRepository(ICartDatasource mLocalDataSource) {
        this.mLocalDataSource = mLocalDataSource;
    }
     public static  CartRepository getInstance(ICartDatasource  mLocalDataSource)
     {
         if(mInstance==null)
         {
             mInstance=new CartRepository(mLocalDataSource);
         }
         return mInstance;
     }
    @Override
    public Flowable<Cart> getCartById(int cartID) {
        return mLocalDataSource.getCartById(cartID);
    }

    @Override
    public Flowable<List<Cart>> getAllCart() {
        return mLocalDataSource.getAllCart();
    }

    @Override
    public void insertCart(Cart... carts) {
           mLocalDataSource.insertCart(carts);
    }

    @Override
    public void updateCart(Cart... carts) {
          mLocalDataSource.updateCart(carts);
    }

    @Override
    public void deleteCart(Cart... carts) {
        mLocalDataSource.deleteCart(carts);
    }

    @Override
    public void deleteAllCarts() {
       mLocalDataSource.deleteAllCarts();
    }

    @Override
    public int countCartItems() {
       return mLocalDataSource.countCartItems();
    }

    @Override
    public float sumPrice() {
        return mLocalDataSource.sumPrice();
    }

    @Override
    public Flowable<Cart> getCartByName(String name) {
        return mLocalDataSource.getCartByName(name);
    }

    @Override
    public void updateAvailable(int available, int productid) {
        mLocalDataSource.updateAvailable(available,productid);
    }

    @Override
    public void updateQuantity(int quantity, int cartID) {
        mLocalDataSource.updateQuantity(quantity,cartID);
    }


}
