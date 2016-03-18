package com.newfobject.popularmovies.data;


import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.newfobject.popularmovies.BuildConfig;
import com.newfobject.popularmovies.ui.adapter.ImageAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class FetchMovies extends AsyncTask<String, Void, ArrayList<MovieItem>> {
    // TODO instead of this class use Retrofit
    public static final String POSTER_PATH = "poster_path";
    final String MOVIES_LIST = "results";
    final String ID = "id";
    final String ADULT = "adult";
    final String OVERVIEW = "overview";
    final String RELEASE_DATE = "release_date";
    final String GENRE_IDS = "genres";
    final String ORIGINAL_TITLE = "original_title";
    final String ORIGINAL_LANGUAGE = "original_language";
    final String TITLE = "title";
    final String BACKDROP_PATH = "backdrop_path";
    final String POPULARITY = "popularity";
    final String VOTES = "vote_count";
    final String VIDEO = "video";
    final String AVERAGE_VOTE = "vote_average";
    private final String LOG_TAG = FetchMovies.class.getSimpleName();
    ImageAdapter mImageAdapter;
    private Context mContext;

    public FetchMovies(ImageAdapter imageAdapter, Context context) {
        mImageAdapter = imageAdapter;
        mContext = context;
    }

    @Override
    protected ArrayList<MovieItem> doInBackground(String... params) {
        if (params.length == 0) {
            return null;
        }
        // Sort request(by popularity or by highest rating)
        String sortby = params[0];
        // Requested page (from 1 to 1000)
        String page = params[1];
        HttpURLConnection urlConnection = null;
        BufferedReader bufferedReader = null;

        String moviesjsonStr = null;

        // API KEY defined in app/buildGradle
        final String API_KEY_PARAM = "api_key";
        final String TMDB_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
        final String SORT_BY = "sort_by";
        final String PAGE = "page";

        final String THUMB_SIZES[] = {
                "w92", "w154", "w185",
                "w342", "w500", "w780",
                "original"};

        Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                .appendQueryParameter(SORT_BY, sortby)
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.MOVIE_API_KEY)
                .appendQueryParameter(PAGE, page)
                .build();
        try {
            URL url = new URL(builtUri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                return null;
            }
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                buffer.append(line).append("\n");
            }
            if (buffer.length() == 0) {
                return null;
            }
            moviesjsonStr = buffer.toString();

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error: ", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        try {
            return getMoviesFromJson(moviesjsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error json convert", e);

        }
        Log.e(LOG_TAG, moviesjsonStr);

        return null;
    }

    private ArrayList<MovieItem> getMoviesFromJson(String moviesjsonStr)
            throws JSONException {

        if (moviesjsonStr == null) {
            return null;
        }
        JSONObject object = new JSONObject(moviesjsonStr);
        JSONArray jsonArray = object.getJSONArray(MOVIES_LIST);
        ArrayList<MovieItem> moviesList = new ArrayList<>();
        MovieItem movieItem;

        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject moviesObject = jsonArray.getJSONObject(i);
            movieItem = new MovieItem(
                    moviesObject.getInt(ID),
                    moviesObject.getString(POSTER_PATH),
                    moviesObject.getBoolean(ADULT),
                    moviesObject.getString(OVERVIEW),
                    moviesObject.getString(RELEASE_DATE),
                    moviesObject.getString(ORIGINAL_TITLE),
                    moviesObject.getString(ORIGINAL_LANGUAGE),
                    moviesObject.getString(TITLE),
                    moviesObject.getString(BACKDROP_PATH),
                    moviesObject.getString(POPULARITY),
                    moviesObject.getString(VOTES),
                    moviesObject.getString(VIDEO),
                    moviesObject.getString(AVERAGE_VOTE)
            );
            moviesList.add(movieItem);

        }
        return moviesList;
    }

    @Override
    protected void onPostExecute(ArrayList<MovieItem> movieItems) {
        if (movieItems != null && mImageAdapter != null) {
            mImageAdapter.addToAdapter(movieItems);
        } else {
            Toast.makeText(
                    mContext,
                    "Please make sure you have a valid internet connection",
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
