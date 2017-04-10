package com.example.android.mypopularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Handler mHandler;

    private String mCurrentPath;

    private List<Movie> mData;

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
        mProgressBar.getIndeterminateDrawable().setColorFilter(0xFFcc0000, android.graphics.PorterDuff.Mode.MULTIPLY);
        mRecyclerView = (RecyclerView) findViewById(R.id.list_movies);

        mLayoutManager = new GridLayoutManager(this, 2);
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

        new MoviesTask().execute(mCurrentPath, "1");
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
            mProgressBar.setVisibility(View.VISIBLE);
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
                return JsonMoviesUtil.getMoviesStringsFromJson(MainActivity.this, jsonMoviesResponse);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            mProgressBar.setVisibility(View.GONE);
            mMovieAdapter.setMoreMovies(movies);
        }
    }
}
