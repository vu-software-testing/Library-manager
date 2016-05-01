package com.example.librarymanager.data;

import android.database.Cursor;

public interface DataContract {

    interface Repository {

        interface LoadBooksCallback {
            void onBooksLoaded(Cursor booksCursor);
        }

        interface LoadUsersCallback {
            void onUsersLoaded(Cursor usersCursor);
        }

        Cursor getAllUsersCursor();

        Cursor searchByNameAndVunetid(String query);

        User getUser(String vunetid);

        boolean insertUser(User user);

        boolean updateUser(User user);

        boolean deleteUser(String vunetid);

        boolean insertBook(Book book);

        Cursor getAllBooksCursor();

        Cursor getBooksBorrowedBy(String vunetid);

        Cursor searchByTitle(String query);

        Book getBook(String id);

        boolean updateBook(Book book);

        boolean updateBookStatus(String id, int status, String vunetid);

        boolean deleteBook(String id);
    }
}
