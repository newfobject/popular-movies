package com.newfobject.popularmovies.ui.fragment;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.newfobject.popularmovies.data.provider.MoviesContract;
import com.newfobject.popularmovies.events.FavoriteMasterEvent;
import com.newfobject.popularmovies.ui.adapter.CursorAdapter;

import org.greenrobot.eventbus.Subscribe;


public class FavoritesFragment extends BaseFragment
        implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String TAG = "favorite_fragment";
    private static final int MOVIES_LOADER = 0;
    private CursorAdapter mAdapter;

    public FavoritesFragment() {
    }


    @Override
    public void restoreFragmentState(Bundle savedInstanceState) {

    }

    @Override
    public void init(View view) {
        super.init(view);
        getLoaderManager().initLoader(MOVIES_LOADER, null, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getContext(),
                MoviesContract.CONTENT_URI,
                MoviesContract.Projections.BROWSE_MOVIES_PROJECTION, null, null, "_id DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (mAdapter == null) {
            mAdapter = new CursorAdapter(getContext(), data);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.changeCursor(data);
        }
    }


    @Override
    public void onLoaderReset(Loader loader) {

    }

    @Subscribe
    public void deleteMovie(FavoriteMasterEvent favoriteMasterEvent) {
        if (mAdapter != null)
            mAdapter.itemFavoriteFromDetails(favoriteMasterEvent.getAdapterPosition());
    }
}
