package com.newfobject.popularmovies.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.newfobject.popularmovies.R;
import com.newfobject.popularmovies.data.api.RestClient;
import com.newfobject.popularmovies.data.model.MovieItem;
import com.newfobject.popularmovies.events.FavoriteMasterEvent;
import com.newfobject.popularmovies.ui.adapter.ImageAdapter;
import com.newfobject.popularmovies.ui.listeners.EndlessScrollListener;
import com.newfobject.popularmovies.utils.Utility;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends BaseFragment {

    public static final String TAG = "movie_fragment";
    private static final String KEY_SAVE_STATE = "save_instance_key";
    private static final String KEY_PAGE = "page";

    private int mPage = 1;
    private String mSortMovies = null;
    private EndlessScrollListener mScrollListener;
    private RestClient mClient;
    private ImageAdapter mAdapter;

    public MoviesFragment() {
    }

    @Override
    public void restoreFragmentState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            List<MovieItem> items = savedInstanceState.getParcelableArrayList(KEY_SAVE_STATE);
            mAdapter.addToAdapter(items);
            mPage = savedInstanceState.getInt(KEY_PAGE);
            mScrollListener.setCurrentPage(mPage);
        } else {
            // load the first page from api
            loadDataFromApi(mPage, mSortMovies);
        }

        mRecyclerView.addOnScrollListener(mScrollListener);
    }

    private void loadDataFromApi(int page, String sortMovies) {
        Log.d(TAG, "load page: " + page);
        mClient.getMovies(page, sortMovies);
    }


    public void init(View view) {
        super.init(view);
        Context context = getContext();
        mAdapter = new ImageAdapter(context);
        mAdapter.setImageSize(Utility.getImageQualityPrefs(context));
        mAdapter.setAdult(Utility.isAdult(context));
        mRecyclerView.setAdapter(mAdapter);
        Bundle args = getArguments();
        mSortMovies = args.getString(TAG);
        mClient = RestClient.getInstance();
        mClient.setMoviesCallback(mAdapter);

        mScrollListener = new EndlessScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemCount) {
                mPage = page;
                loadDataFromApi(page, mSortMovies);
            }
        };
    }

    @Override
    public void onImageQualityPrefsChanged(String imageQualityPref) {
        mAdapter.setImageSize(imageQualityPref);
    }

    @Override
    public void onAdultPrefsChanged(boolean isAdult) {
        mAdapter.setAdult(isAdult);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_PAGE, mPage);
        Log.d(TAG, "onSaveInstanceState: " + mLayoutManager.findFirstVisibleItemPosition());
        outState.putParcelableArrayList(KEY_SAVE_STATE, (ArrayList<? extends Parcelable>) mAdapter.getItems());
        super.onSaveInstanceState(outState);
    }


    @Subscribe
    public void movieFavoriteFromDetail(FavoriteMasterEvent favoriteMasterEvent) {
        int position = favoriteMasterEvent.getAdapterPosition();
        boolean favorite = favoriteMasterEvent.isFavorite();
        boolean has_two_panes = getResources().getBoolean(R.bool.has_two_panes);
        mAdapter.movieFavoriteFromDetail(position, favorite, has_two_panes);
    }
}
