package com.example.librarymanager.setpassword;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.librarymanager.BuildConfig;
import com.example.librarymanager.R;
import com.example.librarymanager.login.LoginActivity;
import com.example.librarymanager.util.Injection;

public class SetPasswordFragment extends Fragment implements SetPasswordContract.View {

    public static final String TAG = "SetPasswordFragment";
    public static final String ARG_VUNETID = "VUNETID";
    public static final String ARG_ACTION_TYPE = "ACTION_TYPE";

    private EditText etPassword;
    private EditText etConfirm;
    private EditText etOldPassword;
    private EditText etAdminUsername;
    private Button btnSubmit;


    private int mActionType;
    private String mVunetId;

    private SetPasswordContract.UserActionListener mUserActionListener;

    public static SetPasswordFragment newInstance(String vunetid, int actionType) {
        SetPasswordFragment fragment = new SetPasswordFragment();
        Bundle args = new Bundle();
        if (vunetid != null) {
            args.putString(ARG_VUNETID, vunetid);
        }
        args.putInt(ARG_ACTION_TYPE, actionType);
        fragment.setArguments(args);
        return fragment;
    }

    public SetPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_set_password, container, false);

        if (getArguments() != null) {
            mActionType = getArguments().getInt(ARG_ACTION_TYPE);
            mVunetId = getArguments().getString(ARG_VUNETID);
        }
        if (BuildConfig.DEBUG &&
                !(mActionType == ACTION_INITIALIZATION || mActionType == ACTION_CHANGE_PASSWORD)) {
            throw new AssertionError();
        }

        mUserActionListener = new SetPasswordPresenter(this,
                Injection.provideRepository(getContext()),
                Injection.provideSharedPref(getActivity().getApplication()));

        etAdminUsername = (EditText) root.findViewById(R.id.et_admin_vunetid);
        etPassword = (EditText) root.findViewById(R.id.et_set_password);
        etConfirm = (EditText) root.findViewById(R.id.et_confirm_password);
        etOldPassword = (EditText) root.findViewById(R.id.et_old_password);

        btnSubmit = (Button) root.findViewById(R.id.btn_submit_password);

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUserActionListener.initUi(mActionType);
    }

    @Override
    public void showMessage(int messageId) {
        Toast.makeText(getContext(), getString(messageId), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finish() {
        getActivity().finish();
    }

    @Override
    public void startLoginActivity() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void setUiForInitialization() {
        etOldPassword.setVisibility(View.GONE);
        etAdminUsername.setVisibility(View.VISIBLE);

        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String password = etPassword.getText().toString();
                String passwordConfirm = etConfirm.getText().toString();
                String adminUsername = etAdminUsername.getText().toString();

                mUserActionListener.initializeSuperAdmin(adminUsername, password,
                        passwordConfirm);
            }
        });
    }

    @Override
    public void setUiForPasswordChange() {
        etOldPassword.setVisibility(View.VISIBLE);
        etAdminUsername.setVisibility(View.GONE);

        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String password = etPassword.getText().toString();
                String passwordConfirm = etConfirm.getText().toString();
                String oldPassword = etOldPassword.getText().toString();

                mUserActionListener.changePassword(mVunetId, oldPassword, password,
                        passwordConfirm);
            }
        });
    }

    @Override
    public void showAdminUsernameError(int messageId) {
        etAdminUsername.setError(getString(messageId));
    }

    @Override
    public void showOldPasswordError(int messageId) {
        etOldPassword.setError(getString(messageId));
    }

    @Override
    public void showPasswordConfirmError(int messageId) {
        etConfirm.setError(getString(messageId));
    }

    @Override
    public void showPasswordError(int messageId) {
        etPassword.setError(getString(messageId));
    }
}
