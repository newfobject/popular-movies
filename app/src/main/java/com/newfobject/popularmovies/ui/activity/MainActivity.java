package com.newfobject.popularmovies.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.newfobject.popularmovies.R;
import com.newfobject.popularmovies.data.model.MovieItem;
import com.newfobject.popularmovies.events.FavoriteDetailEvent;
import com.newfobject.popularmovies.events.FavoriteMasterEvent;
import com.newfobject.popularmovies.ui.adapter.ImageAdapter;
import com.newfobject.popularmovies.ui.fragment.DetailFragment;
import com.newfobject.popularmovies.ui.fragment.FavoritesFragment;
import com.newfobject.popularmovies.ui.fragment.MoviesFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends AppCompatActivity
        implements ImageAdapter.Callback, AdapterView.OnItemSelectedListener {
    public static final String KEY = "main_activity";
    public static final int RESULT_MOVIE_REQUEST = 1;
    boolean mFavorite;
    private int mPosition = -1;
    private int mMovieId;
    private boolean mTwoPane = true;
    private int mFavoriteId = 2;
    private int mCurrSpinnerPosition = 0;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY, mCurrSpinnerPosition);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolBar();

        if (findViewById(R.id.detail_container) == null) mTwoPane = false;

        if (savedInstanceState == null) {
            replaceFragment(mCurrSpinnerPosition);
        } else {
            mCurrSpinnerPosition = savedInstanceState.getInt(KEY);
        }

    }

    private void initToolBar() {
        Toolbar actionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        Spinner spinner = (Spinner) findViewById(R.id.nav_spinner);

        if (actionBarToolbar != null) {
            setSupportActionBar(actionBarToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.spinner_list, R.layout.spinner_drop_title);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_list);
        if (spinner != null) {
            spinner.setAdapter(adapter);
            spinner.setSelection(mCurrSpinnerPosition);
            spinner.setOnItemSelectedListener(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_settings) {

            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
            return true;
        }
        return false;
    }

    void replaceFragment(int id) {
        mCurrSpinnerPosition = id;
        String[] sortMode = getResources().getStringArray(R.array.entry_values);
        Bundle args = new Bundle();
        args.putString(MoviesFragment.TAG, sortMode[id]);

        Fragment fragment;
        if (mCurrSpinnerPosition != mFavoriteId) {
            fragment = new MoviesFragment();
        } else {
            fragment = new FavoritesFragment();
        }
        fragment.setArguments(args);

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.fragment_container, fragment)
                .commit();

        Fragment detailFragment = getSupportFragmentManager()
                .findFragmentById(R.id.detail_container);

        if (detailFragment != null)
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    .remove(detailFragment)
                    .commit();
    }

    @Override
    public void onMovieClick(Context context, MovieItem movieItem, int position) {
        if (!mTwoPane) {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra(DetailActivity.MOVIE_DETAIL_TAG, movieItem);
            intent.putExtra(DetailActivity.MOVIE_POSITION_TAG, position);
            startActivityForResult(intent, RESULT_MOVIE_REQUEST);
        } else {
            Bundle bundle = new Bundle();
            bundle.putSerializable(DetailFragment.EXTRA_KEY_MOVIE_ITEM, movieItem);
            bundle.putInt(DetailFragment.EXTRA_KEY_ITEM_POSITION, position);
            DetailFragment detailFragment = new DetailFragment();
            detailFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.detail_container, detailFragment)
                    .commit();
        }
    }

    // spinner item selected
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position != mCurrSpinnerPosition)
            replaceFragment(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    protected void onStart() {
        EventBus.getDefault().register(this);
        super.onStart();

    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onEventFromDetails(FavoriteMasterEvent favoriteMasterEvent) {
        if (mCurrSpinnerPosition == mFavoriteId) {
            Fragment favoriteFragment = getSupportFragmentManager()
                    .findFragmentById(R.id.detail_container);
            if (favoriteFragment != null)
                getSupportFragmentManager()
                        .beginTransaction()
                        .remove(favoriteFragment)
                        .commit();
        }
    }

    @Subscribe
    public void onDeleteFromMaster(FavoriteDetailEvent favoriteDetailEvent) {
        onEventFromDetails(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_MOVIE_REQUEST && data != null) {
            mPosition = data.getIntExtra(DetailActivity.EXTRA_POSITION, -1);
            mFavorite = data.getBooleanExtra(DetailActivity.EXTRA_FAVORITE, false);
            mMovieId = data.getIntExtra(DetailActivity.EXTRA_MOVIE_ID, -1);
        }
    }

    @Override
    protected void onResumeFragments() {
        if (!mTwoPane && mPosition != -1) {
            EventBus.getDefault().post(new FavoriteMasterEvent(mPosition, mMovieId, mFavorite));
        }
        super.onResumeFragments();
    }
}
