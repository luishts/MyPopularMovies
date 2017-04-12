package com.example.android.mypopularmovies.task;

import android.os.AsyncTask;

import com.example.android.mypopularmovies.model.Movie;
import com.example.android.mypopularmovies.util.JsonMoviesUtil;
import com.example.android.mypopularmovies.util.NetworkUtils;

import java.net.URL;
import java.util.List;

/**
 * Task that runs at background and connects to movie db server requesting a list of movies according to a given path and page. 'Sends' it observer that updates the UI
 */
public class MovieTask extends AsyncTask<String, Void, List<Movie>> {

    private OnMovieTaskCompleted mObserver;

    public MovieTask(OnMovieTaskCompleted observer) {
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
    protected List<Movie> doInBackground(String... params) {
        if (params.length == 0) {
            return null;
        }
        String type = params[0];
        String page = params[1];
        URL moviesUrl = NetworkUtils.buildUrl(type, page);
        try {
            String jsonMoviesResponse = NetworkUtils.getResponseFromHttpUrl(moviesUrl);
            if (!"".equalsIgnoreCase(jsonMoviesResponse)) {
                return JsonMoviesUtil.getMoviesStringsFromJson(jsonMoviesResponse);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        if (mObserver != null) {
            mObserver.onTaskCompleted(movies);
        }
    }

    public interface OnMovieTaskCompleted {
        void onTaskCreated();

        void onTaskCompleted(List<Movie> movies);
    }
}
