package com.udacity.popularmovie2_updated.interfaces;

import com.udacity.popularmovie2_updated.models.Movie;
import com.udacity.popularmovie2_updated.models.MovieReviewsRQ;
import com.udacity.popularmovie2_updated.models.MovieTrailersRQ;
import com.udacity.popularmovie2_updated.models.PopularMovies;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by MA on 5/15/16.
 */


public interface TheMovieDbApi {

    @GET("/movie/popular")
    public void getPopularMovies(@Query("api_key") String apiKey, Callback<PopularMovies> response);


    @GET("/movie/top_rated")
    public void getTopRateMovies(@Query("api_key") String apiKey, Callback<PopularMovies> response);

    //Movie Details
    @GET("/{movie_id}")
    public void getMovieDetails(@Path("movie_id") String movieId, @Query("api_key") String apiKey, Callback<Movie> response);     //string user is for passing values from edittext for eg: user=basil2style,google

    //Video Details
    @GET("/{movie_id}/videos")
    public void getMovieTrailers(@Path("movie_id") String movieId,@Query("api_key") String apiKey, Callback<MovieTrailersRQ> response);     //string user is for passing values from edittext for eg: user=basil2style,google

    //Movie Details
    @GET("/{movie_id}/reviews")
    public void getMovieReviews(@Path("movie_id") String movieId,@Query("api_key") String apiKey, Callback<MovieReviewsRQ> response);     //string user is for passing values from edittext for eg: user=basil2style,google

}
