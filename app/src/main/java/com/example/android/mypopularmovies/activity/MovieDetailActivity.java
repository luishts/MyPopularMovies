package com.example.android.mypopularmovies.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.mypopularmovies.R;
import com.example.android.mypopularmovies.adapter.ReviewAdapter;
import com.example.android.mypopularmovies.adapter.TrailerAdapter;
import com.example.android.mypopularmovies.data.MovieContract;
import com.example.android.mypopularmovies.model.Movie;
import com.example.android.mypopularmovies.model.Review;
import com.example.android.mypopularmovies.model.Trailer;
import com.example.android.mypopularmovies.task.ReviewTask;
import com.example.android.mypopularmovies.task.TrailerTask;
import com.example.android.mypopularmovies.util.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity implements View.OnClickListener, TrailerTask.OnTrailerTaskCompleted, ReviewTask.OnReviewTaskCompleted {

    @BindView(R.id.poster)
    ImageView mPoster;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.releaseDate)
    TextView mDate;
    @BindView(R.id.voteAverage)
    TextView mVote;
    @BindView(R.id.overview)
    TextView mOverview;
    @BindView(R.id.favorite_button)
    ImageView mFavoriteButton;
    @BindView(R.id.trailer_list)
    CustomList mRecyclerView;
    @BindView(R.id.stub)
    ViewStub emptyView;

    @BindView(R.id.review_list)
    CustomList mReviewView;
    @BindView(R.id.stub2)
    ViewStub emptyReview;

    private boolean mIsFavorite;
    private Movie mSelectedVideo;
    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;
    private List<Trailer> mTrailerList;
    private List<Review> mReviewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.getLayoutManager().setAutoMeasureEnabled(true);

        emptyView.setLayoutResource(R.layout.empty_view);
        mRecyclerView.setEmptyView(emptyView);

        mTrailerAdapter = new TrailerAdapter(this, mTrailerList);
        mRecyclerView.setAdapter(mTrailerAdapter);

        mReviewView.setHasFixedSize(true);
        mReviewView.setLayoutManager(new LinearLayoutManager(this));
        mReviewView.getLayoutManager().setAutoMeasureEnabled(true);

        emptyReview.setLayoutResource(R.layout.empty_review);
        mReviewView.setEmptyView(emptyReview);

        mReviewAdapter = new ReviewAdapter(this, mReviewList);
        mReviewView.setAdapter(mReviewAdapter);

        if (getIntent() != null && getIntent().hasExtra(Constants.MOVIE_KEY)) {
            mSelectedVideo = getIntent().getParcelableExtra(Constants.MOVIE_KEY);
            new TrailerTask(this).execute(mSelectedVideo.getId());
            new ReviewTask(this).execute(mSelectedVideo.getId());
            initUI();
        }

        Cursor cursor = getContentResolver().query(MovieContract.MovieEntry.buildMovieUriWithId(mSelectedVideo.getId()), null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            mIsFavorite = true;
        } else {
            mIsFavorite = false;
        }
        updateFavoriteIcon();
        mFavoriteButton.setOnClickListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.detail_title));
    }

    /**
     * Method that fill all UI fields with the information of selected movie
     */
    private void initUI() {
        mTitle.setText(mSelectedVideo.getTitle());
        mDate.setText(mSelectedVideo.getReleaseDate());
        mVote.setText(String.valueOf(mSelectedVideo.getVoteAverage()));
        mOverview.setText(mSelectedVideo.getOverview());
        Picasso.with(this).load(mSelectedVideo.getPosterPath()).placeholder(R.mipmap.ic_launcher).into(mPoster);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.favorite_button:
                if (!mIsFavorite) {
                    //save movie info to database
                    ContentValues values = new ContentValues();
                    values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, mSelectedVideo.getId());
                    values.put(MovieContract.MovieEntry.COLUMN_TITLE, mSelectedVideo.getTitle());
                    values.put(MovieContract.MovieEntry.COLUMN_DATE, mSelectedVideo.getReleaseDate());
                    values.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, mSelectedVideo.getOverview());
                    values.put(MovieContract.MovieEntry.COLUMN_VOTE, mSelectedVideo.getVoteAverage());
                    values.put(MovieContract.MovieEntry.COLUMN_POSTER, mSelectedVideo.getPosterPath());
                    Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, values);
                    if (uri != null) {
                        Toast.makeText(getApplicationContext(), getString(R.string.movie_added_favourites), Toast.LENGTH_SHORT).show();
                        mIsFavorite = true;
                        updateFavoriteIcon();
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.movie_error), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //remove favourite movie from database
                    int rowsDeleted = getContentResolver().delete(MovieContract.MovieEntry.buildMovieUriWithId(mSelectedVideo.getId()), null, null);
                    if (rowsDeleted > 0) {
                        Toast.makeText(getApplicationContext(), getString(R.string.movie_removed_favourites), Toast.LENGTH_SHORT).show();
                        mIsFavorite = false;
                        updateFavoriteIcon();
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.movie_error), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    /**
     * Method that update favorite icon according to user choices (favourite or not)
     */
    private void updateFavoriteIcon() {
        if (mIsFavorite) {
            mFavoriteButton.setImageResource(R.drawable.ic_favorite_white_48dp);
        } else {
            mFavoriteButton.setImageResource(R.drawable.ic_favorite_border_white_48dp);
        }
    }

    @Override
    public void onTaskCreated() {
    }

    @Override
    public void onReviewTaskCompleted(List<Review> reviewList) {
        mReviewList = reviewList;
        mReviewAdapter.setData(reviewList);
    }

    @Override
    public void onTaskCompleted(List<Trailer> trailerList) {
        mTrailerList = trailerList;
        mTrailerAdapter.setData(trailerList);
    }
}
