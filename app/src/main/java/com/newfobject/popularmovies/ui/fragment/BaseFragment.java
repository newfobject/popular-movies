package com.newfobject.popularmovies.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.newfobject.popularmovies.R;
import com.newfobject.popularmovies.ui.PreCachingGridLayoutManager;
import com.newfobject.popularmovies.utils.Utility;

import org.greenrobot.eventbus.EventBus;

public abstract class BaseFragment extends Fragment {

    PreCachingGridLayoutManager mLayoutManager;
    RecyclerView mRecyclerView;
    private String mImageQualityPref;
    private boolean mAdult;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        init(view);

        restoreFragmentState(savedInstanceState);

        return view;
    }

    protected abstract void restoreFragmentState(Bundle savedInstanceState);

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.scrollToTop) {
            mRecyclerView.scrollToPosition(0);
            return true;
        }
        return false;
    }

    void init(View view) {
        Context context = getContext();
        mImageQualityPref = Utility.getImageQualityPrefs(context);
        mAdult = Utility.isAdult(context);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.movies_recyclerview);
        mLayoutManager = new PreCachingGridLayoutManager(context,
                context.getResources().getInteger(R.integer.columns_number));
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.setExtraLayoutSpace(Utility.getScreenHeight(context));
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        EventBus.getDefault().register(this);
        super.onStart();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onResume() {
        String newImageQuality = Utility.getImageQualityPrefs(getContext());
        if (!newImageQuality.equals(mImageQualityPref)) {
            mImageQualityPref = newImageQuality;
            onImageQualityPrefsChanged(newImageQuality);
        }
        boolean isAdult = Utility.isAdult(getContext());
        if (mAdult != isAdult) {
            mAdult = isAdult;
            onAdultPrefsChanged(isAdult);
        }
        super.onResume();
    }

    protected abstract void onImageQualityPrefsChanged(String imageQualityPref);

    protected abstract void onAdultPrefsChanged(boolean isAdult);
}
