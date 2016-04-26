package com.newfobject.popularmovies.events;

public class FavoriteDetailEvent {
    private final int adapterPosition;
    private final int movieId;
    private final boolean favorite;

    public FavoriteDetailEvent(int adapterPosition, int movieId, boolean favorite) {
        this.adapterPosition = adapterPosition;
        this.movieId = movieId;
        this.favorite = favorite;
    }

    public int getAdapterPosition() {
        return adapterPosition;
    }

    public int getMovieId() {
        return movieId;
    }

    public boolean isFavorite() {
        return favorite;
    }
}
