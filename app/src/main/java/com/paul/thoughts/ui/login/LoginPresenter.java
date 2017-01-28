package com.paul.thoughts.ui.login;

import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.paul.thoughts.comm.firebase.FirebaseIdManager;
import com.paul.thoughts.comm.firebase.request.PullIdsRequest;
import com.paul.thoughts.comm.firebase.request.ThinkingOfYouRequest;
import com.paul.thoughts.comm.retrofit.interactor.InteractorExecutor;
import com.paul.thoughts.comm.retrofit.interactor.PullIdsInteractor;
import com.paul.thoughts.comm.retrofit.interactor.ThinkingOfYouInteractor;
import com.paul.thoughts.exception.ConnectionException;
import com.paul.thoughts.thread.MainThread;
import com.paul.thoughts.ui.main.ThoughtsActivity;
import com.paul.thoughts.util.Constants;
import com.paul.thoughts.util.PreferenceHandler;
import com.paul.thoughts.xmpp.Xmpp;

/**
 * The login screen presenter.
 */
public class LoginPresenter implements LoginContract.UserActionListener {

    private LoginContract.View mView;
    private Xmpp.Instance mXmppInstance = Xmpp.getInstance();
    private UserLoginTask mAuthTask = null;
    private String mOtherName;

    public LoginPresenter(LoginContract.View view) {
        this.mView = view;
        setNames();
    }

    private void setNames() {
        if (PreferenceHandler.getName() != null) {
            mView.getNameEdit().setText(PreferenceHandler.getName());
        }
        if (mView.getActivity().getIntent().hasExtra(Constants.OTHER_NAME_NOTIF_KEY)) {
            String otherNameFromNotification = mView.getActivity().getIntent()
                    .getExtras().getString(Constants.OTHER_NAME_NOTIF_KEY);
            mView.getOtherNameEdit().setText(otherNameFromNotification);
        } else if (PreferenceHandler.getLastOtherName() != null) {
            mView.getOtherNameEdit().setText(PreferenceHandler.getLastOtherName());
        }
    }

    @Override
    public void onStartThinkingPressed() {
        if (mAuthTask != null) {
            return;
        }

        EditText nameEdit = mView.getNameEdit();
        EditText otherNameEdit = mView.getOtherNameEdit();

        nameEdit.setError(null);
        otherNameEdit.setError(null);

        String name = nameEdit.getText().toString().toLowerCase();
        String otherName = otherNameEdit.getText().toString().toLowerCase();

        View focusView = null;

        if (TextUtils.isEmpty(name)) {
            nameEdit.setError("You must have a name...");
            focusView = nameEdit;
        } else if (!isNameValid(name)) {
            nameEdit.setError("I do not think I know you...");
            focusView = nameEdit;
        } else if (TextUtils.isEmpty(otherName)) {
            otherNameEdit.setError("You must be thinking of somebody...");
            focusView = otherNameEdit;
        } else if (!isNameValid(otherName)) {
            otherNameEdit.setError("I know not who you think of...");
            focusView = otherNameEdit;
        }

        if (focusView != null) {
            focusView.requestFocus();
        } else {
            mView.showToast("Thinking...");
            mOtherName = otherName;
            mAuthTask = new UserLoginTask(name, Constants.DEFAULT_PASSWORD);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isNameValid(String name) {
        return name.equals("laura") || name.equals("paul") || name.equals("andrei");
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mName;
        private final String mPassword;

        UserLoginTask(String name, String password) {
            mName = name;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            mXmppInstance.init(mName + "13371415", mPassword);

            try {
                mXmppInstance.connect();
                mXmppInstance.login();
            } catch (final ConnectionException e) {
                Log.e("CONN", e.getMessage());
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;

            if (success) {
                onLoginSuccess();
            } else {
                MainThread.post(new Runnable() {
                    @Override
                    public void run() {
                        mView.showToast("Something is off... Try to reach me elsewhere!");
                    }
                });
            }
        }

        private void onLoginSuccess() {
            PreferenceHandler.saveName(mName);
            PreferenceHandler.saveLastOtherName(mOtherName);

            if (PreferenceHandler.getFirebaseId() != null) {
                PullIdsRequest request = new PullIdsRequest(
                        Constants.FIREBASE_IDS_TOPIC, mName, PreferenceHandler.getFirebaseId());
                InteractorExecutor.getInstance().execute(new PullIdsInteractor(request));
            }
            String id = FirebaseIdManager.getInstance().getIdForName(mOtherName);
            if (id != null) {
                ThinkingOfYouRequest request = new ThinkingOfYouRequest(id, mName);
                InteractorExecutor.getInstance().execute(new ThinkingOfYouInteractor(request));
            } else {
                mView.showToast("Your Thoughts cannot reach " + mOtherName + " right now...");
            }

            Intent intent = new Intent(mView.getActivity(), ThoughtsActivity.class);
            intent.putExtra("otherName", mOtherName);
            mView.getActivity().startActivity(intent);
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }
}
