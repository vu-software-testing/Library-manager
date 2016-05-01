package com.example.librarymanager.login;

import com.example.librarymanager.R;
import com.example.librarymanager.books.BooksActivity;
import com.example.librarymanager.util.Injection;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginFragment extends Fragment implements LoginContract.View {

    public static final String TAG = "loginFrament";

    private ProgressDialog pd;
    private EditText et_vunetid;
    private EditText et_passwd;

    private LoginContract.UserActionListener actionsListener;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login, container, false);

        actionsListener = new LoginPresenter(this, Injection.provideRepository(getContext()),
                Injection.provideSharedPref(getActivity().getApplication()));

        et_vunetid = (EditText) root.findViewById(R.id.input_vunetid);
        et_passwd = (EditText) root.findViewById(R.id.input_passwd);
        Button btn_login = (Button) root.findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionsListener.login(et_vunetid.getText().toString(),
                        et_passwd.getText().toString());
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void showUsernameError(int resId) {
        et_vunetid.setError(getString(resId));
    }

    public void showPasswordError(int resId) {
        et_passwd.setError(getString(resId));
    }

    public void startMainActivity() {
        pd = ProgressDialog.show(getActivity(), "Login in", "Please wait");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pd.dismiss();
                Intent intent = new Intent(getContext(), BooksActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        }, 2000);
    }

    public void showLoginError(int resId) {
        Toast.makeText(getActivity().getApplicationContext(), getString(R.string.login_failed_message),
                Toast.LENGTH_SHORT).show();
    }
}
