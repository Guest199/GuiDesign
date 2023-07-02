package com.otot.mikve.activities;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HardwareInfo {

	private static HardwareInfo instance;

	public static final String ANDROID_MODEL = Build.MODEL;				// Holds the name of the andoid board
	public static final int ANDROID_VER 	= Build.VERSION.SDK_INT;	// Holds the version of the andoid board
//	public static OtotNetworkInterface NETWORK_INF = new OtotNetworkInterface();

	public int GPC_Version = 0;		// Holds the numberErr of version running of GPC board
	public String GPC_Name = "";			// Holds the name of the version running on GPC board
	public Class<?> restartToClass;		// Holds the calls of the activity the should be launched when the system should be booted/restarted
//	public NFC_handler NFC_HANDLER = null;

	public final static String GRAPERAIN 		= "G4418";			// large - 160
	public final static String GRAPERAIN_AOSP 	= "g4418";			// large - 160 , loolipop
	public final static String ROCKCHIP 		= "rk3288";			// xlarge- 160
	public final static String CHIPSEE 			= "AM335XEVM_SK";	// large - 160
	public final static String CHIPSEE_FREESCALE = "SABRESD-MX6DQ"; // CHIPSEE MASHROOM
	public final static String RASPBERRY_PI_3 	 = "Raspberry Pi 3";// RASPBERRY_PI_3
	public final static String RAKINDA 	 		 = "X8";			// RAKINDA				N_MR1(25)
	public final static String CARD_LAN = "AOSP on Drone";	// NEXELL
	public final static String MX_QPRO 	= "TVBOX";	// rockchip MX Q PRO streemer
	public final static String PDA_CIPHERLAB 	= "PDA_CIPHERLAB";	// PDA
	public final static String PDA_WINMATE      = "E430M2";			// PDA - 4 inch

	public final static String X96Max_Plus2 	= "X96Max_Plus2";	// rockchip MX Q PRO streemer
	public final static String H20 	= "H20";	// does not support cmd / ip
	public final static String MBOX 	= "MBOX";	// rockchip MX Q PRO streemer

	public final static String APIPO = "X10";	// rockchip X10

	public int PIXEL_WIDTH;
	public int PIXEL_HEIGHT;
	public int SCREEN_INCH;
	public String SCREEN_CATEGOTY;
	public String SCREEN_DENSITY;

	private HardwareInfo(Class<?> restartToClass) {

		this.restartToClass = restartToClass;

	}

	public static HardwareInfo getInstance( Class<?> restartToClass) {

		if (instance == null)
			instance = new HardwareInfo(restartToClass);

		return instance;
	}

	public static HardwareInfo getInstance() {
		return instance;
	}

	public void initScreenData(Activity activity){
		setScreenDimension( activity);
		SCREEN_CATEGOTY = getScreenSizeCategory(activity);
		SCREEN_DENSITY  = getScreenDensity(activity);
	}

	public String getScreenData(){
		return SCREEN_CATEGOTY + " " + SCREEN_DENSITY + " "  + PIXEL_WIDTH + "*" + PIXEL_HEIGHT + " " + SCREEN_INCH + "'";
	}

	/**
	 * Sets the firmware params
	 */
	public void setHwInfo(String ver) {
		GPC_Version = Integer.parseInt(ver.substring(0, 4), 16);
		GPC_Name = ver.substring(4);
	}

	/**
	 * Calculate screen physical size (dimensions)
	 */
	private void setScreenDimension(Activity activity) {
		WindowManager windowManager = activity.getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		DisplayMetrics displayMetrics = new DisplayMetrics();
		display.getMetrics(displayMetrics);

		try {
			if (ANDROID_VER < 14) {
				PIXEL_WIDTH = displayMetrics.widthPixels;
				PIXEL_HEIGHT = displayMetrics.heightPixels;

			}else
			if ( 14 <=ANDROID_VER && ANDROID_VER < 17) {

				PIXEL_WIDTH  = (Integer) Display.class.getMethod("getRawWidth").invoke(display);
				PIXEL_HEIGHT = (Integer) Display.class.getMethod("getRawHeight").invoke(display);
			}else	// includes window decorations (statusbar bar/menu bar)
			if (ANDROID_VER >= 17) {

				Point realSize = new Point();
				Display.class.getMethod("getRealSize", Point.class).invoke(display, realSize);
				PIXEL_WIDTH = realSize.x;
				PIXEL_HEIGHT = realSize.y;
			}
		} catch (Exception ignored) {
			PIXEL_WIDTH = PIXEL_HEIGHT = 0;
		}

		if (PIXEL_WIDTH!=0){
			double x = Math.pow(PIXEL_WIDTH / displayMetrics.densityDpi, 2);
			double y = Math.pow(PIXEL_HEIGHT / displayMetrics.densityDpi, 2);
			SCREEN_INCH = (int)Math.round(Math.sqrt(x + y));
		}
	}

//	public double getLcdSize() {
//		String s = SystemProperties.get("sf.lcd.size");
//		return Double.parseDouble(s.isEmpty() ? "0" : s);
//	}

	private String getScreenSizeCategory(Activity activity) {
		int screen_size = activity.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
		switch (screen_size){
			case Configuration.SCREENLAYOUT_SIZE_LARGE :	// 3
				return "large";

			case Configuration.SCREENLAYOUT_SIZE_NORMAL :	// 2
				return "normal";

			case Configuration.SCREENLAYOUT_SIZE_SMALL :	// 1
				return "small";

			case Configuration.SCREENLAYOUT_SIZE_XLARGE :	// 4
				return "xlarge";

			default:
				return "your screen mask is " + screen_size;
		}
	}

	public String getScreenDensity(Activity activity) {

		DisplayMetrics metrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int density = metrics.densityDpi;

		switch (density){
			case DisplayMetrics.DENSITY_HIGH :	// 240
				return " hdpi-240";

			case DisplayMetrics.DENSITY_MEDIUM :	// 160
				return " mdpi-160";

			case DisplayMetrics.DENSITY_LOW :	// 120
				return " ldpi-120";

			default:
				return "Density is " + density;
		}
	}

	public static int  getScreenOrientation(Activity activity){

		WindowManager windowManager = activity.getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		DisplayMetrics displayMetrics = new DisplayMetrics();
		display.getMetrics(displayMetrics);

// since SDK_INT = 1;
		int mWidthPixels = displayMetrics.widthPixels;
		int mHeightPixels = displayMetrics.heightPixels;
//

		if(mWidthPixels==mHeightPixels)
			return -100;
//			return Configuration.ORIENTATION_UNDEFINED;

		if(mWidthPixels < mHeightPixels)
			return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
//			return Configuration.ORIENTATION_PORTRAIT;

		return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
//		return Configuration.ORIENTATION_LANDSCAPE;

	}

	public static boolean isPortrait(Activity activity){
		return getScreenOrientation(activity) != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
	}

	public static boolean isBoard(String m_boardName){
		return ANDROID_MODEL.contains(m_boardName);
	}

	public static String getBoard(){
//L.INFO(" ANDROID_MODEL = " + ANDROID_MODEL);

		if (isBoard(GRAPERAIN))				return GRAPERAIN;
		if (isBoard(GRAPERAIN_AOSP))		return GRAPERAIN_AOSP;
		if (isBoard(ROCKCHIP))				return ROCKCHIP;
		if (isBoard(CHIPSEE))				return CHIPSEE;
		if (isBoard(PDA_CIPHERLAB))			return PDA_CIPHERLAB;
		if (isBoard(CHIPSEE_FREESCALE))		return CHIPSEE_FREESCALE;
		if (isBoard(RASPBERRY_PI_3))		return RASPBERRY_PI_3;
		if (isBoard(PDA_WINMATE))			return PDA_WINMATE;
		if (isBoard(RAKINDA))				return RAKINDA;
		if (isBoard(CARD_LAN))				return CARD_LAN;
		if (isBoard(MX_QPRO))				return MX_QPRO;
		if (isBoard(MBOX))					return MBOX;
		if (isBoard(X96Max_Plus2))			return X96Max_Plus2;
		if (isBoard(H20))					return H20;
		if (isBoard(APIPO))					return APIPO;
		return "";
	}

	public String getDateCmd(Calendar cal){
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
			return "date -s " + new SimpleDateFormat(getDateFormat()).format(cal.getTime());
		else
			return "date " + new SimpleDateFormat("MMddHHmmyyyy.ss").format(cal.getTime());
	}

	private String getDateFormat(){
		switch (getBoard()){
			case CHIPSEE_FREESCALE: 	return "yyyy.MM.dd-HH:mm:ss";
			default:					return "yyyyMMdd.HHmmss";
		}
	}

	public String getSerialLocation(int port_num){

		switch (getBoard()){

			case GRAPERAIN: 			return "/dev/ttyAMA" + port_num;
			case CHIPSEE_FREESCALE: 	return "/dev/ttymxc2" /*+ port_num*/;
			case RAKINDA: 				return "/dev/ttyS3" /*+ port_num*/;
//case GRAPERAIN_AOSP :		return "/dev/tty" + port_num;
			default: 					return "/dev/ttyO" + port_num;
		}
	}


	public boolean isResize(Activity activity){
		return isPortrait( activity ) && isBoard(GRAPERAIN);
	}


}
