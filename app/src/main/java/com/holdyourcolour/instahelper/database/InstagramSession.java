package com.holdyourcolour.instahelper.database;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by HoldYourColour on 27.03.2017.
 */

public class InstagramSession {
    private static InstagramSession sInstance;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    private static final String SHARED = "Instagram_Preferences";
    private static final String USER_USERNAME = "username";
    private static final String USER_ID = "id";
    private static final String USER_NAME = "name";
    private static final String USER_PROFILE_PICURE_URL = "profile_picure_url";
    private static final String USER_ACCESS_TOKEN = "access_token";

    public static InstagramSession getInstance(Context context){
        if (sInstance == null){
            sInstance = new InstagramSession(context);
        }
        return sInstance;
    }
    private InstagramSession(Context context) {
        sharedPref = context.getSharedPreferences(SHARED, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    public void storeAccessToken(String accessToken, String id, String username, String name, String profilePictureURL) {
        editor.putString(USER_ID, id);
        editor.putString(USER_NAME, name);
        editor.putString(USER_ACCESS_TOKEN, accessToken);
        editor.putString(USER_USERNAME, username);
        editor.putString(USER_PROFILE_PICURE_URL, profilePictureURL);
        editor.commit();
    }

    public void storeAccessToken(String accessToken) {
        editor.putString(USER_ACCESS_TOKEN, accessToken);
        editor.commit();
    }

    /**
     * Reset access token and user name
     */
    public void resetAccessToken() {
        editor.putString(USER_ID, null);
        editor.putString(USER_NAME, null);
        editor.putString(USER_ACCESS_TOKEN, null);
        editor.putString(USER_USERNAME, null);
        editor.putString(USER_PROFILE_PICURE_URL, null);
        editor.commit();
    }

    /**
     * Get user name
     *
     * @return User name
     */
    public String getUsername() {
        return sharedPref.getString(USER_USERNAME, null);
    }

    /**
     *
     * @return
     */
    public String getId() {
        return sharedPref.getString(USER_ID, null);
    }

    /**
     *
     * @return
     */
    public String getName() {
        return sharedPref.getString(USER_NAME, null);
    }

    /**
     *
     * @return
     */
    public String getProfilePicture() {
        return sharedPref.getString(USER_PROFILE_PICURE_URL, null);
    }

    /**
     * Get access token
     *
     * @return Access token
     */
    public String getAccessToken() {
        return sharedPref.getString(USER_ACCESS_TOKEN, null);
    }
}
