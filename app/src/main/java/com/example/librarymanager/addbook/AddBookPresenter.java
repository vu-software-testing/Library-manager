package com.example.librarymanager.addbook;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.VisibleForTesting;

import com.example.librarymanager.R;
import com.example.librarymanager.data.Book;
import com.example.librarymanager.data.DataContract;
import com.example.librarymanager.util.ImageFileApi;

import java.io.IOException;

public class AddBookPresenter implements AddBookContract.UserActionListener {

    private AddBookContract.View mView;
    private DataContract.Repository mRepository;
    private ImageFileApi mImageFile;
    private Bitmap mImageData;
    private Uri mImageUri;
    private Book mBookToSave;

    public AddBookPresenter(AddBookContract.View view, DataContract.Repository repository,
                            ImageFileApi imageFile) {
        mView = view;
        mRepository = repository;
        mImageFile = imageFile;
    }

    @Override
    public void addBook(final String title, final String author, final String isbn) {
        mBookToSave = new Book(null, title, author, isbn, true, null, null);
        if (mImageData != null) {
           mView.requestPermissionToWriteImage();
        } else {
            addBookFinal();
        }
    }

    @Override
    public void onWritePermissionGranted() {
        if (saveImage()) {
            mBookToSave = new Book(null, mBookToSave.getTitle(), mBookToSave.getAuthor(),
                    mBookToSave.getIsbn(), true, null, mImageFile.getPath());
            addBookFinal();
        } else {
            addBookFinal();
        }
    }

    @VisibleForTesting
    void addBookFinal() {

        if (mRepository.insertBook(mBookToSave)) {
            mView.showMessage(R.string.add_book_success_message);
            mView.showBooksList();
        } else {
            mView.showMessage(R.string.add_book_failed_message);
        }
    }

    @Override
    public void imageTaken(Bitmap imageData) {
        if (imageData == null) {
            throw new RuntimeException("AddBookPresenter: Error retrieving image data");
        }
        mImageData = imageData;
        mView.showThumbnail(mImageData);
    }

    @Override
    public void imageSelected(Uri imageUri) {
        mImageUri = imageUri;
        mView.requestPermissionToReadImage();
    }

    @Override
    public void onReadPermissionGranted() {
        retrieveImage();
        mView.showThumbnail(mImageData);
    }

    private void retrieveImage() {
        mImageFile.createImageFileFromPath(resolveContentPath(mImageUri));
        mImageData = mImageFile.getImageData();
    }

    private String resolveContentPath(Uri uri) {
        Cursor cursor = null;
        Context context = mView.getCurrentContext();
        try {
            String[] projection = {MediaStore.MediaColumns.DATA};
            cursor = context.getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            } else {
                return null;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private boolean saveImage() {
        try {
            if (!mImageFile.exists()) {
                mImageFile.createImageFile();
            }
            return mImageFile.saveImageData(mImageData);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
