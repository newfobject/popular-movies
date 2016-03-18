package com.newfobject.popularmovies.ui.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.newfobject.popularmovies.R;
import com.newfobject.popularmovies.data.MovieItem;
import com.newfobject.popularmovies.ui.activity.DetailActivity;
import com.newfobject.popularmovies.ui.fragment.MoviesFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private static String BASE_URL = "http://image.tmdb.org/t/p/w342/";
    List<MovieItem> movies = new ArrayList<>();
    private Context mContext;
    private RecyclerView mRecyclerView;

    public ImageAdapter(Context context, RecyclerView rcv) {
        mContext = context;
        mRecyclerView = rcv;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater =
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_movie, parent, false);
        TextView tv = (TextView) view.findViewById(R.id.title);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = mRecyclerView.getChildAdapterPosition(v);
                if (pos >= 0 && pos < getItemCount()) {
                    Intent intent = new Intent(mContext, DetailActivity.class);
                    intent.putExtra(MoviesFragment.MOVIE_DETAIL, movies.get(pos));
                    mContext.startActivity(intent);
                }
            }
        });

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        String imagePath = movies.get(position).getPosterPath();
        String title = movies.get(position).getTitle();
        String genres;
        holder.titleView.setText(title);


        Picasso.with(mContext)
                .load(BASE_URL.concat(imagePath))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.imageView);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }


    public void addToAdapter(ArrayList<MovieItem> movies) {
        this.movies.addAll(movies);
        notifyDataSetChanged();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.movie_poster);
            titleView = (TextView) itemView.findViewById(R.id.title);
        }
    }
}
