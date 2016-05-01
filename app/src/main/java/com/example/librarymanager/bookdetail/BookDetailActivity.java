package com.example.librarymanager.bookdetail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;

import com.example.librarymanager.R;
import com.example.librarymanager.util.BaseActivity;

public class BookDetailActivity extends BaseActivity {

    public static final String EXTRA_BOOK_ID = "BOOK_ID";

    private Fragment mBookDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar;
        if ((actionBar = getSupportActionBar()) != null) {
            actionBar.setTitle(R.string.toolbar_title_bookdetail);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        //Get the requested book id
        String bookId = getIntent().getStringExtra(EXTRA_BOOK_ID);

        if (null == savedInstanceState || savedInstanceState.isEmpty()) {
            mBookDetailFragment = BookDetailFragment.newInstance(bookId);
            initFragment(mBookDetailFragment, BookDetailFragment.TAG);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mBookDetailFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
