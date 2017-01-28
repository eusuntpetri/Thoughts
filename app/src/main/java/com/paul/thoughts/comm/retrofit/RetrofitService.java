package com.paul.thoughts.comm.retrofit;

import com.paul.thoughts.comm.firebase.request.PullIdsRequest;
import com.paul.thoughts.comm.firebase.request.PushIdRequest;
import com.paul.thoughts.comm.firebase.request.ThinkingOfYouRequest;
import com.paul.thoughts.util.Constants;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Handles all HTTP requests.
 */
public interface RetrofitService {

    @POST("send")
    Call<Void> pushMyId(@Header(Constants.AUTHORIZATION_HEADER) String authorization,
                        @Header(Constants.CONTENT_TYPE_HEADER) String contentType,
                        @Body PushIdRequest request);

    @POST("send")
    Call<Void> pullIds(@Header(Constants.AUTHORIZATION_HEADER) String authorization,
                       @Header(Constants.CONTENT_TYPE_HEADER) String contentType,
                       @Body PullIdsRequest request);

    @POST("send")
    Call<Void> thinkingOfYou(@Header(Constants.AUTHORIZATION_HEADER) String authorization,
                             @Header(Constants.CONTENT_TYPE_HEADER) String contentType,
                             @Body ThinkingOfYouRequest request);
}
