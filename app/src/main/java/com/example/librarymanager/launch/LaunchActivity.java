package com.example.librarymanager.launch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.librarymanager.R;
import com.example.librarymanager.books.BooksActivity;
import com.example.librarymanager.login.LoginActivity;
import com.example.librarymanager.setpassword.SetPasswordActivity;
import com.example.librarymanager.setpassword.SetPasswordFragment;
import com.example.librarymanager.util.Injection;
import com.example.librarymanager.util.PreferenceUtil;
import com.example.librarymanager.util.SharedPrefApi;

public class LaunchActivity extends Activity implements OnClickListener {

    private SharedPrefApi sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ImageView vu_logo = (ImageView) findViewById(R.id.vu_logo);
        RelativeLayout rel_layout = (RelativeLayout) findViewById(R.id.relative_layout);
        rel_layout.setOnClickListener(this);
        vu_logo.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        sharedPref = Injection.provideSharedPref(getApplication());

        Intent intent;

        if (!adminPasswdIsSet()) {
            intent = new Intent(this, SetPasswordActivity.class);
            intent.putExtra(SetPasswordFragment.ARG_ACTION_TYPE,
                    SetPasswordFragment.ACTION_INITIALIZATION);
        } else if (loggedIn()) {
            intent = new Intent(this, BooksActivity.class);
        } else {
            intent = new Intent(this, LoginActivity.class);
        }
        startActivity(intent);
        finish();
    }

    private boolean adminPasswdIsSet() {
        return sharedPref.hasKey(PreferenceUtil.APP_STATES_SUPER_ADMIN_USERNAME);
    }

    private boolean loggedIn() {
        return sharedPref.hasKey(PreferenceUtil.APP_STATES_CURRENT_USER);
    }
}
