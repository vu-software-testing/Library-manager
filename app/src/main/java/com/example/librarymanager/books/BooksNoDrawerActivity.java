package com.example.librarymanager.books;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBar;

import com.example.librarymanager.R;
import com.example.librarymanager.util.BaseActivity;

public class BooksNoDrawerActivity extends BaseActivity {

    public static final String EXTRA_BORROWER_VUNETID = "BORROWER_VUNETID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get the requested borrower
        String vunetid = getIntent().getStringExtra(EXTRA_BORROWER_VUNETID);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle(vunetid == null ? getString(R.string.toolbar_title_books) :
                    getString(R.string.toolbar_title_borrowed_books) + " " + vunetid);
        }

        if (null == savedInstanceState || savedInstanceState.isEmpty()) {
            initFragment(BooksFragment.newInstance(vunetid, null), BooksFragment.TAG);
        }
    }
}
