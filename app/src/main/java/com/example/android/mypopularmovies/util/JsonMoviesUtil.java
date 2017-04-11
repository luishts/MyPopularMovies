package com.example.android.mypopularmovies.util;

import com.example.android.mypopularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * This class handles a json string that application receives from movie server and parse/convert it into a Movie list.
 */

public class JsonMoviesUtil {

    /**
     * Method that given a json string parses it into a movie list
     *
     * @param moviesJsonStr - json string received from movie server
     * @return - movie list
     */
    public static List<Movie> getMoviesStringsFromJson(String moviesJsonStr) {
        JSONObject movieJson;
        List<Movie> parsedMovieData = new ArrayList<>();
        try {
            movieJson = new JSONObject(moviesJsonStr);
            JSONArray items = movieJson.getJSONArray("results");
            JSONObject movieObj;
            for (int i = 0; i < items.length(); i++) {
                movieObj = items.getJSONObject(i);
                Movie movie = new Movie();
                movie.setTitle(movieObj.getString("original_title"));
                movie.setOverview(movieObj.getString("overview"));
                movie.setPosterPath("http://image.tmdb.org/t/p/w185/" + movieObj.getString("poster_path"));
                movie.setReleaseDate(movieObj.getString("release_date"));
                movie.setVoteAverage((float) movieObj.getDouble("vote_average"));
                parsedMovieData.add(movie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return parsedMovieData;
    }
}
