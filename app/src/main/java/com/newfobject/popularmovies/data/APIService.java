package com.newfobject.popularmovies.data;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIService {
    @GET("discover/movie")
    Call<Model> movies(
            @Query("api_key") String apiKey,
            @Query("sort_by") String sort,
            @Query("page") int page);
}
