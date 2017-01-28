package com.paul.thoughts.comm.firebase.request;

import com.paul.thoughts.comm.firebase.request.base.FirebaseRequest;
import com.paul.thoughts.comm.firebase.request.base.RequestOperation;
import com.paul.thoughts.util.Constants;

/**
 * Models a request for all existing Firebase instance IDs.
 */
public class PullIdsRequest extends FirebaseRequest {

    public PullIdsRequest(String to, String name, String id) {
        super(to, RequestOperation.PULL_IDS);
        putData(Constants.NAME_KEY, name);
        putData(Constants.ID_KEY, id);
    }
}
