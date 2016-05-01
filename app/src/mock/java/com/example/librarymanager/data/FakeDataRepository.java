package com.example.librarymanager.data;

import android.database.Cursor;

public class FakeDataRepository implements DataContract.Repository {

    @Override
    public Cursor getAllUsersCursor() {
        return null;
    }

    @Override
    public Cursor searchByNameAndVunetid(String query) {
        return null;
    }

    @Override
    public User getUser(String vunetid) {
        return null;
    }

    @Override
    public boolean insertUser(User user) {
        return false;
    }

    @Override
    public boolean updateUser(User user) {
        return false;
    }

    @Override
    public boolean deleteUser(String vunetid) {
        return false;
    }

    @Override
    public boolean insertBook(Book book) {
        return false;
    }

    @Override
    public Cursor getAllBooksCursor() {
        return null;
    }

    @Override
    public Cursor getBooksBorrowedBy(String vunetid) {
        return null;
    }

    @Override
    public Cursor searchByTitle(String query) {
        return null;
    }

    @Override
    public Book getBook(String id) {
        return null;
    }

    @Override
    public boolean updateBook(Book book) {
        return false;
    }

    @Override
    public boolean updateBookStatus(String id, int status, String vunetid) {
        return false;
    }

    @Override
    public boolean deleteBook(String id) {
        return false;
    }
}
