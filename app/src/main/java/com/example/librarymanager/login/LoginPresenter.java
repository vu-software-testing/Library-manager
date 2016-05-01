package com.example.librarymanager.login;

import com.example.librarymanager.R;
import com.example.librarymanager.data.DataContract;
import com.example.librarymanager.data.User;
import com.example.librarymanager.util.PreferenceUtil;
import com.example.librarymanager.util.SharedPrefApi;

/**
 * Listens to user actions from the UI ({@link LoginFragment}), retrieves the data and updates the
 * UI accordingly
 */
public class LoginPresenter implements LoginContract.UserActionListener {

    private LoginContract.View view;
    private DataContract.Repository mRepository;
    private SharedPrefApi mPrefs;

    public LoginPresenter(LoginContract.View view, DataContract.Repository repository,
                          SharedPrefApi prefs) {
        this.view = view;
        this.mRepository = repository;
        this.mPrefs = prefs;
    }

    public void login(String vunetid, String password) {
        if (vunetid.isEmpty()) {
            view.showUsernameError(R.string.vunetid_error_message);
            return;
        }
        if (password.isEmpty()) {
            view.showPasswordError(R.string.password_error_message);
            return;
        }
        User user = mRepository.getUser(vunetid);
        if (user != null &&
                user.getVunetId().equals(vunetid) && user.getPassword().equals(password)) {
            saveLoginStatus(user);
            view.startMainActivity();
            return;
        }
        view.showLoginError(R.string.login_failed_message);
    }

    private void saveLoginStatus(User user) {
        mPrefs.setString(PreferenceUtil.APP_STATES_CURRENT_USER, user.getVunetId())
                .setBoolean(PreferenceUtil.APP_STATES_CURRENT_USER_IS_ADMIN, user.isAdmin());
    }
}