package com.haleysoftware.popularmovies.utils;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.haleysoftware.popularmovies.objects.ReviewItem;

import java.net.URL;
import java.util.List;

/**
 * The loader for Review items.
 * <p>
 * Created by haleysoft on 10/23/18.
 */
public class ReviewLoader extends AsyncTaskLoader<List<ReviewItem>> {

    public static final int LOADER_ID = 5382;

    private int movie;
    private int page;

    /**
     * Creates the Review Loader.
     *
     * @param context Activity Context.
     * @param movie   The ID of the movie to pull reviews for.
     * @param page    The JSON page number. Not currently used.
     */
    public ReviewLoader(Context context, int movie, int page) {
        super(context);
        this.movie = movie;
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
     * The thread to create the list of review items.
     *
     * @return The list of review items.
     */
    @Override
    public List<ReviewItem> loadInBackground() {
        URL url = NetworkHelper.buildReviewUrl(movie, page);

        try {
            String networkResponse = NetworkHelper.getResponseFromHttpUrl(url);
            return ReviewJson.getReviewsFromJson(networkResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
