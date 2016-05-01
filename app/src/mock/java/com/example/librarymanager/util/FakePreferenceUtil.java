package com.example.librarymanager.util;

import android.content.SharedPreferences;

public class FakePreferenceUtil implements SharedPrefApi{

    private static FakePreferenceUtil instance;

    public static SharedPrefApi getInstance() {
        if (instance == null) {
            instance = new FakePreferenceUtil();
        }
        return instance;
    }

    @Override
    public boolean hasKey(String key) {
        return true;
    }

    @Override
    public SharedPrefApi removeKey(String key) {
        return this;
    }

    @Override
    public void clearPreference(SharedPreferences p) {

    }

    @Override
    public String getString(String key, String defaultValue) {
        switch (key) {
            case APP_STATES_CURRENT_USER:
                return "admin";
            case APP_STATES_CURRENT_USER_IS_ADMIN:
                break;
            case APP_STATES_SUPER_ADMIN_USERNAME:
                return "admin1";
        }
        return null;
    }

    @Override
    public SharedPrefApi setString(String key, String value) {
        return this;
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        switch (key) {
            case APP_STATES_CURRENT_USER:
                break;
            case APP_STATES_CURRENT_USER_IS_ADMIN:
                return true;
            case APP_STATES_SUPER_ADMIN_USERNAME:
                break;
        }
        return false;
    }

    @Override
    public SharedPrefApi setBoolean(String key, boolean value) {
        return this;
    }

    @Override
    public SharedPrefApi setPrefInt(String key, int value) {
        return this;
    }

    @Override
    public int getPrefInt(String key, int defaultValue) {
        return 0;
    }

    @Override
    public SharedPrefApi setPrefFloat(String key, float value) {
        return this;
    }

    @Override
    public float getPrefFloat(String key, float defaultValue) {
        return 0;
    }

    @Override
    public SharedPrefApi setSettingLong(String key, long value) {
        return this;
    }

    @Override
    public long getPrefLong(String key, long defaultValue) {
        return 0;
    }
}
