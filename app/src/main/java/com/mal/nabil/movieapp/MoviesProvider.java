package com.mal.nabil.movieapp;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;


public class MoviesProvider extends ContentProvider {

    private static final String PROVIDER_NAME = "moviecontentproviderdemo.moviecontentprovider.movies";
    private static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/movies");
    private static final int MOVIES = 1;
    private static final int MOVIE_ID = 2;
    private static final UriMatcher uriMatcher = getUriMatcher();
    private static UriMatcher getUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "movies", MOVIES);
        uriMatcher.addURI(PROVIDER_NAME, "movies/#", MOVIE_ID);
        return uriMatcher;
    }

    private MovieDataBase movieDataBase = null;

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case MOVIES:
                return "vnd.android.cursor.dir/vnd.com.moviecontentproviderdemo.moviecontentprovider.provider.movies";
            case MOVIE_ID:
                return "vnd.android.cursor.item/vnd.com.moviecontentproviderdemo.moviecontentprovider.provider.movies";

        }
        return "";
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        movieDataBase = new MovieDataBase(context);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String id = null;
        if(uriMatcher.match(uri) == MOVIE_ID) {
            //Query is for one single movie. Get the ID from the URI.
            id = uri.getPathSegments().get(1);
        }
        return movieDataBase.getMovies(id, projection, selection, selectionArgs, sortOrder);
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        try {
            long id = movieDataBase.addNewMovie(values);
            Uri returnUri = ContentUris.withAppendedId(CONTENT_URI, id);
            return returnUri;
        } catch(Exception e) {
            return null;
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String id = null;
        if(uriMatcher.match(uri) == MOVIE_ID) {
            //Delete is for one single movie. Get the ID from the URI.
            id = uri.getPathSegments().get(1);
        }
        return movieDataBase.deleteMovies(id);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String id = null;
        if(uriMatcher.match(uri) == MOVIE_ID) {
            //Update is for one single movie. Get the ID from the URI.
            id = uri.getPathSegments().get(1);
        }
        return movieDataBase.updateMovies(id, values);
    }
}