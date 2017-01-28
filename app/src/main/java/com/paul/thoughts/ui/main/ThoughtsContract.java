package com.paul.thoughts.ui.main;

import android.app.Activity;
import android.view.KeyEvent;
import android.widget.TextView;

/**
 * Created by Petri on 04-Sep-16.
 */
public interface ThoughtsContract {

    interface View {

        TextView getCursorTextView();

        TextView getThoughtsTextView();

        Activity getActivity();

        void setError(String error);

        void showToast(String message);
    }

    interface UserActionListener {

        void onKeyPressed(int keyCode, KeyEvent event);

    }
}
