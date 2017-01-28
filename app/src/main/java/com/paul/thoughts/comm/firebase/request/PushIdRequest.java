package com.paul.thoughts.comm.firebase.request;

import com.paul.thoughts.comm.firebase.request.base.FirebaseRequest;
import com.paul.thoughts.comm.firebase.request.base.RequestOperation;
import com.paul.thoughts.util.Constants;

/**
 * Request to announce other Thoughts users of this instance's Firebase ID update.
 */
public class PushIdRequest extends FirebaseRequest {

    public PushIdRequest(String to, String name, String id) {
        super(to, RequestOperation.PUSH_ID);
        putData(Constants.NAME_KEY, name);
        putData(Constants.ID_KEY, id);
    }
}
