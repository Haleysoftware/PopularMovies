package com.haleysoftware.popularmovies.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.haleysoftware.popularmovies.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * A helper to create the URL and pull data from the internet.
 * <p>
 * Created by haleysoft on 8/29/18.
 */
public final class NetworkHelper {

    public static final int FAV_SORT_ID = 2;
    public static final int POP_SORT_ID = 1;
    public static final int VOTE_SORT_ID = 0;
    private static final String URI_PATH = "https://api.themoviedb.org/3/movie";
    private static final String URI_TRAILER = "/videos";
    private static final String URI_REVIEW = "/reviews";
    private static final String URI_API_KEY = "api_key";
    //TODO Here is the link for the API Key for Movie DB
    private static final String API_KEY = BuildConfig.API_KEY;
    private static final String URI_LANG_KEY = "language";
    private static final String LANGUAGE = "en-US";
    private static final String URI_PATH_POP = "/popular";
    private static final String URI_PATH_TOP = "/top_rated";

    private static final String URI_PAGE_KEY = "page";

    /**
     * A check if the device has internet.
     *
     * @param context The activity context.
     * @return A Boolean if the device has internet or not.
     */
    public static boolean hasInternet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork;
        try {
            activeNetwork = cm.getActiveNetworkInfo();
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    /**
     * Builds the web address for a list of movies based on the query
     *
     * @param query Should be a 0 if you want popular movies or a 1 if you want the top movies.
     * @param page  The JSON page number. Not currently used.
     * @return The URL that should be used to get the JSON data.
     */
    static URL buildMovieUrl(int query, int page) {
        String path = URI_PATH;
        switch (query) {
            default:
            case VOTE_SORT_ID:
                path = path + URI_PATH_TOP;
                break;
            case POP_SORT_ID:
                path = path + URI_PATH_POP;
                break;
        }
        return masterBuilder(path, page);
    }

    /**
     * Builds the web address for a list of trailers for a movie.
     *
     * @param movie The movie ID that you want trailers for.
     * @param page  The JSON page number. Not currently used.
     * @return The URL used to get the JSON data.
     */
    static URL buildTrailerUrl(int movie, int page) {
        String path = URI_PATH + "/" + movie + URI_TRAILER;
        return masterBuilder(path, page);
    }

    /**
     * Builds the web address for a list of reviews for a movie.
     *
     * @param movie The movie ID that you want reviews for.
     * @param page  The JSON page number. Not currently used.
     * @return The URL used to get the JSON data.
     */
    static URL buildReviewUrl(int movie, int page) {
        String path = URI_PATH + "/" + movie + URI_REVIEW;
        return masterBuilder(path, page);
    }

    /**
     * Takes a base movie, trailer or review URL and adds parameters to create a URL.
     *
     * @param path The base path for movies, trailers or reviews.
     * @param page the page number for the JSON data. Not currently used.
     * @return The URL used to get the JSON data.
     */
    static URL masterBuilder(String path, int page) {
        Uri builtUri = Uri.parse(path).buildUpon()
                .appendQueryParameter(URI_API_KEY, API_KEY)
                .appendQueryParameter(URI_LANG_KEY, LANGUAGE)
                .appendQueryParameter(URI_PAGE_KEY, String.valueOf(page)).build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * Uses a url and returns the string response. This one returns a JSON String.
     *
     * @param url The URL to use for the response.
     * @return Returns the JSON string that came from the URL.
     * @throws IOException If there is an issue with the network connection.
     */
    static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = connection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            connection.disconnect();
        }
    }
}
