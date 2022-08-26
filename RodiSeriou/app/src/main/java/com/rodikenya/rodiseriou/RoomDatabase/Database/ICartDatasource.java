package com.rodikenya.rodiseriou.RoomDatabase.Database;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.rodikenya.rodiseriou.RoomDatabase.Model.Cart;

import java.util.List;

import io.reactivex.Flowable;

public interface ICartDatasource {

    Flowable<Cart> getCartById(int cartID);
    Flowable<List<Cart>> getAllCart();
    void insertCart(Cart... carts);
    void updateCart(Cart... carts);
    void deleteCart(Cart... carts);
    void deleteAllCarts();
    int countCartItems();
    float  sumPrice();
    Flowable<Cart> getCartByName(String name);
    void updateAvailable(int available, int productid);
    void updateQuantity(int quantity, int cartID);
}
