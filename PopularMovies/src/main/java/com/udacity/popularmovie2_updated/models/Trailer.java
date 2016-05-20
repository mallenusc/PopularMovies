package com.udacity.popularmovie2_updated.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Marques Allen on 5/12/16.
 */

public class Trailer {

    public Trailer() {

    }


    @SerializedName("id")
    @Expose
    public String id;

    @SerializedName("iso_639_1")
    @Expose
    public String isoStandard639;

    @SerializedName("iso_3166_1")
    @Expose
    public String isoStandard3166;

    @SerializedName("key")
    @Expose
    public String key;

    @SerializedName("name")
    @Expose
    public String name;


    @SerializedName("site")
    @Expose
    public String site;

    @SerializedName("type")
    @Expose
    public String type;

    @SerializedName("size")
    @Expose
    public int size;


    public String getId() {
        return id;
    }

    public String getIsoStandard639() {
        return isoStandard639;
    }

    public String getIsoStandard3166() {
        return isoStandard3166;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public String getType() {
        return type;
    }

    public int getSize() {
        return size;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setIsoStandard639(String isoStandard639) {
        this.isoStandard639 = isoStandard639;
    }

    public void setIsoStandard3166(String isoStandard3166) {
        this.isoStandard3166 = isoStandard3166;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
