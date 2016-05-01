package com.example.librarymanager.util;

import android.graphics.Bitmap;

import java.io.IOException;

public class FakeImageFileApiImpl implements ImageFileApi {

    @Override
    public String createImageFile() throws IOException {
        return "file:///android_asset/atsl-logo.png";
    }

    @Override
    public void createImageFileFromPath(String imagePath) {

    }

    @Override
    public Bitmap getImageData() {
        return null;
    }

    @Override
    public boolean exists() {
        return true;
    }

    @Override
    public boolean delete() {
        return true;
    }

    @Override
    public String getPath() {
        return "file:///android_asset/atsl-logo.png";
    }

    @Override
    public boolean saveImageData(Bitmap imageDate) {
        return false;
    }
}
