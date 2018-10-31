package com.haleysoftware.popularmovies.objects;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * This object is used to hold data for each movie.
 * <p>
 * Created by haleysoft on 8/26/18.
 */
@Entity
public class MovieItem implements Parcelable {

    public static final String SORT_ID = "grid_sorting";

    public static final String ITEM = "movieItem";

    public static final String POSTER_PATH = "https://image.tmdb.org/t/p/";
    public static final String POSTER_SIZE = "w185/";
    /**
     * Creates a new object from a parcel.
     */
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public MovieItem createFromParcel(Parcel in) {
            return new MovieItem(in);
        }

        /**
         * Creates a new array for movie items.
         *
         * @param size The size of the new array.
         * @return The new array that was created.
         */
        public MovieItem[] newArray(int size) {
            return new MovieItem[size];
        }
    };
    @PrimaryKey
    @NonNull
    private int movieId;
    private String release;
    private String poster;
    private double voteAverage;
    private String overview;
    @NonNull
    private String title;

    /**
     * Creates a movie object.
     *
     * @param movieId     The Movie's ID.
     * @param title       The movie's title.
     * @param release     The movie's release date.
     * @param poster      The URL ID for the movie poster on TheMovieDB.org.
     * @param voteAverage The average votes for the movie.
     * @param overview    The movie's overview.
     */
    public MovieItem(@NonNull int movieId, @NonNull String title, String release, String poster,
                     double voteAverage, String overview) {
        this.movieId = movieId;
        this.title = title;
        this.release = release;
        this.poster = poster;
        this.voteAverage = voteAverage;
        this.overview = overview;
    }

    /**
     * Fills the new movie item with data from a parcel.
     *
     * @param in The parcel that was passed.
     */
    public MovieItem(Parcel in) {
        this.movieId = in.readInt();
        this.title = in.readString();
        this.release = in.readString();
        this.poster = in.readString();
        this.voteAverage = in.readDouble();
        this.overview = in.readString();
    }

    /**
     * This is used when there is no poster for the movie item. It will create a blank poster
     * and insert the title.
     *
     * @param context The context of the activity used to get resources.
     * @param title   The movie's title to use for the blank poster.
     * @return returns the drawable for the poster.
     */
    static public BitmapDrawable noMoviePoster(Context context, String title) {
        Bitmap.Config config = Bitmap.Config.ARGB_8888;
        int height = 277 * 3;
        int width = 185 * 3;
        Bitmap newPoster = Bitmap.createBitmap(width, height, config);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        paint.setTextSize(50);

        Canvas canvas = new Canvas(newPoster);
        canvas.drawText(title, 5, newPoster.getHeight() / 2, paint);

        return new BitmapDrawable(context.getResources(), newPoster);

    }

    /**
     * Not used.
     *
     * @return Zero is sent.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Moves data from the object to a parcel.
     *
     * @param dest
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.movieId);
        dest.writeString(this.title);
        dest.writeString(this.release);
        dest.writeString(this.poster);
        dest.writeDouble(this.voteAverage);
        dest.writeString(this.overview);
    }

    /**
     * Returns the ID of the movie object.
     *
     * @return The Movie ID.
     */
    @NonNull
    public int getMovieId() {
        return movieId;
    }

    /**
     * Sets the movie ID. Needed for Room.
     *
     * @param movieId The new movie ID.
     */
    public void setMovieId(@NonNull int movieId) {
        this.movieId = movieId;
    }

    /**
     * Get the movie's title from this object.
     *
     * @return The movie's title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the movie's title.
     *
     * @param title The new title.
     */
    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    /**
     * Get the movie's release date from this object.
     *
     * @return The movie's release date.
     */
    public String getRelease() {
        return release;
    }

    /**
     * Sets the movie's release date.
     *
     * @param release the new release date.
     */
    public void setRelease(String release) {
        this.release = release;
    }

    /**
     * Get the path to the movie poster from this object.
     * The path will need to be appended to the end of the rest of the path and size.
     *
     * @return the movie path or null.
     */
    public String getPoster() {
        return poster;
    }

    /**
     * Sets the movie's poster path.
     *
     * @param poster The new poster path.
     */
    public void setPoster(String poster) {
        this.poster = poster;
    }

    /**
     * Get the vote average from this object.
     *
     * @return The average vote for this movie.
     */
    public double getVoteAverage() {
        return voteAverage;
    }

    /**
     * Sets the movie's vote average.
     *
     * @param voteAverage The new vote average.
     */
    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    /**
     * Get the overview from this object.
     *
     * @return the overview for this movie.
     */
    public String getOverview() {
        return overview;
    }

    /**
     * Sets the movie's overview.
     *
     * @param overview The new overview.
     */
    public void setOverview(String overview) {
        this.overview = overview;
    }
}
