package com.newfobject.popularmovies.data;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {
    @GET("discover/movie")
    Call<MovieItem.Response> movies(
            @Query("api_key") String apiKey,
            @Query("sort_by") String sort,
            @Query("page") int page);

    @GET("movie/{id}/videos")
    Call<Trailer.Response> trailers(
            @Path("id") int id,
            @Query("api_key") String apiKey);
}
