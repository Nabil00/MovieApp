package com.mal.nabil.movieapp;

/**
 * Created by Lincoln on 15/01/16.
 */

//Movie details layout contains title, release date, movie poster, vote average, and plot synopsis

public class Movie {
    private String title, release_date, movie_poster, vote_average, plot_synopsis, movie_id;
    private boolean favorite;

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public void setMovie_id(String movie_id) {
        this.movie_id = movie_id;
    }

    public String getMovie_id() {
        return movie_id;
    }

    public Movie() {
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name.substring(1,name.length()-1);
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date.substring(1,release_date.length()-1);
    }

    public String getMovie_poster() {
        return movie_poster;
    }

    public void setMovie_poster(String movie_poster) {
        this.movie_poster =  movie_poster.substring(1,movie_poster.length()-1);
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average =  vote_average;
    }

    public String getPlot_synopsis() {
        return plot_synopsis;
    }

    public void setPlot_synopsis(String plot_synopsis) {
        this.plot_synopsis =  plot_synopsis.substring(1,plot_synopsis.length()-1);
    }

    public boolean isFavorite() {
        return favorite;
    }
}
