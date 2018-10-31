package com.haleysoftware.popularmovies.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.haleysoftware.popularmovies.R;
import com.haleysoftware.popularmovies.objects.MovieItem;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * The adapter used to load the list of movies to the RecyclerView grid.
 * <p>
 * Created by haleysoft on 9/1/18.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private final MovieOnClickHandler clickHandler;
    private List<MovieItem> movieItemList;
    private Context context;

    /**
     * Creates the adapter. The data is loaded using a method.
     *
     * @param context      The context of the activity.
     * @param clickHandler The class that will handle the item clicks.
     */
    public MovieAdapter(Context context, MovieOnClickHandler clickHandler) {
        this.context = context;
        this.clickHandler = clickHandler;
    }

    /**
     * Called by the adapter to create the grid items.
     *
     * @param parent   The parent view for the grid.
     * @param viewType This is not used but can change the type of layout item to use.
     * @return The view for the grid item.
     */
    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutItemId = R.layout.poster_grid_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutItemId, parent, false);
        return new MovieViewHolder(view);
    }

    /**
     * Attaches the movie item data to the view item.
     *
     * @param holder   The holder to bind the data to.
     * @param position The position the holder is in on the grid
     */
    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        MovieItem movieItem = movieItemList.get(position);
        String movieURL = MovieItem.POSTER_PATH + MovieItem.POSTER_SIZE + movieItem.getPoster();

        int newHeight = 278 * 3;
        int newWidth = 185 * 3;

        Drawable error = MovieItem.noMoviePoster(context, movieItem.getTitle());
        Picasso.get().load(movieURL).error(error).resize(newWidth, newHeight).into(holder.imageView);
    }

    /**
     * Gets the number of items in the movie list so the adapter knows how many view holders to
     * create.
     *
     * @return The count of items in the list.
     */
    @Override
    public int getItemCount() {
        if (movieItemList == null) return 0;
        return movieItemList.size();
    }

    /**
     * Sets the data and tells the adapter that the data has changed.
     *
     * @param movieData the list of movies the adapter will use to display.
     */
    public void setMovieItemList(List<MovieItem> movieData) {
        this.movieItemList = movieData;
        notifyDataSetChanged();
    }

    /**
     * The interface to handle clicks on the items
     */
    public interface MovieOnClickHandler {
        void onClick(MovieItem currentMovie);
    }

    /**
     * Inner class that is used to hold a link to the views and handle the clicks in the grid items.
     */
    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_movie)
        ImageView imageView;

        /**
         * The view holder that keeps a link to the view items used in the grid.
         *
         * @param itemView The parent view for the grid item.
         */
        MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            imageView.setOnClickListener(this);
        }

        /**
         * Passes the correct movie item to the click handler
         *
         * @param view the view that was touched.
         */
        @Override
        public void onClick(View view) {
            if (clickHandler != null) {
                int adapterPosition = getAdapterPosition();
                clickHandler.onClick(movieItemList.get(adapterPosition));
            }
        }
    }
}
