package com.paul.thoughts.ui.main;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.KeyEvent;

import com.paul.thoughts.exception.ConnectionException;
import com.paul.thoughts.thread.CharacterRemover;
import com.paul.thoughts.thread.CursorBlink;
import com.paul.thoughts.util.Constants;
import com.paul.thoughts.xmpp.Xmpp;

import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;

/**
 * Controller class managing the {@link ThoughtsActivity} presentation business.
 */
public class ThoughtsPresenter implements ThoughtsContract.UserActionListener, ChatMessageListener {

    private Xmpp.Instance mXmppInstance;
    private CharacterRemover mCharacterRemover;
    private CursorBlink mCursorBlink;
    private BroadcastReceiver mBroadcastReceiver;
    private String mOtherName;

    private ThoughtsContract.View mView;

    public ThoughtsPresenter(ThoughtsContract.View view, String otherName) {
        this.mView = view;
        this.mOtherName = otherName;
        this.mXmppInstance = Xmpp.getInstance();
        this.mXmppInstance.startListening(this);
        this.mCharacterRemover = new CharacterRemover(this);
        this.mCursorBlink = new CursorBlink(this);
        this.mCursorBlink.start();
        this.mBroadcastReceiver = new MessageReceiver();
    }

    public void switchCursorBlinkState() {
        String currentText = mView.getCursorTextView().getText().toString();
        char currentCursorState = currentText.charAt(0);
        char newCursorState = currentCursorState == '_' ? ' ' : '_';
        mView.getCursorTextView().setText(currentText.replace(currentCursorState, newCursorState));
    }

    @Override
    public void onKeyPressed(int keyCode, KeyEvent event) {
        char unicode = (char) event.getUnicodeChar();

        if (unicode == 0) {
            return;
        }

        try {
            mXmppInstance.sendCharacterToUser(mOtherName + "13371415@" + Constants.DOMAIN, unicode);
        } catch (ConnectionException e) {
            mView.showToast("Thoughts are not connected!");
        }
    }

    @Override
    public void processMessage(Chat chat, Message message) {
        Intent intent = new Intent("com.paul.thoughts.xmpp.message");
        intent.putExtra("message", message.getBody());
        mView.getActivity().sendBroadcast(intent);
    }

    public void removeFirstCharacter() {
        String currentText = mView.getThoughtsTextView().getText().toString();
        mView.getThoughtsTextView().setText(currentText.substring(1));
    }

    public void removeCharacterAtIndex(int characterIndex) {
        String currentText = mView.getThoughtsTextView().getText().toString();
        String newText = currentText.substring(0, characterIndex) + " " +
                currentText.substring(characterIndex + 1);
        mView.getThoughtsTextView().setText(newText);
    }

    public void registerObserver(Activity activity) {
        activity.registerReceiver(mBroadcastReceiver,
                new IntentFilter("com.paul.thoughts.xmpp.message"));
    }

    public void unregisterObserver(Activity activity) {
        activity.unregisterReceiver(mBroadcastReceiver);
    }

    private class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (!intent.hasExtra("message")) {
                throw new RuntimeException("An unexpected broadcast Intent was intercepted!");
            }
            String receivedMessage = intent.getStringExtra("message");
            String currentText = mView.getThoughtsTextView().getText().toString();
            int newCharPosition = currentText.length();
            String newText = currentText + receivedMessage;
            mView.getThoughtsTextView().setText(newText);

            mCharacterRemover.queueCharacterRemoval(newCharPosition);
        }
    }
}
