package com.paul.thoughts.util;

/**
 * Contains constant values of use throughout the application.
 */
public interface Constants {

    String APP_NAME = "Thoughts";
    String RESOURCE = "mobile";
    String DOMAIN = "chat.safic.net";
    String HOST = "104.223.91.142";
    int PORT = 5222;
    String DEFAULT_PASSWORD = "qwerty1415";
    String HTTP_PATH = "https://fcm.googleapis.com/fcm/";
    String AUTHORIZATION_HEADER = "Authorization";
    String CONTENT_TYPE_HEADER = "Content-Type";
    String FIREBASE_SERVER_KEY = "key=AIzaSyAI6eU8w_5IXu7Ic8cWVeCWeFdbaU89J9M";
    String JSON_TYPE = "application/json";
    String FIREBASE_IDS_TOPIC = "/topics/firebaseIds";
    String OPERATION_KEY = "operation";
    String NAME_KEY = "name";
    String OTHER_NAME_KEY = "otherName";
    String ID_KEY = "id";
    String TEXT_KEY = "text";
    String OTHER_NAME_NOTIF_KEY = "otherNameNotification";

    interface SharedPrefs {

        String KEY = "com.paul.thoughts.Thoughts";
        String NAME = "name";
        String FIREBASE_ID = "firebaseId";
        String OTHER_NAME = "otherName";

    }

}
