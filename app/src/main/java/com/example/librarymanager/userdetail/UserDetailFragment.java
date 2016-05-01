package com.example.librarymanager.userdetail;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.example.librarymanager.BuildConfig;
import com.example.librarymanager.R;
import com.example.librarymanager.books.BooksNoDrawerActivity;
import com.example.librarymanager.data.User;
import com.example.librarymanager.util.Injection;

public class UserDetailFragment extends Fragment implements UserDetailContract.View {

    public static final String TAG = "userDetailFragment";
    public static final String ARG_REQUESTED_VUNETID = "REQUESTED_VUNETID";

    private String mRequestedVunetid;

    private UserDetailContract.UserActionListener mUserActionListener;

    public UserDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param vunetid vunetid.
     * @return A new instance of fragment UserDetailFragment.
     */
    public static UserDetailFragment newInstance(String vunetid) {
        UserDetailFragment fragment = new UserDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_REQUESTED_VUNETID, vunetid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user_detail, container, false);

        mUserActionListener = new UserDetailPresenter(this,
                Injection.provideRepository(getContext()),
                Injection.provideSharedPref(getActivity().getApplication()));

        if (getArguments() != null) {
            mRequestedVunetid = getArguments().getString(ARG_REQUESTED_VUNETID);
        }
        if (BuildConfig.DEBUG && mRequestedVunetid == null) {
            throw new AssertionError();
        }

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mUserActionListener.initUi(mRequestedVunetid);
    }

    @Override
    public void showUser(User user) {
        TextView tvFullname = (TextView) getActivity().findViewById(R.id.tv_fullname);
        TextView tvVunetid = (TextView) getActivity().findViewById(R.id.tv_vunetid);
        TextView tvFaculty = (TextView) getActivity().findViewById(R.id.tv_faculty);
        CheckBox cbAdmin = (CheckBox) getActivity().findViewById(R.id.cb_admin);

        tvFullname.setText(user.getFullname());
        tvVunetid.setText(user.getVunetId());
        tvFaculty.setText(user.getFaculty());
        if (user.isAdmin()) {
            cbAdmin.setChecked(true);
        } else {
            cbAdmin.setVisibility(View.GONE);
        }
    }

    @Override
    public void setBorrowedButtonVisibility(boolean visible) {
        Button btnBorrowed = (Button) getActivity().findViewById(R.id.btn_borrowed);
        if (visible) {
            btnBorrowed.setVisibility(View.VISIBLE);
            btnBorrowed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mUserActionListener.getBorrowedBooks();
                }
            });
        } else {
            btnBorrowed.setVisibility(View.GONE);
        }
    }

    @Override
    public void setEditAndDeleteButtonVisibility(boolean visible) {
        Button btnEdit = (Button) getActivity().findViewById(R.id.btn_edit);
        Button btnDelete = (Button) getActivity().findViewById(R.id.btn_delete);

        if (visible) {
            btnEdit.setVisibility(View.VISIBLE);
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mUserActionListener.editUser();
                }
            });

            btnDelete.setVisibility(View.VISIBLE);
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mUserActionListener.deleteUser();
                }
            });
        } else {
            btnEdit.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
        }
    }

    @Override
    public void showEditView() {
        final TextView tvFullname = (TextView) getActivity().findViewById(R.id.tv_fullname);
        final TextView tvVunetid = (TextView) getActivity().findViewById(R.id.tv_vunetid);
        final TextView tvFaculty = (TextView) getActivity().findViewById(R.id.tv_faculty);

        final EditText etFullname = (EditText) getActivity().findViewById(R.id.et_fullname);
        final TextView etVunetid = (TextView) getActivity().findViewById(R.id.et_vunetid);
        final EditText etFaculty = (EditText) getActivity().findViewById(R.id.et_faculty);
        final EditText etPassword = (EditText) getActivity().findViewById(R.id.et_password);
        final EditText etConfirmPassword = (EditText) getActivity().
                findViewById(R.id.et_confirmPassword);

        etFullname.setText(tvFullname.getText().toString());
        etVunetid.setText(tvVunetid.getText().toString());
        etFaculty.setText(tvFaculty.getText().toString());

        Button btnSave = (Button) getActivity().findViewById(R.id.btn_saveEdit);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullname = etFullname.getText().toString();
                String vunetid = etVunetid.getText().toString();
                String faculty = etFaculty.getText().toString();
                String password = etPassword.getText().toString();
                String passwordConfirm = etConfirmPassword.getText().toString();

                ((ViewAnimator) getActivity().findViewById(R.id.va_animator)).showNext();
                String origVunetid = tvVunetid.getText().toString();
                String origFullname = tvFullname.getText().toString();
                String origFaculty = tvFaculty.getText().toString();
                mUserActionListener.updateUser(vunetid, password, passwordConfirm, fullname, faculty,
                        origVunetid, origFullname, origFaculty);
            }
        });

        Button btnCancel = (Button) getActivity().findViewById(R.id.btn_cancelEdit);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserActionListener.cancelEditUser();
            }
        });

        ViewAnimator viewAnimator = (ViewAnimator) getActivity().findViewById(R.id.va_animator);
        viewAnimator.setDisplayedChild(viewAnimator.indexOfChild(getActivity().findViewById(
                R.id.ll_editView)));
    }

    @Override
    public void showBorrowedBooks() {
        Intent intent = new Intent(getContext(), BooksNoDrawerActivity.class);
        intent.putExtra(BooksNoDrawerActivity.EXTRA_BORROWER_VUNETID, mRequestedVunetid);
        startActivity(intent);
    }

    @Override
    public void showDeleteUserDialog(int titleId, int messageId, int positiveId, int negativeId) {
        new AlertDialog.Builder(getContext())
                .setIcon(R.drawable.ic_launcher)
                .setTitle(getString(titleId))
                .setMessage(getString(messageId))
                .setPositiveButton(getString(positiveId), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mUserActionListener.deleteUserConfirmed(mRequestedVunetid);
                    }
                })
                .setNegativeButton(getString(negativeId), null)
                .show();
    }

    @Override
    public void showStaticView() {
        ViewAnimator viewAnimator = (ViewAnimator) getActivity().findViewById(R.id.va_animator);
        viewAnimator.setDisplayedChild(viewAnimator.indexOfChild(getActivity().findViewById(
                R.id.ll_staticView)));
    }

    @Override
    public void showMessage(int messageId) {
        Toast.makeText(getContext(), getString(messageId), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finish() {
        //mFragmentActionListener.finish(null, null);
        getActivity().finish();
    }

    @Override
    public void setUserDetail(String vunetid, String fullname, String faculty) {
        TextView tvFullname = (TextView) getActivity().findViewById(R.id.tv_fullname);
        TextView tvVunetid = (TextView) getActivity().findViewById(R.id.tv_vunetid);
        TextView tvFaculty = (TextView) getActivity().findViewById(R.id.tv_faculty);
        tvFullname.setText(fullname);
        tvVunetid.setText(vunetid);
        tvFaculty.setText(faculty);
    }

    @Override
    public void showVunetidError(int messageId) {
        EditText etVunetid = (EditText) getActivity().findViewById(R.id.et_vunetid);
        etVunetid.setError(getString(messageId));
    }

    @Override
    public void showFullnameError(int messageId) {
        EditText etFullname = (EditText) getActivity().findViewById(R.id.et_fullname);
        etFullname.setError(getString(messageId));
    }

    @Override
    public void showFacultyError(int messageId) {
        EditText etFaculty = (EditText) getActivity().findViewById(R.id.et_faculty);
        etFaculty.setError(getString(messageId));
    }

    @Override
    public void showPasswdConfirmError(int messageId) {
        EditText etConfirmPassword = (EditText) getActivity().findViewById(R.id.et_confirmPassword);
        etConfirmPassword.setError(getString(messageId));
    }
}
