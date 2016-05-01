package com.example.librarymanager.setpassword;

import com.example.librarymanager.R;
import com.example.librarymanager.data.DataContract;
import com.example.librarymanager.data.User;
import com.example.librarymanager.util.PreferenceUtil;
import com.example.librarymanager.util.SharedPrefApi;

public class SetPasswordPresenter implements SetPasswordContract.UserActionListener {


    private final SharedPrefApi mAppPrefs;
    private final SetPasswordContract.View mView;
    private final DataContract.Repository mRepository;

    public SetPasswordPresenter(SetPasswordContract.View view, DataContract.Repository repository,
                                SharedPrefApi appPrefs) {
        this.mView = view;
        this.mRepository = repository;
        this.mAppPrefs = appPrefs;
    }

    @Override
    public void initUi(int actionType) {
        switch (actionType) {
            case SetPasswordContract.View.ACTION_INITIALIZATION:
                mView.setUiForInitialization();
                break;
            case SetPasswordContract.View.ACTION_CHANGE_PASSWORD:
                mView.setUiForPasswordChange();
                break;
        }
    }

    @Override
    public void initializeSuperAdmin(String adminUsername, String password, String passwordConfirm) {
        if (adminUsername.isEmpty()) {
            mView.showAdminUsernameError(R.string.username_empty_error_message);
            return;
        }

        if (!checkNewPassword(password, passwordConfirm)) {
            return;
        }

        if (mRepository.insertUser(new User(adminUsername, password, adminUsername,
                adminUsername, true))) {
            mView.showMessage(R.string.admin_account_initialized_message);
            mAppPrefs.setString(PreferenceUtil.APP_STATES_SUPER_ADMIN_USERNAME, adminUsername);
            mView.startLoginActivity();
            mView.finish();
        } else {
            mView.showMessage(R.string.set_password_failed_message);
        }

    }

    @Override
    public void changePassword(String vunetId, String oldPassword, String password, String passwordConfirm) {
        if (oldPassword.isEmpty()) {
            mView.showOldPasswordError(R.string.password_error_message);
            return;
        }
        User user = mRepository.getUser(vunetId);
        if (!user.getVunetId().equals(vunetId) || !user.getPassword().equals(oldPassword)) {
            mView.showOldPasswordError(R.string.password_incorrect_message);
            return;
        }

        if (!checkNewPassword(password, passwordConfirm)) return;

        if (mRepository.updateUser(new User(vunetId, password, null, null))) {
            mView.showMessage(R.string.set_password_success_message);
            mView.finish();
        } else {
            mView.showMessage(R.string.set_password_failed_message);
        }
    }

    private boolean checkNewPassword(String password, String passwordConfirm) {
        if (password.isEmpty()) {
            mView.showPasswordError(R.string.password_error_message);
            return false;
        }
        if (!passwordConfirm.equals(password)) {
            mView.showPasswordConfirmError(R.string.password_confirm_error_message);
            return false;
        }
        return true;
    }
}
