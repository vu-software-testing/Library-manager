package com.example.librarymanager.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageFileImpl implements ImageFileApi {

    File mImageFile;

    public String createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        mImageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        return mImageFile.getAbsolutePath();
    }

    @Override
    public void createImageFileFromPath(String path) {
        mImageFile = new File(path);
    }

    @Override
    public Bitmap getImageData() {
        if (!mImageFile.exists()) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mImageFile.getAbsolutePath(), options);
        final int requiredSize = 100;
//        int scale = 1;
//        while (options.outWidth / scale / 2 >= REQUIRED_SIZE
//                && options.outHeight / scale / 2 >= REQUIRED_SIZE)
//            scale *= 2;
        int photoW = options.outWidth;
        int photoH = options.outHeight;
        options.inSampleSize = Math.max(photoW/requiredSize, photoH/requiredSize);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(mImageFile.getAbsolutePath(), options);
    }



    @Override
    public boolean exists() {
        return mImageFile != null && mImageFile.exists();
    }

    @Override
    public boolean delete() {
        return mImageFile.exists() && mImageFile.delete();
    }

    @Override
    public String getPath() {
        if (mImageFile.exists()) {
            return mImageFile.getPath();
        } else {
            return null;
        }
    }

    @Override
    public boolean saveImageData(Bitmap imageData) {
        //File destination = new File(Environment.getExternalStorageDirectory(),
        //        System.currentTimeMillis() + ".jpg");
        try {
            //destination.createNewFile();
            FileOutputStream fo;
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            imageData.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            fo = new FileOutputStream(mImageFile);
            fo.write(bytes.toByteArray());
            fo.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
