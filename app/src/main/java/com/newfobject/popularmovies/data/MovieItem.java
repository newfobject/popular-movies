package com.newfobject.popularmovies.data;


import java.io.Serializable;

public class MovieItem implements Serializable {

    private int id;
    private String posterPath;
    private boolean adult;
    private String overview;
    private String releaseDate;
    private String genreIds;
    private String originalTitle;
    private String originalLanguage;
    private String title;
    private String backdropPath;
    private String popularity;
    private String voteCont;
    private String video;
    private String averageVote;

    public MovieItem() {

    }

    public MovieItem(int id, String posterPath, boolean adult, String overview,
                     String releaseDate, String originalTitle,
                     String originalLanguage, String title, String backdropPath,
                     String popularity, String voteCont, String video, String averageVote) {

        this.id = id;
        this.posterPath = posterPath;
        this.adult = adult;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.genreIds = genreIds;
        this.originalTitle = originalTitle;
        this.originalLanguage = originalLanguage;
        this.title = title;
        this.backdropPath = backdropPath;
        this.popularity = popularity;
        this.voteCont = voteCont;
        this.video = video;
        this.averageVote = averageVote;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getOverview() {
        return overview;
    }

    public String getRating() {
        return averageVote;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getTitle() {
        return title;
    }
}
