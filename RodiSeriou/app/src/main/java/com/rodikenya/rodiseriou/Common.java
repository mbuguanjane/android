package com.rodikenya.rodiseriou;


import android.content.Context;

import com.rodikenya.rodiseriou.Model.Category;
import com.rodikenya.rodiseriou.Model.CentreModel;
import com.rodikenya.rodiseriou.Model.Conferencepackage;
import com.rodikenya.rodiseriou.Model.Order;
import com.rodikenya.rodiseriou.Model.Trainee;
import com.rodikenya.rodiseriou.Model.TrainingMenuModel;
import com.rodikenya.rodiseriou.Model.TrainingOrderModel;
import com.rodikenya.rodiseriou.Model.User;
import com.rodikenya.rodiseriou.Model.centrebooking;
import com.rodikenya.rodiseriou.Remote.FCMClient;
import com.rodikenya.rodiseriou.Remote.IFCMService;
import com.rodikenya.rodiseriou.Remote.IpService;
import com.rodikenya.rodiseriou.Remote.RetrofitClient;
import com.rodikenya.rodiseriou.Remote.RetrofitScalarClient;
import com.rodikenya.rodiseriou.RoomDatabase.Database.CartRepository;
import com.rodikenya.rodiseriou.RoomDatabase.Database.FavouriteRepository;
import com.rodikenya.rodiseriou.RoomDatabase.Local.RodiDatabaseProj;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Common {

    public static   String BASE_URL="http://mbuguanjane.com/drinkshop/";

    public static  final String API_Token=BASE_URL+"BrainTreeToken.php";
    public static Category currentCategory=null;
    public static CartRepository cartRepository;
    public static User CurrentUser;
    public static Order CurrentOrder=null;
    public static FavouriteRepository FavouriteRepository;
    public static RodiDatabaseProj rodiDatabaseProj;
    private static final String FCM_API = "https://fcm.googleapis.com/";
    public static TrainingMenuModel CurrentTrainingMenu;
    public static centrebooking CurrentCentre;
    public static Trainee CurrentTraining;
    public static CentreModel CurrentCentrePrice;
    public static List<Conferencepackage> currentpackage=new ArrayList<>();

    public static IFCMService getFCMService()
    {
        return FCMClient.getClient(FCM_API).create(IFCMService.class);
    }

    public static IpService getIpService()
    {
        return RetrofitClient.getClient(BASE_URL).create(IpService.class);
    }
    public static IpService getScalarAPI()
    {
        return RetrofitScalarClient.getScalarClient(BASE_URL).create(IpService.class);
    }

    public static String convertCodeToStatus(int orderStatus) {
        switch (orderStatus)
        {
            case 0:
                return "placed";
            case 1:
                return "Processing";
            case 2:
                return "Delievered";
            case 3:
                return "Shipped";

            case -1:
                return "Cancelled";
            default:
                return "Order Error";
        }
    }
    public static String getPath(Context context) {
        File dir=new File(android.os.Environment.getExternalStorageDirectory()
                +File.separator
                +context.getResources().getString(R.string.app_name)
                +File.separator
        );
        if(!dir.exists())
            dir.mkdir();
        return dir.getPath()+File.separator;
    }
}
