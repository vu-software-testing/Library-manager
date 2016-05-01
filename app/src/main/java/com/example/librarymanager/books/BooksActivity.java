package com.example.librarymanager.books;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBar;
import android.view.Menu;

import com.example.librarymanager.R;
import com.example.librarymanager.util.BaseDrawerActivity;

public class BooksActivity extends BaseDrawerActivity {

    public static final String EXTRA_BORROWER_VUNETID = "BORROWER_VUNETID";

    BooksContract.View mFragment;
    private String mVunetid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get the requested borrower
        mVunetid = getIntent().getStringExtra(EXTRA_BORROWER_VUNETID);

        ActionBar actionBar;
        actionBar = getSupportActionBar();

            NavigationView nv = (NavigationView) findViewById(R.id.nv_main);
            if (actionBar != null && nv != null) {
                if (mVunetid == null) {
                    actionBar.setTitle(getString(R.string.toolbar_title_books));
                    nv.setCheckedItem(R.id.nav_bookList);
                } else {
                    actionBar.setTitle(getString(R.string.toolbar_title_borrowed_books) +
                            " " + mVunetid);
                    nv.setCheckedItem(R.id.nav_borrowedList);
                }
            }

        if (null == savedInstanceState || savedInstanceState.isEmpty()) {
            mFragment = BooksFragment.newInstance(mVunetid, null);
            initFragment((BooksFragment)mFragment, BooksFragment.TAG);
        }
    }

    @Override
    protected void onSearch(String query) {
        mFragment.onSearch(query);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return mVunetid == null && super.onCreateOptionsMenu(menu);
    }
}
