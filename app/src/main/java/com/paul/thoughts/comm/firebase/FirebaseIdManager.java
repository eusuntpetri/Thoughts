package com.paul.thoughts.comm.firebase;

import android.util.Log;

import com.paul.thoughts.AppController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages known users' Firebase IDs.
 */
public class FirebaseIdManager {

    private static final FirebaseIdManager INSTANCE = new FirebaseIdManager();
    private File mFile = new File(AppController.getInstance().getFilesDir(), "firebase_ids");
    private Map<String, String> mFirebaseIdsByName;

    private FirebaseIdManager() {
        loadIds();
    }

    public static FirebaseIdManager getInstance() {
        return INSTANCE;
    }

    @SuppressWarnings("unchecked")
    public void loadIds() {
        try {
            if(!mFile.exists()) {
                mFile.createNewFile();
            }
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(mFile));
            mFirebaseIdsByName = (Map<String, String>) inputStream.readObject();
        } catch (IOException e) {
            Log.e("FILE", "Could not load Firebase IDs from file!", e);
        } catch (ClassNotFoundException e) {
            Log.e("WHAT", "Time to stop writing Java!", e);
        }
        if(mFirebaseIdsByName == null) {
            mFirebaseIdsByName = new HashMap<>();
            saveIds();
        }
    }

    public void putIdForName(String name, String id) {
        mFirebaseIdsByName.put(name, id);
        saveIds();
    }

    public String getIdForName(String name) {
        return mFirebaseIdsByName.get(name);
    }

    public void saveIds() {
        ObjectOutputStream outputStream = null;
        try {
            outputStream = new ObjectOutputStream(new FileOutputStream(mFile));
            outputStream.writeObject(mFirebaseIdsByName);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            Log.e("Error", "Could not save Firebase IDs to mFile!", e);
        }
    }

}
