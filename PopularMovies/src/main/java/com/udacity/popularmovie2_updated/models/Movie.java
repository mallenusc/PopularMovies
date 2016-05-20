package com.udacity.popularmovie2_updated.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Marques Allen on 5/12/16.
 */

public class Movie {

    public Movie() {

    }

    @SerializedName("poster_path")
    @Expose
    private String posterPath;

    @SerializedName("adult")
    @Expose
    private boolean adult;

    @SerializedName("overview")
    @Expose
    private String overview;

    @SerializedName("release_date")
    @Expose
    private String releaseDate;

    @SerializedName("genre_ids")
    @Expose
    private ArrayList<Integer> genreIds;

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("original_title")
    @Expose
    private String originalTitle;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;

    @SerializedName("popularity")
    @Expose
    private double popularity;

    @SerializedName("vote_count")
    @Expose
    private int voteCount;

    @SerializedName("video")
    @Expose
    private boolean video;

    @SerializedName("vote_average")
    @Expose
    private double voteAverage;

    @SerializedName("homepage")
    @Expose
    private String homePage;

    @SerializedName("imdb_id")
    @Expose
    private String imdbId;

    @SerializedName("original_language")
    @Expose
    private String originalLanguage;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("tagline")
    @Expose
    private String tagline;

    @SerializedName("spoken_languages")
    @Expose
    private ArrayList<SpokenLanguage> spokenLanguages;

    @SerializedName("revenue")
    @Expose
    private int revenue;

    @SerializedName("runtime")
    @Expose
    private int runtime;

    @SerializedName("belongs_to_collection")
    @Expose
    private Collection belongsToCollection;

    @SerializedName("budget")
    @Expose
    private int budget;

    @SerializedName("genres")
    @Expose
    private ArrayList<Genre> genres;

    @SerializedName("production_companies")
    @Expose
    private ArrayList<ProductionCompany> productionCompanies;

    @SerializedName("production_countries")
    @Expose
    private ArrayList<ProductionCountry> productionCountries;

    private boolean favorite = false;

    public boolean isFavorite() { return favorite; }

    public void setFavoriteMovie(boolean favorite) { this.favorite = favorite; }

    public boolean getAdult() {
        return adult;
    }

    public boolean getVideo() {
        return video;
    }

    public int getRevenue() {
        return revenue;
    }

    public int getRuntine() {
        return runtime;
    }

    public int getId() {
        return id;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public int getBudget() {
        return budget;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public double getPopularity() {
        return popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getTitle() {
        return title;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getHomePage() {
        return homePage;
    }

    public String getImdbId() {
        return imdbId;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public String getStatus() {
        return status;
    }

    public String getTagline() {
        return tagline;
    }

    public ArrayList<SpokenLanguage> getSpokenLanguages() {
        return spokenLanguages;
    }

    public ArrayList<Genre> getGenres() {
        return genres;
    }

    public ArrayList<ProductionCompany> getProductionCompanies() {
        return productionCompanies;
    }

    public ArrayList<ProductionCountry> getProductionCountries() {
        return productionCountries;
    }


    public Collection getCollection() {
        return belongsToCollection;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public void getRevenue(int revenue) {
        this.revenue = revenue;
    }

    public void getRuntine(int runtime) {
        this.runtime = runtime;
    }

    public void getId(int id) {
        this.id = id;
    }

    public void getVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public void getBudget(int budget) {
        this.budget = budget;
    }

    public void getVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public void getPopularity(double popularity) {
        this.popularity = popularity;
    }

    public void getPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void getOverview(String overview) {
        this.overview = overview;
    }

    public void getReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void getOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public void getTitle(String title) {
        this.title = title;
    }

    public void getBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public void getHomePage(String homePage) {
        this.homePage = homePage;
    }

    public void getImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public void getOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public void getStatus(String status) {
        this.status = status;
    }

    public void getTagline(String tagline) {
        this.tagline = tagline;
    }

    public void getSpokenLanguages(ArrayList<SpokenLanguage> spokenLanguages) {
        this.spokenLanguages = spokenLanguages;
    }

    public void getGenres(ArrayList<Genre> genres) {
        this.genres = genres;
    }

    public void getProductionCompanies(ArrayList<ProductionCompany> productionCompanies) {
        this.productionCompanies = productionCompanies;
    }

    public void getProductionCountries(ArrayList<ProductionCountry> productionCountries) {
        this.productionCountries = productionCountries;
    }


    public void setCollection(Collection belongsToCollection) {
        this.belongsToCollection = belongsToCollection;
    }


}