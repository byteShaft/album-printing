package com.byteshaft.albumsapp.utils;

import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

public class Config {

    public static void setIsLoggedIn(boolean loggedIn) {
        SharedPreferences preferences = AppGlobals.getPreferences();
        preferences.edit().putBoolean(Constants.CONFIG_KEY_FIRST_RUN, loggedIn).apply();
    }

    public static boolean isLoggedIn() {
        SharedPreferences preferences = AppGlobals.getPreferences();
        return preferences.getBoolean(Constants.CONFIG_KEY_FIRST_RUN, false);
    }

    public static void saveUserProfile(String userDataAsJsonString) {
        SharedPreferences preferences = AppGlobals.getPreferences();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_profile", userDataAsJsonString);
        editor.apply();
    }

    private static String getProfileData() {
        SharedPreferences preferences = AppGlobals.getPreferences();
        return preferences.getString("user_profile", null);
    }

    public static JSONObject getUserProfile() {
        JSONObject profileObject = null;
        try {
            profileObject = new JSONObject(getProfileData());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return profileObject;
    }
}
