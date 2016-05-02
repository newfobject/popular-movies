package com.newfobject.popularmovies.data.provider;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.newfobject.popularmovies.data.provider.MoviesContract.ADULT;
import static com.newfobject.popularmovies.data.provider.MoviesContract.BACKDROP_PATH;
import static com.newfobject.popularmovies.data.provider.MoviesContract.MOVIE_ID;
import static com.newfobject.popularmovies.data.provider.MoviesContract.ORIGINAL_LANGUAGE;
import static com.newfobject.popularmovies.data.provider.MoviesContract.ORIGINAL_TITLE;
import static com.newfobject.popularmovies.data.provider.MoviesContract.OVERVIEW;
import static com.newfobject.popularmovies.data.provider.MoviesContract.POPULARITY;
import static com.newfobject.popularmovies.data.provider.MoviesContract.POSTER_PATH;
import static com.newfobject.popularmovies.data.provider.MoviesContract.RELEASE_DATE;
import static com.newfobject.popularmovies.data.provider.MoviesContract.TITLE;
import static com.newfobject.popularmovies.data.provider.MoviesContract.VOTE_AVERAGE;
import static com.newfobject.popularmovies.data.provider.MoviesContract.VOTE_COUNT;

class DbHelper extends SQLiteOpenHelper {
    static final String TABLE_NAME = "movies";
    private static final String DATABASE_NAME = "movies_db";
    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_DB_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    "_id" + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    MOVIE_ID + " INTEGER UNIQUE, " +
                    TITLE + " TEXT NOT NULL, " +
                    POSTER_PATH + " TEXT, " +
                    ADULT + " INTEGER, " +
                    OVERVIEW + " TEXT, " +
                    RELEASE_DATE + " TEXT, " +
                    ORIGINAL_TITLE + " TEXT, " +
                    ORIGINAL_LANGUAGE + " TEXT, " +
                    BACKDROP_PATH + " TEXT, " +
                    POPULARITY + " REAL, " +
                    VOTE_COUNT + " INTEGER, " +
                    VOTE_AVERAGE + " REAL);";



    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
