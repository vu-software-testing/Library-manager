package com.example.librarymanager.bookdetail;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.example.librarymanager.util.Injection;
import com.example.librarymanager.R;
import com.github.buchandersenn.android_permission_manager.PermissionManager;
import com.github.buchandersenn.android_permission_manager.callbacks.OnPermissionGrantedCallback;

import static com.github.buchandersenn.android_permission_manager.callbacks.PermissionCallbacks.showPermissionDeniedSnackbar;
import static com.github.buchandersenn.android_permission_manager.callbacks.PermissionCallbacks.showPermissionShowRationaleSnackbar;

public class BookDetailFragment extends Fragment implements BookDetailContract.View {

    public static final String TAG = "bookDetailFragment";
    public static final String ARG_BOOK_ID = "BOOK_ID";

    private BookDetailContract.UserActionListener mUserActionListener;

    private String mBookId;
    private String mImagePath;

    private TextView tvTitle;
    private TextView tvAuthor;
    private TextView tvIsbn;
    private TextView tvStatus;
    private TextView tvBorrower;
    private TextView tvBorrowerLabel;
    private Button btnReturn;
    private Button btnBorrow;
    private Button btnEdit;
    private Button btnDelete;
    private ImageView ivImage;
    private PermissionManager mPermissionManager;


    public static BookDetailFragment newInstance(String bookId) {
        Bundle args = new Bundle();
        args.putString(ARG_BOOK_ID, bookId);
        BookDetailFragment fragment = new BookDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public BookDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_book_detail, container, false);
        mUserActionListener = new BookDetailPresenter(this,
                Injection.provideRepository(getContext()), Injection.provideImageFile(),
                Injection.provideSharedPref(getActivity().getApplication()));

        tvTitle = (TextView) root.findViewById(R.id.tv_bookDetail_title);
        tvAuthor = (TextView) root.findViewById(R.id.tv_bookDetail_author);
        tvIsbn = (TextView) root.findViewById(R.id.tv_bookDetail_isbn);
        tvStatus = (TextView) root.findViewById(R.id.tv_bookDetail_status);
        tvBorrower = (TextView) root.findViewById(R.id.tv_bookDetail_borrower);
        tvBorrowerLabel = (TextView) root.findViewById(R.id.tv_bookDetail_borrowerLabel);

        btnReturn = (Button) root.findViewById(R.id.btn_return);
        btnBorrow = (Button) root.findViewById(R.id.btn_borrow);
        btnEdit = (Button) root.findViewById(R.id.btn_edit);
        btnDelete = (Button) root.findViewById(R.id.btn_delete);

        ivImage = (ImageView) root.findViewById(R.id.iv_thumbnail);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserActionListener.editBook(mBookId);
            }
        });
        btnBorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserActionListener.borrowBook(mBookId);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserActionListener.deleteBook(mBookId, mImagePath);
            }
        });
        btnReturn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mUserActionListener.returnBook(mBookId);
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPermissionManager = PermissionManager.create(getActivity());
        mBookId = getArguments().getString(ARG_BOOK_ID);
        mUserActionListener.openBookDetail(mBookId);
    }



    @Override
    public void showBookBasicInfo(String title, String author, String isbn) {
        tvTitle.setText(title);
        tvAuthor.setText(author);
        tvIsbn.setText(isbn);
    }

    @Override
    public void showBookStatus(int stringId) {
        tvStatus.setText(getString(stringId));
    }

    @Override
    public void setBorrowerTextView(boolean visible, String borrower) {
        int visibility = visible ? View.VISIBLE : View.GONE;
        tvBorrower.setVisibility(visibility);
        tvBorrowerLabel.setVisibility(visibility);
        tvBorrower.setText(borrower);
    }

    @Override
    public void setReturnButton(boolean visible) {
        btnReturn.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setEditButton(boolean visible) {
        btnEdit.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setDeleteButton(boolean visible) {
        btnDelete.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setBorrowButton(boolean visible) {
        btnBorrow.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void storeImagePath(String imagePath) {
        mImagePath = imagePath;
    }

    @Override
    public void setThumbnail(Bitmap imageData) {
        ivImage.setImageBitmap(imageData);
        ivImage.setBackground(null);
    }

    @Override
    public void requestPermissionToDeleteImage(final String bookId, final String imagePath) {
        mPermissionManager.with(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .onPermissionGranted(new OnPermissionGrantedCallback() {
                    @Override
                    public void onPermissionGranted() {
                        mUserActionListener.onDeleteImagePermissionGranted(bookId, imagePath);
                    }
                })
                .onPermissionDenied(showPermissionDeniedSnackbar(
                        getActivity().findViewById(R.id.cl_coordinatorLayout),
                        "Delete file permission request was denied.", "SETTINGS"))
                .onPermissionShowRationale(showPermissionShowRationaleSnackbar(
                        getActivity().findViewById(R.id.cl_coordinatorLayout),
                        "File access is required to delete the book.", "OK"))
                .request();
    }

    @Override
    public void showMessage(int messageId) {
        Toast.makeText(getContext(), getString(messageId), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showStaticView() {
        ViewAnimator viewAnimator = (ViewAnimator) getActivity().findViewById(R.id.va_animator);
        viewAnimator.setDisplayedChild(viewAnimator.indexOfChild(getActivity().findViewById(
                R.id.ll_staticView)));
    }

    @Override
    public void showEditView(final String bookId) {

        final TextView tvTitle = (TextView) getActivity().findViewById(R.id.tv_bookDetail_title);
        final TextView tvAuthor = (TextView) getActivity().findViewById(R.id.tv_bookDetail_author);
        final TextView tvIsbn = (TextView) getActivity().findViewById(R.id.tv_bookDetail_isbn);
        final EditText etTitle = (EditText) getActivity().findViewById(R.id.et_bookDetail_title);
        final EditText etAuthor = (EditText) getActivity().findViewById(R.id.et_bookDetail_author);
        final EditText etIsbn = (EditText) getActivity().findViewById(R.id.et_bookDetail_isbn);

        etTitle.setText(tvTitle.getText().toString());
        etAuthor.setText(tvAuthor.getText().toString());
        etIsbn.setText(tvIsbn.getText().toString());

        Button btnSave = (Button) getActivity().findViewById(R.id.btn_saveEdit);
        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String title = etTitle.getText().toString();
                String author = etAuthor.getText().toString();
                String isbn = etIsbn.getText().toString();

                String origTitle = tvTitle.getText().toString();
                String origAuthor = tvAuthor.getText().toString();
                String origIsbn = tvIsbn.getText().toString();

                mUserActionListener.saveBook(bookId, title, author, isbn,
                        origTitle, origAuthor, origIsbn);
            }
        });

        Button btnCancel = (Button) getActivity().findViewById(R.id.btn_cancelEdit);
        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mUserActionListener.cancelEditBook();
            }
        });

        ViewAnimator viewAnimator = (ViewAnimator) getActivity().findViewById(R.id.va_animator);
        viewAnimator.setDisplayedChild(viewAnimator.indexOfChild(getActivity().findViewById(
                R.id.ll_editView)));
    }

    @Override
    public void showTitleError(int messageId) {
        EditText etTitle = (EditText) getActivity().findViewById(R.id.et_bookDetail_title);
        etTitle.setError(getString(messageId));
    }

    @Override
    public void showAuthorError(int messageId) {
        EditText etAuthor = (EditText) getActivity().findViewById(R.id.et_bookDetail_author);
        etAuthor.setError(getString(messageId));
    }

    @Override
    public void showIsbnError(int messageId) {
        EditText etIsbn = (EditText) getActivity().findViewById(R.id.et_bookDetail_isbn);
        etIsbn.setError(getString(messageId));
    }

    @Override
    public void finish() {
        getActivity().finish();
    }

    @Override
    public void showDeleteBookConfirmationDialog(int titleId, int messageId, int positiveId,
                                                 int negativeId, final String bookId,
                                                 final String imagePath) {
        new AlertDialog.Builder(getContext())
                .setIcon(R.drawable.ic_launcher)
                .setTitle(getString(titleId))
                .setMessage(getString(messageId))
                .setPositiveButton(getString(positiveId), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mUserActionListener.deleteBookConfirmed(bookId, imagePath);
                    }
                })
                .setNegativeButton(getString(negativeId), null)
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mPermissionManager.handlePermissionResult(requestCode, grantResults);
    }
}
