package com.paul.thoughts.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.paul.thoughts.AppController;
import com.paul.thoughts.util.Constants.SharedPrefs;

/**
 * Handles all {@link SharedPreferences}-related operations
 */
public final class PreferenceHandler {

    private static SharedPreferences getPreferences() {
        return AppController.getInstance()
                .getSharedPreferences(SharedPrefs.KEY, Context.MODE_PRIVATE);
    }

    public static void saveName(String name) {
        getPreferences().edit().putString(SharedPrefs.NAME, name).apply();
    }

    public static String getName() {
        return getPreferences().getString(SharedPrefs.NAME, null);
    }

    public static void saveFirebaseId(String id) {
        getPreferences().edit().putString(SharedPrefs.FIREBASE_ID, id).apply();
    }

    public static String getFirebaseId() {
        return getPreferences().getString(SharedPrefs.FIREBASE_ID, null);
    }

    public static void saveLastOtherName(String otherName) {
        getPreferences().edit().putString(SharedPrefs.OTHER_NAME, otherName).apply();
    }

    public static String getLastOtherName() {
        return getPreferences().getString(SharedPrefs.OTHER_NAME, null);
    }

}
