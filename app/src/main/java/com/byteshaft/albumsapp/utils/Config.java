package com.byteshaft.albumsapp.utils;

import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

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
        JSONObject data = getStringAsJson(userDataAsJsonString);
        SharedPreferences preferences = AppGlobals.getPreferences();
        SharedPreferences.Editor editor = preferences.edit();
        Iterator<?> keys = data.keys();
        while(keys.hasNext()) {
            String key = (String) keys.next();
            try {
                editor.putString(key, data.getString(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        editor.apply();
    }

    private static JSONObject getStringAsJson(String data) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static String getFullName() {
        SharedPreferences preferences = AppGlobals.getPreferences();
        return preferences.getString("full_name", null);
    }

    public static String getToken() {
        SharedPreferences preferences = AppGlobals.getPreferences();
        return preferences.getString("token", null);
    }

    public static void userActivationState(boolean state) {
        SharedPreferences preferences = AppGlobals.getPreferences();
        preferences.edit().putBoolean(Constants.KEY_USER_ACTIVATE_STATE, state).apply();
    }

    public static boolean isUserActive() {
        SharedPreferences preferences = AppGlobals.getPreferences();
        return preferences.getBoolean(Constants.KEY_USER_ACTIVATE_STATE, false);
    }

    public static void userRegistrationDone(boolean state) {
        SharedPreferences preferences = AppGlobals.getPreferences();
        preferences.edit().putBoolean(Constants.KEY_USER_REGISTER_STATE, state).apply();
    }

    public static boolean isUSerRegister() {
        SharedPreferences preferences = AppGlobals.getPreferences();
        return preferences.getBoolean(Constants.KEY_USER_REGISTER_STATE, false);
    }
}
