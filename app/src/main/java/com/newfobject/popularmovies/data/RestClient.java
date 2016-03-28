package com.newfobject.popularmovies.data;


import android.util.Log;

import com.newfobject.popularmovies.BuildConfig;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {

    private static final String BASE_URL = "http://api.themoviedb.org/3/";
    private static final String TAG = "REST_client";
    private static RestClient instance = null;
    private Model model = null;
    private OnReadyMoviesCallback callback;
    private APIService service;

    public RestClient() {

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();
        service = retrofit.create(APIService.class);
    }

    public static RestClient getInstance() {
        if (instance == null) {
            instance = new RestClient();
        }
        return instance;
    }

    public void getMovies(int page, String sortSetting) {
        final Call<Model> movies = service.movies(
                BuildConfig.MOVIE_API_KEY,
                sortSetting,
                page);
        movies.enqueue(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                if (response.isSuccessful()) {
                    callback.onReadyMovies(response.body().getMovieItems());
                    Log.e(TAG, "onResponse: " + response.message());
                } else {
                    Log.e(TAG, "onResponse: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    public void setCallback(OnReadyMoviesCallback callback) {
        this.callback = callback;
    }

    public interface OnReadyMoviesCallback {
        void onReadyMovies(List<MovieItem> movies);
    }

}
