package com.newfobject.popularmovies.data.api;

import com.newfobject.popularmovies.data.model.MovieItem;
import com.newfobject.popularmovies.data.model.Review;
import com.newfobject.popularmovies.data.model.Trailer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

interface APIService {
    @GET("discover/movie")
    Call<MovieItem.Response> movies(
            @Query("api_key") String apiKey,
            @Query("sort_by") String sort,
            @Query("page") int page);

    @GET("movie/{id}/videos")
    Call<Trailer.Response> trailers(
            @Path("id") int id,
            @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Call<Review.Respond> reviews(
            @Path("id") int id,
            @Query("api_key") String apiKey,
            @Query("page") int page);
}
