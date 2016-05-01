package com.example.librarymanager.util;

import android.graphics.Bitmap;

import java.io.IOException;

/**
 * An interface defined for managing image files
 */
public interface ImageFileApi {

    /**
     * Create a empty file to be used for storing thumbnail
     * @return path of the created image file
     * @throws IOException
     */
    String createImageFile() throws IOException;

    /**
     * Create a image file using the given path
     * @param imagePath the created file will have this path
     */
    void createImageFileFromPath(String imagePath);

    /**
     * Retrieve the image data from the file, if the file exists and is not empty
     * @return the bitmap data of the thumbnail
     */
    Bitmap getImageData();

    /**
     * @return Whether the file is created
     */
    boolean exists();

    /**
     * Delete the file, if exist
     * @return true if deleted, false otherwise
     */
    boolean delete();

    /**
     * Retrieve the absolute path of the file
     * @return the absolute path of the file
     */
    String getPath();

    /**
     * Save the provided image data to the file
     * @param imageDate the bitmap data to be saved
     * @return true if saved, false otherwise
     */
    boolean saveImageData(Bitmap imageDate);
}
