package com.example.librarymanager.users;

import com.example.librarymanager.data.DataContract;

/**
 * Listens to user actions from the UI ({@link UsersFragment}), retrieves the data and updates
 * the UI as required
 */
public class UsersPresenter implements UsersContract.UserActionListener {

    private UsersContract.View mView;
    private DataContract.Repository mRepository;

    public UsersPresenter(UsersContract.View view, DataContract.Repository repository) {

        this.mView = view;
        this.mRepository = repository;
    }

    @Override
    public void addNewUser() {
        mView.showAddUserUi();
    }

    /**
     * Load all users, if no args provided.
     * @param query load all users with query in their name or vunetid, if not null
     */
    @Override
    public void loadUsers(String query) {
        if (query == null) {
            mView.showUsers(mRepository.getAllUsersCursor());
        } else {
            mView.showUsers(mRepository.searchByNameAndVunetid(query));
        }
    }

    @Override
    public void openUserDetail(String vunetid) {
        mView.showUserDetailUi(vunetid);
    }
}
