package com.example.android.mypopularmovies.task;

import android.os.AsyncTask;

import com.example.android.mypopularmovies.model.Trailer;
import com.example.android.mypopularmovies.util.JsonTrailerUtil;
import com.example.android.mypopularmovies.util.NetworkUtils;

import java.net.URL;
import java.util.List;

/**
 * Task that runs at background and connects to movie db server requesting a list of trailer related to a video id.
 */
public class TrailerTask extends AsyncTask<Long, Void, List<Trailer>> {

    private OnTrailerTaskCompleted mObserver;

    public TrailerTask(OnTrailerTaskCompleted observer) {
        this.mObserver = observer;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (mObserver != null) {
            mObserver.onTaskCreated();
        }
    }

    @Override
    protected List<Trailer> doInBackground(Long... params) {
        if (params.length == 0) {
            return null;
        }
        String movieId = String.valueOf(params[0]);
        URL trailerUrl = NetworkUtils.buildTrailerUrl(movieId);
        try {
            String jsonTrailerResponse = NetworkUtils.getResponseFromHttpUrl(trailerUrl);
            if (!"".equalsIgnoreCase(jsonTrailerResponse)) {
                return JsonTrailerUtil.getTrailerFromJson(jsonTrailerResponse);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Trailer> trailerList) {
        if (mObserver != null) {
            mObserver.onTaskCompleted(trailerList);
        }
    }

    public interface OnTrailerTaskCompleted {
        void onTaskCreated();

        void onTaskCompleted(List<Trailer> trailerList);
    }
}
