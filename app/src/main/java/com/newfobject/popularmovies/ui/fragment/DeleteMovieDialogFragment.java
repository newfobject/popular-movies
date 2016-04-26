package com.newfobject.popularmovies.ui.fragment;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.newfobject.popularmovies.R;
import com.newfobject.popularmovies.events.FavoriteDetailEvent;
import com.newfobject.popularmovies.events.FavoriteMasterEvent;

import org.greenrobot.eventbus.EventBus;

public class DeleteMovieDialogFragment extends DialogFragment {
    private static final String MOVIE_ID_TAG = "movie_id";
    private static final String POSITION_TAG = "position";
    private static final String FAVORITE_TAG = "favorite";

    public static DeleteMovieDialogFragment newInstance(int movieId, int position, boolean favorite) {
        DeleteMovieDialogFragment dialogFragment = new DeleteMovieDialogFragment();
        Bundle args = new Bundle();
        args.putInt(MOVIE_ID_TAG, movieId);
        args.putInt(POSITION_TAG, position);
        args.putBoolean(FAVORITE_TAG, favorite);
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        final int position = args.getInt(POSITION_TAG);
        final int id = args.getInt(MOVIE_ID_TAG);
        final boolean favorite = args.getBoolean(FAVORITE_TAG);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.delete_movie_from_favorites)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do work here
                        EventBus.getDefault().post(new FavoriteMasterEvent(position, id, favorite));
                        EventBus.getDefault().post(new FavoriteDetailEvent(position, id, favorite));

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        return builder.create();

    }
}
