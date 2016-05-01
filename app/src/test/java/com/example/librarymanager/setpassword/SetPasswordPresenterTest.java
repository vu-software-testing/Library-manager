package com.example.librarymanager.setpassword;

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

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SetPasswordPresenterTest {

    @Mock
    private SetPasswordContract.View mView;
    @Mock
    private DataRepository mDataRepository;
    @Mock
    private PreferenceUtil mAppPrefs;

    private SetPasswordContract.UserActionListener mPresenter;

    private final String mAdminUsername = "admin";
    private final String mVunetid = "abc123";
    private final String mOldPassword = "foo";
    private final String mPassword = "zip";
    private final String mPasswordConfirm = "zap";

    @Before
    public void setup() {
        // Inject all mocks with the @Mock annotation.
        MockitoAnnotations.initMocks(this);

        // Object to be tested
        mPresenter = new SetPasswordPresenter(mView, mDataRepository, mAppPrefs);
    }

    @Test
    public void initUi_uiForInitialization() {
        // When presenter is asked to initialize the Ui for initialization
        mPresenter.initUi(SetPasswordContract.View.ACTION_INITIALIZATION);

        // Then initialization Ui is shown
        verify(mView).setUiForInitialization();
    }

    @Test
    public void initUi_uiForPasswordChange() {
        // When presenter is asked to initialize the Ui for passwordChange
        mPresenter.initUi(SetPasswordContract.View.ACTION_CHANGE_PASSWORD);

        // Then password change Ui is shown
        verify(mView).setUiForPasswordChange();
    }

    @Test
    public void initializeSuperAdmin_adminUsernameEmptyError() {
        // When presenter is asked to initialize admin with empty username
        mPresenter.initializeSuperAdmin("", mPassword, mPasswordConfirm);

        // Then empty username error is shown
        verify(mView).showAdminUsernameError(R.string.username_empty_error_message);
    }

    @Test
    public void changePassword_oldPasswordEmptyError() {
        // When presenter is asked to initialize admin with empty original password
        mPresenter.changePassword(mVunetid, "", mPassword, mPasswordConfirm);

        // Then empty password error is shown
        verify(mView).showOldPasswordError(R.string.password_error_message);
    }

    @Test
    public void changePassword_wrongOldPassword() {
        // Repository will return a new user
        when(mDataRepository.getUser(mVunetid))
                .thenReturn(new User(mVunetid, mOldPassword, null, null));

        // Define a wrong password
        String mWrongOldPassword = "bar";
        // When presenter is asked to change password with wrong original password
        mPresenter.changePassword(mVunetid, mWrongOldPassword, mPassword,
                mPasswordConfirm);

        // Then wrong password message is shown
        verify(mDataRepository).getUser(mVunetid);
        verify(mView).showOldPasswordError(R.string.password_incorrect_message);
        verify(mDataRepository, never()).updateUser(any(User.class));
    }

    @Test
    public void changePasswordAndInitializeSuperAdmin_passwordEmptyError() {
        // Repository will return a new user
        when(mDataRepository.getUser(mVunetid))
                .thenReturn(new User(mVunetid, mOldPassword, mPassword, mPasswordConfirm));

        // When presenter is asked to do something with wrong original password
        mPresenter.changePassword(mVunetid, mOldPassword, "", mPasswordConfirm);
        mPresenter.initializeSuperAdmin(mAdminUsername, "", mPasswordConfirm);

        // Then password error message is shown 2x
        verify(mView, times(2)).showPasswordError(R.string.password_error_message);
        verify(mDataRepository, never()).updateUser(any(User.class));
    }

    @Test
    public void changePasswordAndInitializeSuperAdmin_passwordConfirmError() {
        // Repository will return a new user
        when(mDataRepository.getUser(mVunetid))
                .thenReturn(new User(mVunetid, mOldPassword, mPassword, mPasswordConfirm));

        // When presenter is asked to do something with inconsistent password
        mPresenter.changePassword(mVunetid, mOldPassword, mPassword, mPasswordConfirm);
        mPresenter.initializeSuperAdmin(mAdminUsername, mPassword, mPasswordConfirm);

        // Then password confirm error message is shown 2x
        verify(mView, times(2)).showPasswordConfirmError(R.string.password_confirm_error_message);
        verify(mDataRepository, never()).updateUser(any(User.class));
    }

    @Test
    public void changePassword_updateSuccess() {
        // Repository will return a new user
        when(mDataRepository.getUser(mVunetid))
                .thenReturn(new User(mVunetid, mOldPassword, null, null));
        // Repository will update the user account
        when(mDataRepository.updateUser(new User(mVunetid, mPassword, null, null)))
                .thenReturn(true);

        mPresenter.changePassword(mVunetid, mOldPassword, mPassword,
                mPassword);

        verify(mDataRepository).getUser(mVunetid);
        verify(mDataRepository).updateUser(new User(mVunetid, mPassword, null, null));
        verify(mView).showMessage(R.string.set_password_success_message);
        verify(mView).finish();
    }

    @Test
    public void changePassword_updateFailed() {
        // Repository will return a new user
        when(mDataRepository.getUser(mVunetid))
                .thenReturn(new User(mVunetid, mOldPassword, null, null));
        // Repository will not update the user account
        when(mDataRepository.updateUser(new User(mVunetid, mPassword, null, null)))
                .thenReturn(false);

        mPresenter.changePassword(mVunetid, mOldPassword, mPassword,
                mPassword);

        verify(mDataRepository).getUser(mVunetid);
        verify(mDataRepository).updateUser(new User(mVunetid, mPassword, null, null));
        verify(mView).showMessage(R.string.set_password_failed_message);
    }

    @Test
    public void initializeSuperAdmin_initSuccess() {
        // Repository will insert the new admin account
        when(mDataRepository.insertUser(new User(mAdminUsername, mPassword, mAdminUsername,
                mAdminUsername, true))).thenReturn(true);

        mPresenter.initializeSuperAdmin(mAdminUsername, mPassword,
                mPassword);

        verify(mDataRepository).insertUser(new User(mAdminUsername, mPassword, mAdminUsername,
                mAdminUsername, true));
        verify(mView).showMessage(R.string.admin_account_initialized_message);
        verify(mView).finish();
    }

    @Test
    public void initializeSuperAdmin_initFailed() {
        // Repository will not insert the new admin account
        when(mDataRepository.insertUser(new User(mAdminUsername, mPassword, mAdminUsername,
                mAdminUsername, true))).thenReturn(false);

        mPresenter.initializeSuperAdmin(mAdminUsername, mPassword,
                mPassword);

        verify(mDataRepository).insertUser(new User(mAdminUsername, mPassword, mAdminUsername,
                mAdminUsername, true));
        verify(mView).showMessage(R.string.set_password_failed_message);
    }
}
