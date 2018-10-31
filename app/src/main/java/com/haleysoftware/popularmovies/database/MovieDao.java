package com.haleysoftware.popularmovies.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.haleysoftware.popularmovies.objects.MovieItem;

import java.util.List;

/**
 * Dao used for Room
 * <p>
 * Created by haleysoft on 10/19/18.
 */
@Dao
public interface MovieDao {

    /**
     * Inserts a movie into the favorite movie database.
     *
     * @param movieItem The movie item to insert.
     */
    @Insert
    void insertMovie(MovieItem movieItem);

    /**
     * Receives a single movie item from the database.
     * Used to see if a movie is in the database or not.
     *
     * @param movieId The ID of the movie that might be in the database.
     * @return The movie item if found or null.
     */
    @Query("SELECT * FROM MovieItem WHERE movieID = :movieId")
    LiveData<MovieItem> getOneFavMovie(int movieId);

    /**
     * Receives all the movie items in the database.
     * Used for the favorite poster list.
     *
     * @return The list of movies.
     */
    @Query("SELECT * FROM MovieItem")
    LiveData<List<MovieItem>> getAllFavMovies();

    /**
     * Deletes a movie from the database when removed as a favorite.
     *
     * @param movieItem The movie item to delete.
     */
    @Delete
    void deleteMovie(MovieItem movieItem);
}
