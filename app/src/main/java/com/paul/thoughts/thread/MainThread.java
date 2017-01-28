package com.paul.thoughts.thread;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by Petri on 07-Sep-16.
 */
public class MainThread {

    public static void post(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }

}
