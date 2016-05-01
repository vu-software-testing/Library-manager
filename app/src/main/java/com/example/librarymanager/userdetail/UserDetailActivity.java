package com.example.librarymanager.userdetail;

import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.example.librarymanager.R;
import com.example.librarymanager.util.BaseActivity;

public class UserDetailActivity extends BaseActivity {

    public static final String EXTRA_VUNETID = "VUNETID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle(R.string.toolbar_title_userdetail);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        //Get the requested book id
        String vunetid = getIntent().getStringExtra(EXTRA_VUNETID);

        if (null == savedInstanceState || savedInstanceState.isEmpty()) {
            initFragment(UserDetailFragment.newInstance(vunetid), UserDetailFragment.TAG);
        }
    }
}
