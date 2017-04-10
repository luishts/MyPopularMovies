package com.example.android.mypopularmovies;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by ltorres on 10/04/2017.
 */

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String STATIC_MOVIES_URL =
            "https://www.themoviedb.org";

    private static final String MOVIES_BASE_URL = STATIC_MOVIES_URL;

    private static final String popular = "/movie/popular";

    private static final String top_rated = "/movie/top-rated";

    private static final String format = "json";

    final static String FORMAT_PARAM = "mode";

    final static String MOVIE_PARAM = "category";

    public static URL buildUrl(String param) {
        Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                .appendQueryParameter(MOVIE_PARAM, param)
                .appendQueryParameter(FORMAT_PARAM, format)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
