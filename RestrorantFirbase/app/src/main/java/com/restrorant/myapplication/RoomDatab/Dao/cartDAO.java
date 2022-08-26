package com.restrorant.myapplication.RoomDatab.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.restrorant.myapplication.RoomDatab.Model.CartModel;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;
@Dao
public interface cartDAO {
   //inserting
    @Insert(onConflict = REPLACE)
    void insert(CartModel cartModel);

    //delete
      @Delete
      void  delete(CartModel cartModel);

      //deleteAll
      @Delete
      void reset(List<CartModel> cartModelList);

      //update
      @Query("UPDATE Cart set productId=:productId ,ProductName=:ProductName,Quantity=:Quantity,Price=:Price where id=:id")
      void update(String productId,String ProductName,String Quantity,String Price,int id);

      //selecting All
      @Query("select * from Cart")
      List<CartModel> getAllCart();


}
