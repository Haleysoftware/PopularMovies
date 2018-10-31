package com.haleysoftware.popularmovies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.haleysoftware.popularmovies.database.MovieDatabase;
import com.haleysoftware.popularmovies.objects.MovieItem;

import java.util.List;

/**
 * The view model used to get the list of movies from the database.
 * <p>
 * Created by haleysoft on 10/30/18.
 */
public class PosterViewModel extends AndroidViewModel {

    private LiveData<List<MovieItem>> movieItems;

    /**
     * Creates the view model to get movies from the database.
     *
     * @param application The calling Activity
     */
    public PosterViewModel(@NonNull Application application) {
        super(application);
        MovieDatabase database = MovieDatabase.getDbInstance(this.getApplication());
        movieItems = database.movieDao().getAllFavMovies();
    }

    /**
     * Used to get all movies from the database.
     *
     * @return The list of movie items.
     */
    public LiveData<List<MovieItem>> getMovieItems() {
        return movieItems;
    }
}
