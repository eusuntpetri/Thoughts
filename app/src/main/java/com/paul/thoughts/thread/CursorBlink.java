package com.paul.thoughts.thread;

import com.paul.thoughts.ui.main.ThoughtsPresenter;

import java.util.concurrent.TimeUnit;

/**
 * Created by Petri on 08-Sep-16.
 */
public class CursorBlink extends Thread {

    private ThoughtsPresenter mPresenter;

    public CursorBlink(ThoughtsPresenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                TimeUnit.MILLISECONDS.sleep(700);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            MainThread.post(new Runnable() {
                @Override
                public void run() {
                    mPresenter.switchCursorBlinkState();
                }
            });
        }
    }
}
