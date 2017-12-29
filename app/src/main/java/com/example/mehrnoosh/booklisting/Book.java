package com.example.mehrnoosh.booklisting;

import java.util.List;

/**
 * Created by Mehrnoosh on 12/14/2017.
 */

public class Book {

    private String mTitle;
    private String mPublisher;
    private String mGoogleId;
    private String mPublishedDate;
    private List <String> mAuthors;
    /** Website URL of the earthquake */
    private String mUrl;

    public Book(String title, String publisher, String googleId, String date, String url, List<String> authors){
        mTitle = title;
        mPublisher = publisher;
        mPublishedDate = date;
        mUrl = url;
        mAuthors = authors;
        mGoogleId = googleId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getPublisher() {
        return mPublisher;
    }

    public String getGoogleId() {
        return mGoogleId;
    }
    public String getDate() {
        return mPublishedDate;
    }

    /**
     * Returns the website URL to find more information about the earthquake.
     */
    public String getUrl() {
        return mUrl;
    }

    public List<String> getAuthors() {
        return mAuthors;
    }

 /*   @Override
    public String toString() {
        return mMagnitude + " " + mLocation + mDate;
    }*/
}
