package com.paul.thoughts.comm.retrofit;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Main use is differentiating between successful and unsuccessful responses.
 */
public abstract class RetrofitCallback<T> implements retrofit2.Callback<T> {

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            success(response.body());
        } else {
            error(response.code(), response.message());
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Log.e(RetrofitCallback.class.getSimpleName(), t.getMessage());
        error(1337, t.getMessage());
    }

    public abstract void success(T response);

    public abstract void error(int code, String message);

}