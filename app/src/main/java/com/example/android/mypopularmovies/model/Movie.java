package com.example.android.mypopularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class represents a Movie entity with all the fields that we need to show in the UI
 */

public class Movie implements Parcelable {

    private long id;
    private String title;
    private String releaseDate;
    private String posterPath;
    private String overview;
    private float voteAverage;

    public Movie() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    protected Movie(Parcel in) {
        title = in.readString();
        releaseDate = in.readString();
        posterPath = in.readString();
        overview = in.readString();
        voteAverage = in.readFloat();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(releaseDate);
        dest.writeString(posterPath);
        dest.writeString(overview);
        dest.writeFloat(voteAverage);
    }
}
