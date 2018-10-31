package com.haleysoftware.popularmovies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.haleysoftware.popularmovies.objects.MovieItem;
import com.haleysoftware.popularmovies.utils.MovieAdapter;
import com.haleysoftware.popularmovies.utils.MovieLoader;
import com.haleysoftware.popularmovies.utils.NetworkHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Main activity for the app.
 * Pulls from the movie database and displays the posters in a grid format.
 */
public class PosterActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<MovieItem>>,
        MovieAdapter.MovieOnClickHandler {

    @BindView(R.id.gv_movies)
    RecyclerView movieGrid;
    @BindView(R.id.pb_movie_load)
    ProgressBar loadingCircle;
    @BindView(R.id.tv_error)
    TextView errorText;

    private SharedPreferences settings;
    private MovieAdapter movieAdapter;

    private int page = 1;

    /**
     * Calculates the number of columns of movie posters the phone can display
     *
     * @param context The context of the activity
     * @return The number of columns the RecycleView should display
     */
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns < 2 ? 2 : noOfColumns;
    }

    /**
     * Sets up the layout, preferences, recycleVeiw, adapter, and launches the data loader
     *
     * @param savedInstanceState received from system
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poster);
        ButterKnife.bind(this);

        settings = getPreferences(Context.MODE_PRIVATE);
        int query = settings.getInt(MovieItem.SORT_ID, 1);

        movieGrid.setLayoutManager(new GridLayoutManager(this, calculateNoOfColumns(this)));
        movieAdapter = new MovieAdapter(this, this);
        movieGrid.setAdapter(movieAdapter);

        if (query == NetworkHelper.FAV_SORT_ID) {
            getAllFavorites();
        } else if (NetworkHelper.hasInternet(this)) {
            getSupportLoaderManager().initLoader(MovieLoader.LOADER_ID, null, this);
        } else {
            errorText.setText(R.string.no_internet);
            errorText.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Option menu inflater
     *
     * @param menu Menu object from the system
     * @return True to let the system know to display the menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.grid_menu, menu);
        int query = settings.getInt(MovieItem.SORT_ID, 0);
        checkSort(query, menu);
        return true;
    }

    /**
     * Switches the query from popular to top movies using the options menu
     *
     * @param item Menu item object from the system
     * @return True to let the system that the click was handled
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences.Editor editor = settings.edit();
        switch (item.getItemId()) {
            case R.id.grid_top:
                editor.putInt(MovieItem.SORT_ID, NetworkHelper.VOTE_SORT_ID);
                editor.apply();
                invalidateOptionsMenu();
                getSupportLoaderManager().restartLoader(MovieLoader.LOADER_ID, null, this);
                break;
            case R.id.grid_pop:
                editor.putInt(MovieItem.SORT_ID, NetworkHelper.POP_SORT_ID);
                editor.apply();
                invalidateOptionsMenu();
                getSupportLoaderManager().restartLoader(MovieLoader.LOADER_ID, null, this);
                break;
            case R.id.grid_fav:
                editor.putInt(MovieItem.SORT_ID, NetworkHelper.FAV_SORT_ID);
                editor.apply();
                invalidateOptionsMenu();
                getAllFavorites();
                break;
        }
        return true;
    }

    /**
     * Changes the text on the action bar and hide the menu item depending on what the preference
     * is set to.
     *
     * @param query The current state of the preference for popular or top movies
     */
    private void checkSort(int query, Menu menu) {
        ActionBar actionBar = getSupportActionBar();
        switch (query) {
            default:
            case NetworkHelper.VOTE_SORT_ID:
                menu.findItem(R.id.grid_top).setVisible(false);
                menu.findItem(R.id.grid_pop).setVisible(true);
                menu.findItem(R.id.grid_fav).setVisible(true);
                if (actionBar != null) {
                    actionBar.setTitle(R.string.top_name);
                }
                break;
            case NetworkHelper.POP_SORT_ID:
                menu.findItem(R.id.grid_top).setVisible(true);
                menu.findItem(R.id.grid_pop).setVisible(false);
                menu.findItem(R.id.grid_fav).setVisible(true);
                if (actionBar != null) {
                    actionBar.setTitle(R.string.pop_name);
                }
                break;
            case NetworkHelper.FAV_SORT_ID:
                menu.findItem(R.id.grid_top).setVisible(true);
                menu.findItem(R.id.grid_pop).setVisible(true);
                menu.findItem(R.id.grid_fav).setVisible(false);
                if (actionBar != null) {
                    actionBar.setTitle(R.string.fav_name);
                }
                break;
        }
    }

    /**
     * Pulls all movie items from the database and updates if anything is changed.
     */
    private void getAllFavorites() {
        loadingCircle.setVisibility(View.GONE);
        PosterViewModel viewModel = ViewModelProviders.of(this).get(PosterViewModel.class);
        viewModel.getMovieItems().observe(this, new Observer<List<MovieItem>>() {
            @Override
            public void onChanged(@Nullable List<MovieItem> movieItems) {
                if (movieItems != null && movieItems.size() > 0) {
                    errorText.setVisibility(View.GONE);
                    movieAdapter.setMovieItemList(movieItems);
                } else {
                    errorText.setVisibility(View.VISIBLE);
                    errorText.setText(R.string.no_favorites);
                }
            }
        });
    }

    /**
     * Called by the system to Set up the data loader to download movie profiles.
     *
     * @param id   The system id to the loader. This is used to reconnect or restart the loader.
     * @param args Arguments to be used with the data loader.
     * @return The loader to be used to download data.
     */
    @NonNull
    @Override
    public AsyncTaskLoader<List<MovieItem>> onCreateLoader(int id, @Nullable Bundle args) {
        loadingCircle.setVisibility(View.VISIBLE);
        int query = settings.getInt(MovieItem.SORT_ID, 1);
        return new MovieLoader(this, query, page);
    }

    /**
     * This is called when the data is ready by the system. It sets the data to the adapter.
     *
     * @param loader     The loader that was started.
     * @param movieItems The list of movie items that was returned from the loader.
     */
    @Override
    public void onLoadFinished(@NonNull Loader<List<MovieItem>> loader, List<MovieItem> movieItems) {
        loadingCircle.setVisibility(View.GONE);
        if (movieItems != null && movieItems.size() > 0) {
            errorText.setVisibility(View.GONE);
            movieAdapter.setMovieItemList(movieItems);
        } else {
            errorText.setText(R.string.no_internet);
            errorText.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Called by the system when the loader is reset
     *
     * @param loader The loader that was reset
     */
    @Override
    public void onLoaderReset(@NonNull Loader loader) {
        loadingCircle.setVisibility(View.VISIBLE);
    }

    /**
     * An item in the grid view was clicked. This sets up the intent to the detail activity.
     *
     * @param currentMovie The movie item that was clicked.
     */
    @Override
    public void onClick(MovieItem currentMovie) {
        Intent intent = new Intent(this, MovieDetail.class);
        intent.putExtra(MovieItem.ITEM, currentMovie);

        startActivity(intent);
    }
}
