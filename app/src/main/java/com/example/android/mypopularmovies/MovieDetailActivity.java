package com.example.android.mypopularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {

    ImageView mPoster;
    TextView mTitle, mDate, mVote, mOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mPoster = (ImageView) findViewById(R.id.poster);

        mTitle = (TextView) findViewById(R.id.title);
        mDate = (TextView) findViewById(R.id.release_date);
        mVote = (TextView) findViewById(R.id.vote_average);
        mOverview = (TextView) findViewById(R.id.overview);

        if (getIntent() != null && getIntent().hasExtra(Constants.MOVIE_KEY)) {
            Movie movie = getIntent().getParcelableExtra(Constants.MOVIE_KEY);
            initUI(movie);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initUI(Movie movie) {
        mTitle.setText(movie.getTitle());
        mDate.setText(movie.getRelease_date());
        mVote.setText(String.valueOf(movie.getVote_average()));
        mOverview.setText(movie.getOverview());
        Picasso.with(this).load(movie.getPoster_path()).placeholder(R.mipmap.ic_launcher).into(mPoster);
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
