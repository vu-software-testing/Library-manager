package com.example.librarymanager.login;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface LoginContract {

    interface View {
        /**
         * Let the view display the error message
         * @param vunetid_error string id
         */
        void showUsernameError(int vunetid_error);

        /**
         * Let the view display the error message
         * @param password_error string id
         */
        void showPasswordError(int password_error);

        /**
         * Let the view start the main activity after logged in
         */
        void startMainActivity();

        /**
         * Let the view display the error message
         * @param login_failed string id
         */
        void showLoginError(int login_failed);
    }

    interface UserActionListener {

        /**
         * Ask the presenter to login with the credentials provided
         * @param vunetid user id
         * @param password password
         */
        void login(String vunetid, String password);

    }
}