package com.udacity.popularmovie2_updated.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by MA on 5/15/16.
 */
public class MovieTrailersRQ {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("results")
    @Expose
    private ArrayList<Trailer> trailerList;

    public int getId() {
        return id;
    }
    public ArrayList<Trailer> getTrailerList(){return trailerList;}

}
