package com.rodikenya.rodiseriou.RoomDatabase.Local;

import com.rodikenya.rodiseriou.RoomDatabase.Database.ICartDatasource;
import com.rodikenya.rodiseriou.RoomDatabase.Model.Cart;

import java.util.List;

import io.reactivex.Flowable;

public class CartDatasource implements ICartDatasource {
    private  CartDAO cartDAO;
    private static CartDatasource mInstance;

    public CartDatasource(CartDAO cartDAO) {
        this.cartDAO = cartDAO;
    }
    public  static  CartDatasource getInstance(CartDAO cartDAO)
    {
         if(mInstance==null)
         {
             mInstance=new CartDatasource(cartDAO);
         }
         return  mInstance;
    }
    @Override
    public Flowable<Cart> getCartById(int cartID) {
        return cartDAO.getCartById(cartID);
    }

    @Override
    public Flowable<List<Cart>> getAllCart() {
        return cartDAO.getAllCart();
    }

    @Override
    public void insertCart(Cart... carts) {
       cartDAO.insertCart(carts);
    }

    @Override
    public void updateCart(Cart... carts) {
          cartDAO.updateCart(carts);
    }

    @Override
    public void deleteCart(Cart... carts) {
        cartDAO.deleteCart(carts);
    }
    @Override
    public void deleteAllCarts() {
         cartDAO.deleteAllCarts();
    }

    @Override
    public int countCartItems() {
        return cartDAO.countCartItems();
    }

    @Override
    public float sumPrice() {
        return cartDAO.sumPrice();
    }

    @Override
    public Flowable<Cart> getCartByName(String name) {
        return cartDAO.getCartByName(name);
    }

    @Override
    public void updateAvailable(int available, int productid) {
        cartDAO.updateAvailable(available,productid);
    }

    @Override
    public void updateQuantity(int quantity, int cartID) {
        cartDAO.updateQuantity(quantity,cartID);
    }


}
