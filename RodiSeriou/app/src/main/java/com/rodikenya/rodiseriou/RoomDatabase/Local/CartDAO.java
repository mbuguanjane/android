package com.rodikenya.rodiseriou.RoomDatabase.Local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.rodikenya.rodiseriou.RoomDatabase.Model.Cart;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface CartDAO {


    @Query("SELECT * FROM cart WHERE id=:cartID")
    Flowable<Cart> getCartById(int cartID);

    @Query("SELECT * FROM cart ")
    Flowable<List<Cart>> getAllCart();

    @Insert
    void insertCart(Cart... carts);

    @Update
    void updateCart(Cart... carts);

    @Query("UPDATE  cart  set Available=:available WHERE productid=:productid")
    void updateAvailable(int available, int productid);

    @Query("UPDATE  cart  set Quantity=:quantity WHERE id=:cartID AND  quantity<Available ")
    void updateQuantity(int quantity, int cartID);

    @Delete
    void deleteCart(Cart... carts);

    @Query("DELETE FROM cart")
    void deleteAllCarts();

    @Query("SELECT COUNT(*) FROM cart ")
    int countCartItems();

    @Query("SELECT SUM(price) FROM cart ")
    float  sumPrice();


    @Query("SELECT * FROM cart WHERE name=:name")
    Flowable<Cart> getCartByName(String name);

}
