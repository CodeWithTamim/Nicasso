package com.dev.nicasso;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Nicasso - A simple image loading library.
 *
 * This class provides a convenient way to load images from URLs into an ImageView.
 * It handles downloading the image on a background thread and updating the ImageView on the main thread.
 * Additionally, it allows setting a default image to be displayed if the URL image fails to load.
 *
 * @developer Tamim Hossain
 * @mail tamimh.dev@gmail.com
 */
public class Nicasso {

    private static final ExecutorService executor = Executors.newFixedThreadPool(4);

    /**
     * Creates an ImageRequest instance for the specified URL.
     *
     * @param url The URL of the image to be loaded.
     * @return An ImageRequest instance for the specified URL.
     */
    public static ImageRequest get(String url) {
        return new ImageRequest(url);
    }

    /**
     * Class representing an image loading request.
     *
     * This class handles downloading the image from the given URL and loading it into an ImageView.
     */
    public static class ImageRequest {

        private final String url;
        private Integer defaultImageResId = null;

        public ImageRequest(String url) {
            this.url = url;
        }

        /**
         * Sets a default image resource to be displayed if the URL image fails to load.
         *
         * @param resId The resource ID of the default image.
         * @return The current ImageRequest instance for chaining.
         */
        public ImageRequest defaultImage(@DrawableRes int resId) {
            this.defaultImageResId = resId;
            return this;
        }

        /**
         * Loads the image from the URL into the provided ImageView.
         *
         * This method executes the image loading operation on a background thread using Executors.
         * Once the image is downloaded, it updates the ImageView on the main thread.
         * If the image cannot be loaded, the default image (if set) is displayed instead.
         *
         * @param imageView The ImageView into which the image will be loaded.
         */
        public void load(final ImageView imageView) {
            executor.execute(() -> {
                final Bitmap bitmap = downloadImage(url, imageView.getWidth(), imageView.getHeight());
                imageView.post(() -> {
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    } else if (defaultImageResId != null) {
                        imageView.setImageResource(defaultImageResId);
                    }
                });
            });
        }

        /**
         * Downloads the image from the given URL.
         *
         * This method is executed in a background thread to avoid blocking the main thread.
         * It performs a network request to fetch the image and decode it into a Bitmap.
         *
         * @param url     The URL of the image to be downloaded.
         * @param reqWidth The required width of the image.
         * @param reqHeight The required height of the image.
         * @return A Bitmap object representing the downloaded image, or null if an error occurs.
         */
        private Bitmap downloadImage(String url, int reqWidth, int reqHeight) {
            try {
                final URL imageUrl = new URL(url);
                final HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
                connection.setDoInput(true);
                connection.connect();
                final InputStream input = connection.getInputStream();

                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(input, null, options);
                input.close();

                // Calculate the inSampleSize
                options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
                options.inJustDecodeBounds = false;

                final InputStream input2 = imageUrl.openStream();
                final Bitmap bitmap = BitmapFactory.decodeStream(input2, null, options);
                input2.close();
                return bitmap;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        /**
         * Calculates the appropriate inSampleSize value for loading a Bitmap.
         *
         * @param options   The BitmapFactory.Options object with the image dimensions.
         * @param reqWidth  The required width of the image.
         * @param reqHeight The required height of the image.
         * @return The calculated inSampleSize value.
         */
        private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {
                final int halfHeight = height / 2;
                final int halfWidth = width / 2;

                while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                    inSampleSize *= 2;
                }
            }

            return inSampleSize;
        }
    }
}
