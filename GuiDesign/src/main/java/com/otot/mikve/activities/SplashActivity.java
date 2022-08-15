package com.otot.mikve.activities;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.otot.mikve.general.R;

public class SplashActivity extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

// REMOVE ANDROID TITLE
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

// SELECT ACTIVITY (VIEW)
		setContentView(R.layout.activity_mikve);
//		setContentView(R.layout.activity_self_charger);
//		setContentView(R.layout.activity_splash);

// SET PORTRAIT OR LANDSCAPE
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


	}

}
