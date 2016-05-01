package com.example.librarymanager.util;

import android.app.Application;
import android.content.Context;

import com.example.librarymanager.data.DataContract;
import com.example.librarymanager.data.DataRepository;

/**
 * Enables injection of production implementations at comoile time.
 */

public class Injection {

    public static ImageFileApi provideImageFile() {
        return new ImageFileImpl();
    }

    public static DataContract.Repository provideRepository(Context context) {
        return DataRepository.getInstance(context);
    }

    public static SharedPrefApi provideSharedPref(Application application) {
        return PreferenceUtil.getInstance(application);
    }
}
