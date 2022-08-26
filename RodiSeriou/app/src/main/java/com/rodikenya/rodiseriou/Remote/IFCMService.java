package com.rodikenya.rodiseriou.Remote;

import com.rodikenya.rodiseriou.Model.DataMessage;
import com.rodikenya.rodiseriou.Model.MyResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMService {


    @Headers({
            "Content-Type:application/json",
            "Authorization: key=AAAAKX0Ll7c:APA91bGwa_vlpxEUckGCytNLepB6CtmIgVxkxfYdXc-Lns2JztPU4qr64JRxkhZtVu2dOZn4aG0ubOVLxedKS2KaPsEgnklyGswYT2RKbi_KOKVCk6nOC9fH0XKIg19QQIe2cCixPfjy"
    })



    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body DataMessage body);
}
