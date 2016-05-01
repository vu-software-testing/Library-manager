package com.example.librarymanager.addbook;

import com.example.librarymanager.R;
import com.example.librarymanager.util.Injection;
import com.github.buchandersenn.android_permission_manager.PermissionManager;
import com.github.buchandersenn.android_permission_manager.callbacks.OnPermissionGrantedCallback;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import static com.github.buchandersenn.android_permission_manager.callbacks.PermissionCallbacks.showPermissionDeniedSnackbar;
import static com.github.buchandersenn.android_permission_manager.callbacks.PermissionCallbacks.showPermissionShowRationaleSnackbar;

public class AddBookFragment extends Fragment implements AddBookContract.View {

    public static final String TAG = "addBookFragment";
    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_SELECT_FILE = 2;

    private PermissionManager mPermissionManager;

    private AddBookContract.UserActionListener mUserActionListener;

    public static AddBookFragment newInstance() {
        return new AddBookFragment();
    }

    public AddBookFragment() {
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
        View root = inflater.inflate(R.layout.fragment_add_book, container, false);

        mUserActionListener = new AddBookPresenter(this, Injection.provideRepository(getContext()),
                Injection.provideImageFile());

        final EditText etTitle = (EditText) root.findViewById(R.id.et_addBook_name);
        final EditText etAuthor = (EditText) root.findViewById(R.id.et_addBook_author);
        final EditText etIsbn = (EditText) root.findViewById(R.id.et_addBook_isbn);

        Button btn_confirm = (Button) root.findViewById(R.id.btn_submit);
        Button btn_addImage = (Button) root.findViewById(R.id.btn_addImage);

        btn_addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {"Take Photo", "Choose from Gallery", "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Add Photo");
                builder.setItems(items, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (items[which].equals("Take Photo")) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                                startActivityForResult(intent, REQUEST_CAMERA);
                            }
                        } else if (items[which].equals("Choose from Gallery")) {
                            Intent intent = new Intent(Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            startActivityForResult(Intent.createChooser(intent, "Select Image"),
                                    REQUEST_SELECT_FILE);
                        } else if (items[which].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = etTitle.getText().toString();
                String author = etAuthor.getText().toString();
                String isbn = etIsbn.getText().toString();

                mUserActionListener.addBook(title, author, isbn);
            }
        });
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) return;

        if (requestCode == REQUEST_CAMERA) {
            mUserActionListener.imageTaken((Bitmap)data.getExtras().get("data"));
        } else if (requestCode == REQUEST_SELECT_FILE) {
            mUserActionListener.imageSelected(data.getData());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mPermissionManager.handlePermissionResult(requestCode, grantResults);
    }

    @Override
    public void showThumbnail(Bitmap thumbnail) {
        ImageView ivThumbnail = (ImageView) getActivity().findViewById(R.id.iv_thumbnail);
        ivThumbnail.setImageBitmap(thumbnail);
        ivThumbnail.setBackground(null);
    }

    @Override
    public Context getCurrentContext() {
        return getContext();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void showMessage(int messageId) {
        Toast.makeText(getContext(), getString(messageId), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showBooksList() {
        getActivity().finish();
    }

    @Override
    public void requestPermissionToReadImage() {
        mPermissionManager.with(Manifest.permission.READ_EXTERNAL_STORAGE)
                .onPermissionGranted(new OnPermissionGrantedCallback() {
                    @Override
                    public void onPermissionGranted() {
                        mUserActionListener.onReadPermissionGranted();
                    }
                })
                .onPermissionDenied(showPermissionDeniedSnackbar(
                        getActivity().findViewById(R.id.cl_coordinatorLayout),
                        "Read file permission is needed to add the image.", "SETTINGS"))
                .onPermissionShowRationale(showPermissionShowRationaleSnackbar(
                        getActivity().findViewById(R.id.cl_coordinatorLayout),
                        "File access is required to add the image.", "OK"))
                .request();
    }

    @Override
    public void requestPermissionToWriteImage() {
        mPermissionManager.with(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .onPermissionGranted(new OnPermissionGrantedCallback() {
                    @Override
                    public void onPermissionGranted() {
                        mUserActionListener.onWritePermissionGranted();
                    }
                })
                .onPermissionDenied(showPermissionDeniedSnackbar(
                        getActivity().findViewById(R.id.cl_coordinatorLayout),
                        "Save file permission is needed to add the book.", "SETTINGS"))
                .onPermissionShowRationale(showPermissionShowRationaleSnackbar(
                        getActivity().findViewById(R.id.cl_coordinatorLayout),
                        "File access is required to add the book.", "OK"))
                .request();
    }
}
