package com.haleysoftware.popularmovies.utils;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.haleysoftware.popularmovies.objects.MovieItem;

import java.net.URL;
import java.util.List;

/**
 * The loader that runs in a background thread to download the JSON data and create a list of
 * movie items.
 * <p>
 * Created by haleysoft on 8/29/18.
 */
public class MovieLoader extends AsyncTaskLoader<List<MovieItem>> {

    public static final int LOADER_ID = 3756;


    private int query;
    private int page;

    /**
     * The movie loader constructor creates the internet connection to collect the movie list.
     *
     * @param context The calling activities context
     * @param query   the kind of query to make (Popular or high rated)
     * @param page    The page of movies to get (not yet used)
     */
    public MovieLoader(Context context, int query, int page) {
        super(context);
        this.query = query;
        this.page = page;
    }

    /**
     * Called before load in background. Used to setup anything before thread is started.
     */
    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * Thread to download the movie database from TheMovieDB.org.
     *
     * @return The list of movie items that was created from the JSON file.
     */
    @Override
    public List<MovieItem> loadInBackground() {
        URL url = NetworkHelper.buildMovieUrl(query, page);

        try {
            String networkResponse = NetworkHelper.getResponseFromHttpUrl(url);
            return MovieJson.getMoviesFromJson(networkResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
