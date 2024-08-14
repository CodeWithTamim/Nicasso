package com.nasa.lib

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

/**
 * Nicasso - A simple image loading library.
 *
 * This object provides a convenient way to load images from URLs into an ImageView using coroutines.
 * It handles downloading the image on a background thread and updating the ImageView on the main thread.
 * Additionally, it allows setting a default image to be displayed if the URL image fails to load.
 *
 * @developer Tamim Hossain
 * @mail tamimh.dev@gmail.com
 */
object Nicasso {

    // CoroutineScope for managing coroutines on the main thread.
    private val scope = CoroutineScope(Dispatchers.Main)

    /**
     * Creates an ImageRequest instance for the specified URL.
     *
     * @param url The URL of the image to be loaded.
     * @return An ImageRequest instance for the specified URL.
     */
    fun get(url: String): ImageRequest {
        return ImageRequest(url)
    }

    /**
     * Class representing an image loading request.
     *
     * This class handles downloading the image from the given URL and loading it into an ImageView.
     *
     * @param url The URL of the image to be loaded.
     */
    class ImageRequest(private val url: String) {

        private var defaultImageResId: Int? = null

        /**
         * Sets a default image resource to be displayed if the URL image fails to load.
         *
         * @param resId The resource ID of the default image.
         * @return The current ImageRequest instance for chaining.
         */
        fun default(resId: Int): ImageRequest {
            defaultImageResId = resId
            return this
        }

        /**
         * Loads the image from the URL into the provided ImageView.
         *
         * This function launches a coroutine to perform the image loading operation on a background thread.
         * Once the image is downloaded, it updates the ImageView on the main thread.
         * If the image cannot be loaded, the default image (if set) is displayed instead.
         *
         * @param imageView The ImageView into which the image will be loaded.
         */
        fun load(imageView: ImageView) {
            scope.launch {
                val bitmap = downloadImage(url)
                withContext(Dispatchers.Main) {
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap)
                    } else {
                        defaultImageResId?.let { resId ->
                            imageView.setImageResource(resId)
                        }
                    }
                }
            }
        }

        /**
         * Downloads the image from the given URL.
         *
         * This function is executed in a background thread to avoid blocking the main thread.
         * It performs a network request to fetch the image and decode it into a Bitmap.
         *
         * @param url The URL of the image to be downloaded.
         * @return A Bitmap object representing the downloaded image, or null if an error occurs.
         */
        private suspend fun downloadImage(url: String): Bitmap? {
            return withContext(Dispatchers.IO) {
                var bitmap: Bitmap? = null
                try {
                    // Open a connection to the URL
                    val url = URL(url)
                    val connection = url.openConnection() as HttpURLConnection
                    connection.doInput = true
                    connection.connect()
                    // Read the input stream and decode it into a Bitmap
                    val input: InputStream = connection.inputStream
                    bitmap = BitmapFactory.decodeStream(input)
                    input.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                bitmap
            }
        }
    }
}
