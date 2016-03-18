package com.newfobject.popularmovies.ui.fragment;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.newfobject.popularmovies.R;
import com.newfobject.popularmovies.data.MovieItem;
import com.newfobject.popularmovies.ui.activity.DetailActivity;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment
        implements ObservableScrollViewCallbacks {
    //TODO size of backdrop should depend on screen size
    public static String BACKDROP_URL = "http://image.tmdb.org/t/p/w780/";
    private static String BASE_URL = "http://image.tmdb.org/t/p/w342/";
    private TextView mTvTitle;
    private TextView mTvReleaseDate;
    private TextView mTvRating;
    private TextView mTvOverview;
    private ImageView mIvBackdrop;
    private ImageView mIvPoster;
    private ObservableScrollView mScrollView;
    private ActionBar mActionBar;


    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        init(view);
        bindData(getActivity());

        return view;
    }

    void init(View view) {

        mActionBar = DetailActivity.sActionBar;
        mScrollView = (ObservableScrollView) view.findViewById(R.id.detail_list);
        mScrollView.setScrollViewCallbacks(this);

        mTvTitle = (TextView) view.findViewById(R.id.tv_detail_title);
        mTvReleaseDate = (TextView) view.findViewById(R.id.tv_detail_releaseDate);
        mTvRating = (TextView) view.findViewById(R.id.tv_detail_rating);
        mTvOverview = (TextView) view.findViewById(R.id.tv_detail_overview);
        mIvBackdrop = (ImageView) view.findViewById(R.id.iv_detail_backdrop);
        mIvPoster = (ImageView) view.findViewById(R.id.iv_detail_poster);
    }

    void bindData(Context context) {

        MovieItem movieItem = (MovieItem) getActivity().getIntent()
                .getSerializableExtra(MoviesFragment.MOVIE_DETAIL);

        Picasso
                .with(context)
                .load(BACKDROP_URL.concat(movieItem.getBackdropPath()))
                .priority(Picasso.Priority.HIGH)
                .into(mIvBackdrop);

        Picasso
                .with(context)
                .load(BASE_URL.concat(movieItem.getPosterPath()))
                .priority(Picasso.Priority.LOW)
                .into(mIvPoster);

        mTvTitle.setText(movieItem.getTitle());
        mTvReleaseDate.setText(movieItem.getReleaseDate());
        mTvRating.setText(movieItem.getRating());
        mTvOverview.setText(movieItem.getOverview());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
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
}
