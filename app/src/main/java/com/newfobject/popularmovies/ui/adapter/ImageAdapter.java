package com.newfobject.popularmovies.ui.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.newfobject.popularmovies.R;
import com.newfobject.popularmovies.data.MovieItem;
import com.newfobject.popularmovies.data.RestClient;
import com.newfobject.popularmovies.ui.PaletteTransformation;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder>
        implements RestClient.OnReadyMoviesCallback {

    private static final String TAG = ImageAdapter.class.getSimpleName();
    private static final String BASE_URL = "http://image.tmdb.org/t/p/w342/";
    List<MovieItem> movies = new ArrayList<>();
    private Context mContext;
    private RecyclerView mRecyclerView;

    public ImageAdapter(Context context, RecyclerView rcv) {
        mContext = context;
        mRecyclerView = rcv;
    }

    public List<MovieItem> getItems() {
        return movies;
    }

    public void clear() {
        this.movies.clear();
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater =
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_movie, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = mRecyclerView.getChildAdapterPosition(v);
                if (pos >= 0 && pos < getItemCount()) {
                    ((Callback) mContext).onMovieClick(mContext, movies.get(pos));
                }
            }
        });

        return new ViewHolder(view);
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
                .placeholder(R.color.placeholder)
                .transform(PaletteTransformation.instance())
                .into(holder.imageView, new PaletteTransformation.PaletteCallback(holder.imageView) {

                    @Override
                    protected void onSuccess(int backgroundColor, int textColor) {
                        holder.title.setBackgroundColor(backgroundColor);
                        holder.titleView.setTextColor(textColor);
                        holder.ratingView.setTextColor(textColor);
                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }


    public void addToAdapter(List<MovieItem> movies) {
        this.movies.addAll(movies);
        notifyDataSetChanged();
    }

    @Override
    public void onReadyMovies(List<MovieItem> movies) {
        addToAdapter(movies);
    }


    public interface Callback {
        void onMovieClick(Context context, MovieItem movieItem);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleView;
        TextView ratingView;
        RelativeLayout title;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.movie_poster);
            titleView = (TextView) itemView.findViewById(R.id.title);
            title = (RelativeLayout) itemView.findViewById(R.id.movie_tag_title);
            ratingView = (TextView) itemView.findViewById(R.id.rating);
        }
    }
}
