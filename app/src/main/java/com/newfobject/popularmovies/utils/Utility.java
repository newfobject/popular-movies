package com.newfobject.popularmovies.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Build;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.WindowManager;

import com.newfobject.popularmovies.R;

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
}
