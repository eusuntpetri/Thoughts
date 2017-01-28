package com.paul.thoughts.comm.firebase.request;

import com.paul.thoughts.comm.firebase.request.base.FirebaseRequest;
import com.paul.thoughts.comm.firebase.request.base.RequestOperation;
import com.paul.thoughts.util.Constants;

/**
 * Request for notifying another user that they are being thought of.
 */
public class ThinkingOfYouRequest extends FirebaseRequest {

    public ThinkingOfYouRequest(String to, String name) {
        super(to, RequestOperation.THINKING_OF_YOU);
        putData(Constants.NAME_KEY, name);
        putData(Constants.TEXT_KEY, name + " is thinking of you...");
    }
}
