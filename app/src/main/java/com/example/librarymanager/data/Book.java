package com.example.librarymanager.data;

import android.provider.BaseColumns;

import java.lang.Object;

public class Book implements BaseColumns {

    // DB column names
    public static final String TABLE_NAME = "books";
    public static final String TITLE = "bookTitle";
    public static final String AUTHOR = "author";
    public static final String ISBN = "isbn";
    public static final String STATUS = "status";
    public static final String BORROWER = "borrower";
    public static final String IMAGE_PATH = "imagePath";


    public static final int STATUS_AVAILABLE = 1;
    public static final int STATUS_BORROWED = 0;

    private final String mId;
    private final String mTitle;
    private final String mAuthor;
    private final String mIsbn;
    private final boolean mAvailable;
    private final String mBorrower;
    private final String mImagePath;

    public Book(String title, String author, String isbn) {
        this(null, title, author, isbn, true, null, null);
    }

    public Book(String id, String title, String author, String isbn) {
        this(id, title, author, isbn, true, null, null);
    }

    public Book(String id, String title, String author, String isbn, boolean status,
                String borrower) {
        this(id, title, author, isbn, status, borrower, null);
    }

    public Book(String id, String title, String author, String isbn, boolean status,
                String borrower, String imagePath) {
        this.mId = id;
        this.mTitle = title;
        this.mAuthor = author;
        this.mIsbn = isbn;
        this.mAvailable = status;
        this.mBorrower = borrower;
        this.mImagePath = imagePath;
    }

    public String getId() {
        return mId;
    }

    public String getIsbn() {
        return mIsbn;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getTitle() {
        return mTitle;
    }

    public boolean isAvailable() {
        return mAvailable;
    }

    public String getBorrower() {
        return mBorrower;
    }

    public String getImagePath() {
        return mImagePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return mId != null ? mId.equals(book.mId) : book.mId == null &&
                mTitle.equals(book.mTitle) &&
                mAuthor.equals(book.mAuthor) &&
                mIsbn.equals(book.mIsbn) &&
                mAvailable == book.mAvailable &&
                (mBorrower != null ? mBorrower.equals(book.mBorrower) : book.mBorrower == null) &&
                (mImagePath != null ? mImagePath.equals(book.mImagePath) : book.mImagePath == null);
    }
}
