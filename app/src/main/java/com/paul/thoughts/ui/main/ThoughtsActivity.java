package com.paul.thoughts.ui.main;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.paul.thoughts.R;
import com.paul.thoughts.comm.firebase.request.PullIdsRequest;
import com.paul.thoughts.comm.firebase.request.PushIdRequest;
import com.paul.thoughts.comm.retrofit.interactor.InteractorExecutor;
import com.paul.thoughts.comm.retrofit.interactor.PullIdsInteractor;
import com.paul.thoughts.comm.retrofit.interactor.PushIdInteractor;
import com.paul.thoughts.util.Constants;
import com.paul.thoughts.util.PreferenceHandler;

public class ThoughtsActivity extends AppCompatActivity implements ThoughtsContract.View {

    private RelativeLayout thoughtsLayout;
    private TextView cursorTextView;
    private TextView thoughtsTextView;
    private TextView errorTextView;

    private ThoughtsPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thoughts);
        this.thoughtsLayout = (RelativeLayout) findViewById(R.id.thoughts_layout);
        this.thoughtsLayout.setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        });
        this.cursorTextView = (TextView) findViewById(R.id.cursor_text_view);
        this.thoughtsTextView = (TextView) findViewById(R.id.thoughts_text_view);
        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/erika_type.ttf");
        thoughtsTextView.setTypeface(face);
        String otherName = getIntent().getStringExtra(Constants.OTHER_NAME_KEY);
        getSupportActionBar().setTitle("Thoughts from " + otherName);
        this.mPresenter = new ThoughtsPresenter(this, otherName);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        mPresenter.onKeyPressed(keyCode, event);
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.registerObserver(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.unregisterObserver(this);
    }

    @Override
    public TextView getCursorTextView() {
        return cursorTextView;
    }

    @Override
    public TextView getThoughtsTextView() {
        return thoughtsTextView;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void setError(String error) {
        errorTextView.setText(error);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
