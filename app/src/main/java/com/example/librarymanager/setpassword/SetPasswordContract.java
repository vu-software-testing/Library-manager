package com.example.librarymanager.setpassword;

import android.app.Activity;

public interface SetPasswordContract {

    interface View {

        //set initial password of admin
        int ACTION_INITIALIZATION = 0;
        //regular password change
        int ACTION_CHANGE_PASSWORD = 1;

        void showMessage(int messageId);

        void finish();

        void showOldPasswordError(int messageId);

        void showPasswordConfirmError(int messageId);

        void showPasswordError(int messageId);

        void showAdminUsernameError(int messageId);

        String getString(int resId);

        void startLoginActivity();

        /**
         * Let the view display the UI for initialize admin account
         */
        void setUiForInitialization();

        /**
         * Let the view display the UI for changing password
         */
        void setUiForPasswordChange();
    }
    
    interface UserActionListener{

        void initUi(int actionType);

        void initializeSuperAdmin(String adminUsername, String password, String passwordConfirm);

        void changePassword(String vunetId, String oldPassword, String password,
                            String passwordConfirm);
    }

}
