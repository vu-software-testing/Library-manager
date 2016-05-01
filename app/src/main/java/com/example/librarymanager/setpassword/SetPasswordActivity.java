package com.example.librarymanager.setpassword;

import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.example.librarymanager.util.BaseActivity;

public class SetPasswordActivity extends BaseActivity {

    public static final String EXTRA_VUNETID = "VUNETID";
    public static final String EXTRA_ACTION_TYPE = "ACTION_TYPE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String vunetid = getIntent().getStringExtra(EXTRA_VUNETID);
        int actionType = getIntent().getIntExtra(EXTRA_ACTION_TYPE, -1);

        ActionBar actionBar;
        if ((actionBar = getSupportActionBar()) != null) {
            switch (actionType) {
                case SetPasswordFragment.ACTION_CHANGE_PASSWORD:
                    actionBar.setTitle("Change your password");
                    actionBar.setDisplayHomeAsUpEnabled(true);
                    actionBar.setDisplayShowHomeEnabled(true);
                    break;
                case SetPasswordFragment.ACTION_INITIALIZATION:
                    if (vunetid != null) {
                        actionBar.setTitle("Set password of " + vunetid);
                    } else {
                        actionBar.setTitle("Set initial administrator account");
                    }
                    break;
            }
        }

        if (null == savedInstanceState || savedInstanceState.isEmpty()) {
            initFragment(SetPasswordFragment.newInstance(vunetid, actionType),
                    SetPasswordFragment.TAG);
        }
    }
}
