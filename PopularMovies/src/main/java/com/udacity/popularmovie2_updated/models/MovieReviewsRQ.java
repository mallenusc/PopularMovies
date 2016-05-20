package com.udacity.popularmovie2_updated.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by MA on 5/15/16.
 */
public class MovieReviewsRQ {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("results")
    @Expose
    private ArrayList<Review> reviewList;

    @SerializedName("page")
    @Expose
    private int page;


    @SerializedName("total_pages")
    @Expose
    private int totalPages;


    @SerializedName("total_results")
    @Expose
    private int totalResults;

    public int getId() {
        return id;
    }
    public ArrayList<Review> getReviewList(){return reviewList;}

}


