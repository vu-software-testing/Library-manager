package com.example.librarymanager.addbook;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

public interface AddBookContract {

    interface View {

        void showMessage(int messageId);

        void showBooksList();

        void showThumbnail(Bitmap imageData);

        Context getCurrentContext();

        Activity getActivity();

        android.view.View getView();

        void requestPermissionToReadImage();

        void requestPermissionToWriteImage();
    }

    interface UserActionListener {

        void addBook(String title, String author, String isbn);

        void imageTaken(Bitmap imageData);

        void imageSelected(Uri imageUri);

        void onReadPermissionGranted();

        void onWritePermissionGranted();
    }
}