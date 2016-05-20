package com.udacity.popularmovie2_updated;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;

import com.udacity.popularmovie2_updated.adapters.MovieAdapter;
import com.udacity.popularmovie2_updated.entities.MovieSortType;
import com.udacity.popularmovie2_updated.interfaces.TheMovieDbApi;
import com.udacity.popularmovie2_updated.listeners.RecyclerViewItemClickListener;
import com.udacity.popularmovie2_updated.models.Movie;
import com.udacity.popularmovie2_updated.models.PopularMovies;
import com.udacity.popularmovie2_updated.tools.AppSession;
import com.udacity.popularmovie2_updated.utils.Constants;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends BaseActivity implements RecyclerViewItemClickListener.OnItemClickListener {

    public static final String MOVIE_ID = "movie id";
    public static final String MOVIE_POSITION = "movie position";

    private AppSession mAppSession;
    private ArrayList<Movie> mTopRatedMovieList = new ArrayList<>();
    private ArrayList<Movie> mMostPopularMovieList = new ArrayList<>();
    private ArrayList<Movie> mMovieList = new ArrayList<>();
    private boolean mPopularMoviesLoaded = false;
    private boolean mTopMoviesLoaded = false;

    private Menu mSortMenu;
    private MovieAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private String mApiKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAppSession = new AppSession(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.gridview);
        mAdapter = new MovieAdapter(MainActivity.this);
        mRecyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(this, this));

        mApiKey = loadApiKey();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        showLoading();
        initColumnSize();
        loadMovies();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
               // popBackStack();
                return true;
            case R.id.menu_most_popular:
                mAppSession.storeMovieSortType(MovieSortType.MOST_POPULAR);
                updateMovies();
                break;
            case R.id.menu_top_rated:
                mAppSession.storeMovieSortType(MovieSortType.TOP_RATED);
                updateMovies();
                break;
            case R.id.menu_favorites:
                mAppSession.storeMovieSortType(MovieSortType.FAVORITES);
                updateMovies();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        mSortMenu = menu;
        updateMovies();

        return true;
    }

    private void loadMovies() {
        //Retrofit section start from here...
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Constants.API).build();                                        //create an adapter for retrofit with base url

        TheMovieDbApi movieDbApi = restAdapter.create(TheMovieDbApi.class);                            //creating a service for adapter with our GET class

        movieDbApi.getPopularMovies(mApiKey, new Callback<PopularMovies>() {
            @Override
            public void success(PopularMovies movies, Response response) {
                mPopularMoviesLoaded = true;
                mMostPopularMovieList = movies.getMovieList();
                updateMovies();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Retrofit Error Response", error.getMessage());
                mPopularMoviesLoaded = true;
                updateMovies();

            }
        });

        movieDbApi.getTopRateMovies(mApiKey, new Callback<PopularMovies>() {
            @Override
            public void success(PopularMovies movies, Response response) {

                mTopRatedMovieList = movies.getMovieList();
                mTopMoviesLoaded = true;
                updateMovies();

            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Retrofit Error Response", error.getMessage());
                mTopMoviesLoaded = true;
                updateMovies();

            }
        });
    }

    private void showDetailsActivity(int position) {
        Intent myIntent = new Intent(MainActivity.this, DetailsActivity.class);
        myIntent.putExtra(MOVIE_ID, String.valueOf(mMovieList.get(position).getId())); //Optional parameters
        myIntent.putExtra(MOVIE_POSITION, position); //Optional parameters
        MainActivity.this.startActivity(myIntent);
    }

    private void setNumberOfCols(int landscape, int portrait) {
        final Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        if (display.getRotation() == Surface.ROTATION_90 || display.getRotation() == Surface.ROTATION_270) {
            mLayoutManager = new GridLayoutManager(this, landscape);


        } else {
            mLayoutManager = new GridLayoutManager(this, portrait);
        }
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    private void initColumnSize() {
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);

        if (tabletSize) {
            setNumberOfCols(6, 4);
        } else {
            setNumberOfCols(3, 2);
        }
    }

    private void updateMovies() {
        boolean upDateAdapter = false;
        mMovieList = new ArrayList<>();

        switch (mAppSession.getMovieSortType()) {
            case MovieSortType.MOST_POPULAR:
                if (mMostPopularMovieList != null) {
                    mMovieList.addAll(mMostPopularMovieList);
                    upDateAdapter = true;
                }
                setTitle(R.string.menu_most_popular);
                mAppSession.storeMovieSortType(MovieSortType.MOST_POPULAR);
                mSortMenu.findItem(R.id.menu_most_popular).setChecked(true);
                break;
            case MovieSortType.TOP_RATED:
                if (mTopRatedMovieList != null) {
                    mMovieList.addAll(mTopRatedMovieList);
                    upDateAdapter = true;
                }
                setTitle(R.string.menu_top_rated);
                mAppSession.storeMovieSortType(MovieSortType.TOP_RATED);
                mSortMenu.findItem(R.id.menu_top_rated).setChecked(true);
                break;
            case MovieSortType.FAVORITES:
                if (mAppSession.getMovies() != null) {
                    mMovieList.addAll(mAppSession.getMovies());
                    upDateAdapter = true;
                }
                setTitle(R.string.menu_favorites);
                mAppSession.storeMovieSortType(MovieSortType.FAVORITES);
                mSortMenu.findItem(R.id.menu_favorites).setChecked(true);
                break;
        }

        if (mPopularMoviesLoaded && mTopMoviesLoaded) {
            hideLoading();
        }

        if (mAdapter != null && mMovieList != null && upDateAdapter) {
            mAdapter.setMovies(mMovieList);
            mRecyclerView.setAdapter(mAdapter);
        }

    }

    @Override
    public void onItemClick(View v, int position) {
        showDetailsActivity(position);
    }



}