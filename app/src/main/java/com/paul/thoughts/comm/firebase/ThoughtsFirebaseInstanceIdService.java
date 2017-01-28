package com.paul.thoughts.comm.firebase;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.paul.thoughts.comm.firebase.request.PullIdsRequest;
import com.paul.thoughts.comm.firebase.request.PushIdRequest;
import com.paul.thoughts.comm.retrofit.interactor.InteractorExecutor;
import com.paul.thoughts.comm.retrofit.interactor.PullIdsInteractor;
import com.paul.thoughts.comm.retrofit.interactor.PushIdInteractor;
import com.paul.thoughts.util.Constants;
import com.paul.thoughts.util.PreferenceHandler;

/**
 * Created by Petri on 08-Sep-16.
 */
public class ThoughtsFirebaseInstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String firebaseId = FirebaseInstanceId.getInstance().getToken();
        PreferenceHandler.saveFirebaseId(firebaseId);
        String name = PreferenceHandler.getName();
        if (name != null) {
            PushIdRequest request = new PushIdRequest(Constants.FIREBASE_IDS_TOPIC, name, firebaseId);
            InteractorExecutor.getInstance().execute(new PushIdInteractor(request));
            PullIdsRequest pullRequest = new PullIdsRequest(Constants.FIREBASE_IDS_TOPIC, name, firebaseId);
            InteractorExecutor.getInstance().execute(new PullIdsInteractor(pullRequest));
        }
    }
}
