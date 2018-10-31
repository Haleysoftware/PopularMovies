package com.haleysoftware.popularmovies.objects;

/**
 * The object used to hold trailer data.
 * <p>
 * Created by haleysoft on 10/21/18.
 */
public class TrailerItem {

    public static final String YOUTUBE_PATH = "https://www.youtube.com/watch?v=";

    public static final String YOUTUBE_IMAGE_START = "https://img.youtube.com/vi/";
    public static final String YOUTUBE_IMAGE_END = "/0.jpg";

    private String id;
    private String key;
    private String name;
    private String site;
    private String type;

    /**
     * Creates a new trailer item.
     *
     * @param id   The ID of the trailer.
     * @param key  The YouTube Key for the web address.
     * @param name The name of the trailer.
     * @param site The website of the video.
     * @param type The type of the video.
     */
    public TrailerItem(String id, String key, String name, String site, String type) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.site = site;
        this.type = type;
    }

    /**
     * Returns the ID of the trailer.
     *
     * @return The trailer's ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the web key of the trailer.
     *
     * @return The trailer's web key.
     */
    public String getKey() {
        return key;
    }

    /**
     * Returns the name of the trailer.
     *
     * @return the trailer's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the site of the video.
     *
     * @return The trailer's site or home.
     */
    public String getSite() {
        return site;
    }

    /**
     * Returns the type of the video.
     *
     * @return The video's type.
     */
    public String getType() {
        return type;
    }
}