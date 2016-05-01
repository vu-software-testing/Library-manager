package com.example.librarymanager.adduser;

import com.example.librarymanager.R;
import com.example.librarymanager.data.DataRepository;
import com.example.librarymanager.data.User;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AddUserPresenterTest {

    @Mock
    private AddUserContract.View mView;
    @Mock
    private DataRepository mDataRepository;

    private User mUser;

    private AddUserContract.UserActionListener mPresenter;

    private final String mVunetid = "abc123";
    private final String mPassword = "foo";
    private final String mFullname = "james";
    private final String mFaculty = "few";
    private boolean mAddAsAdmin = false;

    @Before
    public void setup() {
        // Inject all mocks with the @Mock annotation.
        MockitoAnnotations.initMocks(this);

        // The object to be tested.
        mPresenter = new AddUserPresenter(mView, mDataRepository);

        // The user to be inserted
        mUser = new User(mVunetid, mPassword, mFullname, mFaculty);
    }

    @Test
    public void addUser_userInfoEmptyError() {
        // When any field is missing
        mPresenter.addUser("", mPassword, mPassword, mFullname, mFaculty, mAddAsAdmin);
        mPresenter.addUser(mVunetid, "", mPassword, mFullname, mFaculty, mAddAsAdmin);
        mPresenter.addUser(mVunetid, mPassword, "", mFullname, mFaculty, mAddAsAdmin);
        mPresenter.addUser(mVunetid, mPassword, mPassword, "", mFaculty, mAddAsAdmin);
        mPresenter.addUser(mVunetid, mPassword, mPassword, mFullname, "", mAddAsAdmin);

        // Then the designated error message is displayed
        verify(mView).showVunetidError(R.string.vunetid_error_message);
        verify(mView).showPasswordError(R.string.password_error_message);
        verify(mView).showPasswordConfirmError(R.string.password_confirm_error_message);
        verify(mView).showFullnameError(R.string.fullname_error_message);
        verify(mView).showFacultyError(R.string.faculty_error_message);

    }

    @Test
    public void addUser_success() {
        // The user can be successfully inserted
        when(mDataRepository.insertUser(mUser)).thenReturn(true);

        // When the presenter is asked to add a user with given fields
        mPresenter.addUser(mVunetid, mPassword, mPassword, mFullname, mFaculty, mAddAsAdmin);

        // Then the user is
        verify(mDataRepository).insertUser(mUser); // created and passed to the repository,
        verify(mView).showMessage(R.string.add_user_success_message); // success message is displayed,
        verify(mView).finish(); // addUser view is popped.
    }

    @Test
    public void addUser_failed() {
        // The user cannot be inserted
        when(mDataRepository.insertUser(mUser)).thenReturn(false);

        // When the presenter is asked to add a user with given fields
        mPresenter.addUser(mVunetid, mPassword, mPassword, mFullname, mFaculty, mAddAsAdmin);

        // Then the user is
        verify(mDataRepository).insertUser(mUser); // created and passed to the repository,
        verify(mView).showMessage(R.string.add_user_failed_message); // failed message is displayed.
    }
}
