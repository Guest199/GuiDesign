package com.otot.mikve.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.otot.mikve.general.R;

import java.util.Locale;

import mid.inf.L;

public class MainActivity extends Activity {

	private Button btnSelfCharger, btnMore, btnCancel , btnTowel , btnEng, btnHeb, btnYid ,btnDonate;
	private TextView tvOperatorMsg, tvTitle, tvShowMode, tvPrice, tvPricePaid, tvTime , tvCommis , tvUserMsg , tvName , tvOnline;


	private VideoView videoView, tapCardVideoView, swipeCardVideoView, insertCashVideoView;
	private Uri videoUri, tapCardUri, swipeCardUri, inserCashUri;
	private Button buttonHeb, buttonEng, buttonAlif, buttonDonation;
	private ConstraintLayout constraintLayout;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

// REMOVE ANDROID TITLE
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		//setContentView(R.layout.activity_main);
		//setContentView(R.layout.activity_main_2);
		setContentView(R.layout.activity_main_3);

		tvShowMode  = (TextView) findViewById(R.id.tv_show_mode);
		tvPrice     = (TextView) findViewById(R.id.tv_month_price);
		tvPricePaid = (TextView) findViewById(R.id.tv_paid_by_cash);
		tvTime      = (TextView) findViewById(R.id.tv_time);
		tvCommis    = (TextView) findViewById(R.id.tv_commis);

		btnSelfCharger  = getBtn(R.id.btn_self_charger ,        buttonsClickListener);
		btnMore         = getBtn(R.id.btn_more,                 buttonsClickListener);
		btnCancel       = getBtn(R.id.btn_cancel_transaction ,  buttonsClickListener);
		btnTowel        = getBtn(R.id.btn_towel              ,  buttonsClickListener);

tvName      = (TextView) findViewById(R.id.text_name);
tvOnline    = (TextView) findViewById(R.id.tvOnline);


//tvTitle         = getText(R.id.tv_app_header ,          buttonsClickListener); online + terminal name + logo

//		change to tvOnline , text_name

		tvUserMsg   = (TextView) findViewById(R.id.tv_user_msg);

		btnEng = getBtn(R.id.btn_eng,    buttonsClickListener);
		btnHeb = getBtn(R.id.btn_heb,    buttonsClickListener);
		btnYid = getBtn(R.id.btn_yid,    buttonsClickListener);
		btnDonate = getBtn(R.id.btn_donation,    buttonsClickListener);


				tvOperatorMsg = findViewById(R.id.tv_operator_msg);
//		      if (Settings.getInst().isShomer) {
//                tvPrice.setTextColor(getResources().getColor(  R.color.blue_dark  ));
//                tvPricePaid.setTextColor(getResources().getColor(  R.color.blue_dark  ));
////              tvPrice.setTextAppearance(this, R.style.text_price_msg);
//            }
//		}

		buttonHeb = findViewById(R.id.btn_heb);
		buttonEng = findViewById(R.id.btn_eng);
		buttonAlif = findViewById(R.id.btn_yid);
		buttonDonation = findViewById(R.id.btn_donation);
		constraintLayout = findViewById(R.id.constraint);

		// When we want to hide Donation Button
		/*ConstraintSet constraintSet = new ConstraintSet();
		constraintSet.clone(constraintLayout);
		// Set the width of Button 3 to 0dp to take up the remaining space
		constraintSet.constrainWidth(R.id.btn_yid, 0);
		constraintSet.constrainWidth(R.id.btn_eng, 0);
		constraintSet.constrainWidth(R.id.btn_heb, 0);

		// Apply updated constraints
		constraintSet.applyTo(constraintLayout);

		// Get the existing LayoutParams
		ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) buttonAlif.getLayoutParams();
		// Set the margin end in pixels
		//int marginEndPx = getResources().getDimensionPixelSize(R.dimen.your_margin_end_value);
		layoutParams.setMarginEnd(32);
		// Apply the updated LayoutParams
		buttonAlif.setLayoutParams(layoutParams);*/


		// When want to show Donation Button
		/*ConstraintSet constraints = new ConstraintSet();
		constraints.constrainWidth(R.id.btn_yid, ConstraintSet.WRAP_CONTENT);
		constraints.constrainWidth(R.id.btn_eng, ConstraintSet.WRAP_CONTENT);
		constraints.constrainWidth(R.id.btn_heb, ConstraintSet.WRAP_CONTENT);
		constraints.constrainWidth(R.id.btn_donation, 0);
		constraints.applyTo(constraintLayout);
		buttonDonation.setVisibility(View.VISIBLE);*/



		videoView = (VideoView) findViewById(R.id.videoView);
		tapCardVideoView = (VideoView) findViewById(R.id.vv_tap_card);
		swipeCardVideoView = (VideoView) findViewById(R.id.vv_swipe_cc);
		insertCashVideoView = (VideoView) findViewById(R.id.vv_insert_cash);
		videoUri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.video);
		tapCardUri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.tapcard);
		swipeCardUri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.swipe);
		inserCashUri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.flow);

		MediaController mediaController = new MediaController(this);
		mediaController.setAnchorView(videoView);
		videoView.setMediaController(mediaController);

//		MediaController mediaController1 = new MediaController(this);
//		mediaController.setAnchorView(tapCardVideoView);
//		tapCardVideoView.setMediaController(mediaController1);
//
//		MediaController mediaController2 = new MediaController(this);
//		mediaController.setAnchorView(swipeCardVideoView);
//		swipeCardVideoView.setMediaController(mediaController2);
//
//		MediaController mediaController3 = new MediaController(this);
//		mediaController.setAnchorView(insertCashVideoView);
//		insertCashVideoView.setMediaController(mediaController3);

		// Start playing the video
		playVideo();
		playOptionsVideos();
	}

	View.OnClickListener buttonsClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {


			switch (v.getId()) {

				case R.id.btn_donation:       doOrientation();    break;
				case R.id.btn_eng:       changeLang(new Locale("en" ));    break;
				case R.id.btn_heb:       changeLang(new Locale("iw" ));    break;
				case R.id.btn_yid:       changeLang(new Locale("ji" ));    break;

			}
		}
	};

	private void changeLang(Locale locale){

L.INFO("got here : " + locale.getLanguage());
		Configuration config = new Configuration();
		config.locale = locale;
		Locale.setDefault(locale);
		getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
		recreate();

	}

	public static int       ORIENTATION       = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
	private void doOrientation() {

		Activity activity = this;
		int orient = HardwareInfo.getScreenOrientation(activity);
		if ( ORIENTATION == ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED )
			activity.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE );
		else if (orient!= ORIENTATION )
			activity.setRequestedOrientation(ORIENTATION);

	}


	public Button getBtn(int id , View.OnClickListener listener){
		Button btn = findViewById(id);
		btn.setOnClickListener(listener);
		return btn;
	}

	private void playVideo() {
		videoView.setOnCompletionListener(mp -> {
			// Restart the video playback
			videoView.seekTo(0);
			videoView.start();
		});

		videoView.setVideoURI(videoUri);
		videoView.start();
	}

	private void playOptionsVideos() {
		tapCardVideoView.setVideoURI(tapCardUri);
		swipeCardVideoView.setVideoURI(swipeCardUri);
		insertCashVideoView.setVideoURI(inserCashUri);

		insertCashVideoView.setOnCompletionListener(mp -> {
			// Restart the video playback
			insertCashVideoView.seekTo(0);
			insertCashVideoView.start();
		});

		insertCashVideoView.start();

		/*tapCardVideoView.setOnCompletionListener(mp -> {
			swipeCardVideoView.start();
		});

		swipeCardVideoView.setOnCompletionListener(mp -> {
			insertCashVideoView.start();
		});

		insertCashVideoView.setOnCompletionListener(mp -> {
			tapCardVideoView.start();
		});*/

		//tapCardVideoView.start();
	}
}
