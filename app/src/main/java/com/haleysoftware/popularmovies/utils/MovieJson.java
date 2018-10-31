package com.haleysoftware.popularmovies.utils;

import com.haleysoftware.popularmovies.objects.MovieItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to parse a JSON strings.
 * <p>
 * Created by haleysoft on 8/29/18.
 */
final class MovieJson {


    /**
     * Turns a JSON string into a list of movie items
     *
     * @param jsonString The JSON string to be parsed
     * @return The list of movie items that was created from the JSON string
     * @throws JSONException There was an issue parsing the JSON string
     */
    static List<MovieItem> getMoviesFromJson(String jsonString) throws JSONException {

        final String JSON_RESULTS = "results";

        final String JSON_ID = "id";
        final String JSON_TITLE = "original_title";
        final String JSON_RELEASE = "release_date";
        final String JSON_POSTER = "poster_path";
        final String JSON_VOTEAVG = "vote_average";
        final String JSON_OVERVIEW = "overview";


        JSONObject jsonObject = new JSONObject(jsonString);

        JSONArray movieResults = jsonObject.getJSONArray(JSON_RESULTS);

        int moviesLength = movieResults.length();

        ArrayList<MovieItem> parsedMovies = new ArrayList<>(moviesLength);

        for (int i = 0; i < moviesLength; i++) {
            JSONObject movie = movieResults.getJSONObject(i);

            int id = movie.getInt(JSON_ID);
            String title = movie.getString(JSON_TITLE);
            String release = movie.getString(JSON_RELEASE);
            String poster = movie.getString(JSON_POSTER);
            double voteAvg = movie.getDouble(JSON_VOTEAVG);
            String overview = movie.getString(JSON_OVERVIEW);

            parsedMovies.add(new MovieItem(id, title, release, poster, voteAvg, overview));
        }


        return parsedMovies;
    }
}
