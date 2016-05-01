package com.example.librarymanager.userdetail;

import android.view.View;

import com.example.librarymanager.R;
import com.example.librarymanager.data.DataContract;
import com.example.librarymanager.data.User;
import com.example.librarymanager.util.PreferenceUtil;
import com.example.librarymanager.util.SharedPrefApi;

public class UserDetailPresenter implements UserDetailContract.UserActionListener {

    private final SharedPrefApi mAppPrefs;
    private final UserDetailContract.View mView;
    private final DataContract.Repository mRepository;

    public UserDetailPresenter(UserDetailContract.View view, DataContract.Repository repository,
                               SharedPrefApi appPrefs) {

        this.mView = view;
        this.mRepository = repository;
        this.mAppPrefs = appPrefs;
    }

    @Override
    public void initUi(String requestedVunetid) {
        String mLoggedVunetid = mAppPrefs.getString(PreferenceUtil.APP_STATES_CURRENT_USER, "");
        String superAdmin = mAppPrefs.getString(PreferenceUtil.APP_STATES_SUPER_ADMIN_USERNAME, "");

        User user = mRepository.getUser(requestedVunetid);
        mView.showUser(user);
        mView.setBorrowedButtonVisibility(!user.isAdmin());

        boolean enableEditAndDelete = !user.getVunetId().equals(superAdmin) &&
                !mLoggedVunetid.equals(requestedVunetid);
        mView.setEditAndDeleteButtonVisibility(enableEditAndDelete);
    }

    @Override
    public void editUser() {
        mView.showEditView();
    }

    @Override
    public void cancelEditUser() {
        mView.showStaticView();
    }

    @Override
    public void updateUser(String vunetid, String password, String passwordConfirm,
                           String fullname, String faculty, String origVunetid,
                           String origFullname, String origFaculty) {

        if (fullname.equals(origFullname) && vunetid.equals(origVunetid) &&
                faculty.equals(origFaculty) && password.isEmpty() &&
                password.equals(passwordConfirm)) {

            mView.showMessage(R.string.no_change_made_message);
            mView.showStaticView();
        } else if (vunetid.isEmpty()) {
            mView.showVunetidError(R.string.vunetid_error_message);
        } else if (fullname.isEmpty()) {
            mView.showFullnameError(R.string.fullname_error_message);
        } else if (faculty.isEmpty()) {
            mView.showFacultyError(R.string.faculty_error_message);
        } else if (!password.equals(passwordConfirm)) {
            mView.showPasswdConfirmError(R.string.password_confirm_error_message);
        } else if (mRepository.updateUser(new User(vunetid, password, fullname, faculty))) {
            mView.showMessage(R.string.update_user_success_message);
            mView.setUserDetail(vunetid, fullname, faculty);
            mView.showStaticView();
        } else {
            mView.showMessage(R.string.update_user_failed_message);
        }
    }

    @Override
    public void deleteUser() {
        mView.showDeleteUserDialog(R.string.delete_user_dialog_title,
                R.string.delete_dialog_message, R.string.dialog_positive, R.string.dialog_negative);
    }

    @Override
    public void deleteUserConfirmed(String vunetid) {
        if (mRepository.getBooksBorrowedBy(vunetid).moveToFirst()) {
            mView.showMessage(R.string.delete_book_failed_borrowing);
            return;
        }
        if (mRepository.deleteUser(vunetid)) {
            mView.showMessage(R.string.deleted_message);
            mView.finish();
        } else {
            mView.showMessage(R.string.delete_failed_message);
        }
    }

    @Override
    public void getBorrowedBooks() {
        mView.showBorrowedBooks();
    }

}
