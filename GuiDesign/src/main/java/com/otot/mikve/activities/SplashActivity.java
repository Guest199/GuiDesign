package com.otot.mikve.activities;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.otot.mikve.general.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Calendar;

import mid.inf.L;

public class SplashActivity extends Activity {

	private Button btnSelfCharger, btnMore, btnCancel , btnTowel , btnEng, btnHeb, btnYid ,btnDonate;
	private TextView tvOperatorMsg, tvTitle, tvShowMode, tvPrice, tvPricePaid, tvTime , tvCommis , tvUserMsg;
	private boolean isCombo , isTowelActive = false, isBackFromEnroll = false, isDonation , isLngChoice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

// REMOVE ANDROID TITLE
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

// SELECT ORIENTATION
		setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE );
//		setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );

// SELECT ACTIVITY (LAYOUT)
		int layout = R.layout.activity_mikve;
//		int layout = R.layout.activity_self_charger;
//		int layout = R.layout.activity_splash;

		setContentView(layout);

		if(layout == R.layout.activity_mikve){

			tvShowMode  = (TextView) findViewById(R.id.tv_show_mode);
			tvPrice     = (TextView) findViewById(R.id.tv_month_price);
			tvPricePaid = (TextView) findViewById(R.id.tv_paid_by_cash);
			tvTime      = (TextView) findViewById(R.id.tv_time);
			tvCommis    = (TextView) findViewById(R.id.tv_commis);

			btnSelfCharger  = (Button) findViewById(R.id.btn_self_charger);
			btnMore         = (Button) findViewById(R.id.btn_more);
			btnCancel       = (Button) findViewById(R.id.btn_cancel_transaction);
			btnTowel        = (Button) findViewById(R.id.btn_towel);
			tvTitle         = (TextView) findViewById(R.id.tv_app_header);

			tvUserMsg   = (TextView) findViewById(R.id.tv_user_msg);

			btnEng    =(Button) findViewById(R.id.btn_eng);
			btnHeb    =(Button) findViewById(R.id.btn_heb);
			btnYid    =(Button) findViewById(R.id.btn_yid);
			btnDonate =(Button) findViewById(R.id.btn_donation);

isDonation = true;
isLngChoice = true;


			if(isLngChoice || isDonation){
				findViewById(R.id.ll_lang).setVisibility(View.VISIBLE);

				if (isLngChoice){
					btnYid.setVisibility(View.VISIBLE);
//					if (Settings.getInst().isShomer )
					btnEng.setVisibility(View.VISIBLE);
//					else
//						btnHeb.setVisibility(View.VISIBLE);
				}
//				if (OtotSystem.getTimeInMillis() - Settings.getInst().timeLastClickLang > 1000) {
//					changeLang(Settings.getInst().isShomer ? LngChoice.ENG : LngChoice.HEB);
//				}

				if (isDonation && !(isLngChoice && !HardwareInfo.isPortrait(this))) {
					btnDonate.setVisibility(View.VISIBLE);

					btnEng.setText("En");
					btnYid.setText("א");

					LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
							ViewGroup.LayoutParams.MATCH_PARENT,
							ViewGroup.LayoutParams.MATCH_PARENT,
							0.6f
					);
					btnEng.setLayoutParams(param);
					btnYid.setLayoutParams(param);
				}
			}

			initTime();

			updateMode(Entry.NORMAL , true, true);

		}
//		L.INFO();

// SET PORTRAIT OR LANDSCAPE
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


	}

	private void initTime(){

		setText(R.id.tv_date,     "Mon 08:53:58 AM");
		setText(R.id.tv_date_heb, "06/26/2023");
		setText(R.id.tv_time, 		"ז תמוז תשפג");

	}

	public void setText(int id , CharSequence text){
		((TextView) findViewById(id)).setText(text);
	}

	private void updateMode(int entryType , boolean withPic , boolean isTransctionComplete) {

		switch (entryType) {

			case Entry.NORMAL:
				tvShowMode.setText(getString(R.string.To_enter));
//				adjustIconToHeb(tvShowMode, R.drawable.mifare_card_s );
				tvPrice.setVisibility(View.VISIBLE);
				break;

			case Entry.OPEN:
				tvShowMode.setText(getString(R.string.mode_open));
				tvShowMode.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.entry, 0, 0);
				tvPrice.setVisibility(View.GONE);
//				isTowelActive = false;
				break;

			case Entry.CLOSED:
				String closedMsg = getString(R.string.mode_close);
				tvShowMode.setText(closedMsg);
//				adjustIconToHeb(tvShowMode, 0 );
				tvPrice.setVisibility(View.GONE);
				isTowelActive = false;
				break;

			case Entry.VIP_ONLY:
				tvShowMode.setText(getString(R.string.mode_vip_card));
				tvShowMode.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.card_black, 0, 0);
				tvPrice.setVisibility(View.GONE);
				isTowelActive = false;
				break;

			case Entry.HOMELESS:
//				adjustIconToHeb(tvShowMode, R.drawable.pay );
				if (!isTransctionComplete) {
					tvShowMode.setText(getString(R.string.mode_custom_cc_or_cash));
					tvPrice.setVisibility(View.VISIBLE);
					setEnabledChargingButtons(false);
				} else {
					tvShowMode.setText(getString(R.string.mode_homeless));
					tvPrice.setVisibility(View.GONE);
				}
				isTowelActive = false;
				break;

			case Entry.CUSTOM:

				String textAccept    = getString(R.string.Custom_entry_accepts);
				int drawable     = R.drawable.pay;
				int priceVisibility = View.VISIBLE;

				boolean mifareCardEnabled = true;
				boolean ccEnabled   = true;
				boolean cashEnabled = false;

				if ( mifareCardEnabled || ccEnabled || cashEnabled){
					textAccept += mifareCardEnabled ? getString(R.string.Card) + "," : "";
					textAccept += cashEnabled       ? getString(R.string.Cash) + "," : "";
					textAccept += ccEnabled         ? getString(R.string.Credit) + "," : "";

					textAccept = snipEnd(textAccept,1);

					if (!(ccEnabled || cashEnabled))        // added due to moshe request , do not show price just for cash card ..
						priceVisibility = View.INVISIBLE;

				} else {
					textAccept = getString(R.string.mode_custom_nothing);
					drawable = 0;
					priceVisibility = View.INVISIBLE;
				}

				// Set views
				tvShowMode.setText(textAccept);
//				adjustIconToHeb(tvShowMode, drawable );
				tvPrice.setVisibility(priceVisibility);

				if (priceVisibility == View.INVISIBLE)
					btnTowel.setVisibility(View.INVISIBLE);


				break;

			case Entry.SHABBAT:

				ImageView imageView = new ImageView(this);

/*
				boolean isHeight768 = HardwareInfo.getInstance().PIXEL_HEIGHT == 768;
				L.INFO("yaniv got to SHABBAT_" + isHeight768);
				int shabbatPic = isHeight768 ? (HardwareInfo.isPortrait(this) ? R.drawable.shabbat_port1024x768 : R.drawable.shabbat1024x768)
						: (HardwareInfo.isPortrait(this) ? R.drawable.shabbat_port : R.drawable.shabbat);

				imageView.setImageResource(shabbatPic);
				imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
				FrameLayout fl = ((FrameLayout)findViewById(R.id.fl_msg));

				fl.addView(((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.background_black, null));// set black background
//                fl.setForeground(null);
				fl.addView(imageView);
				fl.setPadding(0 , 0 , 0 , 0);
				fl.setVisibility(View.VISIBLE);

				TextView tv = findViewById(R.id.shabbat_writing);

				Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Bellefair-Regular.ttf");
				tv.setTypeface(typeface);
				tv.setVisibility(View.VISIBLE);
				if (OtotSystem.getTime().get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
					tv.setText("גוט יום טוב");
					if (HardwareInfo.isPortrait(this))
						tv.setTextSize(getResources().getDimension(R.dimen.size_large_2));
				}
*/
//                findViewById(R.id.ll_title).setVisibility(View.GONE);
//                findViewById(R.id.ll_button).setVisibility(View.GONE);
//                findViewById(R.id.ll_lang).setVisibility(View.GONE);

				isTowelActive = false;
				break;

//			default:
//				adjustIconToHeb(tvShowMode, 0 );
		}

		if (withPic)
			initLogo();

//		if (HardwareInfo.getInstance().SCREEN_INCH < 7){
//			tvShowMode.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//			tvShowMode.setTextSize(getResources().getDimension(R.dimen.size_medium_2));
//		}
	}

private void initLogo(){

	byte [] imgByteArr = getByteArr(R.drawable.pesach);

	if ( imgByteArr != null ){
		findViewById(R.id.analog_clock).setVisibility(View.GONE);
		ImageView logo = (ImageView)findViewById(R.id.logo);
		logo.setVisibility(View.VISIBLE);
		logo.setImageBitmap(getImg(imgByteArr));

		findViewById(R.id.ll_date_time).setBackground(getResources().getDrawable(R.color.white));
		((TextView) findViewById(R.id.tv_time      )).setTextColor(Color.BLUE);
		((TextView) findViewById(R.id.tv_date      )).setTextColor(Color.BLUE);
		((TextView) findViewById(R.id.tv_date_heb  )).setTextColor(Color.BLUE);

	}
}

	private void setEnabledChargingButtons(boolean enabled) {
//
		btnSelfCharger.setVisibility(enabled && isCombo          ? View.VISIBLE : View.GONE);
//		btnMore.setVisibility(       enabled && viewBtnMask != 0 ? View.VISIBLE : View.GONE);

		btnDonate.setEnabled(     enabled );

		if (enabled)
			btnCancel.setVisibility( View.GONE);
		else
			btnCancel.setVisibility(  View.VISIBLE);
	}

	public static String snipEnd(String str , int places ){
		return str.length() > places ?  str.substring(0, str.length() - places) : "";
	}

	public  Bitmap getImg(byte[] imgBlob){  // TODO change to zPic or fileManager
		ByteArrayInputStream imageStream = new ByteArrayInputStream(imgBlob);
		Bitmap image = BitmapFactory.decodeStream(imageStream);
		return image;
	}

	private byte[] getByteArr(int id){
		Drawable drawable = getResources().getDrawable(id);
		Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

// Convert the bitmap to a byte array
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
		return stream.toByteArray();
	}

}
