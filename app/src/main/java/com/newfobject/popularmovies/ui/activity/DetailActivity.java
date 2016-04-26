package com.newfobject.popularmovies.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.newfobject.popularmovies.R;
import com.newfobject.popularmovies.events.FavoriteMasterEvent;
import com.newfobject.popularmovies.ui.fragment.DetailFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class DetailActivity extends AppCompatActivity {
    public static final String MOVIE_DETAIL_TAG = "movie_detail";
    public static final String MOVIE_POSITION_TAG = "movie_position";
    public static final String EXTRA_POSITION = "position";
    public static final String EXTRA_FAVORITE = "favorite";
    public static final String EXTRA_MOVIE_ID = "movie_id";


    // If using two pane mode sActionBar remains null
    public static ActionBar sActionBar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        sActionBar = getSupportActionBar();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_container, new DetailFragment())
                    .commit();
        }
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    protected void onStart() {
        EventBus.getDefault().register(this);
        super.onStart();
    }

    @Subscribe
    public void onResult(FavoriteMasterEvent favoriteMasterEvent) {
        Log.d(DetailActivity.class.getSimpleName(), "onResult: ");
        Intent returnIntent = new Intent();
        returnIntent.putExtra(EXTRA_POSITION, favoriteMasterEvent.getAdapterPosition());
        returnIntent.putExtra(EXTRA_FAVORITE, favoriteMasterEvent.isFavorite());
        returnIntent.putExtra(EXTRA_MOVIE_ID, favoriteMasterEvent.getMovieId());
        setResult(RESULT_OK, returnIntent);
    }
}
