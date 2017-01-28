package com.paul.thoughts.comm.retrofit.interactor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Petri on 09-Sep-16.
 */
public class InteractorExecutor {

    private static final InteractorExecutor INSTANCE = new InteractorExecutor();

    private ExecutorService mExecutor = Executors.newCachedThreadPool();

    private InteractorExecutor() {
    }

    public static InteractorExecutor getInstance() {
        return INSTANCE;
    }

    public void execute(final Interactor interactor) {
        if (null == interactor) {
            throw new IllegalArgumentException("Interactor to execute cannot be null!");
        }
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                interactor.interact();
            }
        });
    }
}