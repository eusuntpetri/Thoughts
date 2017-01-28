package com.paul.thoughts.comm.retrofit.interactor;

import android.util.Log;

import com.paul.thoughts.comm.firebase.request.PullIdsRequest;
import com.paul.thoughts.comm.retrofit.RetrofitCallback;
import com.paul.thoughts.util.Constants;

import retrofit2.Call;

/**
 * Created by Petri on 10-Sep-16.
 */
public class PullIdsInteractor extends Interactor {

    private PullIdsRequest mPullIdsRequest;

    public PullIdsInteractor(PullIdsRequest request) {
        this.mPullIdsRequest = request;
    }

    @Override
    void interact() {
        Call<Void> call = mService.pullIds(
                Constants.FIREBASE_SERVER_KEY,
                Constants.JSON_TYPE,
                mPullIdsRequest);

        call.enqueue(new RetrofitCallback<Void>() {
            @Override
            public void success(Void response) {
                Log.d("FCMID", "IDs pull requested successfully.");
            }

            @Override
            public void error(int code, String message) {
                Log.e("FCMID", "IDs pull request failure " + code + ": " + message);
            }
        });
    }
}
