package com.example.librarymanager.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

public class MyDbHelper extends SQLiteOpenHelper {

    private static final String PRIMARY_KEY = " PRIMARY KEY";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String NOT_NULL = " NOT NULL";
    private static final String UNIQUE = " UNIQUE";
    private static final String SQL_CREATE_ENTRIES_USERS =
            "CREATE TABLE " + User.TABLE_NAME + " (" +
                    User._ID + INTEGER_TYPE + PRIMARY_KEY + COMMA_SEP +
                    User.VUNETID + TEXT_TYPE + NOT_NULL + UNIQUE + COMMA_SEP +
                    User.PASSWORD + TEXT_TYPE + NOT_NULL + COMMA_SEP +
                    User.FULL_NAME + TEXT_TYPE + NOT_NULL + COMMA_SEP +
                    User.FACULTY + TEXT_TYPE + NOT_NULL + COMMA_SEP +
                    User.ADMIN + INTEGER_TYPE + NOT_NULL +
            " )";

    private static final String SQL_CREATE_ENTRIES_BOOKS =
            "CREATE TABLE " + Book.TABLE_NAME + " (" +
                    Book._ID + INTEGER_TYPE + PRIMARY_KEY + COMMA_SEP +
                    Book.TITLE + TEXT_TYPE + NOT_NULL + COMMA_SEP +
                    Book.AUTHOR + TEXT_TYPE + NOT_NULL + COMMA_SEP +
                    Book.ISBN + TEXT_TYPE + NOT_NULL + COMMA_SEP +
                    Book.STATUS + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
                    Book.BORROWER + TEXT_TYPE + COMMA_SEP +
                    Book.IMAGE_PATH + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_ENTRIES_USERS =
            "DROP TABLE IF EXISTS " + User.TABLE_NAME;

	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "LibraryManager.db";

    private static MyDbHelper helper;

    public static MyDbHelper getInstance(Context context) {
        if (helper == null) {
            if (context == null) {
                throw new RuntimeException("context needed to initiate DbHelper");
            }
            helper = new MyDbHelper(context);
        }
        return helper;
    }

	private MyDbHelper(Context context) {
		super(context.getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase myDatabase) {
		myDatabase.execSQL(SQL_CREATE_ENTRIES_USERS);
		myDatabase.execSQL(SQL_CREATE_ENTRIES_BOOKS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a demo version, so its upgrade policy is
        // to simply to discard the data and start over
        //db.execSQL(SQL_DELETE_ENTRIES_USERS);
        //db.execSQL(SQL_CREATE_ENTRIES_BOOKS);
        //onCreate(db);
    }

    public class BackgroundTask extends AsyncTask<String, Void, Void> {


        @Override
        protected Void doInBackground(String... params) {
            Log.d("test async: ", "doInBg..");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            //super.onProgressUpdate(values);
        }
    }

}
