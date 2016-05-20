package com.udacity.popularmovie2_updated.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Marques Allen on 5/12/16.
 */

public class SpokenLanguage {

    public SpokenLanguage() {

    }

    @SerializedName("iso_639_1")
    @Expose
    private String isoStandard;

    @SerializedName("name")
    @Expose
    private String countryName;

    public String getIsoStandard() {
        return isoStandard;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public void setIsoStandard(String isoStandard) {
        this.isoStandard = isoStandard;
    }


}
