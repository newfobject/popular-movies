package com.newfobject.popularmovies.ui.fragment;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.newfobject.popularmovies.R;
import com.newfobject.popularmovies.TrailerClickListener;
import com.newfobject.popularmovies.data.MovieItem;
import com.newfobject.popularmovies.data.RestClient;
import com.newfobject.popularmovies.data.Trailer;
import com.newfobject.popularmovies.ui.activity.DetailActivity;
import com.newfobject.popularmovies.utils.Utility;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment
        implements ObservableScrollViewCallbacks, RestClient.OnReadyTrailersCallback {
    public static final String EXTRA_KEY = "extra_key";
    private static final String TAG = "detail_fragment";
    public static String BACKDROP_URL = "http://image.tmdb.org/t/p/w780/";
    private static String BASE_URL = "http://image.tmdb.org/t/p/w342/";
    private TextView mTvTitle;
    private TextView mTvReleaseDate;
    private TextView mTvRating;
    private TextView mTvOverview;
    private ImageView mIvBackdrop;
    private ImageView mIvPoster;
    private ActionBar mActionBar;
    private LinearLayout mTrailers;
    private float mBackdropAlpha;


    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        init(view);

        getData(getActivity());

        return view;
    }

    private void getData(FragmentActivity activity) {
        MovieItem movieItem = (MovieItem) activity.getIntent()
                .getSerializableExtra(MoviesFragment.MOVIE_DETAIL);

        if (movieItem == null) {
            Bundle bundle = this.getArguments();
            if (bundle != null) {
                bindData(activity, (MovieItem) bundle.getSerializable(EXTRA_KEY));
            }
        } else {
            bindData(activity, movieItem);
        }
    }

    private void init(View view) {

        mActionBar = DetailActivity.sActionBar;
        ObservableScrollView scrollView = (ObservableScrollView) view.findViewById(R.id.detail_list);
        scrollView.setScrollViewCallbacks(this);

        mTvTitle = (TextView) view.findViewById(R.id.tv_detail_title);
        mTvReleaseDate = (TextView) view.findViewById(R.id.tv_detail_releaseDate);
        mTvRating = (TextView) view.findViewById(R.id.tv_detail_rating);
        mTvOverview = (TextView) view.findViewById(R.id.tv_detail_overview);
        mIvBackdrop = (ImageView) view.findViewById(R.id.iv_detail_backdrop);
        mIvPoster = (ImageView) view.findViewById(R.id.iv_detail_poster);
        mTrailers = (LinearLayout) view.findViewById(R.id.trailers);
    }

    private void bindData(Context context, MovieItem movieItem) {

        String backdropPath = movieItem.getBackdropPath();
        String posterPath = movieItem.getPosterPath();

        if (backdropPath != null) {
            Picasso
                    .with(context)
                    .load(BACKDROP_URL.concat(backdropPath))
                    .priority(Picasso.Priority.HIGH)
                    .into(mIvBackdrop);
        }

        if (posterPath != null) {
            Picasso
                    .with(context)
                    .load(BASE_URL.concat(posterPath))
                    .into(mIvPoster);
        }

        if (mActionBar != null) {
            mActionBar.setTitle(movieItem.getTitle());
        }

        mTvTitle.setText(movieItem.getTitle());
        mTvReleaseDate.setText(movieItem.getReleaseDate());
        mTvRating.setText(getResources().getString(R.string.average_rating, movieItem.getVoteAverage()));
        mTvOverview.setText(movieItem.getOverview());
        mBackdropAlpha = Utility.getScreenHeight(context) / 4;

        RestClient restClient = RestClient.getInstance();
        restClient.setTrailersCallback(this);
        restClient.getTrailers(movieItem.getId());

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            float alpha = Math.min(1, mBackdropAlpha / scrollY);
            mIvBackdrop.setAlpha(alpha);
            mIvBackdrop.setTranslationY(scrollY / 2);
        }
    }

    @Override
    public void onDownMotionEvent() {
        //nothing here
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        if (mActionBar != null) {
            if (scrollState == ScrollState.UP) {
                if (mActionBar.isShowing()) {
                    mActionBar.hide();
                }
            } else if (scrollState == ScrollState.DOWN) {
                if (!mActionBar.isShowing()) {
                    mActionBar.show();
                }
            }
        }
    }

    @Override
    public void onReadyTrailers(List<Trailer> trailers) {
        LayoutInflater layoutInflater = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (Trailer trailer : trailers) {
            if (trailer.getName().toLowerCase().contains("trailer")) {
                View view = layoutInflater.inflate(R.layout.item_trailer, mTrailers, false);
                TextView trailerTitle = (TextView) view.findViewById(R.id.trailer_title);
                trailerTitle.setText(trailer.getName());
                view.setOnClickListener(new TrailerClickListener(getContext(), trailer));
                mTrailers.addView(view);
            }
        }
    }
}