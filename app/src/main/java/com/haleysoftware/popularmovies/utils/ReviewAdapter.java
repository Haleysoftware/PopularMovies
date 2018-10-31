package com.haleysoftware.popularmovies.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haleysoftware.popularmovies.R;
import com.haleysoftware.popularmovies.objects.ReviewItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * The adapter for the review list.
 * <p>
 * Created by haleysoft on 10/24/18.
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<ReviewItem> reviewItems;
    private Context context;

    /**
     * Creates the adapter.
     *
     * @param context The activity context.
     */
    public ReviewAdapter(Context context) {
        this.context = context;
    }

    /**
     * Creates the view holder.
     *
     * @param parent   The view parent.
     * @param viewType The view type.
     * @return The view holder.
     */
    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutItemId = R.layout.review_grid_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutItemId, parent, false);
        return new ReviewViewHolder(view);
    }

    /**
     * Binds the data to the views.
     *
     * @param holder   The view holder.
     * @param position The position of the view and item.
     */
    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        ReviewItem reviewItem = reviewItems.get(position);
        String contentText = reviewItem.getContent();
        String authorText = reviewItem.getAuthor();

        holder.content.setText(contentText);
        holder.author.setText(authorText);
    }

    /**
     * Returns the number of review items.
     *
     * @return the count of reviews.
     */
    @Override
    public int getItemCount() {
        if (reviewItems == null) return 0;
        return reviewItems.size();
    }

    /**
     * Sets the list of review items.
     *
     * @param reviewItems New list of review items.
     */
    public void setReviewItems(List<ReviewItem> reviewItems) {
        this.reviewItems = reviewItems;
        notifyDataSetChanged();
    }

    /**
     * Holds the views for the review items.
     */
    class ReviewViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_content_review)
        TextView content;
        @BindView(R.id.tv_author_review)
        TextView author;

        /**
         * Creates the holder for the review views.
         *
         * @param itemView The view item.
         */
        public ReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
