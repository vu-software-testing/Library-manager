package com.example.librarymanager.data;


import android.provider.BaseColumns;

import java.util.Objects;

public class User implements BaseColumns {

    public static final String TABLE_NAME = "users";
    public static final String VUNETID = "vunetid";
    public static final String PASSWORD = "password";
    public static final String FULL_NAME = "fullName";
    public static final String FACULTY = "faculty";
    public static final String ADMIN = "admin";

    public static final int USER_VALUE = 0;
    public static final int ADMIN_VALUE = 1;


    private final String mVunetId;
    private final String mPassword;
    private final String mFullname;
    private final String mFaculty;
    private final boolean mIsAdmin;

    public User(String vunetId, String password, String fullname, String faculty) {
        this(vunetId, password, fullname, faculty, false);
    }

    public User(String vunetId, String password, String fullname, String faculty,
                boolean mIsAdmin) {

        this.mVunetId = vunetId;

        //Add salt & hashing
        this.mPassword = password;
        this.mFullname = fullname;
        this.mFaculty = faculty;
        this.mIsAdmin = mIsAdmin;
    }

    public String getVunetId() {
        return mVunetId;
    }

    public String getPassword() {
        return mPassword;
    }

    public String getFullname() {
        return mFullname;
    }

    public String getFaculty() {
        return mFaculty;
    }

    public boolean isAdmin() {
        return mIsAdmin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (null == o || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(mVunetId, user.mVunetId) &&
                Objects.equals(mPassword, user.mPassword) &&
                Objects.equals(mFullname, user.mFullname) &&
                Objects.equals(mFaculty, user.mFaculty) &&
                Objects.equals(mIsAdmin, user.mIsAdmin);
    }
}
