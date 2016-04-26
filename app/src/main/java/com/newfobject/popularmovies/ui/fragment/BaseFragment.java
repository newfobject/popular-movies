package com.newfobject.popularmovies.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        init(view);

        restoreFragmentState(savedInstanceState);

        return view;
    }


    public abstract void restoreFragmentState(Bundle savedInstanceState);


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.scrollToTop) {
            mRecyclerView.scrollToPosition(0);
            return true;
        }
        return false;
    }

    public void init(View view) {
        Context context = getContext();
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
        Log.d("base frag", "onStart: ");
        EventBus.getDefault().register(this);
        super.onStart();
    }

    @Override
    public void onStop() {
        Log.d("base frag", "onStop: ");
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}
