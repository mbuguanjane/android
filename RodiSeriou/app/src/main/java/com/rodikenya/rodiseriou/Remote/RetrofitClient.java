package com.rodikenya.rodiseriou.Remote;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit=null;
    public static Retrofit getClient(String baseURl)
    {
        if(retrofit==null)
        {
            try {
                retrofit = new Retrofit.Builder()
                        .baseUrl(baseURl)
                        .addConverterFactory(GsonConverterFactory
                                .create())

                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build();
            }catch(Exception Ex)
            {
                return null;
            }
        }
        return  retrofit;
    }
}
