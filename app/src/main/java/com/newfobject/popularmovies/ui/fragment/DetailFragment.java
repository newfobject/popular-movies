package com.newfobject.popularmovies.ui.fragment;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.newfobject.popularmovies.data.api.RestClient;
import com.newfobject.popularmovies.data.model.MovieItem;
import com.newfobject.popularmovies.data.model.Review;
import com.newfobject.popularmovies.data.model.Trailer;
import com.newfobject.popularmovies.events.FavoriteDetailEvent;
import com.newfobject.popularmovies.events.FavoriteMasterEvent;
import com.newfobject.popularmovies.ui.activity.DetailActivity;
import com.newfobject.popularmovies.ui.listeners.TrailerClickListener;
import com.newfobject.popularmovies.ui.views.FavoriteImageView;
import com.newfobject.popularmovies.utils.Utility;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment
        implements ObservableScrollViewCallbacks, RestClient.DetailsCallback {
    public static final String EXTRA_KEY_MOVIE_ITEM = "extra_key_movie_item";
    public static final String EXTRA_KEY_ITEM_POSITION = "item_position";
    public static final String EXTRA_KEY_URI = "extra_key_uri";
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
    private LinearLayout mReviews;
    private boolean mReviewsLoaded;
    private boolean mTrailersLoaded;
    private FavoriteImageView mFavoriteButton;
    private int mPosition;
    private MovieItem mMovieItem;


    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        init(view);

        getData(getActivity());
    }

    private void getData(FragmentActivity activity) {
        mMovieItem = (MovieItem) activity.getIntent()
                .getSerializableExtra(DetailActivity.MOVIE_DETAIL_TAG);

        if (mMovieItem == null) {
            Bundle bundle = this.getArguments();

            if (bundle != null) {
                mPosition = bundle.getInt(EXTRA_KEY_ITEM_POSITION);
                mMovieItem = (MovieItem) bundle.getSerializable(EXTRA_KEY_MOVIE_ITEM);
                bindData(activity);
            }

        } else {
            mPosition = activity.getIntent().getIntExtra(DetailActivity.MOVIE_POSITION_TAG, 0);
            bindData(activity);
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
        mReviews = (LinearLayout) view.findViewById(R.id.reviews);
        mFavoriteButton = (FavoriteImageView) view.findViewById(R.id.detailFavoriteView);


    }

    private void bindData(Context context) {

        String backdropPath = mMovieItem.getBackdropPath();
        String posterPath = mMovieItem.getPosterPath();

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
            mActionBar.setTitle(mMovieItem.getTitle());
        }

        mTvTitle.setText(mMovieItem.getTitle());
        mTvReleaseDate.setText(mMovieItem.getReleaseDate());
        mTvRating.setText(getResources().getString(R.string.average_rating, mMovieItem.getVoteAverage()));
        mTvOverview.setText(mMovieItem.getOverview());
        mBackdropAlpha = Utility.getScreenHeight(context) / 4;
        mFavoriteButton.setColorFilter(Color.RED);
        if (mMovieItem.isFavorite())
            mFavoriteButton.setChecked(true);

        RestClient restClient = RestClient.getInstance();
        restClient.setDetailsCallback(this);
        restClient.getTrailers(mMovieItem.getId());
        restClient.getReviews(mMovieItem.getId(), 1);

        mFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int movieId = mMovieItem.getId();
                if (!mMovieItem.isFavorite()) {
                    EventBus.getDefault().post(new FavoriteMasterEvent(mPosition, movieId, true));
                    EventBus.getDefault().post(new FavoriteDetailEvent(mPosition, movieId, true));
                } else {
                    mFavoriteButton.setChecked(true);
                    DeleteMovieDialogFragment dialogFragment = DeleteMovieDialogFragment
                            .newInstance(movieId, mPosition, false);
                    dialogFragment.show(getActivity().getSupportFragmentManager(), "delete_dialog");
                }
            }
        });

    }

    @Override
    public void onStart() {
        EventBus.getDefault().register(this);
        super.onStart();
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop: ");
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onFavoriteEvent(FavoriteDetailEvent favoriteDetailEvent) {
        if (mPosition == favoriteDetailEvent.getAdapterPosition()) {
            mFavoriteButton.setChecked(favoriteDetailEvent.isFavorite());
            mMovieItem.setFavorite(favoriteDetailEvent.isFavorite());
        }
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
            float alpha = Math.min(1, mBackdropAlpha / scrollY);
            mIvBackdrop.setAlpha(alpha);
            mIvBackdrop.setTranslationY(scrollY / 2);
    }

    @Override
    public void onDownMotionEvent() {

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
    public void onReadyTrailers(final List<Trailer> trailers) {
        final LayoutInflater inflater = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (mTrailersLoaded) {
            return;
        }
        mTrailersLoaded = true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (Trailer trailer : trailers) {
                    boolean hasTrailers = false;
                    if (trailer.getName().toLowerCase().contains("trailer")) {
                        hasTrailers = true;
                        final View view = inflater.inflate(R.layout.item_trailer, mTrailers, false);
                        TextView trailerTitle = (TextView) view.findViewById(R.id.trailer_title);
                        trailerTitle.setText(trailer.getName());
                        view.setOnClickListener(new TrailerClickListener(getContext(), trailer));
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mTrailers.addView(view);
                            }
                        });
                    }
                    if (hasTrailers) getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTrailers.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        }).start();
    }


    @Override
    public void onReadyReviews(final List<Review> reviews) {
        final LayoutInflater inflater = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (mReviewsLoaded) {
            return;
        }
        mReviewsLoaded = true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (Review review : reviews) {
                    final View view = inflater.inflate(R.layout.item_review, mReviews, false);
                    TextView authorView = (TextView) view.findViewById(R.id.review_author);
                    TextView contentView = (TextView) view.findViewById(R.id.review_content);
                    authorView.setText(review.getAuthor());
                    contentView.setText(review.getContent());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mReviews.addView(view);
                        }
                    });
                }
                if (!reviews.isEmpty()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mReviews.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        }).start();
    }
}