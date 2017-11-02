package com.deepdroid.coredev;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.deepdroid.coredev.devdialog.serviceurlselection.UrlSelectionItem;

/**
 * Created by evrenozturk on 26.04.2016.
 */
public class HelperForPref {
    private static final String PREF_GENERAL = "CoreDevPrefs";
    private static final String PREF_URL_SELECTION = "CoreDevPrefsForUrlSelection";
    private static SharedPreferences preferences;

    public static SharedPreferences getPreferences(Context applicationContext) {
        if (preferences == null) {
            preferences = applicationContext.getSharedPreferences(PREF_GENERAL, Context.MODE_PRIVATE);
        }
        return preferences;
    }

    private static SharedPreferences getPreferencesForUrlSelection(Context applicationContext) {
        if (preferences == null) {
            preferences = applicationContext.getSharedPreferences(PREF_URL_SELECTION, Context.MODE_PRIVATE);
        }
        return preferences;
    }

    // String
    public static void putString(Context applicationContext, String key, String value) {
        getPreferences(applicationContext).edit().putString(key, value).apply();
    }

    public static String getStringValue(Context applicationContext, String key) {
        return getPreferences(applicationContext).getString(key, "");
    }

    public static String getStringValue(Context applicationContext, String key, String defaultValue) {
        return getPreferences(applicationContext).getString(key, defaultValue);
    }

    // Boolean
    public static void putBoolean(Context applicationContext, String key, boolean value) {
        getPreferences(applicationContext).edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(Context applicationContext, String key, boolean defaultValue) {
        return getPreferences(applicationContext).getBoolean(key, defaultValue);
    }

    // Int
    public static void putInt(Context applicationContext, String key, int value) {
        getPreferences(applicationContext).edit().putInt(key, value).apply();
    }

    public static int getInt(Context applicationContext, String key, int defaultValue) {
        return getPreferences(applicationContext).getInt(key, defaultValue);
    }

    public static void putUrlSelection(Context applicationContext, UrlSelectionItem urlSelectionItem) {
        if (urlSelectionItem == null) {
            return;
        }
        getPreferencesForUrlSelection(applicationContext).edit().putInt(urlSelectionItem.itemId + "_index", urlSelectionItem.selectionIndex).apply();
        getPreferencesForUrlSelection(applicationContext).edit().putString(urlSelectionItem.itemId + "_value", urlSelectionItem.selectionValue).apply();
    }

    public static UrlSelectionItem getUrlSelection(Context applicationContext, int itemId) {
        int selectionIndex = getPreferencesForUrlSelection(applicationContext).getInt(itemId + "_index", -1);
        String selectionValue = getPreferencesForUrlSelection(applicationContext).getString(itemId + "_value", "");
        if (selectionIndex < 0 || TextUtils.isEmpty(selectionValue)) {
            return null;
        }
        return new UrlSelectionItem(itemId, selectionIndex, selectionValue);
    }

    public static void clearUrlSelections(Context appCx) {
        getPreferencesForUrlSelection(appCx).edit().clear().apply();
    }
}