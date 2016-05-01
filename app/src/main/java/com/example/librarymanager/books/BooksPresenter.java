package com.example.librarymanager.books;

import android.graphics.Bitmap;

import com.example.librarymanager.data.DataContract;
import com.example.librarymanager.util.ImageFileApi;

/**
 * Listens to user actions from the UI ({@link BooksPresenter}), retrieves the data and update
 * the UI as required. All objects are injected for testing purpose.
 */
public class BooksPresenter implements BooksContract.UserActionListener {

    private final ImageFileApi mImageFile;
    private final BooksContract.View mView;
    private final DataContract.Repository mRepository;

    private boolean readPermissionGranted = false;
    private String mBorrowerId;
    private String mQuery;

    /**
     * @param mView             the View object
     * @param repository        the Data model object
     * @param imageFile         managing the image file of the book
     */
    public BooksPresenter(BooksContract.View mView, DataContract.Repository repository,
                          ImageFileApi imageFile) {

        this.mView = mView;
        this.mRepository = repository;
        this.mImageFile = imageFile;
    }

    @Override
    public void openBookDetail(String bookId) {
        mView.showBookDetailUi(bookId);
    }

    @Override
    public void loadBooksAndRequestPermissionIfNeeded(final String borrowerId, final String query) {
        mBorrowerId = borrowerId;
        mQuery = query;
        mView.requestPermissionToReadImage();
    }

    @Override
    public void onPermissionGranted() {
        loadBooksFinal();
        readPermissionGranted = true;
    }

    @Override
    public void onPermissionDenied() {
        loadBooksFinal();
        mView.showPermissionDeniedWarning();
    }

    private void loadBooksFinal() {
        if (mBorrowerId != null) {
            mView.showBooks(mRepository.getBooksBorrowedBy(mBorrowerId));
        } else if (mQuery != null) {
            mView.showBooks(mRepository.searchByTitle(mQuery));
        } else {
            mView.showBooks(mRepository.getAllBooksCursor());
        }
    }

    @Override
    public void addNewBook() {
        mView.showAddBookUi();
    }

    @Override
    public Bitmap getThumbnail(String imagePath) {
        if (readPermissionGranted) {
            mImageFile.createImageFileFromPath(imagePath);
            return mImageFile.getImageData();
        } else {
            return null;
        }
    }
}
