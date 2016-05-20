package com.udacity.popularmovie2_updated;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.udacity.popularmovie2_updated.utils.ProgressDialogUtils;

public class BaseActivity extends AppCompatActivity {
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

	public void showLoading() {
		ProgressDialogUtils.showProgress(this);
	}

	public void hideLoading() {
		ProgressDialogUtils.dismissDialog();
	}

	public String loadApiKey() {
		//Loading API key FOR themoviedb.org api in AnroidManifest.xml
		try {
			ApplicationInfo ai = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
			Bundle bundle = ai.metaData;
			return bundle.getString("ApiKey");
			//Log.e("**loading API key**", mApiKey);
		} catch (PackageManager.NameNotFoundException e) {
			// Log.e("MainActivity", "Could not load Api Key: " + e.getMessage());
		} catch (NullPointerException e) {
			//Log.e("MainActivity", "null - " + e.getMessage());
		}

		return "null";
	}

}
