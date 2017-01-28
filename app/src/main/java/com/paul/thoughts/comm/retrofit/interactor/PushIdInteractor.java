package com.paul.thoughts.comm.retrofit.interactor;

import android.util.Log;

import com.paul.thoughts.comm.firebase.request.PushIdRequest;
import com.paul.thoughts.comm.retrofit.RetrofitCallback;
import com.paul.thoughts.util.Constants;

import retrofit2.Call;

/**
 * Interactor for notifying of this app instance's Firebase ID update.
 */
public class PushIdInteractor extends Interactor {

    private PushIdRequest mPushIdRequest;

    public PushIdInteractor(PushIdRequest pushIdRequest) {
        this.mPushIdRequest = pushIdRequest;
    }

    @Override
    public void interact() {
        Call<Void> call = mService.pushMyId(
                Constants.FIREBASE_SERVER_KEY,
                Constants.JSON_TYPE,
                mPushIdRequest);

        call.enqueue(new RetrofitCallback<Void>() {
            @Override
            public void success(Void response) {
                Log.d("FCMID", "ID pushed successfully.");
            }

            @Override
            public void error(int code, String message) {
                Log.e("FCMID", "ID push failure " + code + ": " + message);
            }
        });
    }
}
