package com.example.librarymanager.util;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.librarymanager.R;
import com.example.librarymanager.books.BooksActivity;
import com.example.librarymanager.login.LoginActivity;
import com.example.librarymanager.setpassword.SetPasswordActivity;
import com.example.librarymanager.setpassword.SetPasswordFragment;
import com.example.librarymanager.users.UsersActivity;

public abstract class BaseDrawerActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener{

//    public static String SAVED_VUNETID = "SAVED_VUNETID";

    protected ActionBarDrawerToggle toggle;

    private boolean mSearched;
    private String mVunetid;
    private SharedPrefApi mSharedPref;

    abstract protected void onSearch(String query);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // savedInstanceState is used to pass the viewId to the superclass.
        if (savedInstanceState == null) {
            savedInstanceState = new Bundle();
            savedInstanceState.putBoolean(BaseActivity.SAVED_INSTANCE_STATE_WAS_NULL, true);
        }
        savedInstanceState.putInt(BaseActivity.CONTENT_VIEW_ID, R.layout.activity_base_drawer);
        super.onCreate(savedInstanceState);

        mSharedPref = Injection.provideSharedPref(getApplication());

        // Initialize the drawer with toggle on the toolbar
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (drawer != null) {
            drawer.addDrawerListener(toggle);
        }
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(true);

        NavigationView nv = (NavigationView) findViewById(R.id.nv_main);
        if (nv != null) {
            nv.setNavigationItemSelectedListener(this);
        }

        mVunetid = mSharedPref.getString(PreferenceUtil.APP_STATES_CURRENT_USER, "");
        boolean isAdmin;
        isAdmin = mSharedPref.getBoolean(PreferenceUtil.APP_STATES_CURRENT_USER_IS_ADMIN, false);

        if (nv != null) {
            nv.getMenu().clear();
            if (isAdmin) {
                nv.inflateMenu(R.menu.main_drawer_admin);
            } else {
                nv.inflateMenu(R.menu.main_drawer_student);
            }
            nv.removeHeaderView(nv.getHeaderView(0));

            View headerView = LayoutInflater.from(getApplication()).inflate(
                    R.layout.nav_header_main,
                    (DrawerLayout) findViewById(R.id.drawer_layout), false);
            nv.addHeaderView(headerView);

            ((TextView) headerView.findViewById(R.id.tv_headerName)).setText(mVunetid);

            Drawable drawable = ContextCompat.getDrawable(this,
                    R.drawable.ic_account_circle_black_36dp);
            ((ImageView) headerView.findViewById(R.id.iv_headerImage)).setImageDrawable(drawable);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }

        Intent intent;
        boolean popCurrentActivity = true;
        switch (item.getItemId()) {
            case R.id.nav_borrowedList:
                intent = new Intent(this, BooksActivity.class);
                intent.putExtra(BooksActivity.EXTRA_BORROWER_VUNETID, mVunetid);
                break;

            case R.id.nav_change_password:
                intent = new Intent(this, SetPasswordActivity.class);
                intent.putExtra(SetPasswordActivity.EXTRA_VUNETID, mVunetid);
                intent.putExtra(SetPasswordActivity.EXTRA_ACTION_TYPE,
                        SetPasswordFragment.ACTION_CHANGE_PASSWORD);
                popCurrentActivity = false;
                break;

            case R.id.nav_userList:
                intent = new Intent(this, UsersActivity.class);
                break;

            case R.id.nav_log_out:
                mSharedPref.removeKey(PreferenceUtil.APP_STATES_CURRENT_USER)
                        .removeKey(PreferenceUtil.APP_STATES_CURRENT_USER_IS_ADMIN);
                intent = new Intent(this, LoginActivity.class);
                break;

            default:
                intent = new Intent(this, BooksActivity.class);
                break;
        }
        startActivity(intent);
        if (popCurrentActivity) finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        //searchItem.setVisible(menuState);

        final SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearched = true;
                searchView.clearFocus();
                onSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        MenuItemCompat.OnActionExpandListener expandListener =
                new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                searchView.clearFocus();
                if (mSearched) {
                    mSearched = false;
                    onSearch(null);
                }
                return true;
            }
        };
//
        MenuItemCompat.setOnActionExpandListener(searchItem, expandListener);
        searchView.setIconifiedByDefault(true);
        searchView.setSubmitButtonEnabled(true);

        return true;
    }
}
