package com.newfobject.popularmovies.data.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;


public class MoviesProvider extends ContentProvider {
    private static final String PROVIDER_NAME = MoviesContract.PROVIDER_NAME;
    private static final Uri CONTENT_URI = MoviesContract.CONTENT_URI;
    private static final int MOVIES = 1;
    private static final int MOVIE_ID = 2;
    private static final UriMatcher urimatcher;

    static {
        urimatcher = new UriMatcher(UriMatcher.NO_MATCH);
        urimatcher.addURI(PROVIDER_NAME, "/movies/", MOVIES);
        urimatcher.addURI(PROVIDER_NAME, "/movies/#", MOVIE_ID);
    }

    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DbHelper dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
        return db != null;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(DbHelper.TABLE_NAME);
        switch (urimatcher.match(uri)) {
            case MOVIES:
                break;

            case MOVIE_ID:
                queryBuilder.appendWhere(MoviesContract.MOVIE_ID + "=" + uri.getPathSegments().get(1));
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }


    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (urimatcher.match(uri)) {
            case MOVIES:
                return ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + PROVIDER_NAME;
            case MOVIE_ID:
                return ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + PROVIDER_NAME;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID = db.insert(DbHelper.TABLE_NAME, "", values);

        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count;

        switch (urimatcher.match(uri)) {
            case MOVIES:
                count = db.delete(DbHelper.TABLE_NAME, selection, selectionArgs);
                break;
            case MOVIE_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete(DbHelper.TABLE_NAME, MoviesContract.MOVIE_ID + " = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("This method is not supported. use delete/insert");
    }
}
