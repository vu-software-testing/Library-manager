package com.example.librarymanager.bookdetail;

import com.example.librarymanager.R;
import com.example.librarymanager.data.Book;
import com.example.librarymanager.data.DataRepository;
import com.example.librarymanager.util.ImageFileApi;
import com.example.librarymanager.util.PreferenceUtil;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BookDetailPresenterTest {

    @Mock
    private BookDetailContract.View mView;
    @Mock
    private DataRepository mDataRepository;
    @Mock
    private ImageFileApi mImageFile;
    @Mock
    private PreferenceUtil mAppPrefs;

    private Book mBorrowedBook;
    private Book mAvailableBook;

    private BookDetailContract.UserActionListener mPresenter;

    private String mVunetid = "abc123";
    private String mBookId = "123";
    private String mTitle = "title";
    private String mAuthor = "author";
    private String mIsbn = "isbn";
    private String mOrigTitle = "origTitle";
    private String mOrigAuthor = "origAuthor";
    private String mOrigIsbn = "origIsbn";
    private String mImagePath = "/somewhere";



    @Before
    public void setup() {
        // Inject all mocks with the @Mock annotation.
        MockitoAnnotations.initMocks(this);

        // The object to be tested.
        mPresenter = new BookDetailPresenter(mView, mDataRepository, mImageFile, mAppPrefs);

        String borrower = "asd234";
        mBorrowedBook = new Book(mBookId, mTitle, mAuthor, mIsbn, false, borrower, mImagePath);
        mAvailableBook = new Book(mBookId, mTitle, mAuthor, mIsbn, true, null, mImagePath);

        // Provide a logged username
        when(mAppPrefs.getString(eq(PreferenceUtil.APP_STATES_CURRENT_USER), anyString()))
                .thenReturn(mVunetid);
    }

    @Test
    public void openBookDetail_NonAdminUserAndBookAvailable() {
        // Repository will return the requested book by bookId
        when(mDataRepository.getBook(mBookId)).thenReturn(mAvailableBook);
        // current logged user is not admin
        when(mAppPrefs.getBoolean(eq(PreferenceUtil.APP_STATES_CURRENT_USER_IS_ADMIN), anyBoolean()))
                .thenReturn(false);

        mPresenter.openBookDetail(mBookId);

        // Request is passed to the repository
        verify(mDataRepository).getBook(mBookId);
        // Show the retrieved book detail
        verify(mView).showBookBasicInfo(mAvailableBook.getTitle(), mAvailableBook.getAuthor(),
                mAvailableBook.getIsbn());
        // The ui is configured accordingly
        verify(mView).showBookStatus(R.string.book_status_available);
        verify(mView).setBorrowerTextView(false, null);
        verify(mView).setReturnButton(false);

        verify(mView).setBorrowButton(true);
    }

    @Test
    public void openBookDetail_NonAdminUserAndBookBorrowed() {
        // Repository will return the requested book by bookId
        when(mDataRepository.getBook(mBookId)).thenReturn(mBorrowedBook);
        // current logged user is not admin
        when(mAppPrefs.getBoolean(eq(PreferenceUtil.APP_STATES_CURRENT_USER_IS_ADMIN), anyBoolean()))
                .thenReturn(false);

        mPresenter.openBookDetail(mBookId);

        // Request is passed to the repository
        verify(mDataRepository).getBook(mBookId);
        // Show the retrieved book detail
        verify(mView).showBookBasicInfo(mBorrowedBook.getTitle(), mBorrowedBook.getAuthor(),
                mBorrowedBook.getIsbn());
        // The ui is configured accordingly
        verify(mView).showBookStatus(R.string.book_status_borrowed);
        verify(mView).setBorrowerTextView(true, mBorrowedBook.getBorrower());
        verify(mView).setBorrowButton(false);
    }

    @Test
    public void openBookDetail_AdminUserAndBookAvailable() {
        // Repository will return the requested book by bookId
        when(mDataRepository.getBook(mBookId)).thenReturn(mAvailableBook);
        // current logged user is not admin
        when(mAppPrefs.getBoolean(eq(PreferenceUtil.APP_STATES_CURRENT_USER_IS_ADMIN), anyBoolean()))
                .thenReturn(true);

        mPresenter.openBookDetail(mBookId);

        // Request is passed to the repository
        verify(mDataRepository).getBook(mBookId);
        // Show the retrieved book detail
        verify(mView).showBookBasicInfo(mAvailableBook.getTitle(), mAvailableBook.getAuthor(),
                mAvailableBook.getIsbn());
        // The ui is configured accordingly
        verify(mView).showBookStatus(R.string.book_status_available);
        verify(mView).setBorrowerTextView(false, null);
        verify(mView).setReturnButton(false);

        verify(mView).setEditButton(true);
        verify(mView).setDeleteButton(true);
    }

    @Test
    public void openBookDetail_AdminUserAndBookBorrowed() {
        // Repository will return the requested book by bookId
        when(mDataRepository.getBook(mBookId)).thenReturn(mBorrowedBook);
        // current logged user is admin
        when(mAppPrefs.getBoolean(eq(PreferenceUtil.APP_STATES_CURRENT_USER_IS_ADMIN), anyBoolean()))
                .thenReturn(true);

        mPresenter.openBookDetail(mBookId);

        // Request is passed to the repository
        verify(mDataRepository).getBook(mBookId);
        // Show the retrieved book detail
        verify(mView).showBookBasicInfo(mBorrowedBook.getTitle(), mBorrowedBook.getAuthor(),
                mBorrowedBook.getIsbn());
        // The ui is configured accordingly
        verify(mView).showBookStatus(R.string.book_status_borrowed);
        verify(mView).setBorrowerTextView(true, mBorrowedBook.getBorrower());
        verify(mView).setBorrowButton(false);
        verify(mView).setReturnButton(true);
    }

    @Test
    public void editBook_showEditView() {
        // When presenter is asked to edit the selected book
        mPresenter.editBook(mBookId);

        // Then edit view is showed
        verify(mView).showEditView(mBookId);
    }

    @Test
    public void cancelEditBook_showStaticView() {
        // When presenter is asked to cancel editing the selected book
        mPresenter.cancelEditBook();

        // Then static view is showed
        verify(mView).showStaticView();
    }

    @Test
    public void deleteBook_showDialog() {
        // When presenter is asked to delete the selected book
        mPresenter.deleteBook(mBookId, mImagePath);

        // Then the confirmation dialog is showed
        verify(mView).showDeleteBookConfirmationDialog(R.string.delete_book_dialog_title,
                R.string.delete_dialog_message, R.string.dialog_positive, R.string.dialog_negative,
                mBookId, mImagePath);
    }



    @Test
    public void deleteBookConfirmed_success() {
        // When presenter is asked to delete the book after confirmation
        mPresenter.deleteBookConfirmed(mBookId, mImagePath);

        // Then permission is requested to delete the image
        verify(mView).requestPermissionToDeleteImage(mBookId, mImagePath);

        // Repository will delete the book successfully
        when(mDataRepository.deleteBook(mBookId)).thenReturn(true);

        // When presenter is asked to delete after permission granted
        mPresenter.onDeleteImagePermissionGranted(mBookId, mImagePath);

        // Then
        verify(mImageFile).delete(); // image is deleted
        verify(mDataRepository).deleteBook(mBookId); // request is passed to the repo
        verify(mView).showMessage(R.string.deleted_message); // success message is showed
        verify(mView).finish(); // The book detail view is popped
    }

    @Test
    public void deleteBookConfirmed_failed() {
        // When presenter is asked to delete the book after confirmation
        mPresenter.deleteBookConfirmed(mBookId, mImagePath);

        // Then permission is requested to delete the image
        verify(mView).requestPermissionToDeleteImage(mBookId, mImagePath);

        // Repository will not delete the book successfully
        when(mDataRepository.deleteBook(mBookId)).thenReturn(false);

        // When presenter is asked to delete after permission granted
        mPresenter.onDeleteImagePermissionGranted(mBookId, mImagePath);

        // Then
        verify(mDataRepository).deleteBook(mBookId); // request is passed to the repository
        verify(mView).showMessage(R.string.delete_failed_message); // failed message is showed
    }

    @Test
    public void borrowBook_success() {
        // current logged user is not admin
        when(mAppPrefs.getBoolean(eq(PreferenceUtil.APP_STATES_CURRENT_USER_IS_ADMIN), anyBoolean()))
                .thenReturn(false);
        // Repository will update book status successfully
        when(mDataRepository.updateBookStatus(mBookId, Book.STATUS_BORROWED, mVunetid))
                .thenReturn(true);

        // Provide new book info after borrowed
        when(mDataRepository.getBook(mBookId)).thenReturn(mBorrowedBook);

        // When repository is asked to borrow the selected book by the given username
        mPresenter.borrowBook(mBookId);

        // Then request is passed to repository
        verify(mDataRepository).updateBookStatus(mBookId, Book.STATUS_BORROWED, mVunetid);
        // new book info is retrieved
        verify(mDataRepository).getBook(mBookId);
        // success message is showed
        verify(mView).showMessage(R.string.borrowed_message);
        // view is updated
        verify(mView).showBookStatus(R.string.book_status_borrowed);
        verify(mView).setBorrowerTextView(true, mBorrowedBook.getBorrower());
        verify(mView).setBorrowButton(false);
    }

    @Test
    public void borrowBook_failed() {
        // Repository will not update book status successfully
        when(mDataRepository.updateBookStatus(mBookId, Book.STATUS_BORROWED, mVunetid))
                .thenReturn(false);

        // When repository is asked to borrow the selected book by the given username
        mPresenter.borrowBook(mBookId);

        // Then request is passed to repository and failed message is showed
        verify(mDataRepository).updateBookStatus(mBookId, Book.STATUS_BORROWED, mVunetid);
        verify(mView).showMessage(R.string.borrow_failed_message);
    }

    @Test
    public void returnBook_success() {
        // current logged user is admin
        when(mAppPrefs.getBoolean(eq(PreferenceUtil.APP_STATES_CURRENT_USER_IS_ADMIN), anyBoolean()))
                .thenReturn(true);
        // Repository will update book status successfully
        when(mDataRepository.updateBookStatus(mBookId, Book.STATUS_AVAILABLE, null))
                .thenReturn(true);

        // Provide new book info after updated
        when(mDataRepository.getBook(mBookId)).thenReturn(mAvailableBook);

        // When repository is asked to return the selected book by the given username
        mPresenter.returnBook(mBookId);

        // Then request is passed to repository and success message is showed
        verify(mDataRepository).updateBookStatus(mBookId, Book.STATUS_AVAILABLE, null);
        verify(mView).showMessage(R.string.returned_message);
        // new book info is retrieved
        verify(mDataRepository).getBook(mBookId);
        verify(mView).showBookStatus(R.string.book_status_available);
        verify(mView).setBorrowerTextView(false, null);
        verify(mView).setReturnButton(false);

        verify(mView).setEditButton(true);
        verify(mView).setDeleteButton(true);
    }

    @Test
    public void returnBook_failed() {
        // Repository will not update book status successfully
        when(mDataRepository.updateBookStatus(mBookId, Book.STATUS_AVAILABLE, null))
                .thenReturn(false);

        // When repository is asked to return the selected book by the given username
        mPresenter.returnBook(mBookId);

        // Then request is passed to repository and failed message is showed
        verify(mDataRepository).updateBookStatus(mBookId, Book.STATUS_AVAILABLE, null);
        verify(mView).showMessage(R.string.return_failed_message);
    }

    @Test
    public void saveBook_noChange() {
        // When presenter is asked to update the book info without any changes
        mPresenter.saveBook(mBookId, mTitle, mAuthor, mIsbn, mTitle, mAuthor, mIsbn);

        // Then
        verify(mView).showMessage(R.string.no_change_made_message); // no change message is showed
        verify(mView).showStaticView(); // static view is shoed
    }

    @Test
    public void saveBook_titleEmptyError() {
        // When presenter is asked to update the book info with empty field
        mPresenter.saveBook(mBookId, "", mAuthor, mIsbn, mOrigTitle, mOrigAuthor, mOrigIsbn);

        // Then the designated error message is showed
        verify(mView).showTitleError(R.string.title_error_message);
    }
    @Test
    public void saveBook_authorEmptyError() {
        // When presenter is asked to update the book info with empty field
        mPresenter.saveBook(mBookId, mTitle, "", mIsbn, mOrigTitle, mOrigAuthor, mOrigIsbn);

        // Then the designated error message is showed
        verify(mView).showAuthorError(R.string.author_error_message);
    }
    @Test
    public void saveBook_isbnEmptyError() {
        // When presenter is asked to update the book info with empty field
        mPresenter.saveBook(mBookId, mTitle, mAuthor, "", mOrigTitle, mOrigAuthor, mOrigIsbn);

        // Then the designated error message is showed
        verify(mView).showIsbnError(R.string.isbn_error_message);
    }
    @Test
    public void saveBook_success() {
        // current logged user is admin
        when(mAppPrefs.getBoolean(eq(PreferenceUtil.APP_STATES_CURRENT_USER_IS_ADMIN), anyBoolean()))
                .thenReturn(true);
        // repository will update the book successfully
        when(mDataRepository.updateBook(mAvailableBook)).thenReturn(true);

        // Provide new book info after updated
        when(mDataRepository.getBook(mBookId)).thenReturn(mAvailableBook);

        // When presenter is asked to update the book with valid fields
        mPresenter.saveBook(mBookId, mTitle, mAuthor, mIsbn, mOrigTitle, mOrigAuthor, mOrigIsbn);

        // Then
        verify(mDataRepository).updateBook(mAvailableBook); // request is passed to repository
        // new book info is retrieved
        verify(mDataRepository).getBook(mBookId);
        verify(mView).showMessage(R.string.update_book_success_message); // success message showed
        verify(mView).showStaticView(); // static view showed

        verify(mView).showBookStatus(R.string.book_status_available);
        verify(mView).setBorrowerTextView(false, null);
        verify(mView).setReturnButton(false);

        verify(mView).setEditButton(true);
        verify(mView).setDeleteButton(true);
    }
    @Test
    public void saveBook_failed() {
        // repository will not update the book successfully
        when(mDataRepository.updateBook(mAvailableBook)).thenReturn(false);

        // When presenter is asked to update the book with valid fields
        mPresenter.saveBook(mBookId, mTitle, mAuthor, mIsbn, mOrigTitle, mOrigAuthor, mOrigIsbn);

        verify(mDataRepository).updateBook(mAvailableBook); // request is passed to repository
        verify(mView).showMessage(R.string.update_book_failed_message); // failed message showed
    }
}
