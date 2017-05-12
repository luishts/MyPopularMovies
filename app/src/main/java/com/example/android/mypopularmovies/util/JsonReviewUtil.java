package com.example.android.mypopularmovies.util;

import com.example.android.mypopularmovies.model.Review;
import com.example.android.mypopularmovies.model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * This class handles a json string that application receives from movie server and parse/convert it into a Review list.
 */

public class JsonReviewUtil {

    /**
     * Method that given a json string parses it into a review list
     *
     * @param reviewJsonStr - json string received from movie server
     * @return - review list
     */
    public static List<Review> getReviewFromJson(String reviewJsonStr) {
        JSONObject movieJson;
        List<Review> parsedReviewData = new ArrayList<>();
        try {
            movieJson = new JSONObject(reviewJsonStr);
            JSONArray items = movieJson.getJSONArray("results");
            JSONObject trailerObj;
            for (int i = 0; i < items.length(); i++) {
                trailerObj = items.getJSONObject(i);
                Review review = new Review();
                review.setAuthor(trailerObj.getString("author"));
                review.setContent(trailerObj.getString("content"));
                parsedReviewData.add(review);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return parsedReviewData;
    }
}
