package com.example.android.mypopularmovies.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.android.mypopularmovies.R;
import com.example.android.mypopularmovies.adapter.EndlessRecyclerViewScrollListener;
import com.example.android.mypopularmovies.adapter.MovieAdapter;
import com.example.android.mypopularmovies.model.Movie;
import com.example.android.mypopularmovies.util.Constants;
import com.example.android.mypopularmovies.util.JsonMoviesUtil;
import com.example.android.mypopularmovies.util.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Handler mHandler;

    private Parcelable mListState;

    private String mCurrentPath;

    private ArrayList<Movie> mData;

    private RecyclerView mRecyclerView;
    private GridLayoutManager mLayoutManager;
    private ProgressBar mProgressBar;
    private EndlessRecyclerViewScrollListener mScrollListener;

    private MovieAdapter mMovieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHandler = new Handler();

        mData = new ArrayList<>();
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        DrawableCompat.setTint(mProgressBar.getIndeterminateDrawable(), ContextCompat.getColor(this, R.color.colorAccent));
        mRecyclerView = (RecyclerView) findViewById(R.id.list_movies);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mLayoutManager = new GridLayoutManager(this, 2);
        } else {
            mLayoutManager = new GridLayoutManager(this, 3);
        }
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.getLayoutManager().setAutoMeasureEnabled(true);

        mScrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(final int page, int totalItemCount) {
                //if totalItemCount < than page*MAX_SALES_PER_PAGE, it means that all sales from db are already loaded
                if (totalItemCount < Constants.MAX_MOVIES_PER_PAGE * page) {
                    Log.d(TAG, "all movies are already loaded. totalItemCount = " + totalItemCount + " MAX_MOVIES_PER_PAGE * page = " + (Constants.MAX_MOVIES_PER_PAGE * page));
                    return;
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadMoreMovies(page);
                        }
                    }, Constants.ENDLESS_SCROLL_ANIMATION_TIME);
                }
            }
        };
        mScrollListener.setVisibleThreshold(2);
        mRecyclerView.addOnScrollListener(mScrollListener);
        mMovieAdapter = new MovieAdapter(this, mData);
        mMovieAdapter.setClickListener(this);
        mRecyclerView.setAdapter(mMovieAdapter);

        mCurrentPath = Constants.POPULAR_PATH;

        getSupportActionBar().setTitle(getString(R.string.main_title));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_popular:
                if (!mCurrentPath.equalsIgnoreCase(Constants.POPULAR_PATH)) {
                    mCurrentPath = Constants.POPULAR_PATH;
                    mMovieAdapter.setData(new ArrayList<Movie>());
                    mScrollListener.resetState();
                    new MoviesTask().execute(mCurrentPath, "1");
                }
                break;
            case R.id.action_top_rated:
                if (!mCurrentPath.equalsIgnoreCase(Constants.TOP_RATED_PATH)) {
                    mCurrentPath = Constants.TOP_RATED_PATH;
                    mMovieAdapter.setData(new ArrayList<Movie>());
                    mScrollListener.resetState();
                    new MoviesTask().execute(mCurrentPath, "1");
                }
                break;
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mListState = mLayoutManager.onSaveInstanceState();
        outState.putParcelable(Constants.LIST_STATE_KEY, mListState);
        outState.putParcelableArrayList(Constants.LIST_VALUES_KEY, mMovieAdapter.getItens());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mListState = savedInstanceState.getParcelable(Constants.LIST_STATE_KEY);
            mData = savedInstanceState.getParcelableArrayList(Constants.LIST_VALUES_KEY);
            mMovieAdapter.setData(mData);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mListState != null) {
            mLayoutManager.onRestoreInstanceState(mListState);
        } else {
            mProgressBar.setVisibility(View.VISIBLE);
            new MoviesTask().execute(mCurrentPath, "1");
        }
    }

    private void loadMoreMovies(int page) {
        new MoviesTask().execute(mCurrentPath, String.valueOf(++page));
    }

    @Override
    public void onItemClick(View view, int position) {
        Movie movie = mMovieAdapter.getItem(position);
        Intent detailIntent = new Intent(MainActivity.this, MovieDetailActivity.class);
        detailIntent.putExtra(Constants.MOVIE_KEY, movie);
        startActivity(detailIntent);
    }

    public class MoviesTask extends AsyncTask<String, Void, List<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
                return JsonMoviesUtil.getMoviesStringsFromJson(jsonMoviesResponse);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            mMovieAdapter.setMoreMovies(movies);
            mProgressBar.setVisibility(View.GONE);
        }
    }
}
