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
import com.newfobject.popularmovies.data.FetchMovies;
import com.newfobject.popularmovies.ui.EndlesssScrollListenter;
import com.newfobject.popularmovies.ui.PreCachingGridLayoutManager;
import com.newfobject.popularmovies.ui.adapter.ImageAdapter;
import com.newfobject.popularmovies.utils.Utility;


/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends Fragment {
    public static final String MOVIE_DETAIL = "moviedetail";

    private Context mContext = getContext();
    private RecyclerView mRcv;
    private int mPage = 1;
    private String mSortMovies;

    public MoviesFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getContext();
        mSortMovies = Utility.getSortPrefs(mContext);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        mRcv = (RecyclerView) view.findViewById(R.id.movies_recyclerview);

        mRcv.setAdapter(new ImageAdapter(mContext, mRcv));
        Log.d("log", "1");
        PreCachingGridLayoutManager layoutManager = new PreCachingGridLayoutManager(mContext, 2);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setExtraLayoutSpace(Utility.getScreenHeight(mContext));
        mRcv.setLayoutManager(layoutManager);
        // load first page from api
        loadDataFromApi(mPage, mRcv, mSortMovies);


        mRcv.addOnScrollListener(new EndlesssScrollListenter(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemCount) {
                Log.d("MoviesFragment", "OnLoadMore " + page);
                loadDataFromApi(page, mRcv, mSortMovies);
            }
        });

        return view;
    }

    private void loadDataFromApi(int page, RecyclerView recyclerView, String sortMovies) {
        new FetchMovies((ImageAdapter) recyclerView.getAdapter(), mContext)
                .execute(sortMovies, String.valueOf(page));
    }

    private void updateMovies() {
        String newSort = Utility.getSortPrefs(getContext());
        if (!mSortMovies.equals(newSort)) {
            mSortMovies = newSort;
            mRcv.setAdapter(new ImageAdapter(mContext, mRcv));
            mRcv.invalidate();
            loadDataFromApi(mPage, mRcv, mSortMovies);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateMovies();
    }
}
