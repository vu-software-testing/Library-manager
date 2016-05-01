package com.example.librarymanager.bookdetail;

import com.example.librarymanager.R;
import com.example.librarymanager.data.Book;
import com.example.librarymanager.data.DataContract;
import com.example.librarymanager.util.ImageFileApi;
import com.example.librarymanager.util.PreferenceUtil;
import com.example.librarymanager.util.SharedPrefApi;

public class BookDetailPresenter implements BookDetailContract.UserActionListener {

    private final SharedPrefApi mAppPrefs;
    private final BookDetailContract.View mView;
    private final DataContract.Repository mRepository;
    private final ImageFileApi mImageFile;

    public BookDetailPresenter(BookDetailContract.View view, DataContract.Repository repository,
                               ImageFileApi imageFile, SharedPrefApi appPrefs) {
        this.mView = view;
        this.mRepository = repository;
        this.mImageFile = imageFile;
        this.mAppPrefs = appPrefs;
    }

    @Override
    public void openBookDetail(String requestedBookId) {

        Book book = mRepository.getBook(requestedBookId);
        showBookDetail(book);

        if (book.getImagePath() != null) {
            mImageFile.createImageFileFromPath(book.getImagePath());
            mView.setThumbnail(mImageFile.getImageData());
        }
    }

    @Override
    public void editBook(String bookId) {
        mView.showEditView(bookId);
    }

    @Override
    public void saveBook(String bookId, String title, String author, String isbn,
                         String origTitle, String origAuthor, String origIsbn) {

        if (!title.equals(origTitle) ||
                !author.equals(origAuthor) ||
                !isbn.equals(origIsbn)) {
            if (title.isEmpty()) {
                mView.showTitleError(R.string.title_error_message);
            } else if (author.isEmpty()) {
                mView.showAuthorError(R.string.author_error_message);
            } else if (isbn.isEmpty()) {
                mView.showIsbnError(R.string.isbn_error_message);
            } else if (mRepository.updateBook(new Book(bookId, title, author, isbn))) {
                Book book = mRepository.getBook(bookId);
                mView.showMessage(R.string.update_book_success_message);
                mView.showStaticView();
                showBookDetail(book);
            } else {
                mView.showMessage(R.string.update_book_failed_message);
            }
        } else {
            mView.showMessage(R.string.no_change_made_message);
            mView.showStaticView();
        }
    }

    @Override
    public void cancelEditBook() {
        mView.showStaticView();
    }

    @Override
    public void deleteBook(String bookId, String imagePath) {
        mView.showDeleteBookConfirmationDialog(R.string.delete_book_dialog_title,
                R.string.delete_dialog_message, R.string.dialog_positive, R.string.dialog_negative, bookId, imagePath);
    }

    @Override
    public void deleteBookConfirmed(String bookId, String imagePath) {
        if (imagePath != null) {
            mView.requestPermissionToDeleteImage(bookId, imagePath);
        } else {
            deleteBookFinal(bookId);
        }
    }

    @Override
    public void onDeleteImagePermissionGranted(String bookId, String imagePath) {
        mImageFile.createImageFileFromPath(imagePath);
        mImageFile.delete();
        deleteBookFinal(bookId);
    }

    private void deleteBookFinal(String bookId) {
        if (mRepository.deleteBook(bookId)) {
            mView.showMessage(R.string.deleted_message);
            mView.finish();
        } else {
            mView.showMessage(R.string.delete_failed_message);
        }
    }

    @Override
    public void borrowBook(String bookId) {
        String username = mAppPrefs.getString(PreferenceUtil.APP_STATES_CURRENT_USER, "");
        if (!username.equals("") &&
                mRepository.updateBookStatus(bookId, Book.STATUS_BORROWED, username)) {
            Book book = mRepository.getBook(bookId);
            mView.showMessage(R.string.borrowed_message);
            showBookDetail(book);
        } else {
            mView.showMessage(R.string.borrow_failed_message);
        }
    }

    @Override
    public void returnBook(String bookId) {
        if (mRepository.updateBookStatus(bookId, Book.STATUS_AVAILABLE, null)) {
            Book book = mRepository.getBook(bookId);
            mView.showMessage(R.string.returned_message);
            showBookDetail(book);
        } else {
            mView.showMessage(R.string.return_failed_message);
        }
    }

    private void showBookDetail(Book book) {
        boolean isAdmin;
        isAdmin = mAppPrefs.getBoolean(PreferenceUtil.APP_STATES_CURRENT_USER_IS_ADMIN, false);

        mView.showBookBasicInfo(book.getTitle(), book.getAuthor(), book.getIsbn());
        mView.storeImagePath(book.getImagePath());
        if (book.isAvailable()) {
            mView.showBookStatus(R.string.book_status_available);
            mView.setBorrowerTextView(false, null);
            mView.setReturnButton(false);

            if (isAdmin) {
                mView.setEditButton(true);
                mView.setDeleteButton(true);
            } else {
                mView.setBorrowButton(true);
            }
        } else {
            mView.showBookStatus(R.string.book_status_borrowed);
            mView.setBorrowerTextView(true, book.getBorrower());
            mView.setBorrowButton(false);

            if (isAdmin) {
                mView.setReturnButton(true);
            }
        }
    }
}
