package com.example.librarymanager.adduser;

import com.example.librarymanager.R;
import com.example.librarymanager.util.Injection;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


public class AddUserFragment extends Fragment implements AddUserContract.View {

    public static final String TAG = "addUserFragment";

    private AddUserContract.UserActionListener userActionListener;

    public static AddUserFragment newInstance() {
        return new AddUserFragment();
    }

    public AddUserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_user, container, false);

        userActionListener = new AddUserPresenter(this, Injection.provideRepository(getContext()));

        final EditText etFullname = (EditText) root.findViewById(R.id.et_fullname);
        final EditText etPassword = (EditText) root.findViewById(R.id.et_password);
        final EditText etVunetid = (EditText) root.findViewById(R.id.et_vunetid);
        final EditText etPasswordConfirm = (EditText) root.findViewById(R.id.et_passwordConfirm);
        final EditText etFaculty = (EditText) root.findViewById(R.id.et_faculty);
        final CheckBox cb_addAsAdmin = (CheckBox) root.findViewById(R.id.cb_add_as_admin);
        Button btn_confirm = (Button) root.findViewById(R.id.btn_submit);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullname = etFullname.getText().toString();
                String password = etPassword.getText().toString();
                String passwordConfirm = etPasswordConfirm.getText().toString();
                String vunetid = etVunetid.getText().toString();
                String faculty = etFaculty.getText().toString();
                boolean addAsAdmin = cb_addAsAdmin.isChecked();

                userActionListener.addUser(vunetid, password, passwordConfirm, fullname, faculty,
                        addAsAdmin);
            }
        });
        return root;
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
    public void showPasswordError(int messageId) {
        EditText etPassword = (EditText) getActivity().findViewById(R.id.et_password);
        etPassword.setError(getString(messageId));
    }

    @Override
    public void showPasswordConfirmError(int messageId) {
        EditText etPasswordConfirm = (EditText) getActivity().findViewById(R.id.et_passwordConfirm);
        etPasswordConfirm.setError(getString(messageId));
    }

    @Override
    public void showVunetidError(int messageId) {
        EditText etVunetid = (EditText) getActivity().findViewById(R.id.et_vunetid);
        etVunetid.setError(getString(messageId));
    }

    @Override
    public void showFacultyError(int messageId) {
        EditText etFaculty = (EditText) getActivity().findViewById(R.id.et_faculty);
        etFaculty.setError(getString(messageId));
    }

    @Override
    public void showFullnameError(int messageId) {
        EditText etFullname = (EditText) getActivity().findViewById(R.id.et_fullname);
        etFullname.setError(getString(messageId));
    }
}
