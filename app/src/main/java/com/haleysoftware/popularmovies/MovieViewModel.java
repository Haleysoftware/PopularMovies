package com.haleysoftware.popularmovies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.haleysoftware.popularmovies.database.MovieDatabase;
import com.haleysoftware.popularmovies.objects.MovieItem;

/**
 * The view model for getting the movie item from the database for the MovieDetail class.
 * <p>
 * Created by haleysoft on 10/30/18.
 */
public class MovieViewModel extends ViewModel {

    private LiveData<MovieItem> movieItem;

    /**
     * Gets the movie item from the favorites database.
     *
     * @param database The database to get the item from.
     * @param movieId  The id for the movie to get.
     */
    public MovieViewModel(MovieDatabase database, int movieId) {
        movieItem = database.movieDao().getOneFavMovie(movieId);
    }

    /**
     * The method used to get the movie item.
     *
     * @return The movie item from the database.
     */
    public LiveData<MovieItem> getMovieItem() {
        return movieItem;
    }
}
