package com.example.librarymanager.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataRepository implements DataContract.Repository {

    private static DataContract.Repository dataRepository;
    private static MyDbHelper myDbHelper;

    public static DataContract.Repository getInstance(Context context) {
        if (dataRepository == null) {
            myDbHelper = MyDbHelper.getInstance(context);
            dataRepository = new DataRepository();
        }
        return dataRepository;
    }

    public DataRepository () {
    }

    // UserTable

    public Cursor getAllUsersCursor() {
        SQLiteDatabase readableDatabase = myDbHelper.getReadableDatabase();
        return readableDatabase.query(User.TABLE_NAME, null, null, null, null, null, null);
    }

    @Override
    public Cursor searchByNameAndVunetid(String query) {
        SQLiteDatabase db = myDbHelper.getReadableDatabase();
        String selection = User.FULL_NAME + " LIKE ? OR " + User.VUNETID + " LIKE ?";
        String[] selectionArgs = new String[]{"%" + query + "%"};
        return db.query(User.TABLE_NAME, null, selection, selectionArgs, null, null, null);
    }

    public User getUser(String vunetid) {
        SQLiteDatabase db = myDbHelper.getReadableDatabase();
        Cursor userCursor =  db.query(User.TABLE_NAME, null, User.VUNETID + "=?", new String[]{vunetid},
                null, null, null);
        if (!userCursor.moveToFirst()) {
            return null;
        } else {
            int passwdColId = userCursor.getColumnIndex(User.PASSWORD);
            int fullnameColId = userCursor.getColumnIndex(User.FULL_NAME);
            int facultyColId = userCursor.getColumnIndex(User.FACULTY);
            int adminColId = userCursor.getColumnIndex(User.ADMIN);

            String passwd = userCursor.getString(passwdColId);
            String fullname = userCursor.getString(fullnameColId);
            String faculty = userCursor.getString(facultyColId);
            boolean admin = userCursor.getInt(adminColId) == 1;

            userCursor.close();

            return new User(vunetid, passwd, fullname, faculty, admin);
        }
    }

    @Override
    public boolean insertUser(User user) {
        SQLiteDatabase writableDatabase = myDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(User.FULL_NAME, user.getFullname());
        values.put(User.PASSWORD, user.getPassword());
        values.put(User.VUNETID, user.getVunetId());
        values.put(User.FACULTY, user.getFaculty());
        values.put(User.ADMIN, user.isAdmin() ? User.ADMIN_VALUE : User.USER_VALUE);
        long insert = writableDatabase.insert(User.TABLE_NAME, null, values);
        return insert > 0;
    }

    @Override
    public boolean updateUser(User user) {
        SQLiteDatabase db = myDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        if (user.getFullname() != null) values.put(User.FULL_NAME, user.getFullname());
        if (user.getVunetId() != null) values.put(User.VUNETID, user.getVunetId());
        if (user.getFaculty() != null) values.put(User.FACULTY, user.getFaculty());
        if (user.getPassword() != null && !user.getPassword().isEmpty())
            values.put(User.PASSWORD, user.getPassword());
        int update = db.update(User.TABLE_NAME, values, User.VUNETID + "=?",
                new String[]{user.getVunetId()});
        return update > 0;
    }

    public boolean deleteUser(String vunetid) {
        SQLiteDatabase writableDatabase = myDbHelper
                .getWritableDatabase();
        int delete = writableDatabase.delete(User.TABLE_NAME, User.VUNETID + "=?",
                new String[]{vunetid});
        return delete > 0;
    }

//    public boolean checkCredential(String vunetid, String password) {
//        SQLiteDatabase readableDatabase = myDbHelper.getReadableDatabase();
//        String selection = User.VUNETID + "=? and " +
//                User.PASSWORD + "=?";
//        String[] selectionArgs = {vunetid, password};
//        Cursor query = readableDatabase.query(User.TABLE_NAME, null,
//                selection, selectionArgs, null, null,
//                null);
//        boolean qualified = query.moveToFirst();
//        query.close();
//        return qualified;
//    }

    //Book Table -----------------

    @Override
    public boolean insertBook(Book book) {
        SQLiteDatabase writableDatabase = myDbHelper
                .getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Book.TITLE, book.getTitle());
        values.put(Book.AUTHOR, book.getAuthor());
        values.put(Book.ISBN, book.getIsbn());
        values.put(Book.STATUS, Book.STATUS_AVAILABLE);
        if (book.getImagePath() != null) values.put(Book.IMAGE_PATH, book.getImagePath());
        long insert = writableDatabase.insert(Book.TABLE_NAME, null, values);
        return insert > 0;
    }

    @Override
    public Cursor getAllBooksCursor() {
        SQLiteDatabase readableDatabase = myDbHelper.getReadableDatabase();
        //MyDbHelper.BackgroundTask bgTask = myDbHelper.new BackgroundTask();
        //bgTask.execute("argsPassed");
        return readableDatabase.query(Book.TABLE_NAME, null, null, null, null, null, null);
    }

    @Override
    public Cursor getBooksBorrowedBy(String vunetid) {
        SQLiteDatabase db = myDbHelper.getReadableDatabase();
        String selection = Book.STATUS + "= ? AND " + Book.BORROWER + "= ?";
        String[] selectionArgs = new String[]{String.valueOf(Book.STATUS_BORROWED), vunetid};
        return db.query(Book.TABLE_NAME, null, selection, selectionArgs, null, null, null);
    }

    @Override
    public Cursor searchByTitle(String query) {
        SQLiteDatabase db = myDbHelper.getReadableDatabase();
        String selection = Book.TITLE + " LIKE ?";
        String[] selectionArgs = new String[]{"%" + query + "%"};
        return db.query(Book.TABLE_NAME, null, selection, selectionArgs, null, null, null);
    }

    @Override
    public Book getBook(String id) {
        SQLiteDatabase db = myDbHelper.getReadableDatabase();
        Cursor bookCursor = db.query(Book.TABLE_NAME, null, Book._ID + "=?", new String[]{id},
                null, null, null);
        bookCursor.moveToFirst();
        int titleColId = bookCursor.getColumnIndex(Book.TITLE);
        int authorColId = bookCursor.getColumnIndex(Book.AUTHOR);
        int isbnColId = bookCursor.getColumnIndex(Book.ISBN);
        int statusColId = bookCursor.getColumnIndex(Book.STATUS);
        int borrowerColId = bookCursor.getColumnIndex(Book.BORROWER);
        int imagePathColId = bookCursor.getColumnIndex(Book.IMAGE_PATH);

        String title = bookCursor.getString(titleColId);
        String author = bookCursor.getString(authorColId);
        String isbn = bookCursor.getString(isbnColId);
        boolean available = bookCursor.getInt(statusColId) == Book.STATUS_AVAILABLE;
        String borrower = bookCursor.getString(borrowerColId);
        String imagePath = bookCursor.getString(imagePathColId);

        bookCursor.close();

        return new Book(null, title, author, isbn, available, borrower, imagePath);
    }

    @Override
    public boolean updateBook(Book book) {
        SQLiteDatabase d = myDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        if (book.getTitle() != null) values.put(Book.TITLE, book.getTitle());
        if (book.getAuthor() != null) values.put(Book.AUTHOR, book.getAuthor());
        if (book.getIsbn() != null) values.put(Book.ISBN, book.getIsbn());
        int update = d.update(Book.TABLE_NAME, values, Book._ID + "=?", new String[]{book.getId()});
        return update > 0;
    }

    public boolean updateBookStatus(String id, int status, String vunetid) {
        SQLiteDatabase d = myDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Book.STATUS, status);
        if (vunetid != null) values.put(Book.BORROWER, vunetid);
        int update = d.update(Book.TABLE_NAME, values, Book._ID + "=?", new String[]{id});
        return update > 0;
    }

    public boolean deleteBook(String id) {
        SQLiteDatabase d = myDbHelper.getWritableDatabase();
        int delete = d.delete(Book.TABLE_NAME, Book._ID + "=?", new String[]{id});
        return delete > 0;
    }
}
