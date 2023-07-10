package com.otot.mikve.activities;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import com.otot.mikve.general.R;

public class MainActivity extends Activity {

	private VideoView videoView, tapCardVideoView, swipeCardVideoView, insertCashVideoView;
	private Uri videoUri, tapCardUri, swipeCardUri, inserCashUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

// REMOVE ANDROID TITLE
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		//setContentView(R.layout.activity_main);
		//setContentView(R.layout.activity_main_2);
		setContentView(R.layout.activity_main_3);


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

		MediaController mediaController1 = new MediaController(this);
		mediaController.setAnchorView(tapCardVideoView);
		tapCardVideoView.setMediaController(mediaController1);

		MediaController mediaController2 = new MediaController(this);
		mediaController.setAnchorView(swipeCardVideoView);
		swipeCardVideoView.setMediaController(mediaController2);

		MediaController mediaController3 = new MediaController(this);
		mediaController.setAnchorView(insertCashVideoView);
		insertCashVideoView.setMediaController(mediaController3);

		// Start playing the video
		playVideo();
		playOptionsVideos();
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
