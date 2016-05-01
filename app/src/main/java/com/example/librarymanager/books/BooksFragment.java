package com.example.librarymanager.books;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.librarymanager.addbook.AddBookActivity;
import com.example.librarymanager.bookdetail.BookDetailActivity;
import com.example.librarymanager.data.Book;
import com.example.librarymanager.util.Injection;
import com.example.librarymanager.R;
import com.example.librarymanager.util.PreferenceUtil;
import com.example.librarymanager.util.SharedPrefApi;
import com.github.buchandersenn.android_permission_manager.PermissionManager;
import com.github.buchandersenn.android_permission_manager.callbacks.OnPermissionDeniedCallback;
import com.github.buchandersenn.android_permission_manager.callbacks.OnPermissionGrantedCallback;

import static com.github.buchandersenn.android_permission_manager.callbacks.PermissionCallbacks.showPermissionShowRationaleSnackbar;

public class BooksFragment extends Fragment implements BooksContract.View {

    public static final String TAG = "booksFragment";
    public static final String ARG_BORROWER_VUNETID = "BORROWER_VUNETID";
    public static final String ARG_SEARCH_QUERY = "SEARCH_QUERY";

    private String borrower;
    private String query;
    private Cursor mBookCursor;
    private BookCursorAdapter mCursorAdapter;
    private BooksContract.UserActionListener mUserActionListener;

    private PermissionManager mPermissionManager;

    public static BooksFragment newInstance(String borrower, String query) {
        BooksFragment fragment = new BooksFragment();
        if (borrower != null || query != null) {
            Bundle args = new Bundle();
            args.putString(ARG_BORROWER_VUNETID, borrower);
            args.putString(ARG_SEARCH_QUERY, query);
            fragment.setArguments(args);
        }
        return fragment;
    }

    public BooksFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPermissionManager = PermissionManager.create(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_grid_with_fab, container, false);

        if (getArguments() != null) {
            borrower = getArguments().getString(ARG_BORROWER_VUNETID, null);
            query = getArguments().getString(ARG_SEARCH_QUERY, null);
        }

        mUserActionListener = new BooksPresenter(this, Injection.provideRepository(getContext()),
                Injection.provideImageFile());

        GridView gridView = (GridView) root.findViewById(R.id.gv_grid);
        mCursorAdapter = new BookCursorAdapter(getContext(), null);
        gridView.setAdapter(mCursorAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int columnIndex = ((Cursor) parent.getItemAtPosition(position)).getColumnIndexOrThrow(
                        Book._ID);
                String bookId = ((Cursor) parent.getItemAtPosition(position)).getString(columnIndex);
                mUserActionListener.openBookDetail(bookId);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) root.findViewById(R.id.fab);

        SharedPrefApi sharedPref = Injection.provideSharedPref(getActivity().getApplication());
        boolean isAdmin = sharedPref.getBoolean(
                PreferenceUtil.APP_STATES_CURRENT_USER_IS_ADMIN, false);

        if (!isAdmin || borrower != null) {
            fab.setVisibility(View.GONE);
        } else {
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mUserActionListener.addNewBook();
                }
            });
        }
        return root;
    }

    /**
     * Invoked again when permission window is popped
     */
    @Override
    public void onResume() {
        super.onResume();
        mUserActionListener.loadBooksAndRequestPermissionIfNeeded(borrower, query);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mBookCursor != null) {
            mBookCursor.close();
        }
    }

    @Override
    public void showBookDetailUi(String bookId) {
        Intent intent = new Intent(getContext(), BookDetailActivity.class);
        intent.putExtra(BookDetailActivity.EXTRA_BOOK_ID, bookId);
        startActivity(intent);
    }

    @Override
    public void showAddBookUi() {
        Intent intent = new Intent(getContext(), AddBookActivity.class);
        startActivity(intent);
    }

    @Override
    public void showBooks(Cursor booksCursor) {
        this.mBookCursor = booksCursor;
        mCursorAdapter.changeCursor(booksCursor);
    }

    @Override
    public void onSearch(String query) {
        mUserActionListener.loadBooksAndRequestPermissionIfNeeded(borrower, query);
    }

    @Override
    public void requestPermissionToReadImage() {
        mPermissionManager.with(Manifest.permission.READ_EXTERNAL_STORAGE)
                .onPermissionGranted(new OnPermissionGrantedCallback() {
                    @Override
                    public void onPermissionGranted() {
                        mUserActionListener.onPermissionGranted();
                    }
                })
                .onPermissionDenied(new OnPermissionDeniedCallback() {
                    @Override
                    public void onPermissionDenied() {
                        mUserActionListener.onPermissionDenied();
                    }
                })
                .onPermissionShowRationale(showPermissionShowRationaleSnackbar(
                        getActivity().findViewById(R.id.cl_coordinatorLayout),
                        "File access is required to load the books.", "OK"))
                .request();
    }

    @Override
    public void showPermissionDeniedWarning() {
        Snackbar.make(getActivity().findViewById(R.id.cl_coordinatorLayout),
                "Read file permission request was denied.",
                Snackbar.LENGTH_LONG)
                .setAction("SETTINGS", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context context = v.getContext();

                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                        intent.setData(uri);
                        context.startActivity(intent);
                    }
                })
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mPermissionManager.handlePermissionResult(requestCode, grantResults);
    }

    class BookCursorAdapter extends CursorAdapter {

        public BookCursorAdapter(Context context, Cursor cursor) {
            super(context, cursor, 0);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView tvBookName = (TextView) view.findViewById(R.id.item_book_name);
            //TextView tvAuthor = (TextView) view.findViewById(R.id.item_book_author);
            ImageView ivImage = (ImageView) view.findViewById(R.id.iv_image);

            String bookName = cursor.getString(cursor.getColumnIndexOrThrow(Book.TITLE));

            //String author = cursor.getString(cursor.getColumnIndexOrThrow(Book.AUTHOR));
            int uriColumn = cursor.getColumnIndexOrThrow(Book.IMAGE_PATH);
            Bitmap imageData = null;
            if (!cursor.isNull(uriColumn)) {
                String imagePath = cursor.getString(uriColumn);
                imageData = mUserActionListener.getThumbnail(imagePath);
                ivImage.setImageBitmap(imageData);
                ivImage.setBackground(null);
            }

            tvBookName.setText(bookName);
        }
    }
}
