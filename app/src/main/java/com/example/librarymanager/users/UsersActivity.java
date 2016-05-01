package com.example.librarymanager.users;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;

import com.example.librarymanager.R;
import com.example.librarymanager.util.BaseDrawerActivity;

public class UsersActivity extends BaseDrawerActivity {

    UsersContract.View mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.toolbar_title_users);
        }

        NavigationView nv = (NavigationView) findViewById(R.id.nv_main);
        if (nv != null) {
            nv.setCheckedItem(R.id.nav_userList);
        }

        if (null == savedInstanceState || savedInstanceState.isEmpty()) {
            mFragment = UsersFragment.newInstance();
            initFragment((Fragment) mFragment, UsersFragment.TAG);
        }
    }

    @Override
    protected void onSearch(String query) {
        mFragment.onSearch(query);
    }
}
