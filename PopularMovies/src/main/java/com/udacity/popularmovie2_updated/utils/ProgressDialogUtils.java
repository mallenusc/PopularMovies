package com.udacity.popularmovie2_updated.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.udacity.popularmovie2_updated.R;


public class ProgressDialogUtils {
	private static ProgressDialog mDialog;
	
	public static void showProgress(Context context) {
		if(mDialog != null && mDialog.isShowing()) 
			return;
		
		mDialog = new ProgressDialog(context);
		mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		mDialog.show();
		mDialog.setContentView(R.layout.progress_dialog);
		mDialog.setCancelable(false);
	}
	
	public static void dismissDialog() {
		try {
			if(mDialog != null && mDialog.isShowing())
				mDialog.dismiss();
		} catch(Exception ex) {
			// Do nothing
		}
	}
}
