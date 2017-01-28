package com.paul.thoughts.ui.login;

import android.app.Activity;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

/**
 * Created by Petri on 07-Sep-16.
 */
public interface LoginContract {

    interface View {

        EditText getNameEdit();

        EditText getOtherNameEdit();

        Activity getActivity();

        void showToast(String message);

    }

    interface UserActionListener {

        void onStartThinkingPressed();

    }
}
