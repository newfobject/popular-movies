package com.newfobject.popularmovies.utils;


import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.WindowManager;

import com.newfobject.popularmovies.R;
import com.newfobject.popularmovies.data.model.MovieItem;
import com.newfobject.popularmovies.data.provider.MoviesContract;
import com.newfobject.popularmovies.data.provider.MoviesContract.Projections;

public class Utility {

    public static String getSortPrefs(Context context) {
        String sortMovies;
        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(context);
        sortMovies = mPref.getString(
                context.getResources().getString(R.string.pref_sort_key),
                context.getResources().getString(R.string.pref_sort_default)
        );
        return sortMovies;
    }

    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            display.getSize(size);
            return size.x;
        } else {
            return display.getWidth();
        }
    }

    public static int getScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            display.getSize(size);
            return size.y;
        } else {
            return display.getHeight();
        }
    }

    public static void addMovieToFavorites(Context context, MovieItem movieItem) {
        ContentValues values = new ContentValues();
        values.put(MoviesContract.MOVIE_ID, movieItem.getId());
        values.put(MoviesContract.TITLE, movieItem.getTitle());
        values.put(MoviesContract.OVERVIEW, movieItem.getOverview());
        values.put(MoviesContract.BACKDROP_PATH, movieItem.getBackdropPath());
        values.put(MoviesContract.ORIGINAL_LANGUAGE, movieItem.getOriginalLanguage());
        values.put(MoviesContract.ORIGINAL_TITLE, movieItem.getOriginalTitle());
        values.put(MoviesContract.POPULARITY, movieItem.getPopularity());
        values.put(MoviesContract.POSTER_PATH, movieItem.getPosterPath());
        values.put(MoviesContract.RELEASE_DATE, movieItem.getReleaseDate());
        values.put(MoviesContract.VOTE_AVERAGE, movieItem.getVoteAverage());
        values.put(MoviesContract.VOTE_COUNT, movieItem.getVoteCount());
        context.getContentResolver().insert(MoviesContract.CONTENT_URI, values);
    }

    public static MovieItem getMovieItemFromCursor(Context context, int movieId) {
        Uri uri = Uri.parse(MoviesContract.CONTENT_URI + "/" + movieId);
        Cursor cursor = context.getContentResolver().query(
                uri, Projections.MOVIE_DETAILS_PROJECTION, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            MovieItem movieItem = new MovieItem();
            movieItem.setFavorite(true);
            movieItem.setTitle(cursor.getString(Projections.ID_TITLE));
            movieItem.setId(cursor.getInt(Projections.ID));
            movieItem.setOverview(cursor.getString(Projections.ID_OVERVIEW));
            movieItem.setBackdropPath(cursor.getString(Projections.ID_BACKDROP_PATH));
            movieItem.setOriginalLanguage(cursor.getString(Projections.ID_ORIGINAL_LANGUAGE));
            movieItem.setOriginalTitle(cursor.getString(Projections.ID_ORIGINAL_TITLE));
            movieItem.setPopularity(cursor.getDouble(Projections.ID_POPULARITY));
            movieItem.setPosterPath(cursor.getString(Projections.ID_POSTER_PATH));
            movieItem.setReleaseDate(cursor.getString(Projections.ID_RELEASE_DATE));
            movieItem.setVoteAverage(cursor.getDouble(Projections.ID_VOTE_AVERAGE));
            movieItem.setVoteCount(cursor.getInt(Projections.ID_VOTE_COUNT));
            cursor.close();
            return movieItem;
        }
        return null;
    }

    public static boolean isFavorite(Context context, int movieId) {
        Uri uri = Uri.parse(MoviesContract.CONTENT_URI + "/" + movieId);
        Cursor cursor = context.getContentResolver().query(
                uri, Projections.BROWSE_MOVIES_PROJECTION, null, null, null);
        boolean isFavorite = false;
        if (cursor != null) {
            isFavorite = true;
            cursor.close();
        }
        return isFavorite;
    }

    public static void deleteFromFavorites(Context context, int movieID) {
        Uri uri = Uri.parse(MoviesContract.CONTENT_URI + "/" + movieID);
        context.getContentResolver().delete(uri, null, null);
    }

    public static String getImageQualityPrefs(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(context.getResources()
                .getString(R.string.pref_image_quality_key), "w342");
    }

    public static boolean isAdult(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(context.getResources()
                .getString(R.string.pref_adult_check_box_key), false);
    }


}
