package com.paul.thoughts.exception;

/**
 * Created by Petri on 04-Sep-16.
 */
public class ConnectionException extends RuntimeException {

    public ConnectionException(String message) {
        super(message);
    }

    public ConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
