package com.udacity.popularmovie2_updated.adapters;

/**
 * Created by Marques Allen on 5/12/16.
 */

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovie2_updated.R;
import com.udacity.popularmovie2_updated.models.Movie;
import com.udacity.popularmovie2_updated.utils.Constants;
import com.udacity.popularmovie2_updated.utils.ProgressDialogUtils;

import java.util.ArrayList;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private ArrayList<Movie> mMovies;
    private Context mContext;
    private int mImageHeight;
    private int mImageWidth;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        showLoading();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movies, parent, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Picasso.with(holder.mImageView.getContext()).load(Constants.IMAGE_URL + mMovies.get(position).getPosterPath()).into(holder.mImageView, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                hideLoading();
            }

            @Override
            public void onError() {
                hideLoading();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;

        public ViewHolder(View v) {
            super(v);

            this.mImageView = (ImageView) v.findViewById(R.id.image);

            intImageSize();

            FrameLayout.LayoutParams imageViewParams = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT);
            this.mImageView.setLayoutParams(imageViewParams);
            this.mImageView.getLayoutParams().height = mImageHeight;
            this.mImageView.getLayoutParams().width = mImageWidth;

            this.mImageView.setScaleType(ImageView.ScaleType.FIT_XY);

        }
    }

    public MovieAdapter(Context context, ArrayList<Movie> movies) {

        mContext = context;
        mMovies = movies;
    }

    public MovieAdapter(Context context) {
        mContext = context;

    }

    public void setMovies(ArrayList<Movie> movies) {
        mMovies = movies;
    }


    private void intImageSize() {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int width = size.x;
        int height = size.y;

        int orientation = display.getRotation();

        //Determining the size of Movie Image based on if table and orientation

        boolean isTablet = mContext.getResources().getBoolean(R.bool.isTablet);

        if (orientation == Surface.ROTATION_90 || orientation == Surface.ROTATION_270) {
            //Landscape
            mImageHeight = (isTablet) ? height / 3 : height / 2;
            mImageWidth = (isTablet) ? width / 6 : width / 3;
        } else {
            //Portrait
            mImageHeight = (isTablet) ? height / 4 : height / 2;
            mImageWidth = (isTablet) ? width / 4 : width / 2;

        }

    }

    public void showLoading() {
        ProgressDialogUtils.showProgress(mContext);
    }

    public void hideLoading() {
        ProgressDialogUtils.dismissDialog();
    }
}