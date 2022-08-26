package com.rodikenya.rodiseriou.Remote;



import com.rodikenya.rodiseriou.Model.Banner;
import com.rodikenya.rodiseriou.Model.Category;
import com.rodikenya.rodiseriou.Model.CentreModel;
import com.rodikenya.rodiseriou.Model.Conferencepackage;
import com.rodikenya.rodiseriou.Model.Intro;
import com.rodikenya.rodiseriou.Model.MediaObject;
import com.rodikenya.rodiseriou.Model.Order;
import com.rodikenya.rodiseriou.Model.Products;
import com.rodikenya.rodiseriou.Model.Token;
import com.rodikenya.rodiseriou.Model.Trainee;
import com.rodikenya.rodiseriou.Model.TrainingItems;
import com.rodikenya.rodiseriou.Model.TrainingMenuModel;
import com.rodikenya.rodiseriou.Model.TrainingOrderModel;
import com.rodikenya.rodiseriou.Model.User;
import com.rodikenya.rodiseriou.Model.centrebooking;
import com.rodikenya.rodiseriou.TrainingMenu;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface IpService {

    //getting banner
    @GET("Banners.php")
    Observable<List<Banner>> getBanner();

    //getting banner
    @GET("Introdution.php")
    Observable<List<Intro>> getIntroduction();

    //getMenu
    @GET("Category.php")
    Observable<List<Category>> getMenu();
    //get Drinks
    //get Centre Package
    @FormUrlEncoded
    @POST("ConferencePackage.php")
    Observable<List<Conferencepackage>> getConferencePackage(@Field("Option") String Options);
    //end Centre Package
    //getMenu
    @POST("TrainingMenu.php")
    Observable<List<TrainingMenuModel>> getTrainingMenu();
    //get TrainingMenu
    //getMenu
    @FormUrlEncoded
    @POST("Product.php")
    Observable<List<Products>> getDrinks(@Field("menuid") String menuid);


    //getMenu
    @FormUrlEncoded
    @POST("TrainingItems.php")
    Observable<List<TrainingItems>> getTrainingItems(@Field("TrainingMenuId") String TrainingMenuId);


    @GET("SearchProduct.php")
    Observable<List<Products>> getAllProducts();

    @FormUrlEncoded
    @POST("SubmitOrder.php")
    Call<Order> SubmitOrder(@Field("Price") float Orderprice,
                             @Field("OrderDetail") String OrderDetail,
                             @Field("Phone") String Phone,
                             @Field("Address") String Address,
                             @Field("Comment") String Comment,
                             @Field("PaymentMethod") String PaymentMethod,
                             @Field("Transport") String Transport
                            );

    @FormUrlEncoded
    @POST("CheckOut.php")
    Call<String> makePayment(
                             @Field("nonce") String nonce,
                             @Field("amount") String amount
    );
    //fetch tutorials
    @GET("Tutorial.php")
    Observable<List<MediaObject>> FetchTutorials();

    //fetch CentrePricing
    @GET("CentrePricing.php")
    Observable<List<CentreModel>> FetchCentrePricing();
    //end CentrePricing
    //centre status
    @FormUrlEncoded
    @POST("Server/Category/UpdateCentreStatus.php")
    Call<String> Update_CentreStatus(
            @Field("BookingStatus") String BookingStatus,
            @Field("Phone") String Phone,
            @Field("BookingId") int BookingId);
    //TrainingOrderStatus
    @FormUrlEncoded
    @POST("Server/Category/UpdateTrainingOrderStatus.php")
    Call<String> Update_TrainingOrderStatus(
            @Field("TrainingStatus") String TrainingStatus,
            @Field("PhoneNumber") String PhoneNumber,
            @Field("ID") int ID);
    //load Centre booking
    @FormUrlEncoded
    @POST("getCentreBookingBYStatus.php")
    Observable<List<centrebooking>> getCentreBooking(
            @Field("Status") String status,
            @Field("Phone") String Phone
    );
    //load Centre booking
    @FormUrlEncoded
    @POST("getTrainingOrdersByPhone.php")
    Observable<List<Trainee>> getTrainingRegister(
            @Field("Status") String status,
            @Field("Phone") String Phone
    );

    @FormUrlEncoded
    @POST("registerUser.php")
    Call<User> RegisterUser(
            @Field("phone") String phone,
            @Field("name") String name,
            @Field("birthdate") String birthdate,
            @Field("address") String address
    );
    //book Now
    @FormUrlEncoded
    @POST("BookingCentre.php")
    Call<centrebooking> BookNow(
            @Field("Event") String Event,
            @Field("Phone") String Phone,
            @Field("BookingDate") String BookingDate,
            @Field("EventId") String EventId,
            @Field("Provisions") String Provisions,
            @Field("People") String People
    );
    //update Quatity
    //book Now
    @FormUrlEncoded
    @POST("UpdateQuantity.php")
    Call<String> updateQuantity(
            @Field("Quantity") String Available,
            @Field("Name") String ID
    );
    //end Quantity
    //login
    @FormUrlEncoded
    @POST("CheckUser.php")
    Call<User> LoginUser(
            @Field("phone") String phone
               );

    //load orders
    @FormUrlEncoded
    @POST("getOrderByStatus.php")
    Observable<List<Order>> getMyOrder(
                                @Field("Phonenumber") String Phonenumber,
                                @Field("Status") String status
                                  );

    //update Avataor
    //Register Training
    @FormUrlEncoded
    @POST("NewTrainee.php")
    Call<TrainingOrderModel> registerTraining(
            @Field("Phone") String Phone,
            @Field("TrainingDetails") String TrainingDetails,
            @Field("Members") String Members,
            @Field("Address") String Address,
            @Field("Name") String Name
    );
    //end regidtering training
    @FormUrlEncoded
    @POST("UpdateAvatorImageUser.php")
    Call<String> UpdateAvator(
            @Field("AvatarLink") String AvatarLink,
            @Field("Phone") String Phone );

    //cancelled order
    @FormUrlEncoded
    @POST("CancelledOrder.php")
    Call<String> cancelorder(
            @Field("OrderId") String OrderId,
            @Field("usePhone") String usePhone );

    //update Firebase Token
    //Update FireBase Token order
    @FormUrlEncoded
    @POST("UpdateToken.php")
    Call<String> UpdateFireBaseToken(
            @Field("Phone") String Phone,
            @Field("token") String token,
            @Field("isServerToken") String isServerToken
    );

    //Fecth Token From Database
    @FormUrlEncoded
    @POST("getFirebaseToken.php")
    Call<Token> getFireBaseToken(
            @Field("Phone") String Phone,
            @Field("IsServerToken") String IsServerToken );

    //upload Avator image
    @Multipart
    @POST("uploadAvator.php")
    Call<String> getUploadAvatorFile(@Part MultipartBody.Part file );



}
