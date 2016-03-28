package com.newfobject.popularmovies.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.newfobject.popularmovies.R;
import com.newfobject.popularmovies.data.MovieItem;
import com.newfobject.popularmovies.ui.adapter.ImageAdapter;
import com.newfobject.popularmovies.ui.fragment.DetailFragment;
import com.newfobject.popularmovies.ui.fragment.MoviesFragment;

public class MainActivity extends AppCompatActivity implements ImageAdapter.Callback {
    private boolean mTwoPane = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.detail_container) == null) {
            mTwoPane = false;
        }

        if (!mTwoPane) {
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new MoviesFragment())
                        .commit();
            }
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
        }
        return true;
    }

    @Override
    public void onMovieClick(Context context, MovieItem movieItem) {
        if (!mTwoPane) {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra(MoviesFragment.MOVIE_DETAIL, movieItem);
            context.startActivity(intent);
        } else {
            Bundle bundle = new Bundle();
            bundle.putSerializable(DetailFragment.ETRA_KEY, movieItem);
            DetailFragment detailFragment = new DetailFragment();
            detailFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_container, detailFragment)
                    .commit();
        }
    }
}
