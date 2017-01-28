package com.paul.thoughts.thread;

import com.paul.thoughts.ui.main.ThoughtsPresenter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Petri on 07-Sep-16.
 */
public class CharacterRemover {

    private ThoughtsPresenter mThoughtsPresenter;
    private ExecutorService mCharacterRemovalExecutor;
    private CharacterRemoval mLastRemovalTask;

    public CharacterRemover(ThoughtsPresenter mThoughtsPresenter) {
        this.mThoughtsPresenter = mThoughtsPresenter;
        this.mCharacterRemovalExecutor = Executors.newCachedThreadPool();
    }

    public void queueCharacterRemoval(int characterIndex) {
        CharacterRemoval removalTask = new CharacterRemoval(characterIndex, mLastRemovalTask);
        mLastRemovalTask = removalTask;
        mCharacterRemovalExecutor.execute(removalTask);
    }

    private class CharacterRemoval extends Thread {

        private int characterIndex;
        private CharacterRemoval previousTask;

        public CharacterRemoval(int characterIndex, CharacterRemoval previousTask) {
            this.characterIndex = characterIndex;
            this.previousTask = previousTask;
        }

        @Override
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(5);
                if (previousTask != null) {
                    previousTask.join();
                }
                MainThread.post(new Runnable() {
                    @Override
                    public void run() {
                        mThoughtsPresenter.removeFirstCharacter();
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
