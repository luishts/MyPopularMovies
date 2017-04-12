package com.example.android.mypopularmovies.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.mypopularmovies.R;
import com.example.android.mypopularmovies.model.Movie;
import com.example.android.mypopularmovies.util.Constants;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        if (getIntent() != null && getIntent().hasExtra(Constants.MOVIE_KEY)) {
            Movie movie = getIntent().getParcelableExtra(Constants.MOVIE_KEY);
            initUI(movie);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.detail_title));
    }

    /**
     * Method that fill all UI fields with the information of selected movie
     *
     * @param movie
     */
    private void initUI(Movie movie) {
        mTitle.setText(movie.getTitle());
        mDate.setText(movie.getReleaseDate());
        mVote.setText(String.valueOf(movie.getVoteAverage()));
        mOverview.setText(movie.getOverview());
        Picasso.with(this).load(movie.getPosterPath()).placeholder(R.mipmap.ic_launcher).into(mPoster);
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
}
