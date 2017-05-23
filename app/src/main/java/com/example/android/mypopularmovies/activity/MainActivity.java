package com.example.android.mypopularmovies.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.ProgressBar;

import com.example.android.mypopularmovies.R;
import com.example.android.mypopularmovies.adapter.EndlessRecyclerViewScrollListener;
import com.example.android.mypopularmovies.adapter.MovieAdapter;
import com.example.android.mypopularmovies.data.MovieContract;
import com.example.android.mypopularmovies.model.Movie;
import com.example.android.mypopularmovies.task.MovieTask;
import com.example.android.mypopularmovies.util.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ItemClickListener, MovieTask.OnMovieTaskCompleted {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Handler mHandler;
    private String mCurrentPath;
    private Parcelable mListState;
    private ArrayList<Movie> mData;

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.list_movies)
    CustomList mRecyclerView;
    @BindView(R.id.stub)
    ViewStub emptyView;

    private MovieAdapter mMovieAdapter;
    private GridLayoutManager mLayoutManager;
    private EndlessRecyclerViewScrollListener mScrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mHandler = new Handler();
        mData = new ArrayList<>();

        DrawableCompat.setTint(mProgressBar.getIndeterminateDrawable(), ContextCompat.getColor(this, R.color.colorAccent));

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mLayoutManager = new GridLayoutManager(this, 2);
        } else {
            mLayoutManager = new GridLayoutManager(this, 2);
        }

        emptyView.setLayoutResource(R.layout.empty_movies);
        mRecyclerView.setEmptyView(emptyView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mScrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(final int page, int totalItemCount) {
                //if totalItemCount < than page*MAX_MOVIES_PER_PAGE, it means that all sales from db are already loaded
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
                    new MovieTask(MainActivity.this).execute(mCurrentPath, "1");
                }
                break;
            case R.id.action_top_rated:
                if (!mCurrentPath.equalsIgnoreCase(Constants.TOP_RATED_PATH)) {
                    mCurrentPath = Constants.TOP_RATED_PATH;
                    mMovieAdapter.setData(new ArrayList<Movie>());
                    mScrollListener.resetState();
                    new MovieTask(MainActivity.this).execute(mCurrentPath, "1");
                }
            case R.id.action_favourite:
                if (!mCurrentPath.equalsIgnoreCase(Constants.FAVOURITE_PATH)) {
                    mCurrentPath = Constants.FAVOURITE_PATH;
                    if (mData == null) {
                        mData = new ArrayList<>();
                    } else {
                        mData.clear();
                    }

                    //query movie content provider for all favourites movies
                    Cursor cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, null, null, MovieContract.MovieEntry.COLUMN_TITLE);
                    while (cursor.moveToNext()) {
                        Movie movie = new Movie();
                        movie.setId(cursor.getLong(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID)));
                        movie.setTitle(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE)));
                        movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_DATE)));
                        movie.setOverview(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW)));
                        movie.setPosterPath(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER)));
                        movie.setVoteAverage(cursor.getFloat(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE)));
                        mData.add(movie);
                    }
                    if (mMovieAdapter != null) {
                        mMovieAdapter.setData(mData);
                    }
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
        outState.putParcelableArrayList(Constants.LIST_VALUES_KEY, mMovieAdapter.getItems());
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
            new MovieTask(this).execute(mCurrentPath, "1");
        }
    }

    /**
     * Creates a task that request more movies to server according to current page.
     *
     * @param page
     */
    private void loadMoreMovies(int page) {
        new MovieTask(this).execute(mCurrentPath, String.valueOf(++page));
    }

    @Override
    public void onItemClick(View view, int position) {
        Movie movie = mMovieAdapter.getItem(position);
        Intent detailIntent = new Intent(MainActivity.this, MovieDetailActivity.class);
        detailIntent.putExtra(Constants.MOVIE_KEY, movie);
        startActivity(detailIntent);
    }


    @Override
    public void onTaskCreated() {
        if (mProgressBar != null && !mProgressBar.isShown()) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onTaskCompleted(List<Movie> movies) {
        mMovieAdapter.setMoreMovies(movies);
        if (mProgressBar != null && mProgressBar.isShown()) {
            mProgressBar.setVisibility(View.GONE);
        }
    }
}
