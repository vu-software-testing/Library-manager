package com.example.librarymanager.bookdetail;

import android.graphics.Bitmap;

public interface BookDetailContract {

    interface View {

        void showMessage(int messageId);

        void showStaticView();

        void showEditView(String bookId);

        void showTitleError(int messageId);

        void showAuthorError(int messageId);

        void showIsbnError(int messageId);

        void finish();

        void showDeleteBookConfirmationDialog(int titleId, int messageId, int positiveId,
                                              int negativeId, String bookId, String imagePath);

        void setThumbnail(Bitmap imageData);

        void requestPermissionToDeleteImage(String bookId, String imagePath);

        void showBookBasicInfo(String title, String author, String isbn);

        void showBookStatus(int stringId);

        void setBorrowerTextView(boolean visible, String borrower);

        void setReturnButton(boolean visible);

        void setEditButton(boolean visible);

        void setDeleteButton(boolean visible);

        void setBorrowButton(boolean visible);

        void storeImagePath(String imagePath);
    }

    interface UserActionListener {

        void openBookDetail(String requestedBookId);

        void editBook(String bookId);

        void saveBook(String bookId, String title, String author, String isbn,
                      String origTitle, String origAuthor, String origIsbn);

        void cancelEditBook();

        void deleteBook(String bookId, String imagePath);

        void deleteBookConfirmed(String bookId, String imagePath);

        void borrowBook(String bookId);

        void returnBook(String bookId);

        void onDeleteImagePermissionGranted(String bookId, String imagePath);
    }
}
