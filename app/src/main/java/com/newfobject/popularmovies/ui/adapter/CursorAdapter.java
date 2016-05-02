package com.newfobject.popularmovies.ui.adapter;


import android.content.Context;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.newfobject.popularmovies.R;
import com.newfobject.popularmovies.data.model.MovieItem;
import com.newfobject.popularmovies.data.provider.MoviesContract;
import com.newfobject.popularmovies.ui.PaletteTransformation;
import com.newfobject.popularmovies.ui.fragment.DeleteMovieDialogFragment;
import com.newfobject.popularmovies.ui.views.AspectKeepingImageView;
import com.newfobject.popularmovies.ui.views.FavoriteImageView;
import com.newfobject.popularmovies.utils.Utility;
import com.squareup.picasso.Picasso;

public class CursorAdapter extends BaseCursorRecyclerViewAdapter<CursorAdapter.ViewHolder> {
    private static final String BASE_URL = "http://image.tmdb.org/t/p/";
    private final Context mContext;
    private String mImageSize;

    public CursorAdapter(Context context, Cursor cursor) {
        super(cursor);
        mContext = context;
    }

    public void setImageSize(String imageSize) {
        mImageSize = imageSize;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, Cursor cursor, int position) {

        String title = cursor.getString(MoviesContract.Projections.ID_TITLE);
        String rating = cursor.getString(MoviesContract.Projections.ID_VOTE_AVERAGE);
        holder.titleView.setText(title);
        holder.ratingView.setText(rating);
        setOnClickListeners(holder);
        String imageUrl = BASE_URL + mImageSize + cursor.getString(MoviesContract.Projections.ID_POSTER_PATH);
        Picasso.with(holder.itemView.getContext())
                .load(imageUrl)
                .transform(PaletteTransformation.instance())
                .placeholder(R.color.white)
                .into(holder.imageView, new PaletteTransformation.PaletteCallback(holder.imageView) {

                    @Override
                    protected void onSuccess(int backgroundColor, int textColor) {
                        holder.title.setBackgroundColor(backgroundColor);
                        holder.titleView.setTextColor(textColor);
                        holder.ratingView.setTextColor(textColor);
                        holder.favoriteView.setColorFilter(textColor, PorterDuff.Mode.MULTIPLY);

                    }

                    @Override
                    public void onError() {
                    }
                });
        holder.favoriteView.setChecked(true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater =
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_movie, parent, false);

        return new ViewHolder(view);
    }

    private void setOnClickListeners(final ViewHolder holder) {
        final Cursor cursor = getCursor();
        cursor.moveToPosition(holder.getAdapterPosition());
        final int movieId = cursor.getInt(MoviesContract.Projections.ID);
        holder.favoriteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.favoriteView.setChecked(true);
                DeleteMovieDialogFragment dialogFragment = DeleteMovieDialogFragment
                        .newInstance(movieId, holder.getAdapterPosition(), false);
                FragmentActivity context = (FragmentActivity) mContext;
                dialogFragment.show(context.getSupportFragmentManager(), "dialog");
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovieItem movieItem = Utility.getMovieItemFromCursor(mContext, movieId);
                ((ImageAdapter.Callback) mContext).onMovieClick(mContext,
                        movieItem, holder.getAdapterPosition());
            }
        });
    }

    public void itemFavoriteFromDetails(int position, boolean favorite) {
        if (favorite) return;
        Cursor cursor = getCursor();
        cursor.moveToPosition(position);
        Utility.deleteFromFavorites(mContext, cursor.getInt(MoviesContract.Projections.ID));
        notifyItemChanged(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        AspectKeepingImageView imageView;
        TextView titleView;
        TextView ratingView;
        RelativeLayout title;
        FavoriteImageView favoriteView;

        public ViewHolder(final View itemView) {
            super(itemView);
            imageView = (AspectKeepingImageView) itemView.findViewById(R.id.movie_poster);
            titleView = (TextView) itemView.findViewById(R.id.title);
            title = (RelativeLayout) itemView.findViewById(R.id.movie_tag_title);
            ratingView = (TextView) itemView.findViewById(R.id.rating);
            favoriteView = (FavoriteImageView) itemView.findViewById(R.id.favoriteView);
        }
    }
}
