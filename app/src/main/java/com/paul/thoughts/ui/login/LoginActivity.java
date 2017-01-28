package com.paul.thoughts.ui.login;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.paul.thoughts.R;
import com.paul.thoughts.util.PreferenceHandler;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity
        implements LoginContract.View {

    private LoginPresenter mPresenter;

    private EditText mNameEdit;
    private EditText mOtherNameEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mNameEdit = (EditText) findViewById(R.id.name_edit);
        mOtherNameEdit = (EditText) findViewById(R.id.other_name_edit);
        Button mStartThinkingButton = (Button) findViewById(R.id.start_thinking_btn);
        mPresenter = new LoginPresenter(this);
        mStartThinkingButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onStartThinkingPressed();
            }
        });
    }

    @Override
    public EditText getNameEdit() {
        return mNameEdit;
    }

    @Override
    public EditText getOtherNameEdit() {
        return mOtherNameEdit;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}

