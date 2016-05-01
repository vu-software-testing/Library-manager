package com.example.librarymanager.books;

import android.database.Cursor;

import com.example.librarymanager.data.DataRepository;
import com.example.librarymanager.util.ImageFileApi;
import com.github.buchandersenn.android_permission_manager.PermissionManager;
import com.github.buchandersenn.android_permission_manager.PermissionRequestBuilder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BooksPresenterTest {

    @Mock
    private BooksContract.View mView;
    @Mock
    private DataRepository mDataRepository;
    @Mock
    private ImageFileApi mImageFile;
    @Mock
    private Cursor mCursor;
    @Mock
    private PermissionManager mPermissionManager;
    @Mock
    private PermissionRequestBuilder mPermissionRequestBuilder;

    private BooksPresenter mPresenter;

    @Before
    public void setup() {
        // Inject all mocks with the @Mock annotation.
        MockitoAnnotations.initMocks(this);

        // The object to be tested.
        mPresenter = new BooksPresenter(mView, mDataRepository, mImageFile);
    }

    @Test
    public void loadBooks_showAllBooks() {
        when(mDataRepository.getAllBooksCursor()).thenReturn(mCursor);

        mPresenter.loadBooksAndRequestPermissionIfNeeded(null, null);

        mPresenter.onPermissionGranted();

        verify(mDataRepository).getAllBooksCursor();
        verify(mView).showBooks(mCursor);
    }

    @Test
    public void loadBooks_searchByTitle() {
        String query = "foo";
        when(mDataRepository.searchByTitle(query)).thenReturn(mCursor);

        mPresenter.loadBooksAndRequestPermissionIfNeeded(null, query);

        mPresenter.onPermissionGranted();

        verify(mDataRepository).searchByTitle(query);
        verify(mView).showBooks(mCursor);
    }

    @Test
    public void loadBooks_showBorrowedBooks() {
        String borrower = "bar";
        when(mDataRepository.getBooksBorrowedBy(borrower)).thenReturn(mCursor);

        mPresenter.loadBooksAndRequestPermissionIfNeeded(borrower, null);

        mPresenter.onPermissionGranted();

        verify(mDataRepository).getBooksBorrowedBy(borrower);
        verify(mView).showBooks(mCursor);
    }

    @Test
    public void openBookDetail() {
        String bookId = "1";
        mPresenter.openBookDetail(bookId);

        verify(mView).showBookDetailUi(bookId);
    }

    @Test
    public void addNewBook() {
        mPresenter.addNewBook();

        verify(mView).showAddBookUi();
    }
}
