package com.example.android.mypopularmovies.model;

/**
 * This class represents a Movie entity with all the fields that we need to show in the UI
 */

public class Trailer {

    private String name;
    private String site;
    private String path;

    public Trailer() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
