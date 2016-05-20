package com.udacity.popularmovie2_updated.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by MA on 5/15/16.
 */
public class MovieDetailsRQ {

    @SerializedName("page")
    @Expose
    private int page;

    @SerializedName("total_results")
    @Expose
    private int totalResults;

    @SerializedName("total_pages")
    @Expose
    private int totalPages;

    @SerializedName("results")
    @Expose
    private ArrayList<Movie> movieList;

    public int getTotalResults() {
        return totalResults;
    }
    public int getTotalPages() {
        return totalPages;
    }
    public int getPage() {
        return page;
    }
    public ArrayList<Movie> getMovieList(){return movieList;}

}
