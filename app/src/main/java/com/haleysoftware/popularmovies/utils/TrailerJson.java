package com.haleysoftware.popularmovies.utils;

import com.haleysoftware.popularmovies.objects.TrailerItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haleysoft on 10/22/18.
 * <p>
 * Parse the JSON data for a movie's Trailers.
 */
public class TrailerJson {

    /**
     * Takes the JSON string and makes a list of Trailer items.
     *
     * @param jsonString The JSON string received from the internet.
     * @return The list of Trailer items.
     * @throws JSONException If there are any issues with the JSON string.
     */
    static List<TrailerItem> getTrailersFromJson(String jsonString) throws JSONException {

        final String JSON_RESULTS = "results";

        final String JSON_ID = "id";
        final String JSON_KEY = "key"; //Used for YouTube path
        final String JSON_NAME = "name";
        final String JSON_SITE = "site"; //Like it to be "YouTube"
        final String JSON_TYPE = "type"; //Needs to be "Trailer"

        JSONObject jsonObject = new JSONObject(jsonString);

        JSONArray trailerResults = jsonObject.getJSONArray(JSON_RESULTS);

        int trailersLength = trailerResults.length();

        ArrayList<TrailerItem> parsedTrailers = new ArrayList<>(trailersLength);

        for (int i = 0; i < trailersLength; i++) {
            JSONObject trailer = trailerResults.getJSONObject(i);

            String id = trailer.getString(JSON_ID);
            String key = trailer.getString(JSON_KEY);
            String name = trailer.getString(JSON_NAME);
            String site = trailer.getString(JSON_SITE);
            String type = trailer.getString(JSON_TYPE);

            if (type.equals("Trailer") && site.equals("YouTube")) {
                parsedTrailers.add(new TrailerItem(id, key, name, site, type));
            }
        }
        return parsedTrailers;
    }
}
