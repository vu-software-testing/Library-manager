package com.example.librarymanager.login;

import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.example.librarymanager.R;
import com.example.librarymanager.util.BaseActivity;

public class LoginActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar;
        if ((actionBar = getSupportActionBar()) != null) {
            actionBar.setTitle(R.string.toolbar_title_login);
        }

        if (null == savedInstanceState || savedInstanceState.isEmpty()) {
            initFragment(LoginFragment.newInstance(), LoginFragment.TAG);
        }
    }
}
