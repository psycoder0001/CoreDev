package com.deepdroid.coredev;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by evrenozturk on 26.04.2016.
 */
public class HelperForPref {
    private static final String PREF_USER = "CoreDevPrefs";
    private static SharedPreferences preferences;

    public static SharedPreferences getPreferences(Context applicationContext) {
        if (preferences == null) {
            preferences = applicationContext.getSharedPreferences(PREF_USER, applicationContext.MODE_PRIVATE);
        }
        return preferences;
    }

    // String
    public static void setString(Context applicationContext, String key, String value) {
        getPreferences(applicationContext).edit().putString(key, value).apply();
    }

    public static String getStringValue(Context applicationContext, String key) {
        return getPreferences(applicationContext).getString(key, "");
    }

    public static String getStringValue(Context applicationContext, String key, String defaultValue) {
        return getPreferences(applicationContext).getString(key, defaultValue);
    }

    // Boolean
    public static void setBoolean(Context applicationContext, String key, boolean value) {
        getPreferences(applicationContext).edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(Context applicationContext, String key, boolean defaultValue) {
        return getPreferences(applicationContext).getBoolean(key, defaultValue);
    }

    // Int
    public static void setInt(Context applicationContext, String key, int value) {
        getPreferences(applicationContext).edit().putInt(key, value).apply();
    }

    public static int getInt(Context applicationContext, String key, int defaultValue) {
        return getPreferences(applicationContext).getInt(key, defaultValue);
    }
}