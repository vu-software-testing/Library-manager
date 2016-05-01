package com.example.librarymanager.login;

import com.example.librarymanager.R;
import com.example.librarymanager.data.DataRepository;
import com.example.librarymanager.data.User;
import com.example.librarymanager.util.PreferenceUtil;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoginPresenterTest {

    @Mock
    private LoginContract.View mView;
    @Mock
    private DataRepository mDataRepository;
    @Mock
    private PreferenceUtil mAppPref;

    private LoginContract.UserActionListener mPresenter;

    private final String mUsername = "james";
    private final String mPassword = "bond";

    @Before
    public void setUp() throws Exception {
        // Inject all mocks with the @Mock annotation.
        MockitoAnnotations.initMocks(this);

        // The object to be tested.
        mPresenter = new LoginPresenter(mView, mDataRepository, mAppPref);
    }

    @Test
    public void login_showUsernameIsEmptyErrorMessage() throws Exception {
        // When the presenter is asked to login with empty username
        mPresenter.login("", "");
        mPresenter.login("", mPassword);

        // Then error message is showed twice
        verify(mView, times(2)).showUsernameError(R.string.vunetid_error_message);
        // No further interaction with repository
        verifyNoMoreInteractions(mDataRepository);
    }

    @Test
    public void login_showPasswordIsEmptyErrorMessage() throws Exception {
        // When the presenter is asked to login with empty password
        mPresenter.login(mUsername, "");

        // Then error message is showed
        verify(mView).showPasswordError(R.string.password_error_message);
        // No further interaction with repository
        verifyNoMoreInteractions(mDataRepository);
    }

    @Test
    public void login_startMainActivity() throws Exception {
        // Mocked preference object to prevent null pointer exception
        when(mAppPref.setString(anyString(), anyString())).thenReturn(mAppPref);
        when(mAppPref.setBoolean(anyString(), anyBoolean())).thenReturn(mAppPref);
        // Repository will return a stubbed user
        when(mDataRepository.getUser(mUsername))
                .thenReturn(new User(mUsername, mPassword, null, null));

        // When presenter is asked to login with valid and correct credentials
        mPresenter.login(mUsername, mPassword);

        // Then
        verify(mDataRepository).getUser(mUsername); // user info is retrieved from repository
        verify(mView).startMainActivity(); // logged in and main view is started

    }

    @Test
    public void login_showUsernameOrPasswordErrorMessage() throws Exception {
        // Repository will return a stubbed user
        when(mDataRepository.getUser(mUsername))
                .thenReturn(new User(mUsername, mPassword, null, null));
        // Define a incorrect username
        String wrongUsername = "jame";
        when(mDataRepository.getUser(wrongUsername)).thenReturn(null);
        //Define a incorrect password
        String wrongPassword = "band";

        // When presenter is asked to login with valid but incorrect username or/and password
        mPresenter.login(mUsername, wrongPassword);
        mPresenter.login(wrongUsername, mPassword);
        mPresenter.login(wrongUsername, wrongPassword);

        // Then user info is retrieved from repository
        verify(mDataRepository).getUser(mUsername);
        verify(mDataRepository, times(2)).getUser(wrongUsername);
        // Error message is showed 3x
        verify(mView, times(3)).showLoginError(R.string.login_failed_message);

    }
}