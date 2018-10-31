package com.haleysoftware.popularmovies.utils;

import com.haleysoftware.popularmovies.objects.ReviewItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates a list of reviews from a JSON string.
 * <p>
 * Created by haleysoft on 10/22/18.
 */
public class ReviewJson {

    /**
     * Uses a passed JSON string to make a list of reviews.
     *
     * @param jsonString The JSON string to make the list from.
     * @return The list of review items.
     * @throws JSONException If something went wrong with the JSON.
     */
    static List<ReviewItem> getReviewsFromJson(String jsonString) throws JSONException {

        final String JSON_RESULTS = "results";

        final String JSON_ID = "id";
        final String JSON_AUTHOR = "author";
        final String JSON_CONTENT = "content";
        final String JSON_URL = "url";

        JSONObject jsonObject = new JSONObject(jsonString);

        JSONArray reviewResults = jsonObject.getJSONArray(JSON_RESULTS);

        int reviewLength = reviewResults.length();

        ArrayList<ReviewItem> parsedReviews = new ArrayList<>(reviewLength);

        for (int i = 0; i < reviewLength; i++) {
            JSONObject review = reviewResults.getJSONObject(i);

            String id = review.getString(JSON_ID);
            String author = review.getString(JSON_AUTHOR);
            String content = review.getString(JSON_CONTENT);
            String url = review.getString(JSON_URL);

            parsedReviews.add(new ReviewItem(id, author, content, url));
        }
        return parsedReviews;
    }
}
