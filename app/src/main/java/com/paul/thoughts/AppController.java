package com.paul.thoughts;

import android.support.multidex.MultiDexApplication;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.paul.thoughts.util.Constants;

/**
 * Class for managing application lifecycle.
 */
public class AppController extends MultiDexApplication {

    private static AppController mInstance;

    public static AppController getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        try {
            FirebaseApp.getInstance();
        } catch (IllegalStateException ex) {
            FirebaseApp.initializeApp(this, FirebaseOptions.fromResource(this));
        }
        FirebaseMessaging.getInstance().subscribeToTopic(Constants.FIREBASE_IDS_TOPIC);
    }
}
