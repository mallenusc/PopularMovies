package com.udacity.popularmovie2_updated.tools;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.udacity.popularmovie2_updated.entities.MovieSortType;
import com.udacity.popularmovie2_updated.models.Movie;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by MA on 5/13/16.
 */

public class AppSession {
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    private final String MOVIE_SORT_TYPE = "movie_sort_type";
    private final String FAVORITE_MOVIES = "favorite_movies";
    private final String SHARED = "Popular_Preferences";

    public AppSession(Context context) {
        sharedPref = context.getSharedPreferences(SHARED, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    public void storeMovieSortType(int movieSortType) {
        editor.putInt(MOVIE_SORT_TYPE,movieSortType );
        editor.commit();
    }

    public int getMovieSortType() {
        return sharedPref.getInt(MOVIE_SORT_TYPE, MovieSortType.TOP_RATED);
    }

    public void storeMovies(ArrayList<Movie>  movies) {
        Gson gson = new Gson();
        String json = gson.toJson(movies);
        editor.putString(FAVORITE_MOVIES, json);
        editor.commit();
    }

    public ArrayList<Movie> getMovies() {

        Gson gson = new Gson();
        String json = sharedPref.getString(FAVORITE_MOVIES, gson.toJson(new ArrayList<Movie>()));
        Type type = new TypeToken<ArrayList<Movie>>(){}.getType();
        ArrayList<Movie> students= gson.fromJson(json, type);

        return students;
    }

}


