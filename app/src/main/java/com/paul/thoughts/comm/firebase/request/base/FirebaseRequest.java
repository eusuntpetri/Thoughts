package com.paul.thoughts.comm.firebase.request.base;

import com.paul.thoughts.util.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Base class for firebase request-modelling objects.
 */
public abstract class FirebaseRequest {

    private String to;
    private Map<String, String> data;

    protected FirebaseRequest(String to, RequestOperation operation) {
        this.to = to;
        data = new HashMap<>();
        data.put(Constants.OPERATION_KEY, operation.name());
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void putData(String key, String value) {
        data.put(key, value);
    }

    public Map<String, String> getData() {
        return data;
    }
}
