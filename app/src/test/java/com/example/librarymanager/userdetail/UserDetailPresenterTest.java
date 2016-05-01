package com.example.librarymanager.userdetail;

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

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserDetailPresenterTest {

    @Mock
    private UserDetailContract.View mView;
    @Mock
    private DataRepository mDataRepository;
    @Mock
    private PreferenceUtil mAppPrefs;

    private User mUser;

    private UserDetailContract.UserActionListener mPresenter;

    private final String mNonAdminUsername = "abc123";
    private final String mAdminUsername = "admin";
    private final String mSuperAdminUsername = "superAdmin";
    private final String mFullname = "james";
    private final String mFaculty = "few";
    private final String mPassword = "foo";
    private final String mPasswordConfirm = "bar";
    private final String mOrigVunetid = "def456";
    private final String mOrigFullname = "tom";
    private final String mOrigFaculty = "faw";

    @Before
    public void setup() {
        // Inject all mocks with the @Mock annotation.
        MockitoAnnotations.initMocks(this);

        // The object to be tested.
        mPresenter = new UserDetailPresenter(mView, mDataRepository, mAppPrefs);

        // The selected user to view detail about
        mUser = new User(mNonAdminUsername, mPassword, mFullname, mFaculty, false);

        // The username of the super admin will be returned if requested
        when(mAppPrefs.getString(PreferenceUtil.APP_STATES_SUPER_ADMIN_USERNAME, ""))
                .thenReturn(mSuperAdminUsername);
    }

    @Test
    public void initUi_setUiForCurrentLoggedAccount() {
        // Repository will return an admin username as current logged user
        when(mAppPrefs.getString(PreferenceUtil.APP_STATES_CURRENT_USER, ""))
                .thenReturn(mAdminUsername);
        // Let the selected user be an admin
        mUser = new User(mAdminUsername, mPassword, mFullname, mFaculty, true);
        // Repository will return user info of the selected admin user
        when(mDataRepository.getUser(mAdminUsername)).thenReturn(mUser);

        // When presenter is asked to initiate the Ui for an admin user
        mPresenter.initUi(mAdminUsername);

        // Then edit, delete and borrowed buttons are hidden
        verify(mView).setEditAndDeleteButtonVisibility(false);
        verify(mView).setBorrowedButtonVisibility(false);
    }

    @Test
    public void initUi_setUiForOtherAdminAccount() {
        // Repository will return the super admin username as current logged user
        when(mAppPrefs.getString(PreferenceUtil.APP_STATES_CURRENT_USER, ""))
                .thenReturn(mSuperAdminUsername);
        // Let the selected user be an admin
        mUser = new User(mNonAdminUsername, mPassword, mFullname, mFaculty, true);
        // Repository will return user info of the selected admin user
        when(mDataRepository.getUser(mAdminUsername)).thenReturn(mUser);

        // When presenter is asked to initiate the Ui for an admin user
        mPresenter.initUi(mAdminUsername);

        // Then borrowed buttons are hidden
        verify(mView).setBorrowedButtonVisibility(false);
    }

    @Test
    public void initUi_setUiForSuperAdminAccount() {
        // Repository will return the super admin username as current logged user
        when(mAppPrefs.getString(PreferenceUtil.APP_STATES_CURRENT_USER, ""))
                .thenReturn(mAdminUsername);
        // Let the selected user be an admin
        mUser = new User(mSuperAdminUsername, mPassword, mFullname, mFaculty, true);
        // Repository will return user info of the selected user
        when(mDataRepository.getUser(mSuperAdminUsername)).thenReturn(mUser);

        // When presenter is asked to initiate the Ui for the super admin
        mPresenter.initUi(mSuperAdminUsername);

        // Then edit, delete and borrowed buttons are hidden
        verify(mView).setBorrowedButtonVisibility(false);
        verify(mView).setEditAndDeleteButtonVisibility(false);
    }

    @Test
    public void initUi_setUiForNonAdminUser() {
        // Repository will return the super admin username as current logged user
        when(mAppPrefs.getString(PreferenceUtil.APP_STATES_CURRENT_USER, ""))
                .thenReturn(mAdminUsername);
        // Repository will return user info of the selected user
        when(mDataRepository.getUser(mNonAdminUsername)).thenReturn(mUser);

        // When presenter is asked to initiate the Ui for an user
        mPresenter.initUi(mNonAdminUsername);

        // Then none of the edit, delete or borrowed buttons are hidden
        verify(mView, never()).setEditAndDeleteButtonVisibility(false);
        verify(mView, never()).setBorrowedButtonVisibility(false);
    }

    @Test
    public void editUser_showEditView() {
        // When presenter is asked to start editing an user
        mPresenter.editUser();

        // Then the edit view is showed
        verify(mView).showEditView();
    }

    @Test
    public void cancelEditUser_showStaticView() {
        // When presenter is asked to cancel editing an user
        mPresenter.cancelEditUser();

        // Then the static view is showed
        verify(mView).showStaticView();
    }

    @Test
    public void updateUser_noChangeMadeMessageAndCancelEdit() {
        // When presenter is asked to update an user without any change
        mPresenter.updateUser(mNonAdminUsername, "", "", mFullname, mFaculty,
                mNonAdminUsername, mFullname, mFaculty);

        // Then
        verify(mView).showMessage(R.string.no_change_made_message); // nothing changed message is showed,
        verify(mView).showStaticView(); // the static view is showed
    }

    @Test
    public void updateUser_vunetidEmptyError() {
        // When presenter is asked to update an user with empty field
        mPresenter.updateUser("", mPassword, mPasswordConfirm, mFullname, mFaculty, mOrigVunetid,
                mOrigFullname, mOrigFaculty);

        // Then the empty error is showed
        verify(mView).showVunetidError(R.string.vunetid_error_message);
    }

    @Test
    public void updateUser_nameEmptyError() {
        // When presenter is asked to update an user with empty field
        mPresenter.updateUser(mNonAdminUsername, mPassword, mPasswordConfirm, "", mFaculty,
                mOrigVunetid, mOrigFullname, mOrigFaculty);

        // Then the empty error is showed
        verify(mView).showFullnameError(R.string.fullname_error_message);
    }

    @Test
    public void updateUser_facultyEmptyError() {
        // When presenter is asked to update an user with empty field
        mPresenter.updateUser(mNonAdminUsername, mPassword, mPasswordConfirm, mFullname, "",
                mOrigVunetid, mOrigFullname, mOrigFaculty);

        // Then the empty error is showed
        verify(mView).showFacultyError(R.string.faculty_error_message);
    }

    @Test
    public void updateUser_passwordConfirmError() {
        // When presenter is asked to update an user with inconsistent password
        mPresenter.updateUser(mNonAdminUsername, mPassword, mPasswordConfirm, mFullname, mFaculty,
                mOrigVunetid, mOrigFullname, mOrigFaculty);

        // Then is designated error message is showed
        verify(mView).showPasswdConfirmError(R.string.password_confirm_error_message);
    }

    @Test
    public void updateUser_success() {
        // The repository will update the user successfully
        when(mDataRepository.updateUser(mUser)).thenReturn(true);

        // When presenter is asked to update an user with valid fields
        mPresenter.updateUser(mNonAdminUsername, mPassword, mPassword, mFullname, mFaculty,
                mOrigVunetid, mOrigFullname, mOrigFaculty);

        // Then
        verify(mDataRepository).updateUser(mUser); // update request is passed to the repository,
        verify(mView).showMessage(R.string.update_user_success_message); // success message is displayed,
        verify(mView).showStaticView(); // the static view is showed
    }

    @Test
    public void updateUser_failed() {
        // The repository will not update the user successfully
        when(mDataRepository.updateUser(mUser)).thenReturn(false);

        // When presenter is asked to update an user with valid fields
        mPresenter.updateUser(mNonAdminUsername, mPassword, mPassword, mFullname, mFaculty,
                mOrigVunetid, mOrigFullname, mOrigFaculty);

        // Then
        verify(mDataRepository).updateUser(mUser); // update request is passed to the repository,
        verify(mView).showMessage(R.string.update_user_failed_message); // the failed message is displayed
    }

    @Test
    public void deleteUser_showDialog() {
        // When presenter is asked to delete the user
        mPresenter.deleteUser();

        // Then the confirmation dialog is showed
        verify(mView).showDeleteUserDialog(R.string.delete_user_dialog_title,
                R.string.delete_dialog_message, R.string.dialog_positive, R.string.dialog_negative);
    }

    @Test
    public void deleteUserConfirmed_success() {
        // The repository will delete the user successfully
        when(mDataRepository.deleteUser(mNonAdminUsername)).thenReturn(true);

        // When presenter is asked to delete the user after confirmed
        mPresenter.deleteUserConfirmed(mNonAdminUsername);

        // Then
        verify(mDataRepository).deleteUser(mNonAdminUsername); // request is passed to the repo,
        verify(mView).showMessage(R.string.deleted_message); // success message is displayed,
        verify(mView).finish();
    }

    @Test
    public void deleteUserConfirmed_failed() {
        // The repository will not delete the user successfully
        when(mDataRepository.deleteUser(mNonAdminUsername)).thenReturn(false);

        // When presenter is asked to delete the user after confirmed
        mPresenter.deleteUserConfirmed(mNonAdminUsername);

        // Then
        verify(mDataRepository).deleteUser(mNonAdminUsername);// request is passed to the repo,
        verify(mView).showMessage(R.string.delete_failed_message); // the failed message is displayed
    }

    @Test
    public void getBorrowedBooks_showBorrowedBooks() {
        // When presenter is asked to show the book borrowed by the selected user.
        mPresenter.getBorrowedBooks();

        // Then the borrowed books view is showed
        verify(mView).showBorrowedBooks();
    }
}
