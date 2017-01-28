package com.paul.thoughts.comm.firebase;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.paul.thoughts.AppController;
import com.paul.thoughts.R;
import com.paul.thoughts.comm.firebase.request.PushIdRequest;
import com.paul.thoughts.comm.firebase.request.base.RequestOperation;
import com.paul.thoughts.comm.retrofit.interactor.InteractorExecutor;
import com.paul.thoughts.comm.retrofit.interactor.PushIdInteractor;
import com.paul.thoughts.ui.login.LoginActivity;
import com.paul.thoughts.util.Constants;
import com.paul.thoughts.util.PreferenceHandler;

import java.util.Map;

/**
 * Created by Petri on 08-Sep-16.
 */
public class ThoughtsFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String, String> data = remoteMessage.getData();
        if (data.get(Constants.NAME_KEY).equals(PreferenceHandler.getName())) {
            return;
        }
        switch (RequestOperation.valueOf(data.get(Constants.OPERATION_KEY))) {
            case PUSH_ID:
                managePushId(data);
                break;
            case PULL_IDS:
                managePullIds(data);
                break;
            case THINKING_OF_YOU:
                manageThinkingOfYou(data);
                break;
        }
    }

    private void managePushId(Map<String, String> data) {
        Log.d("FCMID", "Received ID push from " + data.get(Constants.NAME_KEY) + ". Saving ID...");
        FirebaseIdManager.getInstance().putIdForName(
                data.get(Constants.NAME_KEY), data.get(Constants.ID_KEY));
    }

    private void managePullIds(Map<String, String> data) {
        Log.d("FCMID", "Received request for ID push from " + data.get(Constants.NAME_KEY) +
                ". Saving ID and executing...");
        FirebaseIdManager.getInstance().putIdForName(
                data.get(Constants.NAME_KEY), data.get(Constants.ID_KEY));
        PushIdRequest request = new PushIdRequest(data.get(Constants.ID_KEY),
                PreferenceHandler.getName(), PreferenceHandler.getFirebaseId());
        InteractorExecutor.getInstance().execute(new PushIdInteractor(request));
    }

    private void manageThinkingOfYou(Map<String, String> data) {
        Log.d("FCM", "Received Thoughts request. Notifying...");
        Bitmap icon = BitmapFactory.decodeResource(AppController.getInstance().getResources(),
                R.drawable.feather_ic);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.feather_ic)
                        .setLargeIcon(icon)
                        .setContentTitle(Constants.APP_NAME)
                        .setContentText(data.get(Constants.TEXT_KEY));
        Intent resultIntent = new Intent(this, LoginActivity.class);
        resultIntent.putExtra("nameFromNotification", data.get(Constants.NAME_KEY));
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        Notification notification = mBuilder.build();
//        notification.sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.);
        notification.defaults = Notification.DEFAULT_SOUND
                | Notification.DEFAULT_LIGHTS
                | Notification.DEFAULT_VIBRATE;
        int notificationId = 1337;
        NotificationManager notifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notifyMgr.notify(notificationId, notification);
    }
}
