package com.example.librarymanager.users;

import android.database.Cursor;

public interface UsersContract {

    interface View {

        /**
         * Let the view display the UI for adding new user
         */
        void showAddUserUi();

        /**
         * Let the view display the UI for detailed info about the user
         * @param vunetid id of the user to be displayed
         */
        void showUserDetailUi(String vunetid);

        /**
         * Let the view display the users
         * @param userCursor cursor containing users to be displayed
         */
        void showUsers(Cursor userCursor);

        /**
         * Redirect the search action from the container activity to the view
         * @param query search query
         */
        void onSearch(String query);
    }

    interface UserActionListener {

        /**
         * Ask the presenter to add a new user
         */
        void addNewUser();

        /**
         * Load all users, if query is not given
         * @param query Load users the id or name containing the given query, if not null
         */
        void loadUsers(String query);

        /**
         * Ask the presenter to show detailed info of the user
         * @param vunetid requested user id
         */
        void openUserDetail(String vunetid);
    }
}
