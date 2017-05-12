package com.example.android.mypopularmovies.task;

import android.os.AsyncTask;

import com.example.android.mypopularmovies.model.Review;
import com.example.android.mypopularmovies.model.Trailer;
import com.example.android.mypopularmovies.util.JsonReviewUtil;
import com.example.android.mypopularmovies.util.JsonTrailerUtil;
import com.example.android.mypopularmovies.util.NetworkUtils;

import java.net.URL;
import java.util.List;

/**
 * Task that runs at background and connects to movie db server requesting a list of trailer related to a video id. 'Sends' it observer that updates the UI
 */
public class ReviewTask extends AsyncTask<Long, Void, List<Review>> {

    private OnReviewTaskCompleted mObserver;

    public ReviewTask(OnReviewTaskCompleted observer) {
        this.mObserver = observer;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<Review> doInBackground(Long... params) {
        if (params.length == 0) {
            return null;
        }
        String movieId = String.valueOf(params[0]);
        URL reviewUrl = NetworkUtils.buildReviewUrl(movieId);
        try {
            String jsonTrailerResponse = NetworkUtils.getResponseFromHttpUrl(reviewUrl);
            if (!"".equalsIgnoreCase(jsonTrailerResponse)) {
                return JsonReviewUtil.getReviewFromJson(jsonTrailerResponse);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Review> reviewList) {
        if (mObserver != null) {
            mObserver.onReviewTaskCompleted(reviewList);
        }
    }

    public interface OnReviewTaskCompleted {
        void onReviewTaskCompleted(List<Review> reviewList);
    }
}
