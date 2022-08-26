package com.restrorant.myapplication;

import com.restrorant.myapplication.Model.User;
import com.restrorant.myapplication.Retrofit.MpesaInterface;
import com.restrorant.myapplication.Retrofit.RetrofitClient;

public class Common {
    public static User currentUser;
    public static String UPDATE="Update";
    public  static String DELETE="Delete";
    public static String RESERVATION="Reservation";
    //option Reservation
    public  static String PROCESSING="Processing";
    public  static String COMPLETED="Completed";
    public  static String CANCEL="Cancel";



    //Option Profile
    public  static String ACTIVE="Processing";
    public  static String INACTIVE="Completed";
    public  static String ADMIN="Admin";
    public  static String USER="User";




    private static final String Base_URL="https://keenmoversltd.com/TestPay/";



    public static MpesaInterface getMpesa()
    {
        return RetrofitClient.getClient(Base_URL).create(MpesaInterface.class);
    }
}
