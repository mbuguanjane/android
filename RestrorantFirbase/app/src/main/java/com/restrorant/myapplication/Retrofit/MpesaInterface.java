package com.restrorant.myapplication.Retrofit;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface MpesaInterface {
    @FormUrlEncoded
    @POST("stk_initiate.php")
    Call<String> sendPayment(@Field("phone") String phone,
                             @Field("amount") String amount,
                             @Field("account") String account);





    @FormUrlEncoded
    @POST("sendMessage.php")
    Call<String> forgotPassword(@Field("phone") String phone,
                                @Field("password") String password);

    @FormUrlEncoded
    @POST("sendMessage.php")
    Call<String> sendMessage(@Field("phone") String phone,
                                @Field("password") String Message);

    @FormUrlEncoded
    @POST("sendReceipt.php")
    Call<String> sendReceipt(@Field("phone") String phone,
                             @Field("Message") String Message);

}
