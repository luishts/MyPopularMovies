package com.example.android.mypopularmovies.model;

/**
 * This class represents a Review entity with all the fields that we need to show in the UI
 */
public class Review {

    private String author;
    private String content;

    public Review() {
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
