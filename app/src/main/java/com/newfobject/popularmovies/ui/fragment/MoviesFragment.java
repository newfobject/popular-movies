package com.newfobject.popularmovies.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.newfobject.popularmovies.R;
import com.newfobject.popularmovies.data.MovieItem;
import com.newfobject.popularmovies.data.RestClient;
import com.newfobject.popularmovies.ui.EndlessScrollListener;
import com.newfobject.popularmovies.ui.PreCachingGridLayoutManager;
import com.newfobject.popularmovies.ui.adapter.ImageAdapter;
import com.newfobject.popularmovies.utils.Utility;

import java.io.Serializable;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends Fragment {

    public static final String MOVIE_DETAIL = "movie_detail";
    public static final String TAG = "movie_fragment";
    public static final String KEY_SAVE_STATE = "save_instance_key";
    public static final String KEY_PAGE = "page";

    private ImageAdapter mAdapter;
    private int mPage = 1;
    private String mSortMovies;
    private PreCachingGridLayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private EndlessScrollListener mScrollListener;
    private RestClient mClient;

    public MoviesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        init(view);


        if (savedInstanceState != null) {
            List<MovieItem> items = (List<MovieItem>) savedInstanceState.getSerializable(KEY_SAVE_STATE);
            mAdapter.addToAdapter(items);
            mPage = savedInstanceState.getInt(KEY_PAGE);
            mScrollListener.setCurrentPage(mPage);
        } else {
            // load the first page from api
            loadDataFromApi(mPage, mSortMovies);
        }

        mRecyclerView.addOnScrollListener(mScrollListener);
        return view;
    }

    private void loadDataFromApi(int page, String sortMovies) {
        Log.d(TAG, "load page: " + page);
        mClient.getMovies(page, sortMovies);
    }

    private void updateMovies() {
        String newSort = Utility.getSortPrefs(getContext());
        if (!mSortMovies.equals(newSort)) {
            mSortMovies = newSort;
            mAdapter.clear();
            mPage = 1;
            loadDataFromApi(mPage, mSortMovies);
        }
    }

    private void init(View view) {
        Context context = getContext();
        mSortMovies = Utility.getSortPrefs(context);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.movies_recyclerview);
        mAdapter = new ImageAdapter(context, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);

        mLayoutManager = new PreCachingGridLayoutManager(context,
                context.getResources().getInteger(R.integer.columns_number));
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.setExtraLayoutSpace(Utility.getScreenHeight(context));

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mClient = RestClient.getInstance();
        mClient.setCallback(mAdapter);

        mScrollListener = new EndlessScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemCount) {
                mPage = page;
                loadDataFromApi(page, mSortMovies);
            }
        };
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: " + mLayoutManager.findFirstVisibleItemPosition());
        outState.putSerializable(KEY_SAVE_STATE, (Serializable) mAdapter.getItems());
        outState.putInt(KEY_PAGE, mPage);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateMovies();
    }
}
