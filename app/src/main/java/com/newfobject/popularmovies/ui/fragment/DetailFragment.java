package com.newfobject.popularmovies.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment
        implements ObservableScrollViewCallbacks, RestClient.DetailsCallback {
    public static final String EXTRA_KEY_MOVIE_ITEM = "extra_key_movie_item";
    public static final String EXTRA_KEY_ITEM_POSITION = "item_position";
    private static final String TAG = "detail_fragment";
    private static final String KEY_TRAILERS = "key_trailers";
    private static final String KEY_REVIEWS = "key_reviews";
    private static String BACKDROP_URL = "http://image.tmdb.org/t/p/w780/";
    private TextView mTvTitle;
    private TextView mTvReleaseDate;
    private TextView mTvRating;
    private TextView mTvOverview;
    private ImageView mIvBackdrop;
    private ImageView mIvPoster;
    private ImageView mPlayTrailer;
    private ActionBar mActionBar;
    private LinearLayout mTrailerLayout;
    private float mBackdropAlpha;
    private LinearLayout mReviewsLayout;
    private boolean mReviewsLoaded;
    private boolean mTrailersLoaded;
    private FavoriteImageView mFavoriteButton;
    private int mPosition;
    private MovieItem mMovieItem;
    private ShareActionProvider mShareActionProvider;
    private Trailer mTrailer;
    private List<Review> mReviewList;
    private List<Trailer> mTrailerList;

    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    public MovieItem getMovieItem() {
        return mMovieItem;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        init(view);
        Log.d(TAG, "onViewCreated: ");
        getData(getActivity(), savedInstanceState);
    }

    private void getData(FragmentActivity activity, Bundle savedInstanceState) {
        Bundle extras = activity.getIntent().getExtras();
        if (extras != null) mMovieItem = extras.getParcelable(DetailActivity.MOVIE_DETAIL_TAG);

        if (mMovieItem == null) {
            Bundle bundle = this.getArguments();

            if (bundle != null) {
                mPosition = bundle.getInt(EXTRA_KEY_ITEM_POSITION);
                mMovieItem = bundle.getParcelable(EXTRA_KEY_MOVIE_ITEM);
                bindData(activity, savedInstanceState);
            }

        } else {
            mPosition = activity.getIntent().getIntExtra(DetailActivity.MOVIE_POSITION_TAG, 0);
            bindData(activity, savedInstanceState);
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
        mTrailerLayout = (LinearLayout) view.findViewById(R.id.trailers);
        mReviewsLayout = (LinearLayout) view.findViewById(R.id.reviews);
        mFavoriteButton = (FavoriteImageView) view.findViewById(R.id.detailFavoriteView);
        mPlayTrailer = (ImageView) view.findViewById(R.id.play_trailer_detail);
    }

    private void bindData(Context context, Bundle savedInstanceState) {

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
            String BASE_URL = "http://image.tmdb.org/t/p/" + Utility.getImageQualityPrefs(context);
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
        if (savedInstanceState == null) {
            restClient.setDetailsCallback(this);
            restClient.getTrailers(mMovieItem.getId());
            restClient.getReviews(mMovieItem.getId(), 1);
        } else {
            mTrailerList = savedInstanceState.getParcelableArrayList(KEY_TRAILERS);
            onReadyTrailers(mTrailerList);
            mReviewList = savedInstanceState.getParcelableArrayList(KEY_REVIEWS);
            onReadyReviews(mReviewList);
            mTrailer = mTrailerList.get(0);
        }


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
    public void onResume() {
        EventBus.getDefault().register(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause: ");
        EventBus.getDefault().unregister(this);
        super.onPause();
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
        mPlayTrailer.setTranslationY(scrollY / 2);
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
        mTrailerList = trailers;
        final LayoutInflater inflater = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (mTrailersLoaded) {
            return;
        }
        mTrailersLoaded = true;


        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Runnable> runnableList = new ArrayList<>();
                boolean hasTrailers = false;
                for (Trailer trailer : trailers) {
                    if (trailer.getName().toLowerCase().contains("trailer")) {
                        if (!hasTrailers) mTrailer = trailer;
                        hasTrailers = true;
                        final View view = inflater.inflate(R.layout.item_trailer, mTrailerLayout, false);
                        TextView trailerTitle = (TextView) view.findViewById(R.id.trailer_title);
                        trailerTitle.setText(trailer.getName());
                        view.setOnClickListener(new TrailerClickListener(getContext(), trailer));
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                mTrailerLayout.addView(view);
                            }
                        };
                        runnableList.add(runnable);
                    }
                }
                addViewToFragment(runnableList, mTrailerLayout);

                if (mTrailer != null && mShareActionProvider != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mShareActionProvider.setShareIntent(createShareMovieIntent());
                            mPlayTrailer.setOnClickListener(new TrailerClickListener(getContext(), mTrailer));
                        }
                    });
                } else {
                    Log.d(TAG, "run: trailer null andor share null");
                }
            }
        }).start();
    }


    @Override
    public void onReadyReviews(final List<Review> reviews) {
        mReviewList = reviews;
        final LayoutInflater inflater = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (mReviewsLoaded) {
            return;
        }
        mReviewsLoaded = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Runnable> runnableList = new ArrayList<>();
                for (Review review : reviews) {
                    final View view = inflater.inflate(R.layout.item_review, mReviewsLayout, false);
                    TextView authorView = (TextView) view.findViewById(R.id.review_author);
                    TextView contentView = (TextView) view.findViewById(R.id.review_content);
                    authorView.setText(review.getAuthor());
                    contentView.setText(review.getContent());
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            mReviewsLayout.addView(view);
                        }
                    };
                    runnableList.add(runnable);
                }
                addViewToFragment(runnableList, mReviewsLayout);
            }
        }).start();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_details, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        if (mTrailer != null) {
            mShareActionProvider.setShareIntent(createShareMovieIntent());
            mPlayTrailer.setOnClickListener(new TrailerClickListener(getContext(), mTrailer));
        }
    }

    private Intent createShareMovieIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mMovieItem.getTitle() + " "
                + "https://www.youtube.com/watch?v=" + mTrailer.getKey());
        return shareIntent;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(KEY_TRAILERS, (ArrayList<? extends Parcelable>) mTrailerList);
        outState.putParcelableArrayList(KEY_REVIEWS, (ArrayList<? extends Parcelable>) mReviewList);
        super.onSaveInstanceState(outState);
    }

    private void addViewToFragment(ArrayList<Runnable> runnableList, final LinearLayout layout) {
        for (Runnable runnable : runnableList) {
            getActivity().runOnUiThread(runnable);
        }
        if (!runnableList.isEmpty()) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    layout.setVisibility(View.VISIBLE);
                }
            });
        }
    }
}