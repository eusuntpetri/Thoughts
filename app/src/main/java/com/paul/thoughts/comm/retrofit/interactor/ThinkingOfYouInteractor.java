package com.paul.thoughts.comm.retrofit.interactor;

import android.util.Log;

import com.paul.thoughts.comm.firebase.request.ThinkingOfYouRequest;
import com.paul.thoughts.comm.retrofit.RetrofitCallback;
import com.paul.thoughts.util.Constants;

import retrofit2.Call;

/**
 * Created by Petri on 10-Sep-16.
 */
public class ThinkingOfYouInteractor extends Interactor {

    private ThinkingOfYouRequest mThinkingRequest;

    public ThinkingOfYouInteractor(ThinkingOfYouRequest request) {
        this.mThinkingRequest = request;
    }

    @Override
    void interact() {
        Call<Void> call = mService.thinkingOfYou(
                Constants.FIREBASE_SERVER_KEY,
                Constants.JSON_TYPE,
                mThinkingRequest);

        call.enqueue(new RetrofitCallback<Void>() {
            @Override
            public void success(Void response) {
                Log.d("FCM", "Successfully sent Thoughts request.");
            }

            @Override
            public void error(int code, String message) {
                Log.e("FCM", "Thoughts request failure " + code + ": " + message);
            }
        });
    }
}
