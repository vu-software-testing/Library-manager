package com.example.librarymanager.util;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.librarymanager.R;

public abstract class BaseActivity extends AppCompatActivity {

    public static final String CONTENT_VIEW_ID = "CONTENT_VIEW_ID";
    public static final String SAVED_INSTANCE_STATE_WAS_NULL = "SAVED_INSTANCE_STATE_WAS_NULL";

    protected Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int viewId;

        //Set viewId if it isn't provided by the derived class
        if (savedInstanceState == null ||
                (viewId = savedInstanceState.getInt(CONTENT_VIEW_ID, -1)) == -1) {
            viewId = R.layout.activity_base_toolbar;
        } else if (savedInstanceState.getBoolean(SAVED_INSTANCE_STATE_WAS_NULL, false)) {
            savedInstanceState.clear();
        } else {
            savedInstanceState.remove(CONTENT_VIEW_ID);
        }

        super.onCreate(savedInstanceState);

        setContentView(viewId);

        // Set up the toolbar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    protected void initFragment(Fragment fragment, String tag) {
        if (findViewById(R.id.fl_fragmentContainer) != null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.fl_fragmentContainer, fragment, tag);
            ft.commit();
        }
    }
}
