package com.example.android.mypopularmovies.util;

import com.example.android.mypopularmovies.model.Movie;
import com.example.android.mypopularmovies.model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * This class handles a json string that application receives from movie server and parse/convert it into a Movie list.
 */

public class JsonTrailerUtil {

    /**
     * Method that given a json string parses it into a movie list
     *
     * @param trailersJsonStr - json string received from movie server
     * @return - trailer list
     */
    public static List<Trailer> getTrailerFromJson(String trailersJsonStr) {
        JSONObject movieJson;
        List<Trailer> parsedTrailerData = new ArrayList<>();
        try {
            movieJson = new JSONObject(trailersJsonStr);
            JSONArray items = movieJson.getJSONArray("results");
            JSONObject trailerObj;
            for (int i = 0; i < items.length(); i++) {
                trailerObj = items.getJSONObject(i);
                Trailer trailer = new Trailer();
                trailer.setName(trailerObj.getString("name"));
                trailer.setPath(trailerObj.getString("key"));
                trailer.setSite(trailerObj.getString("site"));
                parsedTrailerData.add(trailer);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return parsedTrailerData;
    }
}
