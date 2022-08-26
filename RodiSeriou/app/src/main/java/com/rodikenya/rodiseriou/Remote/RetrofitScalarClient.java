package com.rodikenya.rodiseriou.Remote;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitScalarClient {
    private static Retrofit retrofit=null;
    public static Retrofit getScalarClient(String baseURl)
    {
        if(retrofit==null)
        {
            try {
                retrofit = new Retrofit.Builder()
                        .baseUrl(baseURl)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .build();
            }catch(Exception Ex)
            {
                return null;
            }
        }
        return  retrofit;
    }
}
