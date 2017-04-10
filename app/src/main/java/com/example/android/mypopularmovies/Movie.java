package com.example.android.mypopularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ltorres on 10/04/2017.
 */

public class Movie implements Parcelable {

    private int id;
    private String title;
    private String release_date;
    private String poster_path;
    private String overview;
    private float vote_average;
    private double popularity;
    private boolean adult;

    public Movie() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public float getVote_average() {
        return vote_average;
    }

    public void setVote_average(float vote_average) {
        this.vote_average = vote_average;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
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
        id = in.readInt();
        title = in.readString();
        release_date = in.readString();
        poster_path = in.readString();
        overview = in.readString();
        vote_average = in.readFloat();
        popularity = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(release_date);
        dest.writeString(poster_path);
        dest.writeString(overview);
        dest.writeFloat(vote_average);
        dest.writeDouble(popularity);
    }
}
