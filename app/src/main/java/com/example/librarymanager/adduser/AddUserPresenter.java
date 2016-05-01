package com.example.librarymanager.adduser;

import com.example.librarymanager.R;
import com.example.librarymanager.data.DataContract;
import com.example.librarymanager.data.User;

/**
 * Listens to user actions from the UI ({@link AddUserFragment}), retrieves the data and updates
 * the UI as required.
 */
public class AddUserPresenter implements AddUserContract.UserActionListener {

    private AddUserContract.View mView;
    private DataContract.Repository mRepository;

    public AddUserPresenter(AddUserContract.View view, DataContract.Repository repository) {

        this.mView = view;
        this.mRepository = repository;
    }

    @Override
    public void addUser(String vunetid, String password, String passwordConfirm, String fullname,
                        String faculty, boolean addAsAdmin) {
        if (vunetid.isEmpty()) {
            mView.showVunetidError(R.string.vunetid_error_message);
        } else if (fullname.isEmpty()) {
            mView.showFullnameError(R.string.fullname_error_message);
        } else if (faculty.isEmpty()) {
            mView.showFacultyError(R.string.faculty_error_message);
        } else if (password.isEmpty()) {
            mView.showPasswordError(R.string.password_error_message);
        } else if (!passwordConfirm.equals(password)) {
            mView.showPasswordConfirmError(R.string.password_confirm_error_message);
        } else {
            if (mRepository.insertUser(new User(vunetid, password, fullname, faculty, addAsAdmin))) {
                mView.showMessage(R.string.add_user_success_message);
                mView.finish();
            } else {
                mView.showMessage(R.string.add_user_failed_message);
            }
        }
    }
}
