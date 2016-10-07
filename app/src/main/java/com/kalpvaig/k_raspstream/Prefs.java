package com.kalpvaig.k_raspstream;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Razor on 10/30/2015.
 */
public class Prefs {

    public static String PREF_FILE_NAME="settings";

    public static void saveToPreferences(Context context, String preferenceName, String preferenceValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_APPEND);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }

    public static String getPreference(Context context, String preferenceName, String preferenceDefualtValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName, preferenceDefualtValue);
    }
}

