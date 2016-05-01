package com.example.librarymanager.addbook;

import android.graphics.Bitmap;
import android.net.Uri;

import com.example.librarymanager.R;
import com.example.librarymanager.data.Book;
import com.example.librarymanager.data.DataContract;
import com.example.librarymanager.util.ImageFileApi;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AddBookPresenterTest {

    @Mock
    private AddBookContract.View mView;
    @Mock
    private DataContract.Repository mDataRepository;
    @Mock
    private ImageFileApi mImageFile;
    @Mock
    private Bitmap imageData;
    @Mock
    private Uri imageUri;

    private AddBookContract.UserActionListener mPresenter;

    private final String mTitle = "book for dummies";
    private final String mAuthor = "james";
    private final String mIsbn = "12341234";
    private Book mBook;

    @Before
    public void setup() {
        // Inject all mocks with the @Mock annotation.
        MockitoAnnotations.initMocks(this);

        // The object to be tested
        mPresenter = new AddBookPresenter(mView, mDataRepository, mImageFile);

        // The book to be added
        mBook = new Book(mTitle, mAuthor, mIsbn);
    }

    @Test
    public void addBook_success() {
        // Repository will insert the book successfully
        when(mDataRepository.insertBook(mBook)).thenReturn(true);

        // When the presenter is asked to add a book
        mPresenter.addBook(mTitle, mAuthor, mIsbn);

        // Then
        verify(mDataRepository).insertBook(mBook); // request passed to repository
        verify(mView).showMessage(R.string.add_book_success_message); // success message showed
        verify(mView).showBooksList(); // list of books is showed
    }

    @Test
    public void addBook_failed() {
        // Repository will not insert the book successfully
        when(mDataRepository.insertBook(mBook)).thenReturn(false);

        // When the presenter is asked to add a book
        mPresenter.addBook(mTitle, mAuthor, mIsbn);

        // Then
        verify(mDataRepository).insertBook(mBook); // request passed to repository
        verify(mView).showMessage(R.string.add_book_failed_message); // failed message showed
    }

    @Test
    public void imageTaken() {
        // When the presenter is asked to process the photo created by the camera
        mPresenter.imageTaken(imageData);

        // Then the image is showed as a thumbnail
        verify(mView).showThumbnail(imageData);
    }
}
