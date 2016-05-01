package com.example.librarymanager.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtil implements SharedPrefApi {

    private static PreferenceUtil instance;

    private SharedPreferences appPrefs;


    public static SharedPrefApi getInstance(Application application) {
        if (instance == null) {
            instance = new PreferenceUtil(application);
        }
        return instance;
    }

    public PreferenceUtil(Application application) {
        appPrefs = application.getSharedPreferences(SHARED_PREF_APP_STATES, Context.MODE_PRIVATE);
    }

    public boolean hasKey(final String key) {
        return appPrefs.contains(key);
    }

    public SharedPrefApi removeKey(final String key) {
        appPrefs.edit().remove(key).apply();
        return this;
    }

    public void clearPreference(final SharedPreferences p) {
        p.edit().clear().apply();
    }

    public String getString(String key, final String defaultValue) {
        return appPrefs.getString(key, defaultValue);
    }

    public SharedPrefApi setString(final String key, final String value) {
        appPrefs.edit().putString(key, value).apply();
        return this;
    }

    public boolean getBoolean(final String key,
                              final boolean defaultValue) {
        return appPrefs.getBoolean(key, defaultValue);
    }

    public SharedPrefApi setBoolean(final String key, final boolean value) {
        appPrefs.edit().putBoolean(key, value).apply();
        return this;
    }

    public SharedPrefApi setPrefInt(final String key, final int value) {
        appPrefs.edit().putInt(key, value).apply();
        return this;
    }

    public int getPrefInt(final String key, final int defaultValue) {
        return appPrefs.getInt(key, defaultValue);
    }

    public SharedPrefApi setPrefFloat(final String key, final float value) {
        appPrefs.edit().putFloat(key, value).apply();
        return this;
    }

    public float getPrefFloat(final String key, final float defaultValue) {
        return appPrefs.getFloat(key, defaultValue);
    }

    public SharedPrefApi setSettingLong(final String key, final long value) {
        appPrefs.edit().putLong(key, value).apply();
        return this;
    }

    public long getPrefLong(final String key, final long defaultValue) {
        return appPrefs.getLong(key, defaultValue);
    }
}
