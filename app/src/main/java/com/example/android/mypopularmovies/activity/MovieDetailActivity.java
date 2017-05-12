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
import com.example.android.mypopularmovies.adapter.TrailerAdapter;
import com.example.android.mypopularmovies.data.MovieContract;
import com.example.android.mypopularmovies.model.Movie;
import com.example.android.mypopularmovies.model.Trailer;
import com.example.android.mypopularmovies.task.TrailerTask;
import com.example.android.mypopularmovies.util.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity implements View.OnClickListener, TrailerTask.OnTrailerTaskCompleted {

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

    private boolean mIsFavorite;
    private Movie mSelectedVideo;
    private TrailerAdapter mTrailerAdapter;
    private List<Trailer> mTrailerList;

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

        if (getIntent() != null && getIntent().hasExtra(Constants.MOVIE_KEY)) {
            mSelectedVideo = getIntent().getParcelableExtra(Constants.MOVIE_KEY);
            new TrailerTask(this).execute(mSelectedVideo.getId());
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
                    ContentValues values = new ContentValues();
                    values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, mSelectedVideo.getId());
                    values.put(MovieContract.MovieEntry.COLUMN_TITLE, mSelectedVideo.getTitle());
                    Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, values);
                    if (uri != null) {
                        Toast.makeText(getApplicationContext(), "Movie added to favourites", Toast.LENGTH_SHORT).show();
                        mIsFavorite = true;
                        updateFavoriteIcon();
                    } else {
                        Toast.makeText(getApplicationContext(), "Try again", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    int rowsDeleted = getContentResolver().delete(MovieContract.MovieEntry.buildMovieUriWithId(mSelectedVideo.getId()), null, null);
                    if (rowsDeleted > 0) {
                        Toast.makeText(getApplicationContext(), "Movie removed from favourites", Toast.LENGTH_SHORT).show();
                        mIsFavorite = false;
                        updateFavoriteIcon();
                    } else {
                        Toast.makeText(getApplicationContext(), "Try again", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    private void updateFavoriteIcon() {
        if (mIsFavorite) {
            mIsFavorite = true;
        } else {
            mFavoriteButton.setImageResource(R.drawable.ic_favorite_border_white_48dp);
        }
    }

    @Override
    public void onTaskCreated() {

    }

    @Override
    public void onTaskCompleted(List<Trailer> trailerList) {
        mTrailerList = trailerList;
        mTrailerAdapter.setData(trailerList);
    }
}
