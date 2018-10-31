package com.haleysoftware.popularmovies.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haleysoftware.popularmovies.R;
import com.haleysoftware.popularmovies.objects.TrailerItem;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by haleysoft on 10/24/18.
 * <p>
 * The adapter used to take a list of trailer items and displays them.
 */
public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private final TrailerOnClickHandler clickHandler;
    private List<TrailerItem> trailerItems;
    private Context context;

    /**
     * Creates the adapter for trailer items.
     *
     * @param context      Activity context.
     * @param clickHandler Where the item clicks are handled.
     */
    public TrailerAdapter(Context context, TrailerOnClickHandler clickHandler) {
        this.context = context;
        this.clickHandler = clickHandler;
    }

    /**
     * Creates the view holder.
     *
     * @param parent   the parent view group.
     * @param viewType The type of the view. Not used.
     * @return The view holder.
     */
    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutItemId = R.layout.trailer_grid_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutItemId, parent, false);
        return new TrailerViewHolder(view);
    }

    /**
     * Binds the trailer data with the view.
     *
     * @param holder   The view holder to bind the data to.
     * @param position The position in the list for the data.
     */
    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        TrailerItem trailerItem = trailerItems.get(position);
        String trailerKey = trailerItem.getKey();
        String trailerImage = TrailerItem.YOUTUBE_IMAGE_START + trailerKey +
                TrailerItem.YOUTUBE_IMAGE_END;
        String name = trailerItem.getName();
        holder.titleText.setText(name);
        Picasso.get().load(trailerImage).into(holder.trailer);
    }

    /**
     * Returns the number of trailer items in the list.
     *
     * @return The number of trailer items.
     */
    @Override
    public int getItemCount() {
        if (trailerItems == null) return 0;
        return trailerItems.size();
    }

    /**
     * Changes the data in the trailer list.
     *
     * @param trailerItems The new list of trailer items.
     */
    public void setTrailerItems(List<TrailerItem> trailerItems) {
        this.trailerItems = trailerItems;
        notifyDataSetChanged();
    }

    /**
     * The interface to handle clicks on the items
     */
    public interface TrailerOnClickHandler {
        void onClick(TrailerItem currentTrailer);
    }

    /**
     * The inner class that holds the view data.
     */
    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_trailer)
        ImageView trailer;
        @BindView(R.id.tv_title_trailer)
        TextView titleText;
        @BindView(R.id.ll_trailer_holder)
        LinearLayout trailerHolder;

        /**
         * Sets up the view holder.
         *
         * @param itemView
         */
        public TrailerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            trailerHolder.setOnClickListener(this);
        }

        /**
         * Sets up the click data to pass to the click handler.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            if (clickHandler != null) {
                int adapterPosition = getAdapterPosition();
                clickHandler.onClick(trailerItems.get(adapterPosition));
            }
        }
    }
}
