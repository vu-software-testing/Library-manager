package com.example.librarymanager.util;

import android.content.SharedPreferences;

public interface SharedPrefApi {

    String SHARED_PREF_APP_STATES = "sharedPrefAppStates";
    String APP_STATES_CURRENT_USER = "APP_STATES_CURRENT_USER";
    String APP_STATES_SUPER_ADMIN_USERNAME = "APP_STATES_SUPER_ADMIN_USERNAME";
    String APP_STATES_CURRENT_USER_IS_ADMIN = "CURRENT_USER_IS_ADMIN";

    boolean hasKey(final String key);

    SharedPrefApi removeKey(final String key);

    void clearPreference(final SharedPreferences p);

    String getString(String key, final String defaultValue);

    SharedPrefApi setString(final String key, final String value);

    boolean getBoolean(final String key, final boolean defaultValue);

    SharedPrefApi setBoolean(final String key, final boolean value);

    SharedPrefApi setPrefInt(final String key, final int value);

    int getPrefInt(final String key, final int defaultValue);

    SharedPrefApi setPrefFloat(final String key, final float value);

    float getPrefFloat(final String key, final float defaultValue);

    SharedPrefApi setSettingLong(final String key, final long value);

    long getPrefLong(final String key, final long defaultValue);
}
