package com.example.librarymanager.books;

import android.database.Cursor;
import android.graphics.Bitmap;

public interface BooksContract {

    interface View {

        /**
         * Let the view display Ui for detailed info about the book
         * @param bookId id of the book to be displayed
         */
        void showBookDetailUi(String bookId);

        /**
         * Let the view display Ui for adding new book
         */
        void showAddBookUi();

        /**
         * Let the view display the books
         * @param booksCursor cursor containing books to be displayed
         */
        void showBooks(Cursor booksCursor);

        /**
         * Redirect the search action from the container activity to the view
         * @param query search query
         */
        void onSearch(String query);

        /**
         * Redirect the permission callback from the container activity to the view
         * @param requestCode redirected args
         * @param permissions redirected args
         * @param grantResults redirected args
         */
        void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);

        /**
         * Let the view request user to grant read permission
         */
        void requestPermissionToReadImage();

        /**
         * Let the view display a message why the permission is needed
         */
        void showPermissionDeniedWarning();
    }

    interface UserActionListener {

        /**
         * Ask the presenter to open detailed view of the provided bookId
         * @param bookId requested book
         */
        void openBookDetail(String bookId);

        /**
         * Load all books, if no args are provided.
         * check for read permission first which are needed to load image files.
         * Show books with default image if permission is denied.
         * @param borrowerId Load books borrowed by this user, if not null.
         * @param query Load books with title containing the given string, if not null.
         */
        void loadBooksAndRequestPermissionIfNeeded(String borrowerId, String query);

        /**
         * Ask the presenter to add a new book
         */
        void addNewBook();

        /**
         * Retrieve the thumbnail from the provided path
         * @param imagePath absolute path to the image
         * @return bitmap data of the thumbnail
         */
        Bitmap getThumbnail(String imagePath);

        /**
         * Proceed with loading books after read permission granted
         */
        void onPermissionGranted();

        /**
         * Proceed with loading books (without images) after read permission denied
         */
        void onPermissionDenied();
    }
}
