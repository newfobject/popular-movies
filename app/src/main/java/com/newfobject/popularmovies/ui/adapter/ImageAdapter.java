package com.newfobject.popularmovies.ui.adapter;


import android.content.Context;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.newfobject.popularmovies.R;
import com.newfobject.popularmovies.data.api.RestClient;
import com.newfobject.popularmovies.data.model.MovieItem;
import com.newfobject.popularmovies.data.provider.MoviesContract;
import com.newfobject.popularmovies.events.FavoriteDetailEvent;
import com.newfobject.popularmovies.ui.PaletteTransformation;
import com.newfobject.popularmovies.ui.views.AspectKeepingImageView;
import com.newfobject.popularmovies.ui.views.FavoriteImageView;
import com.newfobject.popularmovies.utils.Utility;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder>
        implements RestClient.OnReadyMoviesCallback {

    private static final String TAG = "image_adapter";
    private static final String BASE_URL = "http://image.tmdb.org/t/p/w342/";
    private List<MovieItem> movies = new ArrayList<>();
    private Set<Integer> mFavoriteIdSet = new HashSet<>();
    private Context mContext;

    public ImageAdapter(Context context) {
        mContext = context;
        getFavoriteIdList();
    }

    private void getFavoriteIdList() {
        Cursor cursor = mContext.getContentResolver().query(MoviesContract.CONTENT_URI,
                new String[]{MoviesContract.MOVIE_ID},
                null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                mFavoriteIdSet.add(cursor.getInt(0));
            }
            cursor.close();
        }
    }

    public List<MovieItem> getItems() {
        return movies;
    }

    public void clear() {
        this.movies.clear();
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        LayoutInflater inflater =
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_movie, parent, false);

        return new ViewHolder(view, mContext, movies);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        String title = movies.get(position).getTitle();
        String rating = String.valueOf(movies.get(position).getVoteAverage());
        holder.titleView.setText(title);
        holder.ratingView.setText(rating);
        String imageUrl = BASE_URL + movies.get(position).getPosterPath();
        Picasso.with(mContext)
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

        if (movies.get(position).isFavorite()) {
            holder.favoriteView.setChecked(true);
        } else {
            holder.favoriteView.setChecked(false);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }


    public void addToAdapter(List<MovieItem> movies) {
        this.movies.addAll(movies);
        notifyItemRangeInserted(getItemCount(), movies.size());
    }

    @Override
    public void onReadyMovies(List<MovieItem> movies) {
        for (int i = 0; i < movies.size(); i++) {
            if (mFavoriteIdSet.contains(movies.get(i).getId())) {
                movies.get(i).setFavorite(true);
            }
        }
        addToAdapter(movies);
    }


    public void movieFavoriteFromDetail(int position, boolean favorite) {
        MovieItem movieItem = movies.get(position);
        if (favorite) {
            Utility.addMovieToFavorites(mContext, movieItem);
        } else {
            Utility.deleteFromFavorites(mContext, movieItem.getId());
        }
        movieItem.setFavorite(favorite);
        notifyItemChanged(position);
    }

    public interface Callback {
        void onMovieClick(Context context, MovieItem movieItem, int pos);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        AspectKeepingImageView imageView;
        TextView titleView;
        TextView ratingView;
        FavoriteImageView favoriteView;
        RelativeLayout title;

        public ViewHolder(View itemView, final Context context, final List<MovieItem> movies) {
            super(itemView);
            imageView = (AspectKeepingImageView) itemView.findViewById(R.id.movie_poster);
            titleView = (TextView) itemView.findViewById(R.id.title);
            title = (RelativeLayout) itemView.findViewById(R.id.movie_tag_title);
            ratingView = (TextView) itemView.findViewById(R.id.rating);
            favoriteView = (FavoriteImageView) itemView.findViewById(R.id.favoriteView);
            favoriteView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    MovieItem movieItem = movies.get(position);

                    if (movieItem.isFavorite()) {
                        Utility.deleteFromFavorites(context, movieItem.getId());
                        movieItem.setFavorite(false);
                        favoriteView.setChecked(false);
                    } else {
                        Utility.addMovieToFavorites(context, movieItem);
                        movieItem.setFavorite(true);
                        favoriteView.setChecked(true);
                    }
                    EventBus.getDefault().post(
                            new FavoriteDetailEvent(position, movieItem.getId(), movieItem.isFavorite()));
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    ((Callback) context).onMovieClick(context, movies.get(pos), pos);
                }
            });
        }
    }
}
