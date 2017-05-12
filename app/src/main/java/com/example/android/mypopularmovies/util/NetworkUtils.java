package com.example.android.mypopularmovies.util;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * This class handles the network connection between the app the the movie server. In charge to build the URL and return the input stream.
 */

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    /**
     * Method that given a user filter (top_rated or popular) and a page index (list view + scroll) builds an urls applying the correct parameters
     *
     * @param path - top_rated or popular
     * @param page - recycler view page index (in this app, each page holds 6 movies)
     * @return - URL with all parameters set
     */
    public static URL buildUrl(String path, String page) {
        Uri builtUri = Uri.parse(Constants.MOVIES_BASE_URL).buildUpon()
                .appendEncodedPath(path)
                .appendQueryParameter(Constants.API_PARAM, Constants.API_KEY)
                .appendQueryParameter(Constants.PAGE_PARAM, page).build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "Built URI " + url);

        return url;
    }

    public static URL buildTrailerUrl(String path) {
        Uri builtUri = Uri.parse(Constants.MOVIES_BASE_URL).buildUpon()
                .appendEncodedPath(Constants.TRAILER_BASE_PATH)
                .appendEncodedPath(path)
                .appendEncodedPath(Constants.TRAILER_PATH)
                .appendQueryParameter(Constants.API_PARAM, Constants.API_KEY).build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "Built URI " + url);

        return url;
    }

    public static Uri buildYoutubeUri(String path) {
        Uri builtUri = Uri.parse(Constants.YOUTUBE_BASE_PATH).buildUpon()
                .appendEncodedPath(path).build();

        Log.d(TAG, "Built Uri builtUri " + builtUri);

        return builtUri;
    }

    /**
     * Method that connects to Movie database and return a json response with a list of movies
     *
     * @param url - movie db url
     * @return - server response
     */
    public static String getResponseFromHttpUrl(URL url) {
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }
}
