package com.newfobject.popularmovies.ui.listeners;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.newfobject.popularmovies.data.model.Trailer;

public class TrailerClickListener implements View.OnClickListener {
    public static final String BASE_URL = "https://www.youtube.com/watch?v=";
    private Trailer mTrailer;
    private Context mContext;

    public TrailerClickListener(Context context, Trailer trailer) {
        mContext = context;
        mTrailer = trailer;
    }

    @Override
    public void onClick(View v) {
        Intent trailerIntent = new Intent();
        trailerIntent.setAction(Intent.ACTION_VIEW);
        trailerIntent.setData(Uri.parse(BASE_URL + mTrailer.getKey()));
        mContext.startActivity(trailerIntent);
    }
}
