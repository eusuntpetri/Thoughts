package com.paul.thoughts.comm.retrofit.interactor;

import com.paul.thoughts.comm.retrofit.RetrofitService;
import com.paul.thoughts.util.Constants;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Interacts through Retrofit with any external APIs.
 */
public abstract class Interactor {

    protected RetrofitService mService;

    public Interactor() {
        HttpLoggingInterceptor body = new HttpLoggingInterceptor();
        OkHttpClient.Builder builder = new OkHttpClient.Builder().addInterceptor(body);
        body.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        builder.addInterceptor(body);
        OkHttpClient client = builder.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.HTTP_PATH)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        mService = retrofit.create(RetrofitService.class);
    }

    abstract void interact();

}
