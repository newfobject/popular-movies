package com.newfobject.popularmovies.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.newfobject.popularmovies.R;
import com.newfobject.popularmovies.ui.fragment.DetailFragment;

public class DetailActivity extends AppCompatActivity {

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
}
