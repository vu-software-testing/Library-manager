package com.example.librarymanager.addbook;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;

import com.example.librarymanager.R;
import com.example.librarymanager.util.BaseActivity;

public class AddBookActivity extends BaseActivity {

    private Fragment mAddBookFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle(R.string.toolbar_title_addbook);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        if (null == savedInstanceState || savedInstanceState.isEmpty()) {
            mAddBookFragment = AddBookFragment.newInstance();
            initFragment(mAddBookFragment, AddBookFragment.TAG);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mAddBookFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
