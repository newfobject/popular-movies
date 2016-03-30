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
    private OnReadyMoviesCallback moviesCallback;
    private OnReadyTrailersCallback trailersCallback;
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
        final Call<MovieItem.Response> movies = service.movies(
                BuildConfig.MOVIE_API_KEY,
                sortSetting,
                page);
        movies.enqueue(new Callback<MovieItem.Response>() {
            @Override
            public void onResponse(Call<MovieItem.Response> call, Response<MovieItem.Response> response) {
                if (response.isSuccessful()) {
                    moviesCallback.onReadyMovies(response.body().getMovieItems());
                    Log.e(TAG, "onResponse: " + response.message());
                } else {
                    Log.e(TAG, "onResponse: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<MovieItem.Response> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    public void getTrailers(int movieId) {
        Call<Trailer.Response> trailers = service.trailers(
                movieId,
                BuildConfig.MOVIE_API_KEY);
        trailers.enqueue(new Callback<Trailer.Response>() {
            @Override
            public void onResponse(Call<Trailer.Response> call, Response<Trailer.Response> response) {
                if (response.isSuccessful()) {
                    trailersCallback.onReadyTrailers(response.body().getResults());
                }
            }

            @Override
            public void onFailure(Call<Trailer.Response> call, Throwable t) {

            }
        });
    }

    public void setMoviesCallback(OnReadyMoviesCallback moviesCallback) {
        this.moviesCallback = moviesCallback;
    }

    public void setTrailersCallback(OnReadyTrailersCallback trailersCallback) {
        this.trailersCallback = trailersCallback;
    }

    public interface OnReadyMoviesCallback {
        void onReadyMovies(List<MovieItem> movies);
    }

    public interface OnReadyTrailersCallback {
        void onReadyTrailers(List<Trailer> trailers);
    }

}
