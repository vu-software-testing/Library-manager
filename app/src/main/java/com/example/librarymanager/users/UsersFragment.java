package com.example.librarymanager.users;

import com.example.librarymanager.R;
import com.example.librarymanager.adduser.AddUserActivity;
import com.example.librarymanager.data.User;
import com.example.librarymanager.userdetail.UserDetailActivity;
import com.example.librarymanager.util.Injection;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class UsersFragment extends Fragment implements UsersContract.View {

    public static final String TAG = "usersFragment";

    private UserCursorAdapter mCursorAdapter;
    private Cursor mUserCursor;
    private UsersContract.UserActionListener userActionListener;

    public static UsersFragment newInstance() {
        return new UsersFragment();
    }

    public UsersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list_with_fab, container, false);

        userActionListener = new UsersPresenter(this, Injection.provideRepository(getContext()));

        ListView listView = (ListView) root.findViewById(R.id.lv_list);
        mCursorAdapter = new UserCursorAdapter(getContext(), null);
        listView.setAdapter(mCursorAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int columnIndex = ((Cursor) parent.getItemAtPosition(position)).getColumnIndexOrThrow(
                        User.VUNETID);
                String vunetid = ((Cursor) parent.getItemAtPosition(position)).getString(columnIndex);
                userActionListener.openUserDetail(vunetid);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userActionListener.addNewUser();
            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        userActionListener.loadUsers(null);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mUserCursor != null) {
            mUserCursor.close();
        }
    }

    @Override
    public void showUsers(Cursor userCursor) {
        this.mUserCursor = userCursor;
        mCursorAdapter.changeCursor(userCursor);
    }

    @Override
    public void onSearch(String query) {
        userActionListener.loadUsers(query);
    }

    @Override
    public void showAddUserUi() {
        Intent intent = new Intent(getContext(), AddUserActivity.class);
        startActivity(intent);
    }

    @Override
    public void showUserDetailUi(String vunetid) {
        Intent intent = new Intent(getContext(), UserDetailActivity.class);
        intent.putExtra(UserDetailActivity.EXTRA_VUNETID, vunetid);
        startActivity(intent);
    }

    class UserCursorAdapter extends CursorAdapter {

        public UserCursorAdapter(Context context, Cursor cursor) {
            super(context, cursor, 0);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView tvUsername = (TextView) view.findViewById(R.id.item_username);
            TextView tvStudentNumber = (TextView) view.findViewById(R.id.item_student_number);

            String username = cursor.getString(cursor.getColumnIndexOrThrow(User.FULL_NAME));
            String studentNumber = cursor.getString(cursor.getColumnIndexOrThrow(
                    User.VUNETID));

            tvUsername.setText(username);
            tvStudentNumber.setText(studentNumber);
        }

    }

}
