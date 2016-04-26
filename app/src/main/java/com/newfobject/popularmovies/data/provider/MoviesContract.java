package com.newfobject.popularmovies.data.provider;


import android.net.Uri;

public class MoviesContract {

    public static final String PROVIDER_NAME = "com.newfobject.provider.movies";
    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/movies");


    //    public static final String _ID = "_id";
    public static final String MOVIE_ID = "movie_id";
    public static final String TITLE = "title";
    public static final String POSTER_PATH = "poster_path";
    public static final String ADULT = "is_adult";
    public static final String OVERVIEW = "overview";
    public static final String RELEASE_DATE = "release_date";
    public static final String ORIGINAL_TITLE = "original_title";
    public static final String ORIGINAL_LANGUAGE = "original_language";
    public static final String BACKDROP_PATH = "backdrop_path";
    public static final String POPULARITY = "popularity";
    public static final String VOTE_COUNT = "vote_count";
    public static final String VOTE_AVERAGE = "vote_average";

    public static class Projections {
        public static final String[] BROWSE_MOVIES_PROJECTION = {
                MOVIE_ID, TITLE, POSTER_PATH, VOTE_AVERAGE};
        public static final String[] MOVIE_DETAILS_PROJECTION = {
                MOVIE_ID, TITLE, POSTER_PATH, VOTE_AVERAGE, OVERVIEW, RELEASE_DATE, BACKDROP_PATH,
                POPULARITY, VOTE_COUNT, ADULT, ORIGINAL_LANGUAGE, ORIGINAL_TITLE
        };
        public static final int ID = 0;
        public static final int ID_TITLE = 1;
        public static final int ID_POSTER_PATH = 2;
        public static final int ID_VOTE_AVERAGE = 3;
        public static final int ID_OVERVIEW = 4;
        public static final int ID_RELEASE_DATE = 5;
        public static final int ID_BACKDROP_PATH = 6;
        public static final int ID_POPULARITY = 7;
        public static final int ID_VOTE_COUNT = 8;
        public static final int ID_ADULT = 9;
        public static final int ID_ORIGINAL_LANGUAGE = 10;
        public static final int ID_ORIGINAL_TITLE = 11;

    }

}
