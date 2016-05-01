package com.example.librarymanager.users;

import android.database.Cursor;

import com.example.librarymanager.data.DataRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UsersPresenterTest {

    @Mock
    private UsersContract.View mView;
    @Mock
    private DataRepository mDataRepository;
    @Mock
    private Cursor mCursor;

    private UsersContract.UserActionListener mPresenter;

    @Before
    public void setup() {
        // Inject all mocks with the @Mock annotation.
        MockitoAnnotations.initMocks(this);

        // The object to be tested.
        mPresenter = new UsersPresenter(mView, mDataRepository);
    }

    @Test
    public void loadUsersFromRepositoryAndPassToView() {
        // Repository will return a stubbed cursor
        when(mDataRepository.getAllUsersCursor()).thenReturn(mCursor);

        // When presenter is asked to load all users
        mPresenter.loadUsers(null);

        // Then
        verify(mDataRepository).getAllUsersCursor(); // users are retrieved from repository,
        verify(mView).showUsers(mCursor); // and passed to the view
    }

    @Test
    public void searchUserFromRepositoryAndPassToView() {
        // Define a random search query
        String query = "abc";
        // Repository will return a stubbed cursor
        when(mDataRepository.searchByNameAndVunetid(query)).thenReturn(mCursor);

        // When presenter is asked to search for users containing the query string
        mPresenter.loadUsers(query);

        // Then the search result is retrieved from the repository,
        verify(mDataRepository).searchByNameAndVunetid(query);
        verify(mView).showUsers(mCursor); // and passed to the view
    }

    @Test
    public void clickOnUser_ShowDetailUi() {
        // Define a random username
        String vunetid = "abc123";

        // When presenter is asked to show the details about a user
        mPresenter.openUserDetail(vunetid);

        // Then the detail Ui is shown
        verify(mView).showUserDetailUi(vunetid);
    }

    @Test
    public void clickOnFab_ShowAddUserUi() {
        // When adding a new user
        mPresenter.addNewUser();

        // Then add user Ui is shown
        verify(mView).showAddUserUi();
    }
}
