package com.haleysoftware.popularmovies.objects;

/**
 * The object used to hold review data.
 * <p>
 * Created by haleysoft on 10/21/18.
 */
public class ReviewItem {

    private String id;
    private String author;
    private String content;
    private String url;

    /**
     * Creates the new review item.
     *
     * @param id      The review ID.
     * @param author  The review author.
     * @param content The review content.
     * @param url     The review URL.
     */
    public ReviewItem(String id, String author, String content, String url) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.url = url;
    }

    /**
     * Returns the review's ID
     *
     * @return The ID of the review.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the review's author.
     *
     * @return The author of the review.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Returns the review's content.
     *
     * @return The content of the review.
     */
    public String getContent() {
        return content;
    }

    /**
     * Returns the review's URL.
     *
     * @return The URL of the review.
     */
    public String getUrl() {
        return url;
    }
}
