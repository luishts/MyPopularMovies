package com.example.android.mypopularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines table and column names for the movie database. This class is used to keep the code organized.
 */
public class MovieContract {

    //name for the entire content provider
    public static final String CONTENT_AUTHORITY = "com.example.android.mypopularmovies";

    //base of all URI's which apps will use to contact
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    //Possible paths that can be appended to BASE_CONTENT_URI
    public static final String PATH_MOVIE = "movie";

    /**
     * Inner class that defines the table contents of the movie table
     */
    public static final class MovieEntry implements BaseColumns {

        /* The base CONTENT_URI used to query the Movie table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIE)
                .build();

        public static final String TABLE_NAME = "movie";

        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static final String COLUMN_TITLE = "title";

        public static final String COLUMN_DATE = "release_date";

        public static final String COLUMN_POSTER = "poster_path";

        public static final String COLUMN_OVERVIEW = "overview";

        public static final String COLUMN_VOTE = "vote_average";

        /**
         * Builds a URI that adds the movie id to the end of the movie content URI path.
         * This is used to query details about a single movie entry by id.
         */
        public static Uri buildMovieUriWithId(long id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(id))
                    .build();
        }
    }
}
