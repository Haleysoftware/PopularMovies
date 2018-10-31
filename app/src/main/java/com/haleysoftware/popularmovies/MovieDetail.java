package com.haleysoftware.popularmovies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.haleysoftware.popularmovies.database.MovieDao;
import com.haleysoftware.popularmovies.database.MovieDatabase;
import com.haleysoftware.popularmovies.database.MovieExecutor;
import com.haleysoftware.popularmovies.objects.MovieItem;
import com.haleysoftware.popularmovies.objects.ReviewItem;
import com.haleysoftware.popularmovies.objects.TrailerItem;
import com.haleysoftware.popularmovies.utils.NetworkHelper;
import com.haleysoftware.popularmovies.utils.ReviewAdapter;
import com.haleysoftware.popularmovies.utils.ReviewLoader;
import com.haleysoftware.popularmovies.utils.TrailerAdapter;
import com.haleysoftware.popularmovies.utils.TrailerLoader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This activity is used to display the details of the movie object that was selected from the
 * grid of movie posters.
 * <p>
 * Created by haleysoft on 8/26/18.
 */
public class MovieDetail extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List>, TrailerAdapter.TrailerOnClickHandler {

    @BindView(R.id.tv_title_detail)
    TextView titleView;
    @BindView(R.id.iv_poster_detail)
    ImageView posterImage;
    @BindView(R.id.tv_released_detail)
    TextView releaseText;
    @BindView(R.id.tv_vote_detail)
    TextView voteText;
    @BindView(R.id.tv_overview_detail)
    TextView overviewText;
    @BindView(R.id.iv_favorite)
    ImageView favoriteImage;

    @BindView(R.id.textView_trailer)
    TextView trailerHead;
    @BindView(R.id.rv_trailer_detail)
    RecyclerView trailerView;
    @BindView(R.id.textView_review)
    TextView reviewHead;
    @BindView(R.id.rv_review_detail)
    RecyclerView reviewView;

    private MovieItem movieItem;
    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;
    private boolean isFavorite;

    /**
     * Creates the activity. Pulls and displays the movie data.
     *
     * @param savedInstanceState the starting state or one restored from a restart.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra(MovieItem.ITEM)) {
            movieItem = intent.getParcelableExtra(MovieItem.ITEM);
            displayData(movieItem);

            favoriteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    markFavorite();
                }
            });

            checkFavorite(movieItem.getMovieId());

            setupRecyclerViews();
        } else {
            finish();
        }
    }

    /**
     * Sets up the Trailer and Review views.
     */
    private void setupRecyclerViews() {
        trailerView.setNestedScrollingEnabled(false);
        trailerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        trailerAdapter = new TrailerAdapter(this, this);
        trailerView.setAdapter(trailerAdapter);

        reviewView.setNestedScrollingEnabled(false);
        reviewView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        reviewAdapter = new ReviewAdapter(this);
        reviewView.setAdapter(reviewAdapter);

        if (NetworkHelper.hasInternet(this)) {
            getSupportLoaderManager().initLoader(TrailerLoader.LOADER_ID, null, this);
            getSupportLoaderManager().initLoader(ReviewLoader.LOADER_ID, null, this);
        } else {
            trailerHead.setVisibility(View.GONE);
            trailerView.setVisibility(View.GONE);
            reviewHead.setVisibility(View.GONE);
            reviewView.setVisibility(View.GONE);
        }
    }

    /**
     * Loads the movie data into the views.
     */
    private void displayData(MovieItem movieItem) {
        String title = movieItem.getTitle();
        titleView.setText(title);

        String poster = movieItem.getPoster();
        if (poster != null) {
            String posterURL = MovieItem.POSTER_PATH + MovieItem.POSTER_SIZE + poster;
            int newHeight = 278 * 2;
            int newWidth = 185 * 2;
            Picasso.get().load(posterURL).resize(newWidth, newHeight).into(posterImage);
        } else {
            posterImage.setVisibility(View.GONE);
        }

        String release = movieItem.getRelease();
        if (release == null || release.equals("")) {
            release = getResources().getString(R.string.no_release);
        }
        releaseText.setText(release);

        double vote = movieItem.getVoteAverage();
        voteText.setText(String.valueOf(vote));

        String overview = movieItem.getOverview();
        if (overview == null || overview.equals("")) {
            overview = getResources().getString(R.string.no_overview);
        }
        overviewText.setText(overview);
    }


    /**
     * Sets up the View Model and live data for the database.
     * Checks if the movie item is in the favorite database or not and updates the UI as needed.
     *
     * @param movieId The ID of the movie to verify is a favorite.
     */
    private void checkFavorite(final int movieId) {
        MovieDatabase database = MovieDatabase.getDbInstance(this);
        MovieViewModelFactory factory = new MovieViewModelFactory(database, movieId);
        final MovieViewModel viewModel
                = ViewModelProviders.of(this, factory).get(MovieViewModel.class);
        viewModel.getMovieItem().observe(this, new Observer<MovieItem>() {
            @Override
            public void onChanged(@Nullable MovieItem movieItem) {
                if (movieItem == null) {
                    favoriteImage.setImageResource(R.drawable.ic_favorite_border_24dp);
                    isFavorite = false;
                } else {
                    favoriteImage.setImageResource(R.drawable.ic_favorite_24dp);
                    isFavorite = true;
                }
            }
        });
    }

    /**
     * Changes the favorite status in the database of the movie.
     */
    private void markFavorite() {
        final MovieDao database = MovieDatabase.getDbInstance(this).movieDao();
        if (isFavorite) {
            MovieExecutor.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    database.deleteMovie(movieItem);
                }
            });
        } else {
            MovieExecutor.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    database.insertMovie(movieItem);
                }
            });
        }
    }

    /**
     * The onClick for the YouTube Trailers
     *
     * @param currentTrailer The trailer item to open in YouTube.
     */
    @Override
    public void onClick(TrailerItem currentTrailer) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String trailerUrl = TrailerItem.YOUTUBE_PATH + currentTrailer.getKey();
        intent.setData(Uri.parse(trailerUrl));
        startActivity(intent);
    }

    /**
     * Creates the loader for downloading Trailers and Reviews for the current movie item.
     *
     * @param id   The ID of the current item.
     * @param args The arguments for the loader. Not currently used.
     * @return The new Trailer or Review loader.
     */
    @NonNull
    @Override
    public Loader onCreateLoader(int id, @Nullable Bundle args) {
        int movieId = movieItem.getMovieId();
        switch (id) {
            case TrailerLoader.LOADER_ID:
                return new TrailerLoader(this, movieId, 1);
            default:
            case ReviewLoader.LOADER_ID:
                return new ReviewLoader(this, movieId, 1);
        }
    }

    /**
     * Called when the loader is finished.
     *
     * @param loader The loader that finished.
     * @param data   The Trailer or Review data from the loader.
     */
    @Override
    public void onLoadFinished(@NonNull Loader<List> loader, List data) {
        switch (loader.getId()) {
            case TrailerLoader.LOADER_ID:
                if (data.size() == 0) {
                    trailerHead.setVisibility(View.GONE);
                    trailerView.setVisibility(View.GONE);
                } else {
                    trailerHead.setVisibility(View.VISIBLE);
                    trailerView.setVisibility(View.VISIBLE);
                    List<TrailerItem> trailerItems = new ArrayList<>();
                    for (Object item : data) {
                        trailerItems.add((TrailerItem) item);
                    }
                    trailerAdapter.setTrailerItems(trailerItems);
                }
                break;
            case ReviewLoader.LOADER_ID:
                if (data.size() == 0) {
                    reviewHead.setVisibility(View.GONE);
                    reviewView.setVisibility(View.GONE);
                } else {
                    reviewHead.setVisibility(View.VISIBLE);
                    reviewView.setVisibility(View.VISIBLE);
                    List<ReviewItem> reviewItems = new ArrayList<>();
                    for (Object item : data) {
                        reviewItems.add((ReviewItem) item);
                    }
                    reviewAdapter.setReviewItems(reviewItems);
                }
                break;
        }
    }

    /**
     * This is currently not being used here. It is ran when a loader is reset.
     *
     * @param loader The loader that was reset.
     */
    @Override
    public void onLoaderReset(@NonNull Loader loader) {
        //Do Nothing
    }
}