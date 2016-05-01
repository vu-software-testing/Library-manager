package com.example.librarymanager.userdetail;

import com.example.librarymanager.data.User;

public interface UserDetailContract {

    interface View {

        void showEditView();

        void showBorrowedBooks();

        void showDeleteUserDialog(int titleId, int messageId, int positiveId, int negativeId);

        void showStaticView();

        void showMessage(int messageId);

        void finish();

        void setUserDetail(String vunetid, String fullname, String faculty);

        void showVunetidError(int messageId);

        void showFullnameError(int messageId);

        void showFacultyError(int messageId);

        void showPasswdConfirmError(int messageId);

        void showUser(User user);

        void setBorrowedButtonVisibility(boolean visible);

        void setEditAndDeleteButtonVisibility(boolean visible);
    }

    interface UserActionListener {

        void deleteUserConfirmed(String vunetid);

        void cancelEditUser();

        void updateUser(String vunetid, String password, String passwordConfirm, String fullname,
                        String faculty, String origVunetid, String origFullname, String origFaculty);

        void deleteUser();

        void getBorrowedBooks();

        void editUser();

        void initUi(String vunetid);
    }
}
