package com.example.librarymanager.util;

import android.app.Application;
import android.content.Context;

import com.example.librarymanager.data.DataContract;
import com.example.librarymanager.data.FakeDataRepository;

/**
 * Enables injection of mock implementations at comoile time.
 */

public class Injection {

    public static ImageFileApi provideImageFile() {
        return new FakeImageFileApiImpl();
    }

    public static SharedPrefApi provideSharedPref(Application application) {
        return FakePreferenceUtil.getInstance();
    }
//    public static DataServiceApi provideDataServiceApi(Context context) {
//        return FakeDataServiceImpl.getInstance();
//    }
//
    public static DataContract.Repository provideRepository(Context context) {
        return new FakeDataRepository();
    }

}
