package com.udacity.popularmovie2_updated;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.udacity.popularmovie2_updated.adapters.MovieAdapter;
import com.udacity.popularmovie2_updated.entities.MovieSortType;
import com.udacity.popularmovie2_updated.interfaces.TheMovieDbApi;
import com.udacity.popularmovie2_updated.listeners.RecyclerViewItemClickListener;
import com.udacity.popularmovie2_updated.models.Movie;
import com.udacity.popularmovie2_updated.models.MovieReviewsRQ;
import com.udacity.popularmovie2_updated.models.MovieTrailersRQ;
import com.udacity.popularmovie2_updated.models.PopularMovies;
import com.udacity.popularmovie2_updated.tools.AppSession;
import com.udacity.popularmovie2_updated.utils.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DetailsActivity extends BaseActivity implements View.OnClickListener,RecyclerViewItemClickListener.OnItemClickListener {

    private final String mRatingOutOf = " / 10";
    private final String mMoiveTimeScale = " min";

    private AppSession mAppSession;
    private ArrayList<Movie> mTopRatedMovieList = new ArrayList<>();
    private ArrayList<Movie> mMostPopularMovieList = new ArrayList<>();
    private ArrayList<Movie> mMovieList = new ArrayList<>();

    //Change how I keep track of loaded items.
    private boolean mImageLoaded = false;
    private boolean mIsFavorite = false;
    private boolean mMovieDetailsLoaded = false;
    private boolean mPopularMoviesLoaded = false;
    private boolean mReviewsLoaded = false;
    private boolean mTopMoviesLoaded = false;
    private boolean mTrailersLoaded = false;

    private Button btnFavorite;

    private ImageView ivImage;

    private int mPositionFavoriteMovie;

    private LinearLayout llReviews;
    private LinearLayout llTrailers;

    private Menu mSortMenu;
    private Movie mFavoriteMovie;
    private MovieAdapter mAdapter;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    private String mApiKey;
    private String mFileName;
    private String mMovieId;

    private TextView tvMinutes;
    private TextView tvRating;
    private TextView tvSynopsis;
    private TextView tvTitle;
    private TextView tvYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mAppSession = new AppSession(this);

        tvYear = (TextView)findViewById(R.id.details_year);
        tvRating = (TextView)findViewById(R.id.details_ratings);
        tvMinutes = (TextView)findViewById(R.id.details_minutes);
        tvSynopsis = (TextView)findViewById(R.id.details_movie_description);
        tvTitle = (TextView)findViewById(R.id.details_title);
        ivImage = (ImageView)findViewById(R.id.thumbnail);
        llReviews = (LinearLayout) findViewById(R.id.reviews_container);
        llTrailers = (LinearLayout) findViewById(R.id.trailers_container);
        btnFavorite = (Button) findViewById(R.id.button_favorites);

            if(getResources().getBoolean(R.bool.isTablet)) {
                mRecyclerView = (RecyclerView) findViewById(R.id.gridview);
                mLayoutManager = new GridLayoutManager(this, 1);
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(this, this));
            }

        mMovieId = getIntent().getStringExtra(MainActivity.MOVIE_ID);
        onSetUpEventHandler();

        mApiKey = loadApiKey();
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle(R.string.menu_movie_details);
        initColumnSize();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(compareMovies())
        {
            markFavorite();
        }

        if(getResources().getBoolean(R.bool.isTablet)){
            loadMovies();
        }

        //loading favorites data
            if (mAppSession.getMovieSortType() == MovieSortType.FAVORITES) {
                ArrayList<Movie> myMovies = mAppSession.getMovies();
                updateDetailsView(myMovies.get(getIntent().getIntExtra(MainActivity.MOVIE_POSITION, 0)));

            } else {
                loadMovieDetails();
            }
        loadTrailers();
        loadReviews();

    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_favorites:

                ArrayList<Movie> myFavoriteMovies = mAppSession.getMovies();

                if(!mIsFavorite) {
                    markFavorite();

                    //Saving favorites for offline use
                    if(myFavoriteMovies != null) {
                        SaveImages(mFavoriteMovie.getPosterPath(), mFavoriteMovie.getId());
                        myFavoriteMovies.add(mFavoriteMovie);
                        mAppSession.storeMovies(myFavoriteMovies);
                    }
                    mIsFavorite = true;
                }
                else
                {
                    mIsFavorite = false;
                    unmarkFavorite();

                    if(compareMovies())
                    {
                        myFavoriteMovies.remove(mPositionFavoriteMovie);
                    }
                    mAppSession.storeMovies(myFavoriteMovies);

                }
                if(getResources().getBoolean(R.bool.isTablet)) {
                    updateMovies();
                }

                break;
        }

    }

    @Override
    public void onItemClick(View v, int position) {

        showLoading();

        //clearing views and resetting values
        llReviews.removeAllViews();
        llTrailers.removeAllViews();
        mPopularMoviesLoaded = false;
        mTopMoviesLoaded = false;

        //Loading current movies selected
        mMovieId = String.valueOf(mMovieList.get(position).getId());
        if(compareMovies())
        {
            markFavorite();
        }
        else {
            unmarkFavorite();
        }


        loadMovieDetails();
        loadTrailers();
        loadReviews();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
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

    private boolean compareMovies()
    {
        int position = 0;

        for(Movie favorite : mAppSession.getMovies())
        {
            String favoriteMovieId = String.valueOf(favorite.getId());
            if(favoriteMovieId.contains(mMovieId) )
            {

                mPositionFavoriteMovie = position;
                return true;
            }
            position++;
        }

        mPositionFavoriteMovie = -1;
        return false;
    }

    private View getSeparator() {
        View separator = new View(this);
        separator.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) separator.getLayoutParams();
        params.height = 1;
        separator.setLayoutParams(params);
        separator.setBackgroundColor(ContextCompat.getColor(this, R.color.separator_color));
        separator.setPadding(15, 0, 15, 0);

        return separator;
    }

    private void initColumnSize() {
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);

        if (tabletSize) {
            setNumberOfCols(2, 1);
        }
    }

    private void loadImageFromDisk(String movieId, ImageView image) {

        String fileName = Environment.getExternalStorageDirectory().getPath() + "/" + "image-" + movieId + ".jpg";

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(fileName, options);
        image.setImageBitmap(bitmap);

    }

    private void loadImageUrl(String url) {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        ivImage.getLayoutParams().height = (size.x / 2);
        ivImage.setScaleType(ImageView.ScaleType.FIT_CENTER);

        if (mAppSession.getMovieSortType() == MovieSortType.FAVORITES) {

            loadImageFromDisk(mMovieId, ivImage);
        } else {
            Picasso.with(this).load(Constants.IMAGE_URL + url).into(ivImage, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    mImageLoaded = true;


                }

                @Override
                public void onError() {

                }
            });
        }

    }

    private void loadMovies() {
        //Retrofit section start from here...
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Constants.API).build();                                        //create an adapter for retrofit with base url

        TheMovieDbApi movieDbApi = restAdapter.create(TheMovieDbApi.class);                            //creating a service for adapter with our GET class

        movieDbApi.getPopularMovies(mApiKey, new Callback<PopularMovies>() {
            @Override
            public void success(PopularMovies movies, Response response) {

                mMostPopularMovieList = movies.getMovieList();
                updateMovies();

                mPopularMoviesLoaded = true;

                if (mPopularMoviesLoaded && mTopMoviesLoaded) {
                    hideLoading();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Retrofit Error Response", error.getMessage());
                mPopularMoviesLoaded = true;

                if (mPopularMoviesLoaded && mTopMoviesLoaded) {
                    hideLoading();
                }
            }
        });

        movieDbApi.getTopRateMovies(mApiKey, new Callback<PopularMovies>() {
            @Override
            public void success(PopularMovies movies, Response response) {

                mTopRatedMovieList = movies.getMovieList();
                mMovieList.addAll(mTopRatedMovieList);

                mAdapter = new MovieAdapter(DetailsActivity.this, mMovieList);
                mRecyclerView.setAdapter(mAdapter);

                mTopMoviesLoaded = true;

                if (mPopularMoviesLoaded && mTopMoviesLoaded) {
                    hideLoading();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Retrofit Error Response", error.getMessage());
                mTopMoviesLoaded = true;

                if (mPopularMoviesLoaded && mTopMoviesLoaded) {
                    hideLoading();
                }
            }
        });
    }

    private void loadMovieDetails() {
        //Retrofit section start from here...
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Constants.DETAILS_API).build();                                        //create an adapter for retrofit with base url

        TheMovieDbApi movieDbApi = restAdapter.create(TheMovieDbApi.class);                            //creating a service for adapter with our GET class

        movieDbApi.getMovieDetails(mMovieId, mApiKey, new Callback<Movie>() {
            @Override
            public void success(Movie movie, Response response) {

                mFavoriteMovie = movie;

                updateDetailsView(mFavoriteMovie);
                mMovieDetailsLoaded = true;
                hideLoading();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Retrofit Error Response", error.getMessage());
                mMovieDetailsLoaded = true;
                hideLoading();
            }
        });

    }


    private void loadReviews() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Constants.DETAILS_API).build();

        TheMovieDbApi movieDbApi = restAdapter.create(TheMovieDbApi.class);

        if(llReviews.getChildCount()  <= 0) {

            movieDbApi.getMovieReviews(mMovieId, mApiKey, new Callback<MovieReviewsRQ>() {
                @Override
                public void success(MovieReviewsRQ movie, Response response) {

                    if (movie.getReviewList() != null) {

                        for (int position = 0; position < movie.getReviewList().size(); position++) {
                            View inflatedLayout = getLayoutInflater().inflate(R.layout.review_row, null, false);

                            TextView tvAuthor = (TextView) inflatedLayout.findViewById(R.id.author);
                            TextView tvContent = (TextView) inflatedLayout.findViewById(R.id.content);
                            tvAuthor.setText(movie.getReviewList().get(position).getAuthor());
                            tvContent.setText(movie.getReviewList().get(position).getContent());

                            llReviews.addView(inflatedLayout);

                            if ((position + 1) < movie.getReviewList().size()) {
                                llReviews.addView(getSeparator());
                            }

                        }
                        mReviewsLoaded = true;
                        hideLoading();
                    }

                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e("Retrofit Response", error.getMessage());
                    mReviewsLoaded = true;
                    hideLoading();
                }
            });
        }

    }

    private void loadTrailers() {
        //create an adapter for retrofit with base url
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Constants.DETAILS_API).build();
        //creating a service for adapter with our GET class
        TheMovieDbApi movieDbApi = restAdapter.create(TheMovieDbApi.class);

        if(llTrailers.getChildCount() <= 0) {

            movieDbApi.getMovieTrailers(mMovieId, mApiKey, new Callback<MovieTrailersRQ>() {
                @Override
                public void success(final MovieTrailersRQ movie, Response response) {

                    if (movie.getTrailerList() != null) {

                        for (int position = 0; position < movie.getTrailerList().size(); position++) {
                            View inflatedLayout = getLayoutInflater().inflate(R.layout.trailer_row, null, false);

                            TextView trailer = (TextView) inflatedLayout.findViewById(R.id.text_view_trailer);
                            trailer.setText("Trailer " + (position + 1));

                            final int pos = position;
                            inflatedLayout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.YOUTUBE_URL + movie.getTrailerList().get(pos).key)));

                                }
                            });

                            llTrailers.addView(inflatedLayout);

                            if ((position + 1) < movie.getTrailerList().size()) {
                                llTrailers.addView(getSeparator());
                            }
                            mTrailersLoaded = true;
                            hideLoading();
                        }

                    }

                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e("Retrofit Response", error.getMessage());

                }
            });
        }

    }

    private void markFavorite()
    {
        mIsFavorite = true;
        btnFavorite.setBackgroundColor(ContextCompat.getColor(this, R.color.gray));
        btnFavorite.setText(getString(R.string.button_unfavorite));
    }

    public void onSetUpEventHandler() {
        findViewById(R.id.button_favorites).setOnClickListener(this);
        findViewById(R.id.details_layout).setOnClickListener(this);
    }

    public void SaveImages(String url, int movieId) {
        mFileName = "image-" + movieId + ".jpg";
        if(!mImageLoaded) {
            Picasso.with(this).load(Constants.IMAGE_URL + url).into(target);
        }
        else
        {
            verifyStoragePermissions(this);

        }
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



    Target target = new Target() {

        @Override
        public void onPrepareLoad(Drawable arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom arg1) {

            SaveImageToSd( bitmap);
        }

        @Override
        public void onBitmapFailed(Drawable arg0) {
            // tTODO Auto-generated method stub

        }
    };

    private void SaveImageToSd(Bitmap bitmap)
    {

        String myFileName = Environment.getExternalStorageDirectory().getPath() + "/" + mFileName;

        File file = new File(myFileName);
        try {
            file.createNewFile();
            FileOutputStream ostream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, ostream);
            ostream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateDetailsView(Movie movie)
    {
        DecimalFormat df2 = new DecimalFormat("###.##");

        tvMinutes.setText(String.valueOf(movie.getRuntine()) + mMoiveTimeScale);
        tvRating.setText(String.valueOf(Double.valueOf(df2.format(movie.getPopularity()))) + mRatingOutOf);
        tvSynopsis.setText(movie.getOverview());
        tvTitle.setText(movie.getTitle());
        tvYear.setText(movie.getReleaseDate().split("-")[0]);

        loadImageUrl(movie.getPosterPath());
    }

    private void unmarkFavorite()
    {
        mIsFavorite = false;
        btnFavorite.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        btnFavorite.setText(getString(R.string.button_favorite));
    }

    private void updateMovies() {
        mMovieList = new ArrayList<>();

        switch (mAppSession.getMovieSortType()) {
            case MovieSortType.MOST_POPULAR:
                if (mMostPopularMovieList != null) {
                    mMovieList.addAll(mMostPopularMovieList);
                }
                mAppSession.storeMovieSortType(MovieSortType.MOST_POPULAR);
                mSortMenu.findItem(R.id.menu_most_popular).setChecked(true);
                break;
            case MovieSortType.TOP_RATED:
                if (mTopRatedMovieList != null) {
                    mMovieList.addAll(mTopRatedMovieList);
                }
                mAppSession.storeMovieSortType(MovieSortType.TOP_RATED);
                mSortMenu.findItem(R.id.menu_top_rated).setChecked(true);
                break;
            case MovieSortType.FAVORITES:
                if (mAppSession.getMovies() != null) {
                    mMovieList.addAll(mAppSession.getMovies());
                }
                mAppSession.storeMovieSortType(MovieSortType.FAVORITES);
                mSortMenu.findItem(R.id.menu_favorites).setChecked(true);
                break;
        }

        if (mAdapter != null && mMovieList != null) {
            mAdapter.setMovies(mMovieList);
            mRecyclerView.setAdapter(mAdapter);
        }

    }


    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
        else
        {
            SaveImageToSd(((BitmapDrawable)ivImage.getDrawable()).getBitmap());
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                SaveImageToSd(((BitmapDrawable)ivImage.getDrawable()).getBitmap());
                    Toast.makeText(getApplicationContext(), "Need permissions ***", Toast.LENGTH_LONG).show();

                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}