package com.haleysoftware.popularmovies.utils;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.haleysoftware.popularmovies.objects.TrailerItem;

import java.net.URL;
import java.util.List;

/**
 * Created by haleysoft on 10/23/18.
 * <p>
 * The loader for getting Trailer data.
 */
public class TrailerLoader extends AsyncTaskLoader<List<TrailerItem>> {

    public static final int LOADER_ID = 8637;

    private int movieId;
    private int page;

    /**
     * Creates the loader for trailers data.
     *
     * @param context The activity context.
     * @param movieId The Id of the movie to pull Trailers for.
     * @param page    The JSON page number. Use 1 as this is not used yet.
     */
    public TrailerLoader(Context context, int movieId, int page) {
        super(context);
        this.movieId = movieId;
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
     * Gets the trailers on a background thread.
     *
     * @return The list of Trailers.
     */
    @Override
    public List<TrailerItem> loadInBackground() {
        URL url = NetworkHelper.buildTrailerUrl(movieId, page);

        try {
            String networkResponse = NetworkHelper.getResponseFromHttpUrl(url);
            return TrailerJson.getTrailersFromJson(networkResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
