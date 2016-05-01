package com.example.librarymanager.adduser;

public interface AddUserContract {

    interface View {

        void showPasswordError(int messageId);

        void showPasswordConfirmError(int messageId);

        void finish();

        void showMessage(int messageId);

        void showVunetidError(int messageId);

        void showFacultyError(int messageId);

        void showFullnameError(int messageId);
    }

    interface UserActionListener {


        void addUser(String vunetid, String password, String passwordConfirm, String fullname,
                     String faculty, boolean addAsAdmin);
    }
}
