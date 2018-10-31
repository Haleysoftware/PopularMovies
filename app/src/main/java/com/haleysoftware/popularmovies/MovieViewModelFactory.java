package com.haleysoftware.popularmovies;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.haleysoftware.popularmovies.database.MovieDatabase;

/**
 * The View Model Factory for the MovieDetail class.
 * <p>
 * Created by haleysoft on 10/30/18.
 */
public class MovieViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final MovieDatabase database;
    private final int movieId;

    /**
     * Creates the factory for view models.
     *
     * @param database The database used to get the movie item.
     * @param movieId  The ID of the movie.
     */
    public MovieViewModelFactory(MovieDatabase database, int movieId) {
        this.database = database;
        this.movieId = movieId;
    }

    /**
     * Creates the view model.
     *
     * @param modelClass the class used to create the view model.
     * @param <T>        Generic cast.
     * @return The view model used to get a movie item form the database.
     */
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MovieViewModel(database, movieId);
    }
}
