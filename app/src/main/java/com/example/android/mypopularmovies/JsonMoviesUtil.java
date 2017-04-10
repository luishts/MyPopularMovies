package com.example.android.mypopularmovies;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ltorres on 10/04/2017.
 */

public class JsonMoviesUtil {


    public static List<Movie> getMoviesStringsFromJson(Context context, String moviesJsonStr)
            throws JSONException {

        /* String array to hold each day's weather String */
        List<Movie> parsedMovieData = new ArrayList<>();

        JSONObject movieJson = new JSONObject(moviesJsonStr);

        JSONArray items = movieJson.getJSONArray("results");

        JSONObject movieObj;
        for (int i=0; i<items.length(); i++){
            movieObj = items.getJSONObject(i);
            Movie movie = new Movie();
            movie.setId(movieObj.getInt("id"));
            movie.setTitle(movieObj.getString("original_title"));
            movie.setOverview(movieObj.getString("overview"));
            movie.setPoster_path("http://image.tmdb.org/t/p/w185/" + movieObj.getString("poster_path"));
            movie.setRelease_date(movieObj.getString("release_date"));
            movie.setVote_average((float) movieObj.getDouble("vote_average"));
            movie.setPopularity(movieObj.getDouble("popularity"));
            parsedMovieData.add(movie);
        }

        return parsedMovieData;

    }
}
